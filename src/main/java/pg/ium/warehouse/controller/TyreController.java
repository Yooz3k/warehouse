package pg.ium.warehouse.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;
import pg.ium.warehouse.exception.TyreNotFoundException;
import pg.ium.warehouse.exception.TyreQuantityLowerThanZeroException;
import pg.ium.warehouse.repository.TyreRepository;
import pg.ium.warehouse.domain.Tyre;

import java.util.List;

@RestController
@RequestMapping("/tyres")
public class TyreController {

	private final TyreRepository repository;

	public TyreController(TyreRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	List<Tyre> findAll() {
		return repository.findAll();
	}

	@GetMapping("/{id}")
	Tyre findById(@PathVariable Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new TyreNotFoundException(id));
	}

	@PostMapping
	Tyre add(@RequestBody Tyre newTyre) {
		return repository.save(newTyre);
	}

	@PutMapping("/{id}")
	Tyre replace(@RequestBody Tyre newTyre, @PathVariable Long id) {
		return repository.findById(id)
				.map(tyre -> {
					tyre.replaceFields(newTyre);
					return repository.save(tyre);
				})
				.orElseThrow(() -> new TyreNotFoundException(id));
	}

	@PatchMapping("/{id}/quantity/change/{diff}")
	Tyre changeQuantity(@PathVariable Long id, @PathVariable Integer change) throws TyreQuantityLowerThanZeroException {
		Tyre tyre = repository.findById(id)
				.orElseThrow(() -> new TyreNotFoundException(id));

		tyre.changeQuantity(change);
		repository.save(tyre);
		return tyre;
	}

	@DeleteMapping("/{id}")
	void remove(@PathVariable Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException ex) {
			throw new TyreNotFoundException(id);
		}
	}

}

package pg.ium.warehouse.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pg.ium.warehouse.domain.Tyre;
import pg.ium.warehouse.exception.TyreNotFoundException;
import pg.ium.warehouse.exception.TyreQuantityLowerThanZeroException;
import pg.ium.warehouse.repository.TyreRepository;

import java.util.List;

import static org.springframework.http.ResponseEntity.noContent;

@RestController
@RequestMapping("/tyres")
public class TyreController {

	private final TyreRepository repository;

	public TyreController(TyreRepository repository) {
		this.repository = repository;
	}

	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@GetMapping
	List<Tyre> findAll() {
		return repository.findAll();
	}

	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@GetMapping("/{id}")
	Tyre findById(@PathVariable Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new TyreNotFoundException(id));
	}

	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@PostMapping
	Tyre add(@RequestBody Tyre newTyre) {
		newTyre.setId(null);
		newTyre.setQuantity(0); //New tyres are always added with no quantity.
		return repository.save(newTyre);
	}

	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@PutMapping("/{id}")
	Tyre replace(@RequestBody Tyre newTyre, @PathVariable Long id) {
		return repository.findById(id)
				.map(tyre -> {
					tyre.replaceFields(newTyre);
					return repository.save(tyre);
				})
				.orElseThrow(() -> new TyreNotFoundException(id));
	}

	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@PatchMapping("/{id}/quantity/{change}")
	Tyre changeQuantity(@PathVariable Long id, @PathVariable Integer change) throws TyreQuantityLowerThanZeroException {
		Tyre tyre = repository.findById(id)
				.orElseThrow(() -> new TyreNotFoundException(id));

		tyre.changeQuantity(change);
		repository.save(tyre);
		return tyre;
	}

	@PreAuthorize("hasRole('ROLE_MANAGER')")
	@DeleteMapping("/{id}")
	ResponseEntity remove(@PathVariable Long id) {
		repository.findById(id)
				.orElseThrow(() -> new TyreNotFoundException(id));

		repository.deleteById(id);
		return noContent().build();
	}

}

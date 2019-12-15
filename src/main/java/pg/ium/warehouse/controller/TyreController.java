package pg.ium.warehouse.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
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
import pg.ium.warehouse.synchronization.LocalDateTimeDeserializer;
import pg.ium.warehouse.synchronization.LocalDateTimeSerializer;
import pg.ium.warehouse.synchronization.MobileSynchronization;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.noContent;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tyres")
public class TyreController {

	private final TyreRepository repository;

	private final MobileSynchronization mobileSynchronization;

	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@GetMapping
	List<Tyre> findAll() {
		return repository.findAll().stream()
				.filter(tyre -> !tyre.isDeleted())
				.collect(Collectors.toList());
	}

	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@GetMapping("/{id}")
	Tyre findById(@PathVariable Long id) {
		return repository.findById(id)
				.filter(tyre -> !tyre.isDeleted())
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
				.filter(tyre -> !tyre.isDeleted())
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
				.filter(t -> !t.isDeleted())
				.orElseThrow(() -> new TyreNotFoundException(id));

		tyre.changeQuantity(change);
		repository.save(tyre);
		return tyre;
	}

	@PreAuthorize("hasRole('ROLE_MANAGER')")
	@DeleteMapping("/{id}")
	ResponseEntity remove(@PathVariable Long id) {
		Tyre tyre = repository.findById(id)
				.filter(t -> !t.isDeleted())
				.orElseThrow(() -> new TyreNotFoundException(id));

		tyre.setDeleted(true);
		tyre.setLastModified(LocalDateTime.now());
		repository.save(tyre);

		return noContent().build();
	}

	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	@PostMapping("/synchronize")
	List<Tyre> synchronize(@RequestBody String json) {
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
				.create();
		json = json.replaceAll("\"[{]", "{")
				.replaceAll("[}]\"", "}")
				.replace("\\\"", "\"");
		Tyre[] tyres = gson.fromJson(json, new TypeToken<Tyre[]>(){}.getType());
		mobileSynchronization.synchronize(Arrays.asList(tyres));
		return repository.findAll();
	}

}

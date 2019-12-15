package pg.ium.warehouse.synchronization;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pg.ium.warehouse.domain.Tyre;
import pg.ium.warehouse.repository.TyreRepository;
import pg.ium.warehouse.security.RoleChecker;
import pg.ium.warehouse.security.config.UserRoles;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MobileSynchronization {

	private final TyreRepository tyreRepository;

	private Map<Long, Tyre> storedTyres;

	@Transactional
	public void synchronize(List<Tyre> synchronizedTyres) {
		storedTyres = tyreRepository.findAll()
				.stream()
				.collect(Collectors.toMap(Tyre::getId, Function.identity()));

		synchronizedTyres.forEach(synchronizedTyre -> {
			if (synchronizedTyre.isToBeAdded()) {
				addTyre(synchronizedTyre);
			} else {
				modifyTyre(synchronizedTyre);
			}
		});
	}

	private void addTyre(Tyre synchronizedTyre) {
		tyreRepository.save(synchronizedTyre);
	}

	private void modifyTyre(Tyre synchronizedTyre) {
		Tyre storedTyre = getStoredTyre(synchronizedTyre.getId());

		//Modification is not valid if there is an unauthorized attempt of deleting a tyre.
		boolean modificationValid = true;
		//We override other stored tyre's info only if synchronized tyre was modified later than the stored one.
		if (synchronizedTyre.getLastModified() != null &&
				synchronizedTyre.getLastModified().isAfter(storedTyre.getLastModified())) {
			modificationValid = updateFields(storedTyre, synchronizedTyre);
		}

		// But we change the quantity, as it doesn't change the last modified time.
		// We don't change anything and don't save the tyre, if the modification is not valid.
		if (modificationValid) {
			changeTyreQuantity(storedTyre, synchronizedTyre);
			tyreRepository.save(storedTyre);
		}
	}

	private Tyre getStoredTyre(long synchronizedId) {
		return storedTyres.get(synchronizedId);
	}

	private void changeTyreQuantity(Tyre storedTyre, Tyre synchronizedTyre) {
		int oldQuantity = storedTyre.getQuantity();
		int newQuantity = oldQuantity + synchronizedTyre.getQuantityChange();
		storedTyre.setQuantity(newQuantity);
	}

	/**
	 * Returns false if user intended to delete tyre, but didn't have rights to do it,
	 * otherwise it returns true.
	 */
	private boolean updateFields(Tyre storedTyre, Tyre synchronizedTyre) {
		//If stored was deleted, but sync was not, revert the delete action
		if (storedTyre.isDeleted() && !synchronizedTyre.isDeleted()) {
			storedTyre.setDeleted(false);
		} else if (!storedTyre.isDeleted() && synchronizedTyre.isDeleted()) {
			RoleChecker roleChecker = new RoleChecker();
			//If synced was deleted, but stored was not, we have to check if user has access to that action.
			if (roleChecker.currentUserInRole(UserRoles.MANAGER)) {
				storedTyre.setDeleted(true);
			} else {
				//If user intended to delete the tyre, we should discard any other changes he made to
				return false;
			}
		}

		//For all other fields, just override stored values with the synced ones.
		storedTyre.setProducer(synchronizedTyre.getProducer());
		storedTyre.setName(synchronizedTyre.getName());
		storedTyre.setRimSize(synchronizedTyre.getRimSize());
		storedTyre.setPrice(synchronizedTyre.getPrice());
		storedTyre.setLastModified(synchronizedTyre.getLastModified());

		return true;
	}
}

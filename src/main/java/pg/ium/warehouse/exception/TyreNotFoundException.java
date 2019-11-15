package pg.ium.warehouse.exception;

public class TyreNotFoundException extends RuntimeException {

	public TyreNotFoundException(Long id) {
		super("Could not find tyre with ID " + id);
	}
}

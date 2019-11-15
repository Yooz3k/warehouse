package pg.ium.warehouse.exception;

public class TyreQuantityLowerThanZeroException extends Exception {

	public TyreQuantityLowerThanZeroException(Long id) {
		super("Quantity of product with ID " + id + " is too low to decrease it by given amount!");
	}
}

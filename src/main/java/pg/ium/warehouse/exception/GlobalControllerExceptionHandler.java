package pg.ium.warehouse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(TyreNotFoundException.class)
	public String tyreNotFoundHandler(TyreNotFoundException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	@ExceptionHandler(TyreQuantityLowerThanZeroException.class)
	public String tyreQuantityLowerThanZeroHandler(TyreQuantityLowerThanZeroException ex) {
		return ex.getMessage();
	}

}

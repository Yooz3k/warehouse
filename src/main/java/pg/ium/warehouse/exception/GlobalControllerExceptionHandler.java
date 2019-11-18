package pg.ium.warehouse.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pg.ium.warehouse.security.InvalidJwtAuthenticationException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(TyreNotFoundException.class)
	public ResponseEntity<Object> tyreNotFoundHandler(TyreNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(TyreQuantityLowerThanZeroException.class)
	public ResponseEntity<Object> tyreQuantityLowerThanZeroHandler(TyreQuantityLowerThanZeroException ex) {
		return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(InvalidJwtAuthenticationException.class)
	public ResponseEntity<Object> invalidJwtAuthentication(InvalidJwtAuthenticationException ex) {
		return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
	}

}

package pg.ium.warehouse.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(TyreNotFoundException.class)
	public ResponseEntity<String> tyreNotFoundHandler(TyreNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(TyreQuantityLowerThanZeroException.class)
	public ResponseEntity<String> tyreQuantityLowerThanZeroHandler(TyreQuantityLowerThanZeroException ex) {
		return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<String> invalidJwtAuthentication(BadCredentialsException ex) {
		return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler({GeneralSecurityException.class, IOException.class})
	public ResponseEntity<String> invalidOAuthTokenVerification(Exception ex) {
		return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<String> accessDenied(AccessDeniedException ex) {
		return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<String> userAlreadyExists(UserAlreadyExistsException ex) {
		return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT);
	}

}

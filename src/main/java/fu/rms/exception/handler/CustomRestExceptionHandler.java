package fu.rms.exception.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import fu.rms.exception.CreateException;
import fu.rms.exception.AuthenException;
import fu.rms.exception.DeleteException;
import fu.rms.exception.DuplicateException;
import fu.rms.exception.NotFoundException;
import fu.rms.exception.UpdateException;

@RestControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

	// handle valid data
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> messages = new ArrayList<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			messages.add(error.getDefaultMessage());
		});
		MessageError messageError = new MessageError(HttpStatus.BAD_REQUEST, messages);
		return new ResponseEntity<Object>(messageError, new HttpHeaders(), messageError.getStatus());
	}

	// default handle
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
		MessageError apiError = new MessageError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	// handle not found exception
	@ExceptionHandler({ NotFoundException.class })
	public ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {

		MessageError messageError = new MessageError(HttpStatus.NOT_FOUND, ex.getMessage());
		return new ResponseEntity<Object>(messageError, new HttpHeaders(), messageError.getStatus());

	}

	// handle add exception
	@ExceptionHandler({ CreateException.class })
	public ResponseEntity<Object> handleAddException(CreateException ex, WebRequest request) {
		MessageError messageError = new MessageError(HttpStatus.BAD_REQUEST, ex.getMessage());
		return new ResponseEntity<Object>(messageError, new HttpHeaders(), messageError.getStatus());

	}

	// handle update exception
	@ExceptionHandler({ UpdateException.class })
	public ResponseEntity<Object> handleUpdateException(UpdateException ex, WebRequest request) {
		MessageError messageError = new MessageError(HttpStatus.BAD_REQUEST, ex.getMessage());
		return new ResponseEntity<Object>(messageError, new HttpHeaders(), messageError.getStatus());

	}

	// handle delete exception
	@ExceptionHandler({ DeleteException.class })
	public ResponseEntity<Object> handleDeleteException(DeleteException ex, WebRequest request) {
		MessageError messageError = new MessageError(HttpStatus.BAD_REQUEST, ex.getMessage());
		return new ResponseEntity<Object>(messageError, new HttpHeaders(), messageError.getStatus());

	}
	
	// handle DuplicatePhone
	@ExceptionHandler({ DuplicateException.class })
	public ResponseEntity<Object> handleDuplicatePhoneException(DuplicateException ex, WebRequest request) {
		MessageError messageError = new MessageError(HttpStatus.BAD_REQUEST, ex.getMessage());
		return new ResponseEntity<Object>(messageError, new HttpHeaders(), messageError.getStatus());

	}
	
	//handle authentication
	@ExceptionHandler({ AuthenException.class })
	public ResponseEntity<Object> handleUserNotActiveException(AuthenException ex, WebRequest request) {
		MessageError messageError = new MessageError(HttpStatus.UNAUTHORIZED, ex.getMessage());
		return new ResponseEntity<Object>(messageError, new HttpHeaders(), messageError.getStatus());

	}
	

}

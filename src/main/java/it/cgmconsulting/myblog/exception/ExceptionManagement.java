package it.cgmconsulting.myblog.exception;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionManagement {
/*
	@ExceptionHandler({IllegalArgumentException.class})
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex){
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
*/
	@ExceptionHandler ({MethodArgumentNotValidException.class})
	public ResponseEntity<String> handleArgumentNotValidValue(MethodArgumentNotValidException ex) {

		BindingResult bindingResults = ex.getBindingResult();
		List<String> errors = bindingResults
				.getFieldErrors()
				.stream().map(e -> {
					return e.getField()+": "+e.getDefaultMessage();
				})
				.collect(Collectors.toList());

		return new ResponseEntity<String>(errors.toString(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler ({ConstraintViolationException.class})
	public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException ex) {

		Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
		List<String> errors = violations
				.stream().map(e -> {
					return e.getMessage();
				})
				.collect(Collectors.toList());

		return new ResponseEntity<String>(errors.toString(), HttpStatus.BAD_REQUEST);
	}
	
}

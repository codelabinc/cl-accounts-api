package com.codelab.accounts.coreapi.advice;

import com.codelab.accounts.conf.exception.ApiException;
import com.codelab.accounts.conf.exception.NotFoundException;
import com.codelab.accounts.domain.response.HttpError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ErrorControllerAdvice {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<HttpError> handle(NotFoundException e) {
        return new ResponseEntity(new HttpError(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiException> handle(ApiException e) {
        return new ResponseEntity(new HttpError(e.getHttpStatus().value(), e.getMessage()), e.getHttpStatus());
    }
    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex) {
       ConstraintViolation<?> violation =  ex.getConstraintViolations().iterator().next();
        return new ResponseEntity(new HttpError(HttpStatus.BAD_REQUEST.value(),
                violation.getPropertyPath() + ": " + violation.getMessage()), HttpStatus.BAD_REQUEST);
    }

}

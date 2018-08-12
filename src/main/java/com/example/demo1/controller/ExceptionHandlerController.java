package com.example.demo1.controller;

import com.example.demo1.exception.DataBindingErrorMessage;
import com.example.demo1.exception.DataNotFoundException;
import com.example.demo1.exception.ExceptionResponse;
import org.hibernate.HibernateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ExceptionResponse> userNotFoundException(final DataNotFoundException ex, final HttpServletRequest request) {
        ExceptionResponse error = new ExceptionResponse();
        error.setMessage(ex.getMessage());
        error.setCallerUrl(request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DataBindingErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
//        exception.printStackTrace();
        DataBindingErrorMessage dataBindingErrorMessage=dataBindingErrorMessagesConverter(exception.getBindingResult());
        return new ResponseEntity<>(dataBindingErrorMessage, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(HibernateException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(final HibernateException ex, final HttpServletRequest request) {
//        ex.printStackTrace();
        ExceptionResponse error = new ExceptionResponse();
        error.setMessage(ex.getMessage());
        error.setCallerUrl(request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
    }

    public DataBindingErrorMessage dataBindingErrorMessagesConverter(BindingResult bindingResult) {
        DataBindingErrorMessage dataBindingErrorMessage = new DataBindingErrorMessage();
        dataBindingErrorMessage.setErrorMessage("Invalid request parameter");
        dataBindingErrorMessage.setCode(HttpStatus.BAD_REQUEST.value());
        List<DataBindingErrorMessage.Error> errors = new ArrayList<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for(FieldError fieldError:fieldErrors){
            DataBindingErrorMessage.Error error = dataBindingErrorMessage.new Error();
            error.setErrorMessage(fieldError.getDefaultMessage());
            error.setRejectedValue(fieldError.getRejectedValue());
            error.setFieldName(fieldError.getField());
            errors.add(error);
        }
        dataBindingErrorMessage.setErrors(errors);
        return dataBindingErrorMessage;
    }
}

package com.algaworks.algashop.ordering.presentation;

import com.algaworks.algashop.ordering.domain.DomainEntityNotFoundException;
import com.algaworks.algashop.ordering.domain.DomainException;
import com.algaworks.algashop.ordering.domain.customer.CustomerEmailsInUseException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle("Invalid Fields");
        problemDetail.setDetail("One or more fields are invalid");
        problemDetail.setType(URI.create("/errors/invalid-fields"));

        Map<String, String> fieldErros = ex.getBindingResult().getAllErrors().stream().collect(Collectors.toMap(objectError -> ((FieldError) objectError).getField(),
                objectError -> messageSource.getMessage(objectError, LocaleContextHolder.getLocale())));

        problemDetail.setProperty("fields", fieldErros);

        return super.handleExceptionInternal(ex,problemDetail, headers, status, request);
    }

    @ExceptionHandler(DomainEntityNotFoundException.class)
    public ProblemDetail handleDomainNotFoundException(DomainEntityNotFoundException e ) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Not found");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("/errors/not-found"));
        return problemDetail;
    }

    @ExceptionHandler(DomainException.class)
    public ProblemDetail handleDomainException(DomainException e ) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setTitle("Uniprocessable entity");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("/errors/unprocessable-entity"));
        return problemDetail;
    }

    @ExceptionHandler(CustomerEmailsInUseException.class)
    public ProblemDetail handleCustomerEmailsInUseException(CustomerEmailsInUseException e ) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setTitle("Conflict");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("/errors/conflict"));
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception e ) {
        log.error(e.getMessage(), e);
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Internal server error");
        problemDetail.setDetail("An unexpected internal error occurred");
        problemDetail.setType(URI.create("/errors/internal-server-error"));
        return problemDetail;
    }

}

package com.backend.sevenX.exception;

import com.backend.sevenX.config.CommonResponse;
import com.backend.sevenX.utills.Constant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.logging.Logger;

/**
 * used to handle exceptions and return response
 */
// assigning this exception class on high precedence
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler {

    protected final Log logger = LogFactory.getLog(getClass());

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<?> validationException(MethodArgumentNotValidException e) {
        String message = "Please Enter Valid Parameters";
        List<ObjectError> errorList = e.getBindingResult().getAllErrors();
        if (errorList.size() > 0)
            message = errorList.get(0).getDefaultMessage();

        return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.BAD_REQUEST.value(),
                message, null), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public final ResponseEntity<?> handleMainException(Exception ex) {
        logger.error(ex.getMessage());
        return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                Constant.Messages.SOMETHING_WENT_WRONG, null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<?> handleNotFoundException(EntityNotFoundException ex) {
        logger.warn(ex.getMessage());
        return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public final ResponseEntity<?> handleUnauthorizedException(UnauthorizedException ex) {
        logger.warn(ex.getMessage());
        return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
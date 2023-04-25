package com.cmdb.integration.controller.advice;

import com.cmdb.integration.util.ApiError;
import com.cmdb.integration.util.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CMDBFreshIntegrationControllerAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(CMDBFreshIntegrationControllerAdvice.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ApiError errorHandling(final UnauthorizedException exception) {
        LOGGER.error("BAD_REQUEST : ", exception);
        return new ApiError(HttpStatus.UNAUTHORIZED, "Unauthorized Exception", exception);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError errorHandling(final NotFoundException exception) {
        LOGGER.error("BAD_REQUEST : ", exception);
        return new ApiError(HttpStatus.BAD_REQUEST, "Parameter Missing", exception);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiError errorHandling(final Exception exception) {
        LOGGER.error("INTERNAL_SERVER_ERROR : ", exception);
        List<String> details = new ArrayList<>();
        details.add(exception.getLocalizedMessage());
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong !!!", details, exception);
    }
}

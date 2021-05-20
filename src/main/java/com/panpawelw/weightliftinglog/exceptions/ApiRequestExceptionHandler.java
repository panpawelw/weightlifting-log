package com.panpawelw.weightliftinglog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiRequestExceptionHandler {

  @ExceptionHandler(value = {ApiRequestException.class})
  public ResponseEntity<Object> handleApiRequestException(ApiRequestException e) {
    ApiRequestExceptionObject apiRequestExceptionObject = new ApiRequestExceptionObject(
        e.getMessage(), e, HttpStatus.NOT_FOUND, ZonedDateTime.now(ZoneId.of("Europe/Warsaw")));
    return new ResponseEntity<>(apiRequestExceptionObject, HttpStatus.NOT_FOUND);
  }
}

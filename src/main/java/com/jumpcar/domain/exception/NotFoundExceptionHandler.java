package com.jumpcar.domain.exception;

import com.jumpcar.domain.model.dto.ErrorDTO;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

import static com.jumpcar.AppConstants.UNKNOWN_ERROR_DATA;

@Produces
@Singleton
@Requires(classes = {NotFoundException.class, ExceptionHandler.class})
public class NotFoundExceptionHandler implements ExceptionHandler<NotFoundException, HttpResponse<ErrorDTO>> {

    @Override
    public HttpResponse<ErrorDTO> handle(HttpRequest request, NotFoundException exception) {
        var error = ErrorDTO.builder()
                .title(UNKNOWN_ERROR_DATA)
                .message(exception.getMessage())
                .build();
        return HttpResponse.notFound(error);
    }

}
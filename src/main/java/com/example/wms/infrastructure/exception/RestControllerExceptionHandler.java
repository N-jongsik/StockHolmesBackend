package com.example.wms.infrastructure.exception;

import com.example.wms.infrastructure.enums.ExceptionMessage;
import com.example.wms.infrastructure.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class RestControllerExceptionHandler {

    /**
     * 중복 예외 처리
     */
    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<ResponseDto<String>> handleDuplicatedException(DuplicatedException exception) {
        return buildResponse(HttpStatus.CONFLICT, ExceptionMessage.DUPLICATED.getMessage());
    }

    /**
     * 데이터 찾을 수 없음 예외 처리
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseDto<String>> handleNotFoundException(NotFoundException exception) {
        return buildResponse(HttpStatus.NOT_FOUND, ExceptionMessage.NOT_FOUND.getMessage());
    }

    /**
     * 잘못된 요청 예외 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDto<String>> handleIllegalArgumentException(IllegalArgumentException exception) {
        return buildResponse(HttpStatus.BAD_REQUEST, ExceptionMessage.ILLEGAL_ARGUMENT.getMessage());
    }

    /**
     * 권한 없음 예외 처리
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ResponseDto<String>> handleForbiddenException(ForbiddenException exception) {
        return buildResponse(HttpStatus.FORBIDDEN, ExceptionMessage.FORBIDDEN.getMessage());
    }

    private ResponseEntity<ResponseDto<String>> buildResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(ResponseDto.create(message));
    }
}

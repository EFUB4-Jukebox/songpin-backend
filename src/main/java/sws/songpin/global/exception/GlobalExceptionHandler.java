package sws.songpin.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CustomException.class})
    protected ResponseEntity<ErrorDto> handleCustomException(CustomException e, HttpServletRequest request) {
        ErrorDto errorDto = new ErrorDto(
                LocalDateTime.now().toString(),
                e.getErrorCode().getStatus(),
                e.getErrorCode().name(),
                e.getErrorCode().getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorDto, HttpStatusCode.valueOf(e.getErrorCode().getStatus()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<ErrorDto> handleBindException(MethodArgumentNotValidException e, HttpServletRequest request){
        String errorMessage = e.getFieldError().getDefaultMessage();
        ErrorCode errorCode;
        if(errorMessage.contains("-")){
            String errorName = errorMessage.split("-")[0];
            errorMessage = e.getFieldError().getField() + ":" + errorMessage.split("-")[1];
            errorCode = ErrorCode.valueOf(errorName);
        } else {
            errorMessage = e.getFieldError().getField() + ":" + errorMessage;
            errorCode = ErrorCode.INVALID_INPUT_VALUE;
        }
        ErrorDto errorDto = new ErrorDto(
                LocalDateTime.now().toString(),
                errorCode.getStatus(),
                errorCode.name(),
                errorMessage,
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorDto,HttpStatusCode.valueOf(errorCode.getStatus()));
    }
}
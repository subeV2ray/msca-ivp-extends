package cn.com.msca.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ServerWebInputException; // Spring WebFlux

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 针对 Spring WebFlux 的参数绑定异常
    @ExceptionHandler(ServerWebInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleServerWebInputException(ServerWebInputException ex) {
        log.error("WebFlux 輸入異常（400 錯誤請求）: {}", ex.getMessage(), ex);
        // 可以根据需要返回更详细的错误信息给客户端
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body("錯誤請求: " + ex.getReason());
    }

    // 捕获所有未被特定处理的异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleAllUncaughtException(Exception ex) {
        log.error("發生意外錯誤: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("內部伺服器錯誤: " + ex.getMessage());
    }
}
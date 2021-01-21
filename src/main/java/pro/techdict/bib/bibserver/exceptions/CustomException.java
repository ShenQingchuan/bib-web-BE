package pro.techdict.bib.bibserver.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class CustomException extends RuntimeException {
    private final CustomExceptionType type ; // 异常错误类型
    private final String message; // 异常信息

    public CustomException(CustomExceptionType type) {
        this.type = type;
        this.message = type.getDesc();
    }
    public CustomException(CustomExceptionType type, String message) {
        this.type = type;
        this.message = type.getDesc() + " - " + message;
    }
}
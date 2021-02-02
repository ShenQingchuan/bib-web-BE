package pro.techdict.bib.bibserver.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import pro.techdict.bib.bibserver.utils.HttpResponse;

import java.util.Arrays;

@ControllerAdvice
public class WebGlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(WebGlobalExceptionHandler.class);

    // 处理程序员主动转换的自定义异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public HttpResponse customerException(CustomException e) {
        if (e.getType() == CustomExceptionType.SERVER_SIDE_ERROR){
            // 仅将服务端异常信息持久化处理，方便运维处理
            log.debug(Arrays.toString(e.getStackTrace()));
            log.error(e.getMessage());
        }
        return HttpResponse.error(e);
    }

    // 处理程序员在程序中未能捕获（遗漏的）异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public HttpResponse exception(Exception e) {
        log.error("Error Message: " + e.getMessage());
        log.error("----- StackTrace is printed below... -----");
        e.printStackTrace();

        return HttpResponse.error(new CustomException(
                CustomExceptionType.OTHER_ERROR, e.getMessage()));
    }

}

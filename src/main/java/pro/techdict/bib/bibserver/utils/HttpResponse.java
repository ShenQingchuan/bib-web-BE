package pro.techdict.bib.bibserver.utils;

import lombok.Data;
import pro.techdict.bib.bibserver.exceptions.CustomException;
import pro.techdict.bib.bibserver.exceptions.CustomExceptionType;

/** 接口请求统一响应数据结构 */
@Data
public class HttpResponse {
    private boolean responseOk;  // 请求是否处理成功
    private int code; // 请求响应代码
    private String message;  // 请求结果描述信息
    private boolean silence = false; // 请求返回后 前端是否静默（即可能不显示任何提示）
    private Object data; // 请求结果数据（通常用于查询操作）

    public HttpResponse setSilence(boolean silence) {
        this.silence = silence;
        return this;
    }

    // 请求出现异常时的响应数据封装
    public static HttpResponse error(CustomException e) {
        HttpResponse r = new HttpResponse();
        r.setResponseOk(false);
        r.setCode(e.getType().getCode());
        r.setMessage(e.getMessage());
        return r;
    }
    // 请求出现异常时的响应数据封装
    public static HttpResponse error(CustomExceptionType customExceptionEnum,
                                     String errorMessage) {
        HttpResponse r = new HttpResponse();
        r.setResponseOk(false);
        r.setCode(customExceptionEnum.getCode());
        r.setMessage(errorMessage);
        return r;
    }
    // 指结果为负面情况，但不造成服务异常
    public static HttpResponse fail(String error) {
        HttpResponse HttpResponse = new HttpResponse();
        HttpResponse.setResponseOk(false);
        HttpResponse.setCode(200);
        HttpResponse.setMessage(error);
        return HttpResponse;
    }
    // 请求成功的响应，不带数据
    public static HttpResponse success() {
        HttpResponse HttpResponse = new HttpResponse();
        HttpResponse.setResponseOk(true);
        HttpResponse.setCode(200);
        HttpResponse.setMessage("请求成功!");
        return HttpResponse;
    }
    // 请求成功的响应，带有查询数据
    public static HttpResponse success(Object data) {
        HttpResponse HttpResponse = new HttpResponse();
        HttpResponse.setResponseOk(true);
        HttpResponse.setCode(200);
        HttpResponse.setMessage("请求成功!");
        HttpResponse.setData(data);
        return HttpResponse;
    }
    public static HttpResponse success(String message) {
        HttpResponse HttpResponse = new HttpResponse();
        HttpResponse.setResponseOk(true);
        HttpResponse.setCode(200);
        HttpResponse.setMessage(message);
        HttpResponse.setData(null);
        return HttpResponse;
    }
    // 请求成功的响应，即有信息也有数据
    public static HttpResponse success(String message, Object data) {
        HttpResponse HttpResponse = new HttpResponse();
        HttpResponse.setResponseOk(true);
        HttpResponse.setCode(200);
        HttpResponse.setMessage(message);
        HttpResponse.setData(data);
        return HttpResponse;
    }

    public HttpResponse putCode(int code) {
        this.code = code;
        return this;
    }
    public HttpResponse putMessage(String message) {
        this.message = message;
        return this;
    }
}
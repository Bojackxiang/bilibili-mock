package org.example.domain;

public class JsonResponse<T> {

    private String code;
    private String message;
    private T data;


    public JsonResponse(String code, String message){
        this.code = code;
        this.message = message;
    }

    public JsonResponse(T data){
        this.data = data;
        this.message = "成功";
        this.code = "0";
    }

    // success - 1
    public static JsonResponse<String> success(){
        return new JsonResponse<>(null);
    }

    // success - 2
    public static JsonResponse<String> success(String data){
        return new JsonResponse<>(null);
    }

    // fail - 1
    public static JsonResponse<String> fail(String message){
        return new JsonResponse<String>("1", "失败");
    }

    // fail - 2
    public static JsonResponse<String> fail(String code, String message){
        return new JsonResponse<String>(code, message);
    }







    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}

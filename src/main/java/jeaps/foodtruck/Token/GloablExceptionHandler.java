package jeaps.foodtruck.Token;


import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

// request throw an unusual exception.
@ControllerAdvice
public class GloablExceptionHandler {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e)  {
        String msg = e.getMessage();
        if (msg == null || msg.equals("")) {
            msg = "service down";
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 500);
        jsonObject.put("message", msg);
        return jsonObject;
    }
}


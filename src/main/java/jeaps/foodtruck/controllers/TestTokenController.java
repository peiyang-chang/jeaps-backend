package jeaps.foodtruck.controllers;


import com.alibaba.fastjson.JSONObject;
import jeaps.foodtruck.Token.TokenService;
import jeaps.foodtruck.Token.UserLoginToken;
import jeaps.foodtruck.common.user.user.User;
import jeaps.foodtruck.common.user.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testToken")
public class TestTokenController {
    @Autowired
    UserRepository userService;
    @Autowired
    TokenService tokenService;
    @PostMapping("/login")
    public Object login( User user){
        JSONObject jsonObject = new JSONObject();
        User userForBase = userService.findByUsername(user.getUsername());
        if(userForBase == null){
            jsonObject.put("message","user is not exist");
            return jsonObject;
        }else {
            if (!userForBase.getPassword().equals(user.getPassword())){
                jsonObject.put("message","password fail");
                return jsonObject;
            }else {
                String token = tokenService.getToken(userForBase);
                jsonObject.put("token", token);
                jsonObject.put("user", userForBase);
                return jsonObject;
            }
        }
    }

    @UserLoginToken
    @GetMapping("/getMessage")
    public String getMessage(){
        return "You are current login";
    }
}

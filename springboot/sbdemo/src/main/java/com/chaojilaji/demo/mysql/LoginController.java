package com.chaojilaji.demo.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
@RestController
public class LoginController {
    @Autowired
    LoginRepository loginRepository;


    @RequestMapping(value = "/login",method = { RequestMethod.POST})
    public LoginResultVo loginRequest(@RequestParam("data") String data){
        LoginResultVo loginResultVo = new LoginResultVo();
        try {
            Gson gson = new Gson();
            LoginRequestVo loginRequestVo = gson.fromJson(data,LoginRequestVo.class);
            int userphone = loginRequestVo.getUserphone();
            String username = loginRequestVo.getUsername();
            UsersEntity usersEntity=loginRepository.findByUserphoneAndUsername(userphone);
            String password = usersEntity.getUsername();
            if (username.equals(password)){
                loginResultVo.setCode(200);
                loginResultVo.setInfo("密码正确，登录成功");
            }else{
                loginResultVo.setCode(400);
                loginResultVo.setInfo("密码错误，登录失败");
            }
            return loginResultVo;
        }catch (Exception e){
            e.printStackTrace();
        }
        return loginResultVo;
    }
}

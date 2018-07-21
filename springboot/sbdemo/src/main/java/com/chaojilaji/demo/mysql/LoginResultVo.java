package com.chaojilaji.demo.mysql;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResultVo {
    private int code;
    private String info;
    public int getCode(){
        return code;
    }
    public String getInfo(){return info;}
    public void setCode(int code){
        this.code = code;
    }
    public void setInfo(String info){
        this.info = info;
    }
}

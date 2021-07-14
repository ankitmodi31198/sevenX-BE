package com.backend.sevenX.config;

import com.backend.sevenX.utills.Constant;

import java.util.HashMap;

public class CommonResponse {
    public HashMap<String,Object> getResponse(int status, String message, Object data){
        HashMap<String, Object> result = new HashMap<>();
        result.put(Constant.Response.STATUS,status);
        result.put(Constant.Response.MESSAGE,message);
        if(data!=null){
            result.put(Constant.Response.DATA,data);
        }
        return result;
    }
}

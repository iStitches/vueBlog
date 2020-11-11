package com.demo.blog.common;

import lombok.Data;
import org.apache.tomcat.util.bcel.Const;

/**
 * 统一结果封装
 */
@Data
public class Result {
    private Integer code;
    private String msg;
    private Object data;

    //成功
    public static Result success(Integer code,String msg,Object object){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(object);
        return result;
    }
    public static Result success(String msg,Object object){
       return success(Constants.SUCCESS,msg,object);
    }

    //失败
    public static Result fail(Integer code,String msg,Object object){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(object);
        return result;
    }
    public static Result fail(Integer code,String msg){
        return fail(code,msg,null);
    }
    public static Result fail(String msg){
        return fail(Constants.ERROR,msg,null);
    }
}

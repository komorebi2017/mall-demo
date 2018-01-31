package com.mall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * @ Author: 陌北有棵树
 * @ Date: 2018/1/10 19:17
 * @ Description: 通用的数据端响应对象，用泛型构造----高复用服务响应对象的抽象封装
 */

// 在返回时用@JsonSerialize序列化后再返回给前端
// 如果在没有data时，返回时也会是一个带有data作为key的空节点，其实是不需要返回的
// JsonSerialize.Inclusion.NON_NULL:保证序列化Json的时候，如果是null的对象，key也会消失

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
/*用泛型T来代表响应里封装的数据对象是什么类型*/
public class ServerResponse<T> implements Serializable {

    private int status;
    private String msg;
    private T data;  //泛型的好处，在返回时候可以指定泛型的内容，也可以不指定泛型里面的强制类型

    /*构造方法全设成私有的*/
    private ServerResponse(int status) {
        this.status = status;
    }

    private ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    //使之不在json序列化结果当中，如果不加@JsonIgnore，“isSuccess”就也会出现在json里
    @JsonIgnore
    public boolean isSuccess(){
        // 封装一个这个响应是正确的的一个响应
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public static <T> ServerResponse<T> createBySuccess(){
        // 这个响应只返回一个status
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        // 成功事返回一个文本供前端使用
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data){
        // 创建一个成功的服务器响应，并把data填充进去
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg,T data){
        // 把消息和数据一起传回去
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }

    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }

    // 把code做成变量的方法，因为有时需要暴露出具体错误的服务端响应
    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode, String errorMessage){
        return new ServerResponse<T>(errorCode,errorMessage);
    }

}

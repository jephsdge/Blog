package com.jephsdge.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private Integer code;
    private String message;
    private Object data;

    public static Message success(String message){
        return new Message(200,message,null);
    }
    public static Message success(String message, Object data){
        return new Message(200,message,data);
    }
    public static Message error(String message){
        return new Message(500,message,null);
    }
    public static Message error(String message,Object data){
        return new Message(500,message,data);
    }
}

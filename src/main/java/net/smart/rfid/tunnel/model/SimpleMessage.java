package net.smart.rfid.tunnel.model;
 
public class SimpleMessage {
    private String code;
    private String message;
 
    public SimpleMessage() {}
 
    public SimpleMessage(String code, String message) {
        this.code = code;
        this.message = message;
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
}
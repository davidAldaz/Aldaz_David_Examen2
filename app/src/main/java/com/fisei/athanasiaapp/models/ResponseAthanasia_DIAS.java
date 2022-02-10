package com.fisei.athanasiaapp.models;

public class ResponseAthanasia_DIAS {
    public boolean Success;
    public String Message;

    public ResponseAthanasia_DIAS(Boolean success, String message){
        this.Success = success;
        this.Message = message;
    }
}
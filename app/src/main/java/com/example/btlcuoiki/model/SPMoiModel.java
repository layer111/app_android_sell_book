package com.example.btlcuoiki.model;

import java.util.List;

public class SPMoiModel {
    boolean success;
    String message;
    List<SP> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SP> getResult() {
        return result;
    }

    public void setResult(List<SP> result) {
        this.result = result;
    }
}

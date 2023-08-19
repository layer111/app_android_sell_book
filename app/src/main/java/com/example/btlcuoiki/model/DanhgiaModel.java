package com.example.btlcuoiki.model;

import java.util.List;

public class DanhgiaModel {
    boolean success;
    String message;
    List<Danhgia> result;

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

    public List<Danhgia> getResult() {
        return result;
    }

    public void setResult(List<Danhgia> result) {
        this.result = result;
    }
}

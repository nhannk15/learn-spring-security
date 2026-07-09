package com.nhannkl.model;

public class MacResult {
    private String message;
    private String algorithm;
    private String mac;
    private boolean isValid;

    public MacResult(String message, String algorithm, String mac, boolean isValid) {
        this.message = message;
        this.algorithm = algorithm;
        this.mac = mac;
        this.isValid = isValid;
    }

    public MacResult(String message, String algorithm, String mac) {
        this.message = message;
        this.algorithm = algorithm;
        this.mac = mac;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

}

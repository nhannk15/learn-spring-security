package com.nhannkl.service;

public interface MacService {
    String generateMac(String message);
    boolean verifyMac(String message, String macToVerify);
    String getSecretKey();
}

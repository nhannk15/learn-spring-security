package com.nhannkl.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MacServiceImpl implements MacService {

    private final String key;
    private final String algorithm;

    public MacServiceImpl(String key, String algorithm) {
        this.key = key;
        this.algorithm = algorithm;
    }

    @Override
    public String generateMac(String message) {
        try {
            Mac mac = Mac.getInstance(this.algorithm);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);
            mac.init(keySpec);
            byte[] macBytes = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(macBytes);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getClass().getSimpleName());
        }
    }

    @Override
    public boolean verifyMac(String message, String macToVerify) {
        String calculatedMac = generateMac(message);
        return macToVerify.equals(calculatedMac);
    }

    @Override
    public String getSecretKey() {
        return key;
    }

}

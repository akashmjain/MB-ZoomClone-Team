package io.mountblue.ZoomClone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UrlEncryption {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public String encryption(String decryptedUrl){
        String encryptedUrl = bCryptPasswordEncoder.encode(decryptedUrl);

        return encryptedUrl;
    }

    public String decryption(String encryptedUrl){
        String decryptedUrl = encryptedUrl;

        return decryptedUrl;
    }

    public static void main(String[] args) {
        UrlEncryption urlEncryption = new UrlEncryption();
        String decryptedUrl = "main?sessionName=Session 3";

        String encryptedUrl = urlEncryption.encryption(decryptedUrl);
        System.out.println("Encrypted String: "+encryptedUrl);

        decryptedUrl = urlEncryption.decryption(encryptedUrl);
        System.out.println("Decrypted String: "+decryptedUrl);

    }
}

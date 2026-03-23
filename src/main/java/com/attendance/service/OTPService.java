package com.attendance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;

@Service
public class OTPService {
    
    @Autowired
    private EmailService emailService;
    
    // Store OTPs temporarily (in production, use Redis or database)
    private final Map<String, OTPData> otpStore = new ConcurrentHashMap<>();
    private static final int OTP_LENGTH = 6;
    private static final long OTP_VALIDITY_MS = 10 * 60 * 1000; // 10 minutes
    
    private static class OTPData {
        String otp;
        long expiryTime;
        String username;
        
        OTPData(String otp, String username) {
            this.otp = otp;
            this.username = username;
            this.expiryTime = System.currentTimeMillis() + OTP_VALIDITY_MS;
        }
        
        boolean isValid() {
            return System.currentTimeMillis() < expiryTime;
        }
    }
    
    public String generateOTP(String email, String username) {
        // Generate 6-digit OTP
        Random random = new Random();
        String otp = String.format("%06d", random.nextInt(1000000));
        
        // Store OTP
        otpStore.put(email, new OTPData(otp, username));
        
        // Send OTP via email
        emailService.sendOTP(email, otp);
        
        return otp;
    }
    
    public boolean validateOTP(String email, String otp) {
        OTPData data = otpStore.get(email);
        if (data == null) {
            return false;
        }
        
        if (!data.isValid()) {
            otpStore.remove(email);
            return false;
        }
        
        if (data.otp.equals(otp)) {
            otpStore.remove(email);
            return true;
        }
        
        return false;
    }
    
    public String getUsernameForOTP(String email) {
        OTPData data = otpStore.get(email);
        if (data != null && data.isValid()) {
            return data.username;
        }
        return null;
    }
    
    public void clearOTP(String email) {
        otpStore.remove(email);
    }
}


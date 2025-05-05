package com.amn.dto;

import lombok.Data;

@Data
public class OtpVerificationRequest {
    private String email;
    private String otpCode;
}

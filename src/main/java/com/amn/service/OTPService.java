package com.amn.service;

import com.amn.entity.OTP;
import com.amn.entity.User;
import com.amn.entity.enums.OTPChannel;
import com.amn.entity.enums.OTPStatus;
import com.amn.repository.OTPRepository;
import com.amn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OTPService {

    private final OTPRepository otpRepository;
    private final UserRepository userRepository;
    private final MailService mailService; // ✅ Inject the mail service

    // ✅ Generate and send OTP via email
    public OTP generateOtpForUser(Long userId, OTPChannel channel) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String code = String.valueOf(new Random().nextInt(900000) + 100000);

        OTP otp = OTP.builder()
                .code(code)
                .expiration(LocalDateTime.now().plusMinutes(5))
                .status(OTPStatus.PENDING)
                .channel(channel)
                .user(user)
                .build();

        otpRepository.save(otp);

        // ✅ Send OTP via email
        mailService.sendOtpEmail(user.getEmail(), code);

        return otp;
    }

    public boolean verifyOtp(Long userId, String code) {
        Optional<OTP> latestOtpOpt = otpRepository.findTopByUserIdOrderByExpirationDesc(userId);

        if (latestOtpOpt.isEmpty()) {
            throw new RuntimeException("OTP not found");
        }

        OTP otp = latestOtpOpt.get();

        if (otp.getCode().equals(code) && otp.getExpiration().isAfter(LocalDateTime.now())) {
            otp.setStatus(OTPStatus.VERIFIED);
            otpRepository.save(otp);
            return true;
        } else {
            otp.setStatus(OTPStatus.EXPIRED);
            otpRepository.save(otp);
            return false;
        }
    }
    public void sendOtpToUser(User user) {
        generateOtpForUser(user.getId(), OTPChannel.EMAIL);
    }


}

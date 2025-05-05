package com.amn.entity;

import com.amn.entity.enums.OTPChannel;
import com.amn.entity.enums.OTPStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private LocalDateTime expiration;
    private Boolean used;
    @Enumerated(EnumType.STRING)
    private OTPStatus status;

    // Relationship with Patient

    @Enumerated(EnumType.STRING)
    private OTPChannel channel;
    // Relationship with Doctor who generated this OTP
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    private Patient patient; //

}
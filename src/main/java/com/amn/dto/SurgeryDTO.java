package com.amn.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurgeryDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate uploadDate;
    private String url;
    private String surgeonName;
    private String hospitalName;
}

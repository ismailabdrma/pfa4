package com.amn.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate uploadDate;
    private String url;
}
package com.amn.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScanDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate uploadDate;
    private String url;
}

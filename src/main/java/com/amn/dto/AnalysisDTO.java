package com.amn.dto;

import com.amn.entity.Analysis;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate uploadDate;
    private String fileType;
    private String url;

    public static AnalysisDTO fromEntity(Analysis analysis) {
        return AnalysisDTO.builder()
                .id(analysis.getId())
                .title(analysis.getTitle())
                .description(analysis.getDescription())
                .uploadDate(analysis.getUploadDate())
                .fileType(analysis.getFileType().name()) // ✅ fixed
                .url(analysis.getUrl())
                .build();
    }
}

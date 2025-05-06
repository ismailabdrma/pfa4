package com.amn.dto;

import com.amn.entity.Scan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScanDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate uploadDate;
    private String fileType;
    private String url;

    public static ScanDTO fromEntity(Scan scan) {
        return ScanDTO.builder()
                .id(scan.getId())
                .title(scan.getTitle())
                .description(scan.getDescription())
                .uploadDate(scan.getUploadDate())
                .fileType(scan.getFileType().name()) // ✅ Fixed here
                .url(scan.getUrl())
                .build();
    }
}


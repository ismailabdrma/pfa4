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
    private String url;
    private String uploadDate;

    public static ScanDTO fromEntity(Scan scan) {
        ScanDTO dto = new ScanDTO();
        dto.setId(scan.getId());
        dto.setTitle(scan.getTitle());
        dto.setDescription(scan.getDescription());
        dto.setUploadDate(scan.getUploadDate().toString());
        dto.setUrl(scan.getUrl());
        return dto;
    }
}

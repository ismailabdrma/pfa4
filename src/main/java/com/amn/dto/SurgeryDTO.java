package com.amn.dto;

import com.amn.entity.Surgery;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SurgeryDTO {
    private Long id;
    private String title;
    private String description;
    private String surgeonName;
    private String hospitalName;
    private LocalDate uploadDate;
    private String fileType;
    private String url;

    public static SurgeryDTO fromEntity(Surgery surgery) {
        return SurgeryDTO.builder()
                .id(surgery.getId())
                .title(surgery.getTitle())
                .description(surgery.getDescription())
                .surgeonName(surgery.getSurgeonName())
                .hospitalName(surgery.getHospitalName())
                .uploadDate(surgery.getUploadDate())
                .fileType(surgery.getFileType().name()) // ✅ fixed
                .url(surgery.getUrl())
                .build();
    }
}

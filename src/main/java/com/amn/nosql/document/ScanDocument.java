package com.amn.nosql.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "scans")
public class ScanDocument {
    @Id
    private String id;
    private String fileUrl;
    private String scanType;
    private String title;
    private String medicalFolderId;
    private String uploadedByDoctor;
    private String patientCin;
    private LocalDateTime uploadDate;
}

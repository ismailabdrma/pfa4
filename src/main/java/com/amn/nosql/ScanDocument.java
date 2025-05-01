package com.amn.nosql;

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

    private String fileUrl;             // Link to the scan in Cloudinary
    private String scanType;            // e.g. MRI, CT, X-Ray
    private String title;               // Optional title
    private String medicalFolderId;     // Link to SQL folder entity (String ID for relation)
    private String uploadedByDoctor;    // Doctor's full name or ID
    private String patientCin;          // Useful reference to patient
    private LocalDateTime uploadDate;   // Use LocalDateTime instead of String
}

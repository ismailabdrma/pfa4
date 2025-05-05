package com.amn.nosql.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "surgeries")
public class SurgeryDocument {
    @Id
    private String id;
    private String fileUrl;
    private String procedureType;
    private String surgeonName;
    private String hospitalName;
    private String patientCin;
    private String uploadedByDoctor;
    private String uploadDate;
}

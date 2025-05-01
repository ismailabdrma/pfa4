package com.amn.nosql;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "analyses")
public class AnalysisDocument {
    @Id
    private String id;

    private String fileUrl;
    private String analysisType;
    private String patientCin;
    private String uploadedByDoctor;
    private String uploadDate;
}

package com.amn.nosql;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface AnalysisDocumentRepository extends MongoRepository<AnalysisDocument, String> {
    List<AnalysisDocument> findByPatientCin(String patientCin);
}

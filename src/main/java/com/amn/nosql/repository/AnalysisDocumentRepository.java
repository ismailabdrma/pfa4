package com.amn.nosql.repository;

import com.amn.nosql.document.AnalysisDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface AnalysisDocumentRepository extends MongoRepository<AnalysisDocument, String> {
    List<AnalysisDocument> findByPatientCin(String patientCin);
}

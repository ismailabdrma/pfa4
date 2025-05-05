package com.amn.nosql.repository;

import com.amn.nosql.document.ScanDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ScanDocumentRepository extends MongoRepository<ScanDocument, String> {
    List<ScanDocument> findByPatientCin(String patientCin);
}

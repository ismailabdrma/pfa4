package com.amn.nosql;


import com.amn.nosql.ScanDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ScanDocumentRepository extends MongoRepository<ScanDocument, String> {
    List<ScanDocument> findByPatientCin(String patientCin);
}


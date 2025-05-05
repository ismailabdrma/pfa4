package com.amn.nosql.repository;

import com.amn.nosql.document.SurgeryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface SurgeryDocumentRepository extends MongoRepository<SurgeryDocument, String> {
    List<SurgeryDocument> findByPatientCin(String patientCin);
}

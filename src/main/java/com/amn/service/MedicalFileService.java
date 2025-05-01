package com.amn.service;

import com.amn.nosql.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalFileService {

    private final ScanDocumentRepository scanDocumentRepository;
    private final SurgeryDocumentRepository surgeryDocumentRepository;
    private final AnalysisDocumentRepository analysisDocumentRepository;

    // Fetch all scans for a patient
    public List<ScanDocument> getScansByPatientCin(String patientCin) {
        return scanDocumentRepository.findByPatientCin(patientCin);
    }

    // Fetch all surgeries for a patient
    public List<SurgeryDocument> getSurgeriesByPatientCin(String patientCin) {
        return surgeryDocumentRepository.findByPatientCin(patientCin);
    }

    // Fetch all analysis for a patient
    public List<AnalysisDocument> getAnalysisByPatientCin(String patientCin) {
        return analysisDocumentRepository.findByPatientCin(patientCin);
    }
}

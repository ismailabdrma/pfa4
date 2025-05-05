package com.amn.service;

import com.amn.entity.*;
import com.amn.nosql.document.ScanDocument;
import com.amn.nosql.repository.ScanDocumentRepository;
import com.amn.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalDocumentService {

    private final MedicalFolderRepository medicalFolderRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;
    private final VaccinationRepository vaccinationRepository;
    private final VisitLogRepository visitLogRepository;
    private final FileStorageService fileStorageService;
    private final ScanDocumentRepository scanDocumentRepository;// for files (scan, analysis, surgery)

    // ----------- View methods (simple) -----------

    public List<MedicalRecord> viewAllMedicalRecords(Long folderId) {
        return medicalRecordRepository.findByMedicalFolderId(folderId);
    }

    public List<Appointment> viewAllAppointments(Long folderId) {
        return appointmentRepository.findByMedicalFolderId(folderId);
    }

    public List<Vaccination> viewAllVaccinations(Long folderId) {
        return vaccinationRepository.findByMedicalFolder_Id(folderId);
    }

    public List<VisitLog> viewAllVisitLogs(Long folderId) {
        return visitLogRepository.findByMedicalFolder_Id(folderId);
    }

    // ----------- Add methods (simple) -----------

    public MedicalRecord addMedicalRecord(Long folderId, MedicalRecord record) {
        MedicalFolder folder = findFolder(folderId);
        record.setMedicalFolder(folder);
        return medicalRecordRepository.save(record);
    }

    public Appointment addAppointment(Long folderId, Appointment appointment) {
        MedicalFolder folder = findFolder(folderId);
        appointment.setMedicalFolder(folder);
        return appointmentRepository.save(appointment);
    }

    public Vaccination addVaccination(Long folderId, Vaccination vaccination) {
        MedicalFolder folder = findFolder(folderId);
        vaccination.setMedicalFolder(folder);
        return vaccinationRepository.save(vaccination);
    }

    public VisitLog addVisitLog(Long folderId, VisitLog visitLog) {
        MedicalFolder folder = findFolder(folderId);
        visitLog.setMedicalFolder(folder);
        return visitLogRepository.save(visitLog);
    }

    // ----------- Add methods (file upload) -----------

    public Scan uploadScan(Long folderId, MultipartFile file, String scanType) {
        return fileStorageService.saveScan(file, scanType, folderId);
    }

    public Analysis uploadAnalysis(Long folderId, MultipartFile file, String type) {
        return fileStorageService.saveAnalysis(file, type, folderId);
    }

    public Surgery uploadSurgery(Long folderId, MultipartFile file, String procedureType, String surgeonName, String hospitalName, String complications) {
        return fileStorageService.saveSurgery(file, procedureType, surgeonName, hospitalName, complications, folderId);
    }

    // ----------- Utility -----------

    private MedicalFolder findFolder(Long folderId) {
        return medicalFolderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Medical Folder not found"));
    }


    public ScanDocument addScan(String medicalFolderId, ScanDocument scan) {
        scan.setMedicalFolderId(medicalFolderId);
        return scanDocumentRepository.save(scan);}
}

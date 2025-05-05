package com.amn.service;

import com.amn.entity.*;
import com.amn.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final ScanRepository scanRepository;
    private final AnalysisRepository analysisRepository;
    private final SurgeryRepository surgeryRepository;
    private final MedicalFolderRepository medicalFolderRepository;

    public Scan saveScan(MultipartFile file, String scanType, Long folderId) {
        MedicalFolder folder = findFolder(folderId);
        String fileUrl = uploadFile(file);

        Scan scan = Scan.builder()
                .type("scan")
                .url(fileUrl)
                .medicalFolder(folder)
                .build();

        return scanRepository.save(scan);
    }

    public Analysis saveAnalysis(MultipartFile file, String type, Long folderId) {
        MedicalFolder folder = findFolder(folderId);
        String fileUrl = uploadFile(file);

        Analysis analysis = Analysis.builder()
                .type(type)
                .uploadDate(LocalDate.from(LocalDateTime.now()))
                .url(fileUrl)
                .medicalFolder(folder)
                .build();

        return analysisRepository.save(analysis);
    }

    public Surgery saveSurgery(MultipartFile file, String procedureType, String surgeonName, String hospitalName, String complications, Long folderId) {
        MedicalFolder folder = findFolder(folderId);
        String fileUrl = uploadFile(file);

        Surgery surgery = Surgery.builder()
                .type("operation")
                .surgeonName(surgeonName)
                .hospitalName(hospitalName)
                .url(fileUrl)
                .medicalFolder(folder)
                .build();

        return surgeryRepository.save(surgery);
    }

    private String uploadFile(MultipartFile file) {
        try {
            // Later integrate with Cloudinary / AWS
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            System.out.println("Uploading file: " + filename);
            String fileUrl = "https://your-cloud-storage.com/amn/" + filename; // temporary
            return fileUrl;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    private MedicalFolder findFolder(Long folderId) {
        return medicalFolderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Medical Folder not found"));
    }
    public Scan saveScanFromLink(Scan scan, Long folderId) {
        scan.setMedicalFolder(findFolder(folderId));
        scan.setUploadDate(LocalDate.from(LocalDateTime.now()));
        return scanRepository.save(scan);
    }

    public Analysis saveAnalysisFromLink(Analysis analysis, Long folderId) {
        analysis.setMedicalFolder(findFolder(folderId));
        analysis.setUploadDate(LocalDate.from(LocalDateTime.now()));
        return analysisRepository.save(analysis);
    }

    public Surgery saveSurgeryFromLink(Surgery surgery, Long folderId) {
        surgery.setMedicalFolder(findFolder(folderId));
        surgery.setUploadDate(LocalDate.from(LocalDateTime.now()));
        return surgeryRepository.save(surgery);
    }
    public List<Scan> getScansByFolderId(Long folderId) {
        return scanRepository.findByMedicalFolderId(folderId);
    }

    public List<Analysis> getAnalysesByFolderId(Long folderId) {
        return analysisRepository.findByMedicalFolderId(folderId);
    }

    public List<Surgery> getSurgeriesByFolderId(Long folderId) {
        return surgeryRepository.findByMedicalFolderId(folderId);
    }

    public Scan getScanById(Long id) {
        return scanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Scan not found with id: " + id));
    }

    public Analysis getAnalysisById(Long id) {
        return analysisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Analysis not found with id: " + id));
    }

    public Surgery getSurgeryById(Long id) {
        return surgeryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Surgery not found with id: " + id));
    }


}

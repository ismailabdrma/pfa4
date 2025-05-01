package com.amn.service;

import com.amn.entity.*;
import com.amn.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

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
                .uploadDate(LocalDateTime.now())
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
}

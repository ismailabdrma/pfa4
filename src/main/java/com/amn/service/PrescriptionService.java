package com.amn.service;

import com.amn.dto.MedicationDTO;
import com.amn.dto.PrescriptionDTO;
import com.amn.dto.PrescriptionRequest;
import com.amn.entity.*;
import com.amn.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final MedicationRepository medicationRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    /**
     * ✅ Create a prescription and link it to a patient.
     */
    public PrescriptionDTO createPrescription(Long patientId, Prescription prescription) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        prescription.setPrescribedDate(LocalDateTime.now());
        prescription.setPatient(patient);

        Prescription saved = prescriptionRepository.save(prescription);
        return PrescriptionDTO.fromEntity(saved);
    }

    /**
     * ✅ Attach medications to a prescription.
     */
    public PrescriptionDTO addMedications(Long prescriptionId, List<Medication> medications) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        for (Medication med : medications) {
            med.setPrescription(prescription);
        }

        medicationRepository.saveAll(medications);
        prescription.setMedications(medications);

        return PrescriptionDTO.fromEntity(prescription);
    }

    /**
     * ✅ Create a prescription with medications
     */
    public PrescriptionDTO createPrescriptionWithMedications(Long patientId, Long recordId, PrescriptionRequest request, String doctorEmail) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Medical Record not found"));

        Prescription prescription = new Prescription();
        prescription.setMedicationName(request.getPrescription().getMedicationName());
        prescription.setDosage(request.getPrescription().getDosage());
        prescription.setPeriod(request.getPrescription().getPeriod());
        prescription.setPermanent(request.getPrescription().isPermanent());
        prescription.setStatus(request.getPrescription().getStatus());
        prescription.setPatient(patient);
        prescription.setPrescribingDoctor(doctor);
        prescription.setMedicalRecord(record);
        prescription.setPrescribedDate(LocalDateTime.now());

        Prescription saved = prescriptionRepository.save(prescription);

        for (Medication med : request.getMedications()) {
            med.setPrescription(saved);
            med.setPrice(0.0);
        }

        medicationRepository.saveAll(request.getMedications());
        saved.setMedications(request.getMedications());

        return PrescriptionDTO.fromEntity(saved);
    }

    /**
     * ✅ Fetch a prescription by ID, including patient info
     */
    public PrescriptionDTO getById(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        return PrescriptionDTO.fromEntity(prescription);
    }

    /**
     * ✅ Get all prescriptions without filtering
     */
    public List<PrescriptionDTO> getAllPrescriptions() {
        return prescriptionRepository.findAll().stream()
                .map(PrescriptionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ✅ Fetch prescriptions by patient CIN and full name
     */
    public List<PrescriptionDTO> getPrescriptionsByPatient(String cin, String fullName) {
        return prescriptionRepository.findAll().stream()
                .filter(p -> p.getPatient().getCin().equalsIgnoreCase(cin)
                        && p.getPatient().getFullName().equalsIgnoreCase(fullName))
                .map(PrescriptionDTO::fromEntity)
                .collect(Collectors.toList());
    }
    /**
     * ✅ Fetch prescriptions by Medical Record ID
     */
    public List<PrescriptionDTO> getPrescriptionsByRecordId(Long recordId) {
        List<Prescription> prescriptions = prescriptionRepository.findAll().stream()
                .filter(prescription -> prescription.getMedicalRecord() != null
                        && prescription.getMedicalRecord().getId().equals(recordId))
                .collect(Collectors.toList());

        return prescriptions.stream()
                .map(prescription -> {
                    List<MedicationDTO> medications = medicationRepository.findByPrescriptionId(prescription.getId()).stream()
                            .map(MedicationDTO::fromEntity)
                            .collect(Collectors.toList());

                    PrescriptionDTO dto = PrescriptionDTO.fromEntity(prescription);
                    dto.setMedications(medications);
                    return dto;
                })
                .collect(Collectors.toList());
    }

}

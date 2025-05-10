import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { DoctorService } from 'src/app/services/doctor.service';

@Component({
  selector: 'app-doctor',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './doctor.component.html',
  styleUrls: ['./doctor.component.css']
})
export class DoctorComponent implements OnInit {
  doctorName = '';
  doctorEmail = '';
  cin = '';
  fullName = '';
  patientProfile: any = null;
  showEditProfileForm = false;
  patientId: number | null = null;
  folderId: number | null = null;

  // Manual form data
  manualData = {
    bloodType: '',
    emergencyContact: '',
    allergies: '',
    chronicDiseases: '',
    hasHeartProblem: false,
    hasSurgery: false,
    birthDate: ''
  };

  // Vaccination form data
  vaccination = {
    vaccineName: '',
    doseNumber: 1,
    manufacturer: '',
    date: ''
  };

  // Upload data
  scanPayload = { title: '', description: '' };
  analysisPayload = { title: '', description: '' };
  surgeryPayload = { title: '', description: '' };

  // Consultation and prescription data
  consultation = {
    reason: '',
    notes: '',
    diagnosis: ''
  };

  medications: any[] = [];

  scanFile: File | null = null;
  analysisFile: File | null = null;
  surgeryFile: File | null = null;

  // Active tab for medical documents
  activeDocTab: string = 'scans';

  constructor(private doctorService: DoctorService, private router: Router) {}

  ngOnInit(): void {
    this.loadDoctorProfile();
  }

  loadDoctorProfile(): void {
    this.doctorService.getCurrentDoctorProfile().subscribe({
      next: (doctor) => {
        this.doctorName = doctor.fullName || 'Docteur';
      },
      error: () => {
        console.error('Erreur lors du chargement du profil du médecin.');
      }
    });
  }

  searchPatient(): void {
    this.doctorService.getFullPatientProfile(this.cin, this.fullName).subscribe({
      next: (profile) => {
        this.patientProfile = profile;
        this.patientId = profile.id;
        this.folderId = profile.medicalFolderId;

        // Initialize manual data with existing profile data
        if (profile) {
          this.manualData.bloodType = profile.bloodType || '';
          this.manualData.allergies = profile.allergies || '';
          this.manualData.chronicDiseases = profile.chronicDiseases || '';
          this.manualData.emergencyContact = profile.emergencyContact || '';
        }
      },
      error: () => {
        alert('Patient introuvable.');
      }
    });
  }

  editPatientProfile(): void {
    this.showEditProfileForm = true;
  }

  submitManualFolder(): void {
    if (!this.cin || !this.fullName || !this.folderId) return;

    this.doctorService.createOrUpdateMedicalFolder(this.cin, this.fullName, this.manualData).subscribe({
      next: () => {
        alert('Dossier médical mis à jour avec succès.');
        this.showEditProfileForm = false;
        this.searchPatient(); // Refresh patient data
      },
      error: () => {
        console.error('Erreur lors de la mise à jour du dossier médical.');
      }
    });
  }

  // File upload handlers
  onScanFileChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.scanFile = input.files ? input.files[0] : null;
  }

  onAnalysisFileChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.analysisFile = input.files ? input.files[0] : null;
  }

  onSurgeryFileChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.surgeryFile = input.files ? input.files[0] : null;
  }

  uploadScan(): void {
    if (!this.folderId || !this.scanFile) {
      alert('Veuillez sélectionner un fichier et remplir tous les champs.');
      return;
    }

    if (!this.scanPayload.title.trim()) {
      alert('Le titre est obligatoire.');
      return;
    }

    const formData = new FormData();
    formData.append('file', this.scanFile);
    formData.append('title', this.scanPayload.title);
    formData.append('description', this.scanPayload.description);

    this.doctorService.uploadScan(formData, this.folderId).subscribe({
      next: () => {
        alert('Scan ajouté avec succès.');
        this.resetScanForm();
        this.searchPatient(); // Refresh data
        this.setActiveDocTab('scans'); // Switch to scans tab to show the newly added scan
      },
      error: (err) => {
        console.error('Erreur lors de l\'ajout du scan:', err);
        alert('Erreur lors de l\'ajout du scan. Veuillez réessayer.');
      }
    });
  }

  uploadAnalysis(): void {
    if (!this.folderId || !this.analysisFile) {
      alert('Veuillez sélectionner un fichier et remplir tous les champs.');
      return;
    }

    if (!this.analysisPayload.title.trim()) {
      alert('Le titre est obligatoire.');
      return;
    }

    const formData = new FormData();
    formData.append('file', this.analysisFile);
    formData.append('title', this.analysisPayload.title);
    formData.append('description', this.analysisPayload.description);

    this.doctorService.uploadAnalysis(formData, this.folderId).subscribe({
      next: () => {
        alert('Analyse ajoutée avec succès.');
        this.resetAnalysisForm();
        this.searchPatient(); // Refresh data
        this.setActiveDocTab('analyses'); // Switch to analyses tab to show the newly added analysis
      },
      error: (err) => {
        console.error('Erreur lors de l\'ajout de l\'analyse:', err);
        alert('Erreur lors de l\'ajout de l\'analyse. Veuillez réessayer.');
      }
    });
  }

  uploadSurgery(): void {
    if (!this.folderId || !this.surgeryFile) {
      alert('Veuillez sélectionner un fichier et remplir tous les champs.');
      return;
    }

    if (!this.surgeryPayload.title.trim()) {
      alert('Le titre est obligatoire.');
      return;
    }

    const formData = new FormData();
    formData.append('file', this.surgeryFile);
    formData.append('title', this.surgeryPayload.title);
    formData.append('description', this.surgeryPayload.description);

    this.doctorService.uploadSurgery(formData, this.folderId).subscribe({
      next: () => {
        alert('Chirurgie ajoutée avec succès.');
        this.resetSurgeryForm();
        this.searchPatient(); // Refresh data
        this.setActiveDocTab('surgeries'); // Switch to surgeries tab to show the newly added surgery
      },
      error: (err) => {
        console.error('Erreur lors de l\'ajout de la chirurgie:', err);
        alert('Erreur lors de l\'ajout de la chirurgie. Veuillez réessayer.');
      }
    });
  }

  resetScanForm(): void {
    this.scanPayload = { title: '', description: '' };
    this.scanFile = null;
    // Reset file input
    const fileInput = document.getElementById('scanFileInput') as HTMLInputElement;
    if (fileInput) fileInput.value = '';
  }

  resetAnalysisForm(): void {
    this.analysisPayload = { title: '', description: '' };
    this.analysisFile = null;
    // Reset file input
    const fileInput = document.getElementById('analysisFileInput') as HTMLInputElement;
    if (fileInput) fileInput.value = '';
  }

  resetSurgeryForm(): void {
    this.surgeryPayload = { title: '', description: '' };
    this.surgeryFile = null;
    // Reset file input
    const fileInput = document.getElementById('surgeryFileInput') as HTMLInputElement;
    if (fileInput) fileInput.value = '';
  }

  // Method to set active tab
  setActiveDocTab(tabId: string): void {
    this.activeDocTab = tabId;

    // This is a workaround for programmatic tab activation if not using Bootstrap JS
    // First, remove active class from all tabs
    document.querySelectorAll('.nav-link').forEach(el => {
      el.classList.remove('active');
      el.setAttribute('aria-selected', 'false');
    });

    // Then add active class to the selected tab
    const selectedTab = document.getElementById(`${tabId}-tab`);
    if (selectedTab) {
      selectedTab.classList.add('active');
      selectedTab.setAttribute('aria-selected', 'true');
    }

    // Hide all tab panes
    document.querySelectorAll('.tab-pane').forEach(el => {
      el.classList.remove('show', 'active');
    });

    // Show the selected tab pane
    const selectedPane = document.getElementById(tabId);
    if (selectedPane) {
      selectedPane.classList.add('show', 'active');
    }
  }

  // Vaccination methods with enhanced validation
  addVaccination(): void {
    if (!this.folderId) {
      alert("Veuillez d'abord sélectionner un patient.");
      return;
    }

    // Basic validation
    if (!this.vaccination.vaccineName.trim()) {
      alert("Le nom du vaccin est obligatoire.");
      return;
    }

    if (!this.vaccination.manufacturer.trim()) {
      alert("Le fabricant est obligatoire.");
      return;
    }

    if (!this.vaccination.date) {
      alert("La date de vaccination est obligatoire.");
      return;
    }

    this.doctorService.addVaccination(
      this.folderId,
      this.vaccination.vaccineName,
      this.vaccination.doseNumber,
      this.vaccination.manufacturer,
      this.vaccination.date
    ).subscribe({
      next: () => {
        alert('Vaccination ajoutée avec succès.');
        this.vaccination = {
          vaccineName: '',
          doseNumber: 1,
          manufacturer: '',
          date: ''
        };
        this.searchPatient(); // Refresh data
        this.setActiveDocTab('vaccinations'); // Switch to vaccinations tab to show the newly added vaccination
      },
      error: (err) => {
        console.error('Erreur lors de l\'ajout de la vaccination:', err);
        alert('Erreur lors de l\'ajout de la vaccination. Veuillez vérifier les données et réessayer.');
      }
    });
  }

  // Consultation and prescription methods
  createConsultation(): void {
    if (!this.patientId) return;

    if (!this.consultation.reason.trim()) {
      alert("Le motif de consultation est obligatoire.");
      return;
    }

    const consultationData = {
      reason: this.consultation.reason,
      notes: this.consultation.notes,
      diagnosis: this.consultation.diagnosis
    };

    this.doctorService.createMedicalRecord(this.patientId, consultationData).subscribe({
      next: (record) => {
        alert('Consultation créée avec succès.');
        this.searchPatient(); // Refresh data
      },
      error: (err) => {
        console.error('Erreur lors de la création de la consultation:', err);
        alert('Erreur lors de la création de la consultation. Veuillez réessayer.');
      }
    });
  }

  addMedication(): void {
    this.medications.push({
      name: '',
      dosage: '',
      period: ''
    });
  }

  removeMedication(index: number): void {
    this.medications.splice(index, 1);
  }

  submitPrescription(): void {
    if (!this.patientId || !this.patientProfile?.medicalRecords?.length) {
      alert("Veuillez créer une consultation avant d'ajouter une ordonnance.");
      return;
    }

    // Validate medications
    if (this.medications.length === 0) {
      alert("Veuillez ajouter au moins un médicament à l'ordonnance.");
      return;
    }

    // Check if all medications have required fields
    const invalidMeds = this.medications.some(med =>
      !med.name.trim() || !med.dosage.trim() || !med.period.trim()
    );

    if (invalidMeds) {
      alert("Veuillez remplir tous les champs pour chaque médicament.");
      return;
    }

    // Get the latest medical record ID
    const latestRecordId = this.patientProfile.medicalRecords[this.patientProfile.medicalRecords.length - 1].id;

    const prescriptionData = {
      medications: this.medications,
      notes: this.consultation.notes
    };

    this.doctorService.submitPrescriptionWithMedications(
      this.patientId,
      latestRecordId,
      prescriptionData
    ).subscribe({
      next: () => {
        alert('Ordonnance créée avec succès.');
        this.medications = [];
        this.consultation = {
          reason: '',
          notes: '',
          diagnosis: ''
        };
        this.searchPatient(); // Refresh data
      },
      error: (err) => {
        console.error('Erreur lors de la création de l\'ordonnance:', err);
        alert('Erreur lors de la création de l\'ordonnance. Veuillez réessayer.');
      }
    });
  }

  // Navigation methods
  navigateToConsultation(): void {
    if (!this.patientProfile) {
      alert("Veuillez d'abord sélectionner un patient.");
      return;
    }
    this.router.navigate([
      '/dashboard/doctor/consultation-prescription',
      this.cin,
      this.fullName
    ]);
  }

  viewConsultationDetails(recordId: number): void {
    this.router.navigate(['/dashboard/doctor/consultation', recordId]);
  }

  viewPrescriptionDetails(prescriptionId: number): void {
    this.router.navigate(['/dashboard/doctor/prescription', prescriptionId]);
  }

  // Method to open a file in a new tab
  openFile(fileUrl: string): void {
    if (fileUrl) {
      const fullUrl = 'http://localhost:8080' + fileUrl;
      window.open(fullUrl, '_blank');
    } else {
      alert('Lien du fichier invalide ou manquant.');
    }
  }
}

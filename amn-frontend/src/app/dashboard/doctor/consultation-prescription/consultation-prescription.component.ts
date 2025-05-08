import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { DoctorService } from 'src/app/services/doctor.service';

@Component({
  selector: 'app-consultation-prescription',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './consultation-prescription.component.html',
})
export class ConsultationPrescriptionComponent implements OnInit {
  cin: string = '';
  fullName: string = '';
  patientId: number | null = null;
  loading: boolean = false;
  success: boolean = false;

  record = {
    reason: '',
    diagnosis: '',
    notes: ''
  };

  medications = [
    { name: '', dosage: '', period: '', permanent: false }
  ];

  constructor(
    private doctorService: DoctorService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.cin = params['cin'];
      this.fullName = params['fullName'];

      if (this.cin && this.fullName) {
        this.searchPatient();
      }
    });
  }

  searchPatient() {
    if (!this.cin || !this.fullName) return;

    this.loading = true;
    this.doctorService.getPatientIdByCin(this.cin, this.fullName).subscribe({
      next: id => {
        this.patientId = id;
        this.loading = false;
      },
      error: (error) => {
        console.error('Patient not found', error);
        this.loading = false;
        alert('Patient introuvable.');
        // Navigate back to dashboard
        this.router.navigate(['/doctor/dashboard']);
      }
    });
  }

  addMedication() {
    this.medications.push({ name: '', dosage: '', period: '', permanent: false });
  }

  removeMedication(index: number) {
    if (this.medications.length > 1) {
      this.medications.splice(index, 1);
    } else {
      // Don't remove the last medication, just clear it
      this.medications[0] = { name: '', dosage: '', period: '', permanent: false };
    }
  }

  submit() {
    if (!this.patientId) {
      alert("Veuillez d'abord chercher le patient.");
      return;
    }

    if (!this.record.reason || !this.record.diagnosis) {
      alert("Veuillez remplir le motif et le diagnostic.");
      return;
    }

    if (!this.medications[0].name || !this.medications[0].dosage) {
      alert("Veuillez ajouter au moins un médicament avec nom et dosage.");
      return;
    }

    this.loading = true;

    // Step 1: Create consultation record
    this.doctorService.createMedicalRecord(this.patientId, this.record).subscribe({
      next: (createdRecord) => {
        const recordId = createdRecord.id;

        // Step 2: Create prescription linked to this consultation
        const prescription = {
          medicationName: this.medications.length > 1 ? 'Ordonnance multiple' : this.medications[0].name,
          dosage: this.medications.length > 1 ? '' : this.medications[0].dosage,
          period: this.medications.length > 1 ? '' : this.medications[0].period,
          permanent: this.medications.length > 1 ? false : this.medications[0].permanent,
          status: 'PENDING'
        };

        const request = {
          prescription,
          medications: this.medications
        };

        this.doctorService.submitPrescriptionWithMedications(this.patientId!, recordId, request).subscribe({
          next: () => {
            this.loading = false;
            this.success = true;

            // Reset form
            this.record = { reason: '', diagnosis: '', notes: '' };
            this.medications = [{ name: '', dosage: '', period: '', permanent: false }];

            // Automatically return to dashboard after 2 seconds
            setTimeout(() => {
              this.router.navigate(['/doctor/dashboard']);
            }, 2000);
          },
          error: (error) => {
            console.error('Prescription save error', error);
            this.loading = false;
            alert("Erreur lors de l'enregistrement de l'ordonnance.");
          }
        });
      },
      error: (error) => {
        console.error('Consultation save error', error);
        this.loading = false;
        alert("Erreur lors de l'enregistrement de la consultation.");
      }
    });
  }

  cancel() {
    this.router.navigate(['/doctor/dashboard']);
  }
}

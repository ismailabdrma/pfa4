import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PharmacistService } from 'src/app/services/pharmacist.service';

// Define interfaces locally if they're not exported from the service
interface MedicationDTO {
  id: number;
  name: string;
  dosage: string;
  period: string;
  permanent: boolean;
}

interface PrescriptionDTO {
  id: number;
  status: string;
  permanent: boolean;
  prescribedDate: string;
  dispensedDate: string;
  prescribingDoctorName: string;
  dispensingPharmacistName: string;
  medicalRecordId: number;
  medicalFolderId: number;
  patientId: number;
  patientName: string;
  patientCIN: string;
  medications: MedicationDTO[];
}

@Component({
  selector: 'app-pharmacist',
  standalone: true,
  templateUrl: './pharmacist.component.html',
  styleUrls: ['./pharmacist.component.css'],
  imports: [CommonModule, FormsModule]
})
export class PharmacistComponent implements OnInit {

  cin: string = '';
  fullName: string = '';
  prescriptions: PrescriptionDTO[] = [];
  errorMessage: string = '';
  successMessage: string = '';
  isLoading: boolean = false;
  searchPerformed: boolean = false;

  // Search filter - Default to PENDING to show prescriptions that need to be dispensed
  statusFilter: string = 'PENDING';

  // Selected prescription for detailed view
  selectedPrescription: PrescriptionDTO | null = null;

  constructor(private pharmacistService: PharmacistService) {}

  ngOnInit(): void {}

  /**
   * ✅ Search Prescriptions for Patient
   */
  searchPrescriptions(): void {
    // Clear previous results
    this.prescriptions = [];
    this.errorMessage = '';
    this.successMessage = '';
    this.selectedPrescription = null;

    // Validate input
    if (!this.cin.trim() || !this.fullName.trim()) {
      this.errorMessage = 'Veuillez entrer le CIN et le nom complet.';
      return;
    }

    // Set loading state
    this.isLoading = true;
    this.searchPerformed = true;

    // Log for debugging
    console.log(`Searching ${this.statusFilter} prescriptions for CIN: ${this.cin}, Name: ${this.fullName}`);

    this.pharmacistService.getPrescriptions(this.cin, this.fullName, this.statusFilter).subscribe({
      next: (data) => {
        this.isLoading = false;
        this.prescriptions = data;
        console.log(`Found ${data.length} prescriptions:`, data);

        if (data.length === 0) {
          if (this.statusFilter === 'PENDING') {
            this.successMessage = 'Aucune ordonnance en attente trouvée pour ce patient.';
          } else if (this.statusFilter === 'DISPENSED') {
            this.successMessage = 'Aucune ordonnance délivrée trouvée pour ce patient.';
          } else {
            this.successMessage = 'Aucune ordonnance trouvée pour ce patient.';
          }
        }
      },
      error: (err) => {
        this.isLoading = false;
        console.error('Error fetching prescriptions:', err);
        this.errorMessage = 'Erreur lors de la recherche des ordonnances. ' + (err.message || '');
      }
    });
  }

  /**
   * ✅ Change status filter and search again
   */
  changeStatusFilter(status: string): void {
    this.statusFilter = status;

    // If a search has already been performed, do the search again with new filter
    if (this.searchPerformed && this.cin && this.fullName) {
      this.searchPrescriptions();
    }
  }

  /**
   * ✅ Mark as DISPENSED
   */
  markAsDispensed(prescriptionId: number): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.pharmacistService.markAsDispensed(prescriptionId).subscribe({
      next: (updatedPrescription) => {
        this.isLoading = false;
        this.successMessage = 'Ordonnance délivrée avec succès.';

        // If we're viewing PENDING prescriptions, remove this from the list
        if (this.statusFilter === 'PENDING') {
          this.prescriptions = this.prescriptions.filter(p => p.id !== prescriptionId);
        }
        // If we're viewing ALL prescriptions, update the status
        else if (this.statusFilter === 'ALL') {
          const index = this.prescriptions.findIndex(p => p.id === prescriptionId);
          if (index !== -1) {
            this.prescriptions[index] = updatedPrescription;
          }
        }

        // Clear the selected prescription if it was just dispensed
        if (this.selectedPrescription && this.selectedPrescription.id === prescriptionId) {
          this.selectedPrescription = null;
        }
      },
      error: (err) => {
        this.isLoading = false;
        console.error('Error marking prescription as dispensed:', err);
        this.errorMessage = 'Erreur lors de la mise à jour de la prescription. ' + (err.message || '');
      }
    });
  }

  /**
   * ✅ View prescription details
   */
  viewPrescriptionDetails(prescription: PrescriptionDTO): void {
    this.selectedPrescription = prescription;
  }

  /**
   * ✅ Clear prescription details view
   */
  clearDetails(): void {
    this.selectedPrescription = null;
  }

  /**
   * ✅ Format date for display
   */
  formatDate(dateString: string | null): string {
    if (!dateString) return 'N/A';

    try {
      const date = new Date(dateString);
      return date.toLocaleString();
    } catch (e) {
      return dateString;
    }
  }

  /**
   * ✅ Clear search results
   */
  clearSearch(): void {
    this.cin = '';
    this.fullName = '';
    this.prescriptions = [];
    this.selectedPrescription = null;
    this.errorMessage = '';
    this.successMessage = '';
    this.searchPerformed = false;
  }
}

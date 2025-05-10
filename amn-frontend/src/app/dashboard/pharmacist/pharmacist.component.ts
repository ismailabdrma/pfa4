import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PharmacistService } from 'src/app/services/pharmacist.service';

@Component({
  selector: 'app-pharmacist',
  standalone: true,
  templateUrl: './pharmacist.component.html',
  styleUrls: ['./pharmacist.component.css'],
  imports: [CommonModule, FormsModule],  // ✅ Include CommonModule
})
export class PharmacistComponent implements OnInit {

  cin: string = '';
  fullName: string = '';
  prescriptions: any[] = [];
  errorMessage: string = '';

  constructor(private pharmacistService: PharmacistService) {}

  ngOnInit(): void {}

  /**
   * ✅ Search Prescriptions for Patient
   */
  searchPrescriptions(): void {
    if (!this.cin.trim() || !this.fullName.trim()) {
      this.errorMessage = 'Veuillez entrer le CIN et le nom complet.';
      return;
    }

    this.pharmacistService.getPrescriptions(this.cin, this.fullName, 'ALL').subscribe({
      next: (data) => {
        this.prescriptions = data;
        this.errorMessage = '';
      },
      error: () => {
        this.prescriptions = [];
        this.errorMessage = 'Aucune ordonnance trouvée pour ce patient.';
      }
    });
  }

  /**
   * ✅ Mark as DISPENSED
   */
  markAsDispensed(prescriptionId: number): void {
    this.pharmacistService.markAsDispensed(prescriptionId).subscribe({
      next: () => {
        this.searchPrescriptions(); // Refresh the list
      },
      error: () => {
        this.errorMessage = 'Erreur lors de la mise à jour de la prescription.';
      }
    });
  }
}

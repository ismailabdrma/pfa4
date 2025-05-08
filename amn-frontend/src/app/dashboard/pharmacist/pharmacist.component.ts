import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PharmacistService } from 'src/app/services/pharmacist.service';
import { PrescriptionDTO } from 'src/app/models/prescription.dto';

@Component({
  selector: 'app-pharmacist',
  standalone: true,
  templateUrl: './pharmacist.component.html',
  styleUrls: ['./pharmacist.component.css'],
  imports: [CommonModule, FormsModule],
})
export class PharmacistComponent implements OnInit {
  prescriptions: PrescriptionDTO[] = [];
  cin: string = '';
  fullName: string = '';
  errorMessage: string = '';

  constructor(private pharmacistService: PharmacistService) {}

  ngOnInit(): void {
    this.prescriptions = [];
  }

  /**
   * ✅ Search for DISPENSED prescriptions by CIN and Full Name
   */
  searchPrescriptions(): void {
    if (!this.cin.trim() || !this.fullName.trim()) {
      this.errorMessage = 'Veuillez entrer le CIN et le nom complet.';
      return;
    }

    this.pharmacistService.getPrescriptions(this.cin, this.fullName, 'DISPENSED').subscribe({
      next: (data) => {
        this.prescriptions = data;
        this.errorMessage = '';
      },
      error: () => {
        this.prescriptions = [];
        this.errorMessage = 'Aucune ordonnance DISPENSED trouvée pour ce patient.';
      },
    });
  }

  /**
   * ✅ Mark a prescription as DISPENSED
   */
  markAsDispensed(prescriptionId: number): void {
    this.pharmacistService.markAsDispensed(prescriptionId).subscribe({
      next: () => {
        this.searchPrescriptions(); // Refresh after marking as dispensed
      },
      error: () => {
        this.errorMessage = 'Erreur lors de la mise à jour de la prescription.';
      },
    });
  }
}

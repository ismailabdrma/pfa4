import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-pharmacist',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './pharmacist.component.html'
})
export class PharmacistComponent {
  cin = '';
  fullName = '';
  prescriptions: any[] = [];
  pharmacistId: number = 1; // for demo, replace with real value
  patientId: number | null = null;

  otcMedication = {
    name: '',
    size: '',
    price: 0
  };

  constructor(private http: HttpClient) {}

  searchPrescriptions() {
    this.http.get<any[]>(`/api/pharmacist/prescriptions/by-patient?cin=${this.cin}&fullName=${this.fullName}`).subscribe({
      next: (res) => {
        this.prescriptions = res;
        if (res.length > 0) {
          this.patientId = res[0].patient.id;
        }
      },
      error: () => alert('Erreur lors de la recherche')
    });
  }

  validatePrescription(prescriptionId: number) {
    this.http.post(`/api/pharmacist/dispense?prescriptionId=${prescriptionId}&pharmacistId=${this.pharmacistId}`, {}).subscribe({
      next: () => alert('Prescription validée'),
      error: () => alert('Erreur lors de la validation')
    });
  }

  addOTC() {
    if (!this.patientId) return;
    this.http.post(`/api/pharmacist/add-otc?pharmacistId=${this.pharmacistId}&patientId=${this.patientId}`, this.otcMedication).subscribe({
      next: () => alert('Médicament OTC ajouté et prescription enregistrée'),
      error: () => alert('Erreur lors de l’ajout')
    });
  }
}

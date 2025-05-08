import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { DoctorService } from 'src/app/services/doctor.service';

@Component({
  selector: 'app-prescription-detail',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div *ngIf="prescription">
      <h2>Détails de l'ordonnance</h2>
      <p><strong>Date:</strong> {{ prescription.prescribedDate }}</p>
      <p><strong>Médicaments:</strong> {{ prescription.medicines }}</p>
      <p><strong>Instructions:</strong> {{ prescription.instructions }}</p>
    </div>
  `
})
export class PrescriptionDetailComponent implements OnInit {
  prescription: any;

  constructor(
    private route: ActivatedRoute,
    private doctorService: DoctorService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.doctorService.getPrescriptionById(+id).subscribe({
        next: (data) => this.prescription = data,
        error: () => alert('Ordonnance introuvable')
      });
    }
  }
}

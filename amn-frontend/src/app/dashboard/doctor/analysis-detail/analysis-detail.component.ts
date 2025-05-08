import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { DoctorService } from 'src/app/services/doctor.service';

@Component({
  selector: 'app-analysis-detail',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div *ngIf="analysis">
      <h2>Détails de l'analyse</h2>
      <p><strong>Titre:</strong> {{ analysis.title }}</p>
      <p><strong>Description:</strong> {{ analysis.description }}</p>
      <p><strong>Date:</strong> {{ analysis.uploadDate }}</p>
      <img *ngIf="analysis.url" [src]="analysis.url" alt="Fichier analyse" width="500" />
    </div>
  `
})
export class AnalysisDetailComponent implements OnInit {
  analysis: any;

  constructor(
    private route: ActivatedRoute,
    private doctorService: DoctorService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('analysisId');
    if (id) {
      this.doctorService.getAnalysisById(+id).subscribe({
        next: (data) => this.analysis = data,
        error: () => alert("Analyse introuvable.")
      });
    }
  }
}

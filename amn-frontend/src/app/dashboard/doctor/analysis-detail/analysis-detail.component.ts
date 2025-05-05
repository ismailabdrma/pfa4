import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-analysis-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './analysis-detail.component.html'
})
export class AnalysisDetailComponent implements OnInit {
  analysis: any = null;
  error: string | null = null;

  constructor(private route: ActivatedRoute, private http: HttpClient) {}

  ngOnInit(): void {
    const analysisId = this.route.snapshot.paramMap.get('analysisId');
    const token = localStorage.getItem('jwt') || '';

    if (analysisId) {
      this.http.get(`http://localhost:8080/api/doctor/analysis/${analysisId}`, {
        headers: new HttpHeaders({ 'Authorization': `Bearer ${token}` })
      }).subscribe({
        next: (data: any) => this.analysis = data,
        error: () => this.error = 'Erreur lors du chargement de l’analyse.'
      });
    } else {
      this.error = 'Aucun ID d’analyse fourni.';
    }
  }
}

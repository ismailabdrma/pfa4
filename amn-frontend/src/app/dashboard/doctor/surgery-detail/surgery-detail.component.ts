import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-surgery-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './surgery-detail.component.html'
})
export class SurgeryDetailComponent implements OnInit {
  surgery: any = null;
  error: string | null = null;

  constructor(private route: ActivatedRoute, private http: HttpClient) {}

  ngOnInit(): void {
    const surgeryId = this.route.snapshot.paramMap.get('surgeryId');
    const token = localStorage.getItem('jwt') || '';

    if (surgeryId) {
      this.http.get(`http://localhost:8080/api/doctor/surgery/${surgeryId}`, {
        headers: new HttpHeaders({ 'Authorization': `Bearer ${token}` })
      }).subscribe({
        next: (data: any) => this.surgery = data,
        error: () => this.error = 'Erreur lors du chargement de la chirurgie.'
      });
    } else {
      this.error = 'Aucun ID de chirurgie fourni.';
    }
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-scan-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './scan-detail.component.html',
  styleUrls: ['./scan-detail.component.css']
})
export class ScanDetailComponent implements OnInit {
  scan: any = null;
  error: string | null = null;

  constructor(private route: ActivatedRoute, private http: HttpClient) {}

  ngOnInit(): void {
    const scanId = this.route.snapshot.paramMap.get('scanId');
    const token = localStorage.getItem('jwt') || '';

    if (scanId) {
      this.http.get(`http://localhost:8080/api/doctor/scan/${scanId}`, {
        headers: new HttpHeaders({ 'Authorization': `Bearer ${token}` })
      }).subscribe({
        next: (data: any) => {
          this.scan = data;
        },
        error: () => {
          this.error = 'Erreur lors du chargement du scan.';
        }
      });
    } else {
      this.error = 'Aucun ID de scan fourni.';
    }
  }
}

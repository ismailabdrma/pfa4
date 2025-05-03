import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-patient',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './patient.component.html'
})
export class PatientComponent {
  profile: any = null;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.http.get('/api/patient/me').subscribe({
      next: (data) => {
        this.profile = data;
      },
      error: () => alert('Erreur lors du chargement du profil')
    });
  }
}

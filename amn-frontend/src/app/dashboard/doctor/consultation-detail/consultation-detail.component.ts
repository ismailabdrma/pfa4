import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { DoctorService } from 'src/app/services/doctor.service';

@Component({
  selector: 'app-consultation-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './consultation-detail.component.html'
})
export class ConsultationDetailComponent implements OnInit {
  consultation: any;

  constructor(
    private route: ActivatedRoute,
    private doctorService: DoctorService
  ) {
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('consultationId');
    if (id) {
      this.doctorService.getConsultationById(+id).subscribe({
        next: (data) => {
          console.log('Consultation loaded:', data); // ✅ check in browser console
          this.consultation = data;
        },
        error: () => alert('Consultation introuvable')
      });
    }
  }
}


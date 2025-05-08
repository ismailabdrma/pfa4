import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DoctorService } from 'src/app/services/doctor.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-scan-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './scan-detail.component.html',
})
export class ScanDetailComponent implements OnInit {
  scan: any = null;

  constructor(
    private route: ActivatedRoute,
    private doctorService: DoctorService
  ) {}

  ngOnInit(): void {
    const scanId = this.route.snapshot.paramMap.get('scanId');
    if (scanId) {
      this.doctorService.getScanById(+scanId).subscribe({
        next: (data) => (this.scan = data),
        error: () => alert('Erreur lors du chargement du scan.')
      });
    }
  }
}

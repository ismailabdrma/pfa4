import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DoctorService } from 'src/app/services/doctor.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-surgery-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './surgery-detail.component.html'
})
export class SurgeryDetailComponent implements OnInit {
  surgery: any = null;

  constructor(
    private route: ActivatedRoute,
    private doctorService: DoctorService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('surgeryId');
    if (id) {
      this.doctorService.getSurgeryById(+id).subscribe({
        next: (data) => (this.surgery = data),
        error: () => alert('Erreur lors du chargement de la chirurgie.')
      });
    }
  }
}

// ✅ File: patient-profile.component.ts
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { DoctorService } from 'src/app/services/doctor.service';

@Component({
  selector: 'app-patient-profile',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './patient-profile.component.html',
  styleUrls: ['./patient-profile.component.css']
})
export class PatientProfileComponent implements OnInit {
  profile: any = null; // ✅ used in the HTML
  error: string | null = null;

  constructor(
    private doctorService: DoctorService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const cin = this.route.snapshot.queryParamMap.get('cin');
    const fullName = this.route.snapshot.queryParamMap.get('fullName');

    if (cin && fullName) {
      this.doctorService.getFullPatientProfile(cin, fullName).subscribe({
        next: (data) => {
          this.profile = data;
          this.error = null;
        },
        error: (err) => {
          this.error = 'Failed to fetch patient profile.';
          console.error(err);
        }
      });
    } else {
      this.error = 'Missing CIN or Full Name in URL';
    }
  }
}

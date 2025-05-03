import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register-patient',
  standalone: true,
  templateUrl: './register-patient.component.html',
  styleUrls: ['./register-patient.component.css'],
  imports: [CommonModule, ReactiveFormsModule]
})
export class RegisterPatientComponent {
  patientForm!: FormGroup;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    // ✅ Initialize the form
    this.patientForm = this.fb.group({
      fullName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      phone: ['', Validators.required],
      cin: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.patientForm.invalid) return;

    const payload = {
      name: this.patientForm.value.fullName,
      email: this.patientForm.value.email,
      password: this.patientForm.value.password,
      phone: this.patientForm.value.phone,
      cin: this.patientForm.value.cin,
      role: 'PATIENT'
    };

    this.authService.register(payload).subscribe({
      next: (res) => {
        this.authService.saveToken(res.token);
        this.authService.redirectToDashboard();
      },
      error: () => {
        this.errorMessage = 'Registration failed. Try again.';
      }
    });
  }
}

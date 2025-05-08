import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-register-patient',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register-patient.component.html',
  styleUrls: ['./register-patient.component.css']
})
export class RegisterPatientComponent {
  registerForm: FormGroup;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      fullName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      phone: ['', Validators.required],
      cin: ['', Validators.required],
      birthDate: ['', Validators.required],
      bloodType: ['', Validators.required],
      emergencyContact: ['', Validators.required],
      allergies: [''],
      chronicDiseases: [''],
      hasHeartProblem: [false],
      hasSurgery: [false]
    });
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      this.errorMessage = "Please fill in all required fields.";
      return;
    }

    const payload = this.registerForm.value;

    this.authService.registerPatient(payload).subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = 'Registration failed. Please try again.';
      }
    });
  }
}

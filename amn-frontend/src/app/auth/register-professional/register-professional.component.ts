// src/app/auth/register-professional/register-professional.component.ts
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register-professional',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register-professional.component.html',
  styleUrls: ['./register-professional.component.css']
})
export class RegisterProfessionalComponent {
  registerForm: FormGroup;
  roles = ['DOCTOR', 'PHARMACIST', 'ADMIN'];
  errorMessage = '';

  constructor(private fb: FormBuilder, private http: HttpClient, private router: Router) {
    this.registerForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', Validators.required],
      password: ['', Validators.required],
      role: ['', Validators.required],
      matricule: ['', Validators.required],
      specialty: [''] // Only required for DOCTOR
    });

    // Adjust form controls based on role selection
    this.registerForm.get('role')?.valueChanges.subscribe((role) => {
      const specialtyControl = this.registerForm.get('specialty');

      if (role === 'DOCTOR') {
        specialtyControl?.setValidators([Validators.required]);
        specialtyControl?.updateValueAndValidity();
      } else {
        specialtyControl?.clearValidators();
        specialtyControl?.setValue(''); // Clear specialty if not DOCTOR
        specialtyControl?.updateValueAndValidity();
      }
    });
  }

  onSubmit() {
    if (this.registerForm.invalid) {
      this.errorMessage = 'Veuillez remplir tous les champs requis.';
      return;
    }

    const formData = this.registerForm.value;

    // Setting default status for DOCTOR and PHARMACIST as PENDING
    if (formData.role === 'DOCTOR' || formData.role === 'PHARMACIST') {
      formData.status = 'PENDING';
    }

    this.http.post('/api/auth/register', formData).subscribe({
      next: () => {
        console.log('Registration successful');
        this.errorMessage = '';
        this.router.navigate(['/login']); // Redirect to login after registration
      },
      error: (err) => {
        console.error('Registration error', err);
        this.errorMessage = 'L\'inscription a échoué. Veuillez réessayer.';
      }
    });
  }
}

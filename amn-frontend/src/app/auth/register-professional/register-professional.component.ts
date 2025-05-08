import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

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
      role: ['', Validators.required],
      matricule: ['', Validators.required],
      specialty: ['']  // Only required for DOCTOR
    });

    // Adjust form controls based on role selection
    this.registerForm.get('role')?.valueChanges.subscribe((role) => {
      const specialtyControl = this.registerForm.get('specialty');

      if (role === 'DOCTOR') {
        specialtyControl?.setValidators([Validators.required]);
        specialtyControl?.updateValueAndValidity();
      } else {
        specialtyControl?.clearValidators();
        specialtyControl?.setValue('');  // Clear specialty if not DOCTOR
        specialtyControl?.updateValueAndValidity();
      }
    });
  }

  /**
   * ✅ Handle form submission
   */
  onSubmit(): void {
    if (this.registerForm.invalid) {
      this.errorMessage = 'Veuillez remplir tous les champs requis.';
      return;
    }

    // Prepare form data and ensure correct field mapping
    const formData = {
      ...this.registerForm.value,
      name: this.registerForm.value.fullName,
      specialization: this.registerForm.value.specialty
    };

    // Clean up unnecessary fields
    delete formData.fullName;
    delete formData.specialty;

    // Set status to PENDING for DOCTOR and PHARMACIST
    if (formData.role === 'DOCTOR' || formData.role === 'PHARMACIST') {
      formData.status = 'PENDING';
    }

    // Call appropriate registration method based on role
    switch (formData.role) {
      case 'DOCTOR':
        this.authService.registerDoctor(formData).subscribe({
          next: () => this.handleSuccess(),
          error: (err) => this.handleError(err)
        });
        break;
      case 'PHARMACIST':
        this.authService.registerPharmacist(formData).subscribe({
          next: () => this.handleSuccess(),
          error: (err) => this.handleError(err)
        });
        break;
      case 'ADMIN':
        this.authService.registerAdmin(formData).subscribe({
          next: () => this.handleSuccess(),
          error: (err) => this.handleError(err)
        });
        break;
      default:
        this.errorMessage = 'Rôle invalide.';
    }
  }

  /**
   * ✅ Handle successful registration
   */
  private handleSuccess(): void {
    alert('Inscription réussie ! Veuillez vous connecter.');
    this.registerForm.reset();
    this.router.navigate(['/login']);
  }

  /**
   * ✅ Handle errors
   */
  private handleError(error: any): void {
    console.error('Registration error:', error);
    this.errorMessage = error?.error?.message || 'L\'inscription a échoué. Veuillez réessayer.';
  }
}

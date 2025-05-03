// ✅ RegisterProfessionalComponent.ts
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-register-professional',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register-professional.component.html'
})
export class RegisterProfessionalComponent {
  registerForm: FormGroup;

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.registerForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      phone: ['', Validators.required],
      role: ['', Validators.required],
      matricule: ['', Validators.required]// Must be DOCTOR, PHARMACIST, or ADMIN
    });
  }

  onSubmit() {
    if (this.registerForm.valid) {
      this.http.post('/api/auth/register', this.registerForm.value).subscribe({
        next: res => console.log('Registered', res),
        error: err => alert('Registration failed')
      });
    }
  }
}

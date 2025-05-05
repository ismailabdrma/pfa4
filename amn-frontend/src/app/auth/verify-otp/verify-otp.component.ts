// src/app/auth/verify-otp/verify-otp.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-verify-otp',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './verify-otp.component.html',
  styleUrls: ['./verify-otp.component.css']
})
export class VerifyOtpComponent {
  otpForm: FormGroup;
  errorMessage = '';
  email = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.email = this.authService.getPendingEmail();
    this.otpForm = this.fb.group({
      otpCode: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit() {
    if (this.otpForm.invalid) return;

    this.authService.verifyOtp(this.email, this.otpForm.value.otpCode).subscribe({
      next: (res: any) => {
        this.authService.saveToken(res.token);   // Save JWT
        this.authService.redirectToDashboard();  // Redirect by role
      },
      error: () => {
        this.errorMessage = 'Code OTP invalide ou expiré.';
      }
    });
  }
}

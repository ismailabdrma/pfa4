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
  errorMessage: string = '';
  email: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    // ✅ Retrieve the pending email for OTP verification
    this.email = this.authService.getPendingEmail();

    // ✅ Initialize OTP Form
    this.otpForm = this.fb.group({
      otpCode: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(6)]]
    });
  }

  /**
   * ✅ Handle OTP Submission
   */
  onSubmit(): void {
    if (this.otpForm.invalid) {
      this.errorMessage = 'Veuillez entrer un code OTP valide.';
      return;
    }

    const otpCode = this.otpForm.value.otpCode.trim();

    this.authService.verifyOtp(this.email, otpCode).subscribe({
      next: (response: any) => {
        const { token, role } = response;

        if (token && role) {
          // ✅ Save the JWT token and redirect
          this.authService.saveToken(token);
          this.authService.redirectToDashboard(role);
        } else {
          this.errorMessage = 'Erreur: Le rôle ou le token est manquant dans la réponse.';
        }
      },
      error: (err) => {
        console.error('OTP Verification Error:', err);
        this.errorMessage = err?.error?.message || 'Code OTP invalide ou expiré.';
      }
    });
  }

  /**
   * ✅ Resend OTP (Optional - Implement Backend Logic)
   */
  resendOtp(): void {
    this.authService.verifyOtp(this.email, '').subscribe({
      next: () => {
        alert('Le code OTP a été renvoyé à votre email.');
      },
      error: () => {
        this.errorMessage = 'Erreur lors de l\'envoi du code OTP.';
      }
    });
  }
}

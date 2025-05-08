import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  /**
   * ✅ Handle Login Submission
   */
  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.errorMessage = 'Veuillez saisir un email et un mot de passe valides.';
      return;
    }

    const { email, password } = this.loginForm.value;

    this.authService.login({ email, password }).subscribe({
      next: (response: any) => {
        const { token, role, status } = response;

        if (token && role === 'ADMIN') {
          // ✅ Admin - Immediate redirection to dashboard
          this.authService.saveToken(token);
          this.authService.redirectToDashboard(role);
        }
        else if (status === 'APPROVED' && (role === 'DOCTOR' || role === 'PHARMACIST')) {
          // ✅ Approved Doctor/Pharmacist - Redirect to OTP verification
          this.authService.setPendingEmail(email);
          this.router.navigate(['/verify-otp']);
        }
        else if (status === 'PENDING' && (role === 'DOCTOR' || role === 'PHARMACIST')) {
          this.errorMessage = 'Votre compte est en attente d\'approbation par l\'administrateur.';
        }
        else if (role === 'PATIENT') {
          // ✅ Patient - Proceed to OTP verification
          this.authService.setPendingEmail(email);
          this.router.navigate(['/verify-otp']);
        }
        else {
          this.errorMessage = 'Rôle utilisateur non reconnu ou réponse invalide.';
        }
      },
      error: (err) => {
        console.error('Login Error:', err);
        this.errorMessage = err?.error?.message || 'Email ou mot de passe invalide.';
      }
    });
  }
}

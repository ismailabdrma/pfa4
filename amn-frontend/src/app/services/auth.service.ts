import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private tokenKey = 'auth_token'; // 🔄 unified key
  private emailKey = 'pending_email';

  constructor(private http: HttpClient, private router: Router) {}

  login(credentials: { email: string; password: string }): Observable<any> {
    localStorage.setItem(this.emailKey, credentials.email);
    return this.http.post(`${this.apiUrl}/login`, credentials, { responseType: 'text' });
  }

  verifyOtp(email: string, otpCode: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/verify-otp?email=${email}&otpCode=${otpCode}`, {}, { responseType: 'json' });
  }

  register(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, data);
  }

  saveToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.emailKey);
    this.router.navigate(['/login']);
  }

  getPendingEmail(): string {
    return localStorage.getItem(this.emailKey) || '';
  }

  setPendingEmail(email: string): void {
    localStorage.setItem(this.emailKey, email);
  }

  redirectToDashboard(): void {
    const token = this.getToken();
    if (!token) return;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const role = payload.role;

      switch (role) {
        case 'PATIENT':
          this.router.navigate(['/dashboard/patient']);
          break;
        case 'DOCTOR':
          this.router.navigate(['/dashboard/doctor']);
          break;
        case 'PHARMACIST':
          this.router.navigate(['/dashboard/pharmacist']);
          break;
        case 'ADMIN':
          this.router.navigate(['/dashboard/admin']);
          break;
        default:
          this.router.navigate(['/select-role']);
      }
    } catch (e) {
      console.error('Invalid token:', e);
      this.logout();
    }
  }
}

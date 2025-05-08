import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private tokenKey = 'auth_token';
  private emailKey = 'pending_email';

  constructor(private http: HttpClient, private router: Router) {}

  /**
   * ✅ Register Patient
   */
  registerPatient(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, { ...data, role: 'PATIENT' });
  }

  /**
   * ✅ Register Doctor
   */
  registerDoctor(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, { ...data, role: 'DOCTOR' });
  }

  /**
   * ✅ Register Pharmacist
   */
  registerPharmacist(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, { ...data, role: 'PHARMACIST' });
  }

  /**
   * ✅ Register Admin
   */
  registerAdmin(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, { ...data, role: 'ADMIN' });
  }

  /**
   * ✅ Login
   */
  login(credentials: { email: string; password: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, credentials).pipe(
      tap({
        next: (response: any) => {
          const { token, role, status } = response;

          if (!token && role !== 'PATIENT') {
            // For Doctor/Pharmacist, OTP is required
            this.setPendingEmail(credentials.email);
            this.router.navigate(['/verify-otp']);
          }
          else if (token && role === 'ADMIN') {
            this.saveToken(token);
            this.redirectToDashboard(role);
          }
          else if (role === 'PATIENT') {
            this.setPendingEmail(credentials.email);
            this.router.navigate(['/verify-otp']);
          }
          else if (!token && status === 'PENDING') {
            alert('Your account is pending approval by the admin.');
          }
          else {
            alert('Unrecognized user role or invalid response.');
          }
        },
        error: (err) => {
          alert(err?.error?.message || 'Invalid email or password.');
        }
      })
    );
  }

  /**
   * ✅ Verify OTP
   */
  verifyOtp(email: string, otpCode: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/verify-otp?email=${email}&otpCode=${otpCode}`, {}).pipe(
      tap({
        next: (response: any) => {
          const { token, role } = response;

          if (token && role) {
            this.saveToken(token);
            this.redirectToDashboard(role);
          } else {
            alert('OTP verification failed. Missing token or role.');
          }
        },
        error: (err) => {
          alert(err?.error?.message || 'Invalid or expired OTP.');
        }
      })
    );
  }

  /**
   * ✅ Save Token
   */
  saveToken(token: string): void {
    console.log("Saving token:", token);
    localStorage.setItem(this.tokenKey, token);
  }

  /**
   * ✅ Get Token
   */
  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  /**
   * ✅ Set Pending Email
   */
  setPendingEmail(email: string): void {
    console.log("Setting pending email:", email);
    localStorage.setItem(this.emailKey, email);
  }

  /**
   * ✅ Get Pending Email
   */
  getPendingEmail(): string {
    return localStorage.getItem(this.emailKey) || '';
  }

  /**
   * ✅ Redirect to Dashboard
   */
  redirectToDashboard(role: string): void {
    console.log("Redirecting to dashboard for role:", role);
    switch (role) {
      case 'ADMIN':
        this.router.navigate(['/dashboard/admin']);
        break;
      case 'DOCTOR':
        this.router.navigate(['/dashboard/doctor']);
        break;
      case 'PHARMACIST':
        this.router.navigate(['/dashboard/pharmacist']);
        break;
      case 'PATIENT':
        this.router.navigate(['/dashboard/patient']);
        break;
      default:
        this.router.navigate(['/login']);
    }
  }

  /**
   * ✅ Logout
   */
  logout(): void {
    console.log("Logging out...");
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.emailKey);
    this.router.navigate(['/login']);
  }

  /**
   * ✅ Parse JWT Token
   */
  private parseToken(token: string): any {
    try {
      return JSON.parse(atob(token.split('.')[1]));
    } catch (e) {
      console.error('Token parsing error:', e);
      return null;
    }
  }
}

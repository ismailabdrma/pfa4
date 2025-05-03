// src/app/services/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient, private router: Router) {}

  login(credentials: any) {
    return this.http.post<any>(`${this.API_URL}/login`, credentials);
  }

  register(data: any) {
    return this.http.post<any>(`${this.API_URL}/register`, data);
  }

  saveToken(token: string) {
    localStorage.setItem('jwt', token);
  }

  getToken(): string | null {
    return localStorage.getItem('jwt');
  }

  logout() {
    localStorage.removeItem('jwt');
    this.router.navigate(['/login']);
  }

  redirectToDashboard() {
    const token = this.getToken();
    if (!token) {
      this.router.navigate(['/login']);
      return;
    }

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
        this.router.navigate(['/login']);
    }
  }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserDTO } from 'src/app/models/user.dto';
import { DoctorDTO } from 'src/app/models/doctor.dto';
import { PharmacistDTO } from 'src/app/models/pharmacist.dto';

@Injectable({
  providedIn: 'root',
})
export class AdminService {
  private apiUrl = 'http://localhost:8080/api/admin';

  constructor(private http: HttpClient) {}

  /**
   * ✅ Safely Get Token
   */
  private getToken(): string | null {
    if (typeof window !== 'undefined' && window.localStorage) {
      return localStorage.getItem('auth_token');
    }
    return null;
  }

  /**
   * ✅ Construct HTTP Options with Authorization Header
   */
  private getHttpOptions(): { headers: HttpHeaders } {
    const token = this.getToken();
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    });
    return { headers };
  }

  getAllPatients(): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>(`${this.apiUrl}/users/patients`, this.getHttpOptions());
  }

  getAllDoctors(): Observable<DoctorDTO[]> {
    return this.http.get<DoctorDTO[]>(`${this.apiUrl}/users/doctors`, this.getHttpOptions());
  }

  getAllPharmacists(): Observable<PharmacistDTO[]> {
    return this.http.get<PharmacistDTO[]>(`${this.apiUrl}/users/pharmacists`, this.getHttpOptions());
  }

  approveUser(userId: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/users/approve/${userId}`, {}, this.getHttpOptions());
  }

  rejectUser(userId: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/users/reject/${userId}`, {}, this.getHttpOptions());
  }

  suspendUser(userId: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/users/suspend/${userId}`, {}, this.getHttpOptions());
  }

  deleteUser(userId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/users/${userId}`, this.getHttpOptions());
  }
}

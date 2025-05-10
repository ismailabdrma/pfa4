import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PharmacistService {
  private API_URL = 'http://localhost:8080/api/pharmacist';
  private http = inject(HttpClient);

  private getJwt(): string | null {
    return localStorage.getItem('jwt');
  }

  private getHeaders(): HttpHeaders {
    const token = this.getJwt();
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return headers;
  }

  /**
   * ✅ Get Prescriptions by CIN and Full Name with optional status filter
   */
  getPrescriptions(cin: string, fullName: string, status: string = 'ALL'): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/prescriptions`, {
      headers: this.getHeaders(),
      params: { cin, fullName, status }
    });
  }

  /**
   * ✅ Mark Prescription as DISPENSED
   */
  markAsDispensed(prescriptionId: number): Observable<any> {
    return this.http.put(`${this.API_URL}/prescriptions/${prescriptionId}/dispense`, {}, {
      headers: this.getHeaders()
    });
  }
}

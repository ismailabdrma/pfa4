import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';

export interface MedicationDTO {
  id: number;
  name: string;
  dosage: string;
  period: string;
  permanent: boolean;
}

export interface PrescriptionDTO {
  id: number;
  status: string;
  permanent: boolean;
  prescribedDate: string;
  dispensedDate: string;
  prescribingDoctorName: string;
  dispensingPharmacistName: string;
  medicalRecordId: number;
  medicalFolderId: number;
  patientId: number;
  patientName: string;
  patientCIN: string;
  medications: MedicationDTO[];
}

@Injectable({ providedIn: 'root' })
export class PharmacistService {
  private apiUrl = 'http://localhost:8080/api/pharmacist';

  constructor(private http: HttpClient) {}

  /**
   * Get JWT token from localStorage
   */
  private getJwt(): string | null {
    return localStorage.getItem('jwt');
  }

  /**
   * Get authorization headers
   */
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
  getPrescriptions(cin: string, fullName: string, status: string = 'ALL'): Observable<PrescriptionDTO[]> {
    const params = new HttpParams()
      .set('cin', cin)
      .set('fullName', fullName)
      .set('status', status);

    console.log(`Fetching prescriptions for CIN: ${cin}, Name: ${fullName}, Status: ${status}`);

    return this.http.get<PrescriptionDTO[]>(`${this.apiUrl}/prescriptions`, {
      headers: this.getHeaders(),
      params: params
    }).pipe(
      catchError(error => {
        console.error('Error fetching prescriptions:', error);
        return throwError(() => new Error('Failed to fetch prescriptions. Please try again.'));
      })
    );
  }

  /**
   * ✅ Mark Prescription as DISPENSED
   */
  markAsDispensed(prescriptionId: number): Observable<PrescriptionDTO> {
    return this.http.put<PrescriptionDTO>(`${this.apiUrl}/prescriptions/${prescriptionId}/dispense`, {}, {
      headers: this.getHeaders()
    }).pipe(
      catchError(error => {
        console.error('Error marking prescription as dispensed:', error);
        return throwError(() => new Error('Failed to update prescription status. Please try again.'));
      })
    );
  }

  /**
   * ✅ Get a specific prescription by ID
   */
  getPrescriptionById(id: number): Observable<PrescriptionDTO> {
    return this.http.get<PrescriptionDTO>(`${this.apiUrl}/prescriptions/${id}`, {
      headers: this.getHeaders()
    }).pipe(
      catchError(error => {
        console.error('Error fetching prescription details:', error);
        return throwError(() => new Error('Failed to fetch prescription details. Please try again.'));
      })
    );
  }
}

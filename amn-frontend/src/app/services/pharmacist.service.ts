import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PrescriptionDTO } from '../models/prescription.dto';

@Injectable({
  providedIn: 'root',
})
export class PharmacistService {
  private apiUrl = 'http://localhost:8080/api/pharmacist';

  constructor(private http: HttpClient) {}

  /**
   * ✅ Get prescriptions by CIN and Full Name with optional status filter.
   * Default status is 'ALL'. Can also be 'DISPENSED', 'PENDING', etc.
   */
  getPrescriptions(cin: string, fullName: string, status: string = 'ALL'): Observable<PrescriptionDTO[]> {
    return this.http.get<PrescriptionDTO[]>(`${this.apiUrl}/prescriptions`, {
      params: { cin, fullName, status },
    });
  }

  /**
   * ✅ Mark a prescription as DISPENSED
   */
  markAsDispensed(prescriptionId: number): Observable<PrescriptionDTO> {
    return this.http.put<PrescriptionDTO>(`${this.apiUrl}/prescriptions/${prescriptionId}/dispense`, {});
  }

  /**
   * ✅ Get a single prescription by ID
   */
  getPrescriptionById(id: number): Observable<PrescriptionDTO> {
    return this.http.get<PrescriptionDTO>(`${this.apiUrl}/prescriptions/${id}`);
  }
}

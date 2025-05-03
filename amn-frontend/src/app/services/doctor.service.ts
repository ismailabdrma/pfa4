// src/app/services/doctor.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DoctorService {
  private readonly API_URL = 'http://localhost:8080/api/doctor';

  constructor(private http: HttpClient) {}

  /**
   * Get patient's medical folder using CIN and full name
   */
  getMedicalFolder(cin: string, fullName: string) {
    return this.http.get(`${this.API_URL}/folder`, {
      params: { cin, fullName }
    });
  }

  /**
   * Create a new medical record for a given patient ID
   */
  createMedicalRecord(patientId: number, record: any) {
    return this.http.post(`${this.API_URL}/add-record/${patientId}`, record);
  }

  /**
   * Write a prescription for a given patient ID
   */
  writePrescription(patientId: number, prescription: any) {
    return this.http.post(`${this.API_URL}/prescribe/${patientId}`, prescription);
  }
}

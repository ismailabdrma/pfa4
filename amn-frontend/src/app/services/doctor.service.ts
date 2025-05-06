import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class DoctorService {

  private API_URL = 'http://localhost:8080/api/doctor'; // ✅ Define API base URL

  constructor(private http: HttpClient) {}

  // --- 🔐 JWT Headers ---
  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwt');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  // --- 🧍‍♂️ Patient Management ---

  getFullPatientProfile(cin: string, fullName: string): Observable<any> {
    return this.http.get(`${this.API_URL}/full-profile`, {
      headers: this.getHeaders(),
      params: { cin, fullName }
    });
  }

  getPatientIdByCin(cin: string, fullName: string): Observable<number> {
    return this.http.get<number>(`${this.API_URL}/get-id`, {
      headers: this.getHeaders(),
      params: { cin, fullName }
    });
  }

  createOrUpdateMedicalFolder(cin: string, fullName: string, patientData: any): Observable<any> {
    return this.http.post(`${this.API_URL}/create-folder/${cin}`, patientData, {
      headers: this.getHeaders(),
      params: { fullName }
    });
  }

  // --- 📋 Medical Records & Prescriptions ---

  createMedicalRecord(patientId: number, record: any): Observable<any> {
    return this.http.post(`${this.API_URL}/add-record/${patientId}`, record, {
      headers: this.getHeaders()
    });
  }

  writePrescription(patientId: number, prescription: any): Observable<any> {
    return this.http.post(`${this.API_URL}/prescribe/${patientId}`, prescription, {
      headers: this.getHeaders()
    });
  }

  // --- 💉 Vaccination ---

  addVaccination(
    folderId: number,
    vaccineName: string,
    doseNumber: number,
    manufacturer: string,
    date: string
  ): Observable<any> {
    return this.http.post(`${this.API_URL}/add-vaccination`, null, {
      headers: this.getHeaders(),
      params: {
        folderId,
        vaccineName,
        doseNumber,
        manufacturer,
        date
      }
    });
  }

  // --- 📎 Upload Files ---

  uploadScan(data: FormData, folderId: number): Observable<any> {
    return this.http.post(`${this.API_URL}/upload-scan-file`, data, {
      headers: this.getHeaders().delete('Content-Type'),
      params: { folderId }
    });
  }

  uploadAnalysis(data: FormData, folderId: number): Observable<any> {
    return this.http.post(`${this.API_URL}/upload-analysis`, data, {
      headers: this.getHeaders().delete('Content-Type'),
      params: { folderId }
    });
  }

  uploadSurgery(data: FormData, folderId: number): Observable<any> {
    return this.http.post(`${this.API_URL}/upload-surgery`, data, {
      headers: this.getHeaders().delete('Content-Type'),
      params: { folderId }
    });
  }

  // --- 🔍 View Files by ID ---

  getScanById(id: number): Observable<any> {
    return this.http.get(`${this.API_URL}/scan/${id}`, {
      headers: this.getHeaders()
    });
  }

  getAnalysisById(id: number): Observable<any> {
    return this.http.get(`${this.API_URL}/analysis/${id}`, {
      headers: this.getHeaders()
    });
  }

  getSurgeryById(id: number): Observable<any> {
    return this.http.get(`${this.API_URL}/surgery/${id}`, {
      headers: this.getHeaders()
    });
  }

  // --- 📂 List Files by Folder ---

  getScansByFolder(folderId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/scans`, {
      headers: this.getHeaders(),
      params: { folderId }
    });
  }

  getAnalysesByFolder(folderId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/analyses`, {
      headers: this.getHeaders(),
      params: { folderId }
    });
  }

  getSurgeriesByFolder(folderId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/surgeries`, {
      headers: this.getHeaders(),
      params: { folderId }
    });
  }
}

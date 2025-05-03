import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class DoctorService {
  private readonly API_URL = 'http://localhost:8080/api/doctor';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwt');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getMedicalFolder(cin: string, fullName: string): Observable<any> {
    return this.http.get(`${this.API_URL}/folder`, {
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

  createFolder(cin: string, fullName: string, folderData: any): Observable<any> {
    return this.http.post(`${this.API_URL}/create-folder/${cin}?fullName=${fullName}`, folderData, {
      headers: this.getHeaders()
    });
  }

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
  getFullPatientProfile(cin: string, fullName: string): Observable<any> {
    return this.http.get(`${this.API_URL}/full-profile`, {
      headers: this.getHeaders(),
      params: { cin, fullName }
    });
  }
  createMedicalFolder(cin: string, fullName: string, folderData: any): Observable<any> {
    return this.http.post(`${this.API_URL}/create-folder/${cin}?fullName=${fullName}`, folderData, {
      headers: this.getHeaders()
    });
  }
  createOrUpdateMedicalFolder(cin: string, fullName: string, patientData: any): Observable<any> {
    return this.http.post(`${this.API_URL}/create-folder/${cin}?fullName=${fullName}`, patientData, {
      headers: this.getHeaders()
    });
  }



}

import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PatientBasicInfoDTO {
  id: number;
  fullName: string;
  email: string;
  cin: string;
  address: string;
  phone: string;
  bloodType: string;
}

@Injectable({
  providedIn: 'root',
})
export class PatientService {
  private apiUrl = 'http://localhost:8080/api/patient';

  constructor(private http: HttpClient) {}

  /**
   * ✅ Fetch basic patient info by CIN
   */
  getPatientBasicInfo(cin: string): Observable<PatientBasicInfoDTO> {
    const params = new HttpParams().set('cin', cin);
    return this.http.get<PatientBasicInfoDTO>(`${this.apiUrl}/basic-info`, { params });
  }

  /**
   * ✅ Update patient basic info
   */
  updatePatientBasicInfo(cin: string, basicInfo: PatientBasicInfoDTO): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/basic-info/${cin}`, basicInfo);
  }
}

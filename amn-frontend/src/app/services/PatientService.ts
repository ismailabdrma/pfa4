import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PatientProfileDTO } from '../models/patient-profile.dto';

@Injectable({
  providedIn: 'root',
})
export class PatientService {
  private apiUrl = 'http://localhost:8080/api/patient';

  constructor(private http: HttpClient) {}

  /**
   * Fetch patient profile by CIN
   */
  getPatientProfile(cin: string): Observable<PatientProfileDTO> {
    return this.http.get<PatientProfileDTO>(`${this.apiUrl}/profile`, {
      params: { cin }
    });
  }
}

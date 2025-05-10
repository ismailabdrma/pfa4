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

export interface PatientProfileDTO {
  id: number;
  fullName: string;
  cin: string;
  bloodType: string;
  birthDate: string;
  emergencyContact: string;
  allergies: string;
  chronicDiseases: string;
  hasHeartProblem: boolean;
  hasSurgery: boolean;
  email: string;
  phone: string;
  address: string;
  medicalFolderId: number;

  // Collections
  medicalRecords: MedicalRecordDTO[];
  scans: ScanDTO[];
  analyses: AnalysisDTO[];
  surgeries: SurgeryDTO[];
  vaccinations: VaccinationDTO[];
  prescriptions: PrescriptionDTO[];
}

export interface MedicalRecordDTO {
  id: number;
  reason: string;
  diagnosis: string;
  notes: string;
  creationDate: string;
  doctorName: string;
  prescriptions?: PrescriptionDTO[];
}

export interface PrescriptionDTO {
  id: number;
  prescribedDate: string;
  status: string;
  prescribingDoctor: string;
  medications: MedicationDTO[];
  medicalRecordId?: number;
}

export interface MedicationDTO {
  id: number;
  name: string;
  dosage: string;
  period: string;
  permanent: boolean;
}

export interface ScanDTO {
  id: number;
  title: string;
  description: string;
  uploadDate: string;
  url: string;
}

export interface AnalysisDTO {
  id: number;
  title: string;
  description: string;
  uploadDate: string;
  url: string;
}

export interface SurgeryDTO {
  id: number;
  title: string;
  description: string;
  uploadDate: string;
  url: string;
}

export interface VaccinationDTO {
  id: number;
  vaccineName: string;
  doseNumber: number;
  manufacturer: string;
  vaccinationDate: string;
}

@Injectable({
  providedIn: 'root',
})
export class PatientService {
  private apiUrl = 'http://localhost:8080/api/patient';

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
   * ✅ Get complete patient profile by email
   */
  getPatientProfileByEmail(email: string): Observable<PatientProfileDTO> {
    const params = new HttpParams().set('email', email);
    return this.http.get<PatientProfileDTO>(`${this.apiUrl}/profile-by-email`, {
      params,
      headers: this.getHeaders()
    });
  }

  /**
   * ✅ Get complete patient profile by CIN
   */
  getPatientProfileByCin(cin: string): Observable<PatientProfileDTO> {
    const params = new HttpParams().set('cin', cin);
    return this.http.get<PatientProfileDTO>(`${this.apiUrl}/profile-by-cin`, {
      params,
      headers: this.getHeaders()
    });
  }

  /**
   * ✅ Get patient profile by folder ID
   */
  getPatientProfileByFolderId(folderId: number): Observable<PatientProfileDTO> {
    return this.http.get<PatientProfileDTO>(`${this.apiUrl}/profile-by-folder/${folderId}`, {
      headers: this.getHeaders()
    });
  }

  /**
   * ✅ Get current patient basic data
   */
  getCurrentPatient(email: string): Observable<{cin: string, email: string}> {
    const params = new HttpParams().set('email', email);
    return this.http.get<{cin: string, email: string}>(`${this.apiUrl}/current`, {
      params,
      headers: this.getHeaders()
    });
  }

  /**
   * ✅ Fetch basic patient info by CIN
   */
  getPatientBasicInfo(cin: string): Observable<PatientBasicInfoDTO> {
    const params = new HttpParams().set('cin', cin);
    return this.http.get<PatientBasicInfoDTO>(`${this.apiUrl}/basic-info`, {
      params,
      headers: this.getHeaders()
    });
  }

  /**
   * ✅ Update patient basic info
   */
  updatePatientBasicInfo(cin: string, basicInfo: PatientBasicInfoDTO): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/basic-info/${cin}`, basicInfo, {
      headers: this.getHeaders()
    });
  }

  /**
   * ✅ Get medical records for a patient by folder ID
   */
  getMedicalRecords(folderId: number): Observable<MedicalRecordDTO[]> {
    return this.http.get<MedicalRecordDTO[]>(`${this.apiUrl}/records/${folderId}`, {
      headers: this.getHeaders()
    });
  }

  /**
   * ✅ Get prescriptions for a patient by folder ID
   */
  getPrescriptions(folderId: number): Observable<PrescriptionDTO[]> {
    return this.http.get<PrescriptionDTO[]>(`${this.apiUrl}/prescriptions/${folderId}`, {
      headers: this.getHeaders()
    });
  }

  /**
   * ✅ Get scans for a patient by folder ID
   */
  getScans(folderId: number): Observable<ScanDTO[]> {
    return this.http.get<ScanDTO[]>(`${this.apiUrl}/scans/${folderId}`, {
      headers: this.getHeaders()
    });
  }

  /**
   * ✅ Get analyses for a patient by folder ID
   */
  getAnalyses(folderId: number): Observable<AnalysisDTO[]> {
    return this.http.get<AnalysisDTO[]>(`${this.apiUrl}/analyses/${folderId}`, {
      headers: this.getHeaders()
    });
  }

  /**
   * ✅ Get surgeries for a patient by folder ID
   */
  getSurgeries(folderId: number): Observable<SurgeryDTO[]> {
    return this.http.get<SurgeryDTO[]>(`${this.apiUrl}/surgeries/${folderId}`, {
      headers: this.getHeaders()
    });
  }

  /**
   * ✅ Get vaccinations for a patient by folder ID
   */
  getVaccinations(folderId: number): Observable<VaccinationDTO[]> {
    return this.http.get<VaccinationDTO[]>(`${this.apiUrl}/vaccinations/${folderId}`, {
      headers: this.getHeaders()
    });
  }
}

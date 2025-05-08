import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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
   * ✅ Get All Patients
   */
  getAllPatients(): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>(`${this.apiUrl}/patients`);
  }

  /**
   * ✅ Get All Doctors
   */
  getAllDoctors(): Observable<DoctorDTO[]> {
    return this.http.get<DoctorDTO[]>(`${this.apiUrl}/doctors`);
  }

  /**
   * ✅ Get All Pharmacists
   */
  getAllPharmacists(): Observable<PharmacistDTO[]> {
    return this.http.get<PharmacistDTO[]>(`${this.apiUrl}/pharmacists`);
  }

  /**
   * ✅ Suspend User (Doctor/Pharmacist only)
   */
  suspendUser(userId: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/suspend/${userId}`, {});
  }

  /**
   * ✅ Delete User
   */
  deleteUser(userId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${userId}`);
  }
}

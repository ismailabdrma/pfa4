// src/app/app.routes.ts
import { Routes } from '@angular/router';

// Auth flow
import { SelectRoleComponent } from './auth/select-role/select-role.component';
import { AuthChoiceComponent } from './auth/auth-choice/auth-choice.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { RegisterPatientComponent } from './auth/register-patient/register-patient.component';
import { RegisterProfessionalComponent } from './auth/register-professional/register-professional.component';
import { VerifyOtpComponent } from './auth/verify-otp/verify-otp.component';

// Dashboards
import { PatientComponent } from './dashboard/patient/patient.component';
import { DoctorComponent } from './dashboard/doctor/doctor.component';
import { PharmacistComponent } from './dashboard/pharmacist/pharmacist.component';
import { AdminComponent } from './dashboard/admin/admin.component';
import { CreateMedicalFolderComponent } from './dashboard/doctor/create-medical-folder/create-medical-folder.component';

import { UploadScanComponent } from './dashboard/doctor/upload-scan.component';
import { ScanDetailComponent } from './dashboard/doctor/scan-detail/scan-detail.component';
import { SurgeryDetailComponent } from './dashboard/doctor/surgery-detail/surgery-detail.component';
import { ConsultationDetailComponent } from './dashboard/doctor/consultation-detail/consultation-detail.component';
import { PrescriptionDetailComponent } from './dashboard/doctor/prescription-detail/prescription-detail.component';
import {AnalysisDetailComponent} from 'src/app/dashboard/doctor/analysis-detail/analysis-detail.component';
import { ConsultationPrescriptionComponent } from './dashboard/doctor/consultation-prescription/consultation-prescription.component';

export const routes: Routes = [
  // Default
  { path: '', redirectTo: 'select-role', pathMatch: 'full' },

  // Role selection and flow
  { path: 'select-role', component: SelectRoleComponent },
  { path: 'auth-choice/:role', component: AuthChoiceComponent },

  // Auth pages
  { path: 'login', component: LoginComponent },
  { path: 'verify-otp', component: VerifyOtpComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'register/patient', component: RegisterPatientComponent },
  { path: 'register/professional', component: RegisterProfessionalComponent },

  // Dashboards
  { path: 'dashboard/patient', component: PatientComponent },
  { path: 'dashboard/doctor', component: DoctorComponent },
  { path: 'dashboard/pharmacist', component: PharmacistComponent },
  { path: 'dashboard/admin', component: AdminComponent },
  { path: 'dashboard/doctor/create-folder', component: CreateMedicalFolderComponent },

  {path: 'dashboard/doctor/upload-scan', component: UploadScanComponent },
  { path: 'dashboard/doctor/scan/:scanId', component: ScanDetailComponent },
  { path: 'dashboard/doctor/surgery/:surgeryId', component: SurgeryDetailComponent},

  { path: 'dashboard/doctor/consultation/:consultationId', component: ConsultationDetailComponent },
  { path: 'dashboard/doctor/prescription/:prescriptionId', component: PrescriptionDetailComponent },
  { path: 'dashboard/doctor/analysis/:analysisId', component: AnalysisDetailComponent},
  { path: 'dashboard/doctor/consultation-prescription/:cin/:fullName', component: ConsultationPrescriptionComponent },
  { path: 'dashboard/admin', component: AdminComponent },

];

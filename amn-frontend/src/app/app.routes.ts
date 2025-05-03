// src/app/app.routes.ts
import { Routes } from '@angular/router';

// Auth flow
import { SelectRoleComponent } from './auth/select-role/select-role.component';
import { AuthChoiceComponent } from './auth/auth-choice/auth-choice.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { RegisterPatientComponent } from './auth/register-patient/register-patient.component';
import { RegisterProfessionalComponent } from './auth/register-professional/register-professional.component';

// Dashboards
import { PatientComponent } from './dashboard/patient/patient.component';
import { DoctorComponent } from './dashboard/doctor/doctor.component';
import { PharmacistComponent } from './dashboard/pharmacist/pharmacist.component';
import { AdminComponent } from './dashboard/admin/admin.component';
import { CreateMedicalFolderComponent } from './dashboard/doctor/create-medical-folder/create-medical-folder.component';
import { PatientProfileComponent } from './dashboard/doctor/patient-profile/patient-profile.component';
export const routes: Routes = [
  // Default
  { path: '', redirectTo: 'select-role', pathMatch: 'full' },

  // Role selection and flow
  { path: 'select-role', component: SelectRoleComponent },
  { path: 'auth-choice/:role', component: AuthChoiceComponent },

  // Auth pages
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'register/patient', component: RegisterPatientComponent },
  { path: 'register/professional', component: RegisterProfessionalComponent },

  // Dashboards
  { path: 'dashboard/patient', component: PatientComponent },
  { path: 'dashboard/doctor', component: DoctorComponent },
  { path: 'dashboard/pharmacist', component: PharmacistComponent },
  { path: 'dashboard/admin', component: AdminComponent },
  { path: 'dashboard/doctor/create-folder', component: CreateMedicalFolderComponent },
  { path: 'dashboard/doctor/patient-profile', component: PatientProfileComponent }
];

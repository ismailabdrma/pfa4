import { MedicalRecordDTO } from './medical-record.dto';
import { Vaccination } from './vaccination.dto';
import { VisitLog } from './visit-log.dto';
import { ScanDTO } from './scan.dto';
import { SurgeryDTO } from './surgery.dto';
import { PrescriptionDTO } from './prescription.dto';
import { AnalysisDTO } from './analysis.dto';

export interface PatientProfileDTO {
  id: number;
  fullName: string;
  cin: string;
  bloodType?: string;
  birthDate?: string;
  emergencyContact?: string;
  allergies?: string;
  chronicDiseases?: string;
  hasHeartProblem: boolean;
  hasSurgery: boolean;
  email: string;
  phone: string;
  address?: string;
  medicalFolderId?: number;

  // Medical Records
  medicalRecords: MedicalRecordDTO[];

  // Vaccinations
  vaccinations: Vaccination[];

  // Visit Logs
  visitLogs: VisitLog[];

  // Scans
  scans: ScanDTO[];

  // Surgeries
  surgeries: SurgeryDTO[];

  // Prescriptions
  prescriptions: PrescriptionDTO[];

  // Analyses
  analyses: AnalysisDTO[];
}

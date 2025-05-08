export interface MedicalRecordDTO {
  id: number;
  reason: string;
  diagnosis: string;
  date: string;
  doctorName: string;
  notes?: string;
}

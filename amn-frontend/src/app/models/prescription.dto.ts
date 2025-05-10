import { MedicationDTO } from './medication.dto';

export interface PrescriptionDTO {
    instructions: string;
  id: number;
  status: string; // PENDING, DISPENSED, CREATED, etc.
  permanent: boolean;
  prescribedDate: string;
  dispensedDate?: string;
  prescribingDoctorName: string;
  dispensingPharmacistName?: string;
  medications: MedicationDTO[];
}

export interface PrescriptionRequest {
  prescription: PrescriptionDTO;
  medications: MedicationDTO[];
}

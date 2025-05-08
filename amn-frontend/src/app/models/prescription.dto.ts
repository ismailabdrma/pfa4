import { MedicationDTO } from './medication.dto';

export interface PrescriptionDTO {
  id: number;
  status: string;
  permanent: boolean;
  prescribedDate: string;
  prescribingDoctorName: string;
  medications: MedicationDTO[];
}

export interface PrescriptionRequest {
  prescription: PrescriptionDTO;
  medications: MedicationDTO[];
}

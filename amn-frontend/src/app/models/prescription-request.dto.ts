import { MedicationDTO } from './medication.dto';
import { PrescriptionDTO } from './prescription.dto';

export interface PrescriptionRequest {
  prescription: PrescriptionDTO;
  medications: MedicationDTO[];
}

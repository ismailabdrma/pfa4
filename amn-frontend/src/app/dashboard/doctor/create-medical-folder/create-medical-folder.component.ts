import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { DoctorService } from 'src/app/services/doctor.service';

@Component({
  selector: 'app-create-medical-folder',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './create-medical-folder.component.html',
  styleUrls: ['./create-medical-folder.component.css']
})
export class CreateMedicalFolderComponent {
  folderForm: FormGroup;
  message: string | null = null;
  error: string | null = null;

  constructor(private fb: FormBuilder, private doctorService: DoctorService) {
    this.folderForm = this.fb.group({
      cin: ['', Validators.required],
      fullName: ['', Validators.required],
      bloodType: ['', Validators.required],
      allergies: [''],
      chronicDiseases: [''],
      emergencyContactName: ['', Validators.required],
      emergencyContactPhone: ['', [Validators.required, Validators.pattern('^[0-9]{10,15}$')]],
      currentMedications: [''],
      pastSurgeries: [''],
      vaccinationHistory: [''],
      notes: ['']
    });
  }

  onSubmit(): void {
    const folderData = this.folderForm.value;

    this.doctorService.createOrUpdateMedicalFolder(folderData.cin, folderData.fullName, folderData).subscribe({
      next: () => {
        this.message = '✅ Dossier médical créé avec succès.';
        this.error = null;
        this.folderForm.reset();
      },
      error: (err: any) => {
        this.error = err.error?.message || '❌ Error creating medical folder.';
      }

    });
  }
}

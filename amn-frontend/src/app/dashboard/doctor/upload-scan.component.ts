import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-upload-scan',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './upload-scan.component.html',
})
export class UploadScanComponent {
  scanForm: FormGroup;
  message = '';
  error = '';

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.scanForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      uploadDate: [new Date().toISOString().slice(0, 10), Validators.required],
      fileType: ['scan', Validators.required],
      url: ['', Validators.required],
      folderId: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.scanForm.invalid) return;

    const payload = {
      title: this.scanForm.value.title,
      description: this.scanForm.value.description,
      uploadDate: this.scanForm.value.uploadDate,
      fileType: this.scanForm.value.fileType,
      url: this.scanForm.value.url,
    };

    const folderId = this.scanForm.value.folderId;
    const token = localStorage.getItem('jwt') || '';

    this.http.post(`http://localhost:8080/api/doctor/upload-scan?folderId=${folderId}`, payload, {
      headers: new HttpHeaders({ 'Authorization': `Bearer ${token}` })
    }).subscribe({
      next: () => {
        this.message = '✅ Scan uploaded and saved.';
        this.error = '';
        this.scanForm.reset();
      },
      error: () => {
        this.error = '❌ Failed to upload scan.';
        this.message = '';
      }
    });
  }
}

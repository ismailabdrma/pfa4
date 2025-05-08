import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-select-role',
  templateUrl: './select-role.component.html',
  styleUrls: ['./select-role.component.css']
})
export class SelectRoleComponent {
  constructor(private router: Router) {}

  chooseRole(role: string) {
    if (['PATIENT', 'DOCTOR', 'PHARMACIST', 'ADMIN'].includes(role)) {
      this.router.navigate(['/auth-choice', role]);
    } else {
      alert("Rôle non valide !");
    }
  }
}

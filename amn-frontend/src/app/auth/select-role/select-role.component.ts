// src/app/auth/select-role/select-role.component.ts
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
    this.router.navigate(['/auth-choice', role]);
  }
}

import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-auth-choice',
  standalone: true,
  templateUrl: './auth-choice.component.html',
  styleUrls: ['./auth-choice.component.css'],
})
export class AuthChoiceComponent {
  role: string = '';

  constructor(private route: ActivatedRoute, private router: Router) {
    this.route.params.subscribe(params => {
      this.role = params['role'];
    });
  }

  goToLogin() {
    this.router.navigate(['/login'], { queryParams: { role: this.role } });
  }

  goToRegister() {
    if (this.role === 'PATIENT') {
      this.router.navigate(['/register/patient']);
    } else {
      this.router.navigate(['/register/professional'], {
        queryParams: { role: this.role }
      });
    }
  }
}

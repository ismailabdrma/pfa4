import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterProfessionalComponent } from './register-professional.component';

describe('RegisterProfessionalComponent', () => {
  let component: RegisterProfessionalComponent;
  let fixture: ComponentFixture<RegisterProfessionalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterProfessionalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterProfessionalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

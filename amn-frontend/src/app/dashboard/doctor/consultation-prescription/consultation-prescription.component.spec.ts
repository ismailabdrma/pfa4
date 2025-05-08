import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsultationPrescriptionComponent } from './consultation-prescription.component';

describe('ConsultationPrescriptionComponent', () => {
  let component: ConsultationPrescriptionComponent;
  let fixture: ComponentFixture<ConsultationPrescriptionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConsultationPrescriptionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConsultationPrescriptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

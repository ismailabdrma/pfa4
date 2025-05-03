import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PharmacistComponent } from './pharmacist.component';

describe('PharmacistComponent', () => {
  let component: PharmacistComponent;
  let fixture: ComponentFixture<PharmacistComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PharmacistComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PharmacistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

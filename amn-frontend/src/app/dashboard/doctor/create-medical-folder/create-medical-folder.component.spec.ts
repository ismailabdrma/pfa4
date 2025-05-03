import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateMedicalFolderComponent } from './create-medical-folder.component';

describe('CreateMedicalFolderComponent', () => {
  let component: CreateMedicalFolderComponent;
  let fixture: ComponentFixture<CreateMedicalFolderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateMedicalFolderComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateMedicalFolderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

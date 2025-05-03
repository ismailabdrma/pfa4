import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthChoiceComponent } from './auth-choice.component';

describe('AuthChoiceComponent', () => {
  let component: AuthChoiceComponent;
  let fixture: ComponentFixture<AuthChoiceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AuthChoiceComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AuthChoiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

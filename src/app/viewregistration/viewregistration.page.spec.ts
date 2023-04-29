import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ViewregistrationPage } from './viewregistration.page';

describe('ViewregistrationPage', () => {
  let component: ViewregistrationPage;
  let fixture: ComponentFixture<ViewregistrationPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ViewregistrationPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

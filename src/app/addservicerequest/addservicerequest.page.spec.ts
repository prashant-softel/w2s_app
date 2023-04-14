import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddservicerequestPage } from './addservicerequest.page';

describe('AddservicerequestPage', () => {
  let component: AddservicerequestPage;
  let fixture: ComponentFixture<AddservicerequestPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(AddservicerequestPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

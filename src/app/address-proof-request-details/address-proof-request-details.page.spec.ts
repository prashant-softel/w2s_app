import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddressProofRequestDetailsPage } from './address-proof-request-details.page';

describe('AddressProofRequestDetailsPage', () => {
  let component: AddressProofRequestDetailsPage;
  let fixture: ComponentFixture<AddressProofRequestDetailsPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(AddressProofRequestDetailsPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddressProofRequestPage } from './address-proof-request.page';

describe('AddressProofRequestPage', () => {
  let component: AddressProofRequestPage;
  let fixture: ComponentFixture<AddressProofRequestPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(AddressProofRequestPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

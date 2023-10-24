import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { AddAddressProof } from './addaddressproof';
describe('AddTaskPage', () => {
  let component: AddAddressProof;
  let fixture: ComponentFixture<AddAddressProof>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(AddAddressProof);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

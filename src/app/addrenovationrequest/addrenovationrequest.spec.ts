import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { AddRenovationRequest } from './addrenovationrequest';

describe('EventsPage', () => {
  let component: AddRenovationRequest;
  let fixture: ComponentFixture<AddRenovationRequest>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(AddRenovationRequest);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

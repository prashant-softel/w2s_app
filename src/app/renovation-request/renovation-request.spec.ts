import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { RenovationRequestPage } from './renovation-request';

describe('EventsPage', () => {
  let component: RenovationRequestPage;
  let fixture: ComponentFixture<RenovationRequestPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(RenovationRequestPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

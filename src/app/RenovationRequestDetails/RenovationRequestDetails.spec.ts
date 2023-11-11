import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { RenovationRequestDetailsPage } from './RenovationRequestDetails';

describe('EventsPage', () => {
  let component: RenovationRequestDetailsPage;
  let fixture: ComponentFixture<RenovationRequestDetailsPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(RenovationRequestDetailsPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

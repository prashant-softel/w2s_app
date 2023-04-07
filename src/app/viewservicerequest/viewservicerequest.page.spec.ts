import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ViewservicerequestPage } from './viewservicerequest.page';

describe('ViewservicerequestPage', () => {
  let component: ViewservicerequestPage;
  let fixture: ComponentFixture<ViewservicerequestPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ViewservicerequestPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

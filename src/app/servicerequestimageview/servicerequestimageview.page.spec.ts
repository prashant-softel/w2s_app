import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ServicerequestimageviewPage } from './servicerequestimageview.page';

describe('ServicerequestimageviewPage', () => {
  let component: ServicerequestimageviewPage;
  let fixture: ComponentFixture<ServicerequestimageviewPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ServicerequestimageviewPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

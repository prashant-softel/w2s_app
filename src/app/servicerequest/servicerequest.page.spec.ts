import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ServicerequestPage } from './servicerequest.page';

describe('ServicerequestPage', () => {
  let component: ServicerequestPage;
  let fixture: ComponentFixture<ServicerequestPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ServicerequestPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

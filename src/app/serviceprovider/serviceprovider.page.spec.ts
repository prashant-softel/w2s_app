import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ServiceproviderPage } from './serviceprovider.page';

describe('ServiceproviderPage', () => {
  let component: ServiceproviderPage;
  let fixture: ComponentFixture<ServiceproviderPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ServiceproviderPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

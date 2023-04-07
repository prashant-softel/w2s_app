import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TenantsdetailsPage } from './tenantsdetails.page';

describe('TenantsdetailsPage', () => {
  let component: TenantsdetailsPage;
  let fixture: ComponentFixture<TenantsdetailsPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(TenantsdetailsPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

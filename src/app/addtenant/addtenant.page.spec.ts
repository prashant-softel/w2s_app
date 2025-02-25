import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddTenantPage } from './addtenant.page';
describe('AddTaskPage', () => {
  let component: AddTenantPage;
  let fixture: ComponentFixture<AddTenantPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(AddTenantPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

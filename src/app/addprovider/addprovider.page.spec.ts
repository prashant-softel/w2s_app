import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddProviderPage } from './addprovider.page';
describe('AddProviderPage', () => {
  let component: AddProviderPage;
  let fixture: ComponentFixture<AddProviderPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(AddProviderPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

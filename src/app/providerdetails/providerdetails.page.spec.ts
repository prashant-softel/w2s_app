import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProviderdetailsPage } from './providerdetails.page';

describe('ProviderdetailsPage', () => {
  let component: ProviderdetailsPage;
  let fixture: ComponentFixture<ProviderdetailsPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ProviderdetailsPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

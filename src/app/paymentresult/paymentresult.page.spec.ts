import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PaymentresultPage } from './paymentresult.page';

describe('PaymentresultPage', () => {
  let component: PaymentresultPage;
  let fixture: ComponentFixture<PaymentresultPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(PaymentresultPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

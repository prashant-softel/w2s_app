import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import { PaymentgatewayPage } from './paymentgateway.page';

describe('PaymentgatewayPage', () => {
  let component: PaymentgatewayPage;
  let fixture: ComponentFixture<PaymentgatewayPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(PaymentgatewayPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

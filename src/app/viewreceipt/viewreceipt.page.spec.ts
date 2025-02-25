import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ViewreceiptPage } from './viewreceipt.page';

describe('ViewreceiptPage', () => {
  let component: ViewreceiptPage;
  let fixture: ComponentFixture<ViewreceiptPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ViewreceiptPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

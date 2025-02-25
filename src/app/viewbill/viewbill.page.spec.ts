import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ViewbillPage } from './viewbill.page';

describe('ViewbillPage', () => {
  let component: ViewbillPage;
  let fixture: ComponentFixture<ViewbillPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ViewbillPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

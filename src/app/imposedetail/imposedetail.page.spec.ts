import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ImposedetailPage } from './imposedetail.page';

describe('ImposedetailPage', () => {
  let component: ImposedetailPage;
  let fixture: ComponentFixture<ImposedetailPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ImposedetailPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

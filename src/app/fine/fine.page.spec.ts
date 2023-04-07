import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FinePage } from './fine.page';

describe('FinePage', () => {
  let component: FinePage;
  let fixture: ComponentFixture<FinePage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(FinePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CnotePage } from './cnote.page';

describe('CnotePage', () => {
  let component: CnotePage;
  let fixture: ComponentFixture<CnotePage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(CnotePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

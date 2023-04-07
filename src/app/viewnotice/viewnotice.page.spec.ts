import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ViewnoticePage } from './viewnotice.page';

describe('ViewnoticePage', () => {
  let component: ViewnoticePage;
  let fixture: ComponentFixture<ViewnoticePage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ViewnoticePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

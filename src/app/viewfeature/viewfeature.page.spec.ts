import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ViewfeaturePage } from './viewfeature.page';

describe('ViewfeaturePage', () => {
  let component: ViewfeaturePage;
  let fixture: ComponentFixture<ViewfeaturePage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ViewfeaturePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

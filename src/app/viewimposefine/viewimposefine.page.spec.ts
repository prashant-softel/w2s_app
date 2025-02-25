import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ViewimposefinePage } from './viewimposefine.page';

describe('ViewimposefinePage', () => {
  let component: ViewimposefinePage;
  let fixture: ComponentFixture<ViewimposefinePage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ViewimposefinePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

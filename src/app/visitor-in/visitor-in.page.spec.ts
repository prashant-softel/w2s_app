import { ComponentFixture, TestBed } from '@angular/core/testing';
import { VisitorInPage } from './visitor-in.page';

describe('VisitorInPage', () => {
  let component: VisitorInPage;
  let fixture: ComponentFixture<VisitorInPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(VisitorInPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

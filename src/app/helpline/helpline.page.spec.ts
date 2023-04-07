import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HelplinePage } from './helpline.page';

describe('HelplinePage', () => {
  let component: HelplinePage;
  let fixture: ComponentFixture<HelplinePage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(HelplinePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

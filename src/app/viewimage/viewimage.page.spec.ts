import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ViewimagePage } from './viewimage.page';

describe('ViewimagePage', () => {
  let component: ViewimagePage;
  let fixture: ComponentFixture<ViewimagePage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ViewimagePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

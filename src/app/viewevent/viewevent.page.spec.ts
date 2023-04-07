import { ComponentFixture, TestBed } from '@angular/core/testing';
import { VieweventPage } from './viewevent.page';

describe('VieweventPage', () => {
  let component: VieweventPage;
  let fixture: ComponentFixture<VieweventPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(VieweventPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

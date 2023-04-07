import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoticesPage } from './notices.page';

describe('NoticesPage', () => {
  let component: NoticesPage;
  let fixture: ComponentFixture<NoticesPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(NoticesPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TakepollPage } from './takepoll.page';

describe('TakepollPage', () => {
  let component: TakepollPage;
  let fixture: ComponentFixture<TakepollPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(TakepollPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

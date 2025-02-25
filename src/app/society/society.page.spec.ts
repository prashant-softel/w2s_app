import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SocietyPage } from './society.page';

describe('SocietyPage', () => {
  let component: SocietyPage;
  let fixture: ComponentFixture<SocietyPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(SocietyPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

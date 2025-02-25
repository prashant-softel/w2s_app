import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ClassifiedsPage } from './classifieds.page';

describe('ClassifiedsPage', () => {
  let component: ClassifiedsPage;
  let fixture: ComponentFixture<ClassifiedsPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ClassifiedsPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

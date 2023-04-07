import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ClassifiedsdetailsPage } from './classifiedsdetails.page';

describe('ClassifiedsdetailsPage', () => {
  let component: ClassifiedsdetailsPage;
  let fixture: ComponentFixture<ClassifiedsdetailsPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ClassifiedsdetailsPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

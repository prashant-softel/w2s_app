import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ClassifiedsimageviewPage } from './classifiedsimageview.page';

describe('ClassifiedsimageviewPage', () => {
  let component: ClassifiedsimageviewPage;
  let fixture: ComponentFixture<ClassifiedsimageviewPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ClassifiedsimageviewPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

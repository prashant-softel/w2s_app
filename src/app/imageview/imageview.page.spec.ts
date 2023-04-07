import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ImageviewPage } from './imageview.page';

describe('ImageviewPage', () => {
  let component: ImageviewPage;
  let fixture: ComponentFixture<ImageviewPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ImageviewPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

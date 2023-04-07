import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PhotoalbumPage } from './photoalbum.page';

describe('PhotoalbumPage', () => {
  let component: PhotoalbumPage;
  let fixture: ComponentFixture<PhotoalbumPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(PhotoalbumPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

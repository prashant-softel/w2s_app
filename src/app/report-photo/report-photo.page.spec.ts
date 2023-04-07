import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReportPhotoPage } from './report-photo.page';

describe('ReportPhotoPage', () => {
  let component: ReportPhotoPage;
  let fixture: ComponentFixture<ReportPhotoPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ReportPhotoPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

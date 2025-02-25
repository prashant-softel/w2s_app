import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ServiceproviederimageviewPage } from './serviceproviederimageview.page';

describe('ServiceproviederimageviewPage', () => {
  let component: ServiceproviederimageviewPage;
  let fixture: ComponentFixture<ServiceproviederimageviewPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ServiceproviederimageviewPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FineimageviewPage } from './fineimageview.page';

describe('FineimageviewPage', () => {
  let component: FineimageviewPage;
  let fixture: ComponentFixture<FineimageviewPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(FineimageviewPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UpdateproviderPage } from './updateprovider.page';

describe('UpdateproviderPage', () => {
  let component: UpdateproviderPage;
  let fixture: ComponentFixture<UpdateproviderPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(UpdateproviderPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UpdatetaskPage } from './updatetask.page';

describe('UpdatetaskPage', () => {
  let component: UpdatetaskPage;
  let fixture: ComponentFixture<UpdatetaskPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(UpdatetaskPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

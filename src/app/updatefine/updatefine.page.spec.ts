import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UpdatefinePage } from './updatefine.page';

describe('UpdatefinePage', () => {
  let component: UpdatefinePage;
  let fixture: ComponentFixture<UpdatefinePage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(UpdatefinePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { AddclassifiedPage } from './addclassified';

describe('ClassifiedsPage', () => {
  let component: AddclassifiedPage;
  let fixture: ComponentFixture<AddclassifiedPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(AddclassifiedPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

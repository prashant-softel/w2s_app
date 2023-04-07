import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RecordneftPage } from './recordneft.page';

describe('RecordneftPage', () => {
  let component: RecordneftPage;
  let fixture: ComponentFixture<RecordneftPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(RecordneftPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

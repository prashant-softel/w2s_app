import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CommitteemembersPage } from './committeemembers.page';

describe('CommitteemembersPage', () => {
  let component: CommitteemembersPage;
  let fixture: ComponentFixture<CommitteemembersPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(CommitteemembersPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MyvisitorsPage } from './myvisitors.page';

describe('MyvisitorsPage', () => {
  let component: MyvisitorsPage;
  let fixture: ComponentFixture<MyvisitorsPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(MyvisitorsPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

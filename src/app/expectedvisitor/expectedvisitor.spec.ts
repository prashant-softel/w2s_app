import { ComponentFixture, TestBed, async } from '@angular/core/testing';
// import { MyvisitorsPage } from './myvisitors.page';
import { ExpectedvisitorPage } from './expectedvisitor';


describe('ExpectedvisitorPage', () => {
  let component: ExpectedvisitorPage;
  let fixture: ComponentFixture<ExpectedvisitorPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ExpectedvisitorPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

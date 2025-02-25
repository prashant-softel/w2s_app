import { ComponentFixture, TestBed, async } from '@angular/core/testing';
// import { MyvisitorsPage } from './myvisitors.page';
import { ViewsosPage } from './viewsos';


describe('ViewsosPage', () => {
  let component: ViewsosPage;
  let fixture: ComponentFixture<ViewsosPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ViewsosPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

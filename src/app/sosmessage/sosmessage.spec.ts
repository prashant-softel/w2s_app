import { ComponentFixture, TestBed, async } from '@angular/core/testing';
// import { MyvisitorsPage } from './myvisitors.page';
import { SosMessage } from './sosmessage';


describe('SosMessage', () => {
  let component: SosMessage;
  let fixture: ComponentFixture<SosMessage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(SosMessage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

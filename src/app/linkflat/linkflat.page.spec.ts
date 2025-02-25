import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LinkflatPage } from './linkflat.page';

describe('LinkflatPage', () => {
  let component: LinkflatPage;
  let fixture: ComponentFixture<LinkflatPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(LinkflatPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

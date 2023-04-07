import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BlockproviderPage } from './blockprovider.page';

describe('BlockproviderPage', () => {
  let component: BlockproviderPage;
  let fixture: ComponentFixture<BlockproviderPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(BlockproviderPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

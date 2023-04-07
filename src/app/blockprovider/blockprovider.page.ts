import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-blockprovider',
  templateUrl: './blockprovider.page.html',
  styleUrls: ['./blockprovider.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class BlockproviderPage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

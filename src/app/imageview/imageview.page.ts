import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-imageview',
  templateUrl: './imageview.page.html',
  styleUrls: ['./imageview.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ImageviewPage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

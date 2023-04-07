import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-photoalbum',
  templateUrl: './photoalbum.page.html',
  styleUrls: ['./photoalbum.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class PhotoalbumPage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

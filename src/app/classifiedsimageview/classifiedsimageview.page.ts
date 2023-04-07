import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-classifiedsimageview',
  templateUrl: './classifiedsimageview.page.html',
  styleUrls: ['./classifiedsimageview.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ClassifiedsimageviewPage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

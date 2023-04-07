import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-fineimageview',
  templateUrl: './fineimageview.page.html',
  styleUrls: ['./fineimageview.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class FineimageviewPage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-fine',
  templateUrl: './fine.page.html',
  styleUrls: ['./fine.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class FinePage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

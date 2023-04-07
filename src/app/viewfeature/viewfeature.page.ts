import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-viewfeature',
  templateUrl: './viewfeature.page.html',
  styleUrls: ['./viewfeature.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ViewfeaturePage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

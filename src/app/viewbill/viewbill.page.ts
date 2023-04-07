import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-viewbill',
  templateUrl: './viewbill.page.html',
  styleUrls: ['./viewbill.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ViewbillPage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

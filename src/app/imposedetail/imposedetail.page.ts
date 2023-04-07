import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-imposedetail',
  templateUrl: './imposedetail.page.html',
  styleUrls: ['./imposedetail.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ImposedetailPage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

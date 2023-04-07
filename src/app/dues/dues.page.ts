import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-dues',
  templateUrl: './dues.page.html',
  styleUrls: ['./dues.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class DuesPage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

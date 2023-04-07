import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-updatefine',
  templateUrl: './updatefine.page.html',
  styleUrls: ['./updatefine.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class UpdatefinePage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

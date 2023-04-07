import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-takepoll',
  templateUrl: './takepoll.page.html',
  styleUrls: ['./takepoll.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class TakepollPage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

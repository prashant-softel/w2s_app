import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-visitor-in',
  templateUrl: './visitor-in.page.html',
  styleUrls: ['./visitor-in.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class VisitorInPage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

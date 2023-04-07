import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-viewimage',
  templateUrl: './viewimage.page.html',
  styleUrls: ['./viewimage.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ViewimagePage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

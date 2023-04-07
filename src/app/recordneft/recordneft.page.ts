import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-recordneft',
  templateUrl: './recordneft.page.html',
  styleUrls: ['./recordneft.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class RecordneftPage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

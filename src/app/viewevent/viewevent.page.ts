import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-viewevent',
  templateUrl: './viewevent.page.html',
  styleUrls: ['./viewevent.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class VieweventPage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

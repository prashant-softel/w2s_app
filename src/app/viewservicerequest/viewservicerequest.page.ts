import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-viewservicerequest',
  templateUrl: './viewservicerequest.page.html',
  styleUrls: ['./viewservicerequest.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ViewservicerequestPage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

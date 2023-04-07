import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-servicerequest',
  templateUrl: './servicerequest.page.html',
  styleUrls: ['./servicerequest.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ServicerequestPage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

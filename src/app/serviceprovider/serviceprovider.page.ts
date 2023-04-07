import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-serviceprovider',
  templateUrl: './serviceprovider.page.html',
  styleUrls: ['./serviceprovider.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ServiceproviderPage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

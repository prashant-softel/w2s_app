import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-tenants',
  templateUrl: './tenants.page.html',
  styleUrls: ['./tenants.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class TenantsPage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

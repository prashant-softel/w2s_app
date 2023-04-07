import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-providerdetails',
  templateUrl: './providerdetails.page.html',
  styleUrls: ['./providerdetails.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ProviderdetailsPage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-address-proof-request-details',
  templateUrl: './address-proof-request-details.page.html',
  styleUrls: ['./address-proof-request-details.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class AddressProofRequestDetailsPage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

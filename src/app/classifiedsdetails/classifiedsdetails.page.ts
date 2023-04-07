import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-classifiedsdetails',
  templateUrl: './classifiedsdetails.page.html',
  styleUrls: ['./classifiedsdetails.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ClassifiedsdetailsPage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

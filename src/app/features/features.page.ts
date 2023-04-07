import { Component, OnInit,CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-features',
  templateUrl: './features.page.html',
  styleUrls: ['./features.page.scss'],
  schemas: [ CUSTOM_ELEMENTS_SCHEMA ],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class FeaturesPage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

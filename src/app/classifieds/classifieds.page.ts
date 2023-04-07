import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-classifieds',
  templateUrl: './classifieds.page.html',
  styleUrls: ['./classifieds.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ClassifiedsPage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

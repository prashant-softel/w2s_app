import { Component, OnInit,CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-helpline',
  templateUrl: './helpline.page.html',
  styleUrls: ['./helpline.page.scss'],
  standalone: true,
  schemas: [ CUSTOM_ELEMENTS_SCHEMA ],
  imports: [IonicModule, CommonModule, FormsModule]
})
export class HelplinePage implements OnInit {
  Helpline_array :Array<{}>=[];
  Category_array: Array<{}> = [];
  constructor() { 
    this.Helpline_array=[];
    this.Category_array=[];
  }

  ngOnInit() {
  }

}

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-serviceproviederimageview',
  templateUrl: './serviceproviederimageview.page.html',
  styleUrls: ['./serviceproviederimageview.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ServiceproviederimageviewPage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

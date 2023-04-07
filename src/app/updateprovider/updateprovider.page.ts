import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-updateprovider',
  templateUrl: './updateprovider.page.html',
  styleUrls: ['./updateprovider.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class UpdateproviderPage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

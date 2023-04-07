import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-report-photo',
  templateUrl: './report-photo.page.html',
  styleUrls: ['./report-photo.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ReportPhotoPage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-committeemembers',
  templateUrl: './committeemembers.page.html',
  styleUrls: ['./committeemembers.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class CommitteemembersPage implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}

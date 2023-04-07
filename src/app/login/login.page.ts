import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';


@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class LoginPage implements OnInit {
  userData: { Email: string; Password: any; }={
    Email: '',
    Password: '',
   
  };
  showLogin : any;
  message : any;
  notificationDetails : any;
  bHasNotification : any;
  constructor( 
   
   ) {
    this.userData = {Email : "", Password : ""};
        this.showLogin = false;
        this.message = "";

        this.notificationDetails = [];

        this.bHasNotification = false;
    }

  ngOnInit() {
    
  }
}
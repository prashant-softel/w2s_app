import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { NavigationExtras } from '@angular/router';
import { GlobalVars } from 'src/service/globalvars';
import { ConnectServer } from 'src/service/connectserver';
import { LoaderView } from 'src/service/loaderview';
@Component({
  selector: 'app-newuser',
  templateUrl: './newuser.page.html',
  styleUrls: ['./newuser.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class NewuserPage implements OnInit {
  userData: { Name: string, Email: string, Password: any, ConPassword: any, AccessCode: any };
  message: any;
  notificationDetails: any;
  bHasNotification: any;
  LoginPage: any = 'login';

  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams) {
    this.userData = { Name: "", Email: "", Password: "", ConPassword: "", AccessCode: "" };
    this.message = "";
    this.notificationDetails = [];
    this.bHasNotification = false;
  }

  ngOnInit() {
  }
  @HostListener('document:ionBackButton', ['$event'])
  overrideHardwareBackAction(event: any) {
    event.detail.register(100, async () => {
      event.stopImmediatePropagation();
      event.stopPropagation();
      event.preventDefault();
    });
  }

  signup() {
    this.message = "";
    if (this.userData.Name.length == 0) {
      this.message = "Please enter your Name";
    }
    else if (this.userData.Email.length == 0) {
      this.message = "Please enter your Email Address";
    }
    else if (this.userData.Password.length == 0) {
      this.message = "Please choose your own Password";
    }
    else if (this.userData.ConPassword.length == 0) {
      this.message = "Please re-enter your Password";
    }
    else if (this.userData.Password != this.userData.ConPassword) {
      this.message = "Oops ! Password and Confirm password does not match";
    }
    else if (this.userData.AccessCode.length == 0) {
      this.message = "Please enter Access Code";
    }
    else {
      this.loaderView.showLoader('Verifying Details ...');
      var errorCode = 0;
      this.message = "Verifying Details. Please Wait ...";
      this.connectServer.NewUser(this.userData).then(
        resolve => {
          this.message = "";
          this.loaderView.dismissLoader();
          if (resolve != null && resolve.hasOwnProperty('success')) {
            this.globalVars.LATEST_APP_VERSION = resolve['version'];
            this.globalVars.APP_DOWNLOAD_LINK = resolve['app_link'];
            if (resolve['success'] == 0) {
              this.message = resolve['message'];
              errorCode = resolve['ErrorCode'];
              if (errorCode == 0) {
                alert('You are already register member');
              }
              else if (errorCode == 1) {
                alert('Login created but unable to connect society');
              }
              else if (errorCode == 2) {
                alert('Unable to create login');
              }
              else if (errorCode == 3) {
                alert('Access code already in use');
              }
              else if (errorCode == 4) {
                alert('Invalid access code');
              }
            }
            else if (resolve['success'] == 1) {
              alert("Login created but unable to connect society");
            }
            else if (resolve['success'] == 2) {
              alert("Your login created and connected to " + resolve['SocietyName']);

              const navigationExtras: NavigationExtras = {
                queryParams: {
                  activation_details: this.userData
                }
              };

              // this.navCtrl.navigateRoot(pages[this.notificationDetails.pagename], navigationExtras);

              this.navCtrl.navigateRoot(this.LoginPage, navigationExtras);
            }
          }
        }
      );
    }
  }

}

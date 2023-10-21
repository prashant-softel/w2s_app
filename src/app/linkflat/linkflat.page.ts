import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';


@Component({
  selector: 'app-linkflat',
  templateUrl: './linkflat.page.html',
  styleUrls: ['./linkflat.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class LinkflatPage implements OnInit {
  LoginPage: any = 'login';
  userData: { activationCode: any };
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) {
    this.userData = { activationCode: "" };
  }
  @HostListener('document:ionBackButton', ['$event'])
  overrideHardwareBackAction(event: any) {
    event.detail.register(100, async () => {
      event.stopImmediatePropagation();
      event.stopPropagation();
      event.preventDefault();
    });
  }
  ngOnInit() {
  }
  submit() {


    if (this.userData.activationCode.length == 0) {
      alert("Please enter activation code!");

    }
    else {

      this.loaderView.showLoader('Loading ...');
      // var code = this.userData.activationCode.slice(-4);
      //var userEmail = this.userData.activationCode.substring(0, this.userData.activationCode.length-4);
      var objData = [];
      objData['fetch'] = 9;
      //objData['UserEmail'] = userEmail;
      // objData['code'] = code;
      objData['Activationcodecode'] = this.userData.activationCode;
      this.connectServer.getData("Directory", objData).then(
        resolve => {
          this.loaderView.dismissLoader();
          console.log('Response : ' + resolve);
          if (resolve['success'] == 1) {
            alert("Your flat successfully linked !");
            //this.reinitializeData();
            this.navCtrl.navigateRoot(this.LoginPage);
          }
          else {
            alert("Invalid activation code");

          }
        }
      );
    }
  }
}

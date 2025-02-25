import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { NavController, NavParams, ActionSheetController } from '@ionic/angular';
import { NavigationExtras } from '@angular/router';
import { GlobalVars } from 'src/service/globalvars';
import { ConnectServer } from 'src/service/connectserver';
import { LoaderView } from 'src/service/loaderview';



@Component({
  selector: 'app-sosmessage',
  templateUrl: './sosmessage.html',
  styleUrls: ['./sosmessage.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class SosMessage implements OnInit {
  connectDB: any;
  ViewsosPage: any = 'viewsos';
  constructor(public globalVars: GlobalVars,
    public connectServer: ConnectServer,
    private loaderView: LoaderView,
    private navCtrl: NavController) {
    this.connectDB = 0;
  }
  @HostListener('document:ionBackButton', ['$event'])
  overrideHardwareBackAction(event: any) {
    event.detail.register(100, async () => {
      event.stopImmediatePropagation();
      event.stopPropagation();
      event.preventDefault();
    });
  }
  ngOnInit(): void {
    console.log('ionViewDidLoad SosmassagePage');
    this.GetAccess();
  }
  GetAccess() {
    var objData = [];
    objData['fetch'] = "7";
    this.connectServer.getData("SOSAlert", objData).then(
      resolve => {
        //this.loaderView.dismissLoader();
        console.log('Response : ' + resolve);
        if (resolve['success'] == 1) {
          var Databases = resolve['response']['smDatabse'];
          this.connectDB = 1;

        }
        else {
          this.connectDB = 0;
        }
      }
    );

  }
  medical() {
    //lert("medical");
    /*if(this.connectDB == 0)
     {
       alert("This feature is available when Security Manager service.");
     }
     else
     {*/
    var p = [];
    p['sos'] = "1";
    let navigationExtras: NavigationExtras = {
      queryParams:
      {
        details: p,
      }
    };
    //  this.navCtrl.navigateRoot(this.ViewsosPage, {details : p});
    this.navCtrl.navigateRoot(this.ViewsosPage, navigationExtras);

    // }

  }
  fire() {
    /* if(this.connectDB == 0)
     {
       alert("This feature is available when Security Manager service.");
     }
     else
     {*/
    var p = [];
    p['sos'] = "2";
    let navigationExtras: NavigationExtras = {
      queryParams:
      {
        details: p,
      }
    };
    this.navCtrl.navigateRoot(this.ViewsosPage, navigationExtras);
    //}
  }
  lift() {
    //if(this.connectDB == 0)
    // {
    //  alert("This feature is available when Security Manager service.");
    // }
    // else
    // {
    var p = [];
    p['sos'] = "3";
    let navigationExtras: NavigationExtras = {
      queryParams:
      {
        details: p,
      }
    };
    this.navCtrl.navigateRoot(this.ViewsosPage, navigationExtras);
    // }
  }
  other() {
    //  if(this.connectDB == 0)
    // {
    //  alert("This feature is available when Security Manager service.");
    // }
    // else
    // {
    var p = [];
    p['sos'] = "4";
    let navigationExtras: NavigationExtras = {
      queryParams:
      {
        details: p,
      }
    };
    this.navCtrl.navigateRoot(this.ViewsosPage, navigationExtras);
    // }
  }




}

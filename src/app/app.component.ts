import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Component, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, MenuController, Platform, AlertController } from '@ionic/angular';
import { ConnectServer } from 'src/service/connectserver';
// import { IonicStorageModule } from '@ionic/storage-angular';

import { GlobalVars } from 'src/service/globalvars';
// import { LoaderView } from 'src/service/loaderview';
// import { StorageService } from 'src/service/StorageService';
import { Router, NavigationExtras } from '@angular/router';
import { LoaderView } from 'src/service/loaderview';
import { StorageService } from 'src/service/StorageService';
import { FcmService } from 'src/service/fcm.service';
// import { GlobalVars } from 'src/service/globalvars';
// import { FCM } from 'plugins/cordova-plugin-fcm-with-dependecy-updated/ionic/ngx';
// import { FCM } from '@cordova-plugin-fcm-with-dependecy-updated/ionic/ngx';
//import { PdfViewerModule } from 'ng2-pdf-viewer';
//import { LoginPage } from '../pages/login/login';

//import { Platform, MenuController, Nav, AlertController } from 'ionic-angular';
@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss'],
  standalone: true,
  providers: [ConnectServer
    , GlobalVars,
    LoaderView,
    NavParams,
    StorageService,
    FcmService
  ],

  imports: [
    IonicModule,
    HttpClientModule,
    CommonModule,
    FormsModule,
    // IonicStorageModule
  ],
})
export class AppComponent {




  LoginPage: any = 'login';
  DashboardPage: any = 'dashboard';
  HelplinePage: any = 'helpline';
  SettingsPage: any = 'settings';
  LinkflatPage: any = 'linkflat';
  AboutUsPage: any = 'about-us';
  SocietyPage: any = 'society';
  ClassifiedsPage: any = 'classifieds';
  //rootPage: any = this.LoginPage;
  pagesMember: Array<{ title: string, component: any }>;
  constructor(
    public platform: Platform,
    // public fcm: FCM,
    public alertCtrl: AlertController,
    public fcm: FcmService,
    public globalVars: GlobalVars,
    private navCtrl: NavController,
    public menu: MenuController) {
    // this.initializeApp();


    this.pagesMember = [
      { title: 'Dashboard', component: this.DashboardPage },
      { title: 'Helpline Number', component: this.HelplinePage },
      { title: 'Classifieds', component: this.ClassifiedsPage },
      { title: 'Settings', component: this.SettingsPage },
      //{title : 'VisitorInPage',component: VisitorInPage},
      { title: 'Link Another Society/Flat', component: this.LinkflatPage },
      { title: 'About Us', component: this.AboutUsPage }
    ];
    console.log("dsmghvdhjsgfhj1");

    this.platform.ready().then(() => {
      console.log("dsmghvdhjsgfhj");
      this.fcm.initPush();
    }).catch(e => {
      console.log({ "initili error": e });
    });
  }

  Dashboard() {
    //this.globalVars.clearStorage();
    this.navCtrl.navigateRoot(this.DashboardPage);
  }
  logout() {
    this.menu.close();
    localStorage.clear();
    this.globalVars.clearStorage();
    this.navCtrl.navigateRoot(this.LoginPage);
  }
  setting() {
    this.navCtrl.navigateRoot(this.SettingsPage);
  }
  selectSociety() {
    this.menu.close();
    this.navCtrl.navigateRoot(this.SocietyPage);
  }
  openPage(page) {
    // close the menu when clicking a link from the menu
    this.menu.close();
    // navigate to the new page if it is not the current page
    // this.nav.setRoot(page.component);
    var p = [];
    p['dash'] = "society";
    console.log(p, "page:", page);
    /*if(page.title == "Dashboard"){
      this.navCtrl.navigateRoot(page.component, {details : p});
      
    }
      else {

        this.navCtrl.navigateRoot(page.component, {details : p});    
      }*/
    this.navCtrl.navigateRoot(page.component);
  }
  initializeApp() {
    this.platform.ready().then(() => {




      /* this.globalVars.DEVICE_MODEL = this.device.model;
       this.globalVars.DEVICE_PLATFORM = this.device.platform;
      this.globalVars.DEVICE_UUID = this.device.uuid;
       this.globalVars.DEVICE_OS_VERSION = this.device.version;
       this.globalVars.DEVICE_MANUFACTURER = this.device.manufacturer;
       this.globalVars.DEVICE_SERIAL = this.device.serial;*/
      // this.initFCMPushNotification();
      // this.statusBar.styleDefault();
      // this.splashScreen.hide();

    });


  }
  //  async initFCMPushNotification() {
  //   // alert("initCall");
  //   // this.nativeAudio.preloadSimple('visitorNotify', 'asset/visiterIn.mp3'1, 1, 0)


  //   if (!this.platform.is('cordova')) {

  //     console.warn("Push notifications not initialized. Cordova is not available - Run in physical device");
  //     return;

  //   }

  //   try {
  //     //  this.nativeAudio.preloadComplex('visitorNotify', 'visitorIn.mp3', 1, 1, 0).then(() => {
  //     //this.nativeAudio.play('visitorNotify', () => console.log('uniqueId1 is done playing'));
  //     // }, (e) => alert('on error' +e) ).catch((err) => alert(err));

  //     this.fcm.getToken().then(token => {

  //       this.globalVars.DEVICE_ID = token;
  //     });

  //     this.fcm.onTokenRefresh().subscribe(token => {

  //       this.globalVars.DEVICE_ID = token;
  //       this.fcm.getToken();

  //     });

  //     this.fcm.onNotification().subscribe(async data => {

  //       //this.nativeAudio.preloadComplex('visitorNotify', 'visitorIn.mp3', 1, 1, 0).then(() => {
  //       //this.nativeAudio.play('visitorNotify', () => console.log('uniqueId1 is done playing'));
  //       //}, (e) => alert('on error' +e) ).catch((err) => alert(err));
  //       if (data.wasTapped) {

  //         this.navCtrl.navigateForward(this.LoginPage, { state: { notification_details: data } });

  //       }
  //       else {
  //         // if application open, show popup
  //         let confirmAlert = await this.alertCtrl.create({
  //           header: data['title'],
  //           message: `${data?.['message']}`,
  //           buttons: [{
  //             text: 'Ignore',
  //             role: 'cancel'
  //           }, {
  //             text: 'View',
  //             handler: () => {
  //               //TODO: Your logic here
  //               //var notificationDetails = JSON.parse(data.additionalData.details);

  //               this.navCtrl.navigateForward(this.LoginPage, { state: { notification_details: data } });
  //               //S this.nav.setRoot(LoginPage, {notification_details: data});
  //             }
  //           }]
  //         });
  //         await confirmAlert.present();

  //       }
  //     });
  //   }
  //   catch (err) {
  //     let confirmAlert6 = await this.alertCtrl.create({
  //       header: "Error",
  //       message: err.message,
  //       buttons: [{
  //         text: 'Ignore',
  //         role: 'cancel'
  //       }, {
  //         text: 'View',
  //         handler: () => {
  //           //TODO: Your logic here
  //         }
  //       }]
  //     });

  //     await confirmAlert6.present();
  //   }

  // }
}


// import { Component } from '@angular/core';
// import { IonicModule, Platform } from '@ionic/angular';
// import { FcmService } from '../services/fcm/fcm.service';

// @Component({
//   selector: 'app-root',
//   templateUrl: 'app.component.html',
//   styleUrls: ['app.component.scss'],
//   standalone: true,
//   imports: [IonicModule],
// })
// export class AppComponent {
//   constructor(private plateform: Platform,
//     private fcm: FcmService
//   ) {
//     this.plateform.ready().then(() => {
//       this.fcm.initPush();
//     }).catch(e => {
//       console.log({ "initili error": e });
//     });

//   }
// }

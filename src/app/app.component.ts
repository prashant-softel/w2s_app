import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Component, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, MenuController } from '@ionic/angular';
import { ConnectServer } from 'src/service/connectserver';
import { IonicStorageModule } from '@ionic/storage-angular';

import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { StorageService } from 'src/service/StorageService';
import { Router, NavigationExtras } from '@angular/router';
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
    StorageService

  ],

  imports: [
    IonicModule,
    HttpClientModule,
    CommonModule,
    FormsModule,
    IonicStorageModule
  ],
})
export class AppComponent {

  LoginPage: any = 'login';
  DashboardPage: any = 'dashboard';
  HelplinePage: any = 'helpline';
  SettingsPage: any = 'settings';
  LinkflatPage: any = 'linkflat';
  AboutUsPage: any = 'AboutusPage';
  SocietyPage: any = 'society';
  ClassifiedsPage: any = 'classifieds';
  //rootPage: any = this.LoginPage;
  pagesMember: Array<{ title: string, component: any }>;
  constructor(public globalVars: GlobalVars,
    private navCtrl: NavController,
    public menu: MenuController) {
    this.pagesMember = [
      // { title: 'Dashboard', component: this.DashboardPage },
      // { title: 'Helpline Number', component: this.HelplinePage },
      // { title: 'Classifieds', component: this.ClassifiedsPage },
      //{ title: 'Settings', component:this.SettingsPage },
      //{title : 'VisitorInPage',component: VisitorInPage},
      // { title: 'Link Another Society/Flat', component: this.LinkflatPage },
      { title: 'About Us', component: this.AboutUsPage }




    ];
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
}

import { Component, OnInit, CUSTOM_ELEMENTS_SCHEMA, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';


@Component({
  selector: 'app-about-us',
  templateUrl: './about-us.page.html',
  styleUrls: ['./about-us.page.scss'],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class AboutUsPage implements OnInit {
  version: any;
  latest_version: any;
  war_version: any;
  app_link: any;
  displayData: any;
  OldApp: any;
  Masseges: any;

  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) {

    this.version = globalVars.APP_VERSION;
    this.latest_version = globalVars.LATEST_APP_VERSION;
    this.war_version = globalVars.WAR_VERSION;

    if (this.platform.is('ios')) {
      this.app_link = "https://apps.apple.com/us/app/way2society/id1389751648";
    }
    else {
      this.app_link = "https://play.google.com/store/apps/details?id=com.ionicframework.way2society869487&rdid=com.ionicframework.way2society869487";
    }

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
  openWebsite() {
    window.open('https://www.way2society.com', '_blank', 'location=no');
    return false;
  }

  downloadApp() {
    window.open(this.app_link, '_blank', 'location=no');
    return false;
  }
}

import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';

/**
 * Generated class for the AboutusPage page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */


@Component({

  templateUrl: 'aboutus.html',
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class AboutusPage {

  version: any;
  latest_version: any;
  app_link: any;



  constructor(private globalVars: GlobalVars) {

    this.version = globalVars.APP_VERSION;
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad AboutusPage');
  }
  openWebsite() {
    window.open('https://unichemlabs.com/', '_blank', 'location=no');
    return false;
  }

  downloadApp() {
    window.open(this.app_link, '_blank', 'location=no');
    return false;
  }


}

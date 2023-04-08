import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams } from '@ionic/angular';
import { ConnectServer } from 'src/service/connectserver';
import { IonicStorageModule } from '@ionic/storage-angular';

import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { StorageService } from 'src/service/StorageService';
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
  constructor(public globalVars: GlobalVars,
    private navCtrl: NavController) { }

  logout() {
    this.globalVars.clearStorage();
    this.navCtrl.navigateRoot(this.LoginPage);
  }
}

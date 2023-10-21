import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform, } from '@ionic/angular';

import { NavigationExtras } from '@angular/router';
import { GlobalVars } from 'src/service/globalvars';
import { ConnectServer } from 'src/service/connectserver';
import { LoaderView } from 'src/service/loaderview';

@Component({
  selector: 'app-updateprofile',
  templateUrl: './updateprofile.page.html',
  styleUrls: ['./updateprofile.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class UpdateprofilePage implements OnInit {
  ProfilePage: any = 'profile';
  Unit_list: Array<any>;
  unitstatigy_array: Array<any>;
  userData: { unit_id: any };
  TotalUnits: any;
  TotalActiveUser: any;
  TotalActiveUnits: any;
  TotalInactiveUnits: any;
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams) {
    this.Unit_list = [];
    this.userData = { unit_id: "" };
    this.unitstatigy_array = [];
    this.TotalUnits = "";
    this.TotalActiveUser = "";
    this.TotalActiveUnits = "";
    this.TotalInactiveUnits = "";
  }

  ngOnInit() {
    this.fetchUnitList();
  }
  fetchUnitList() {
    this.loaderView.showLoader('Loading ...');
    var objData = [];
    this.connectServer.getData("ServiceProvider/fetchUnits", objData).then(
      resolve => {
        //this.UnitStats();
        this.loaderView.dismissLoader();
        if (resolve['success'] == 1) {
          console.log(resolve);
          var UnitList = resolve['response']['member']['0'];
          for (var i = 0; i < UnitList.length; i++) {
            this.Unit_list.push(UnitList[i]);
          }
        }
      }
    );
    this.UnitStats();
  }

  UnitStats() {
    //this.loaderView.showLoader('Loading');
    var objData = [];
    objData['fetch'] = 8;
    this.connectServer.getData("Directory", objData).then(
      resolve => {
        //this.loaderView.dismissLoader();
        if (resolve['success'] == 1) {
          var UnitStatigy = resolve['response']['UnitStatistics'];
          this.unitstatigy_array = UnitStatigy;
          this.TotalUnits = this.unitstatigy_array['TotalUnits'];
          this.TotalActiveUser = this.unitstatigy_array['ActiveUsers'];
          this.TotalActiveUnits = this.unitstatigy_array['ActiveUnitCount'];
          this.TotalInactiveUnits = this.unitstatigy_array['InactiveUnits'];
        }
        else {

        }
      }
    );
  }

  fetchDues() {
    this.loaderView.showLoader('Loading ...');
    var objData = [];
    objData['fetch'] = 10;
    objData['unit_id'] = this.userData['unit_id'];
    this.connectServer.getData("Directory", objData).then(
      resolve => {
        this.loaderView.dismissLoader();
        if (resolve['success'] == 1) {
          var DuesAmounts = resolve['response']['DuesAmount'][0];
          this.globalVars.MEMBER_DUES_AMOUNT = DuesAmounts['Total'];
        }
      }
    );
  }

  ferch() {
    var objData = [];
    objData['dash'] = "society";
    objData['uID'] = this.userData['unit_id'];
    let navigationExtras: NavigationExtras = {
      queryParams:
      {
        details: objData,
      }
    };
    this.navCtrl.navigateRoot(this.ProfilePage, navigationExtras);
    //this.navCtrl.push(this.ProfilePage, {details : objData}); 
  }
}

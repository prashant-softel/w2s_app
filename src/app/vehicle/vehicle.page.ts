import { Component, HostListener, OnInit, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { GlobalVars } from 'src/service/globalvars';
import { ConnectServer } from 'src/service/connectserver';
import { LoaderView } from 'src/service/loaderview';

@Component({
  selector: 'app-vehicle',
  templateUrl: './vehicle.page.html',
  styleUrls: ['./vehicle.page.scss'],
  standalone: true,
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ],
  imports: [IonicModule, CommonModule, FormsModule]
})
export class VehiclePage implements OnInit {
  ProfilePage: any = 'profile';
  page_title: string;
  message: any;
  isadmin: any;
  Block_unit = 0;
  Block_desc = "";
  role: string;
  roleWise: string;
  uID: any;
  userData: { member_id: any, vehicle_id: any, type: any, slot: any, sticker: any, regno: any, owner: any, make: any, model: any, color: any, set: any };
  mode: number;
  editable_name: number;
  editable_other: number;

  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) {
    this.mode = 0;
    this.uID = 0;
    this.role = "";
    this.roleWise = "";
    this.roleWise = this.role;
    this.editable_name = 1;
    this.editable_other = 1;
    this.page_title = "";
    this.message = "";
    this.isadmin = 0;
    this.userData = { member_id: 0, vehicle_id: 0, type: 2, slot: "", sticker: "", regno: "", owner: "", make: "", model: "", color: "", set: "vehicle" };

    /*
    else if(params.get("add") != null)
    {
      this.page_title = "Add Vehicle";
      this.mode = 0;
      this.uID =params.get("unitID"); 
      this.userData['member_id'] = params.get("member_id");
    }
*/
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
    let details: any;
    this.route.queryParams.subscribe(params => {
      details = params["details"];
    });
    this.page_title = "Add Vehicle";
    this.mode = 0;
    this.uID = details["unitID"];   // 18
    this.userData['member_id'] = details["member_id"]; //88

    this.Block_unit = this.globalVars.MAP_UNIT_BLOCK;
    this.Block_desc = this.globalVars.MAP_BLOCK_DESC;
    if (this.globalVars.MAP_USER_ROLE == "Admin") {
      this.role = "Admin";
    }
    else if (this.globalVars.MAP_USER_ROLE == "Super Admin") {
      this.role = "SuperAdmin";
    }
    else if (this.globalVars.MAP_USER_ROLE == "Admin Member") {
      this.role = "AdminMember";
    }
    else {
      this.role = "Member";
    }
    this.roleWise = this.role;
  }

  submit() {
    this.message = "";
    if (this.userData.regno.length == 0) {
      this.message = "Please enter vehicle registration no";
    }
    else if (this.userData.owner.length == 0) {
      this.message = "Please enter owner name";
    }
    else if (this.userData.make.length == 0) {
      this.message = "Please enter vehicle make ";
    }
    else if (this.userData.model.length == 0) {
      this.message = "Please enter vehicle madel";
    }
    else if (this.userData.color.length == 0) {
      this.message = "Please enter vehicle color ";
    }
    else {
      this.loaderView.showLoader('Please Wait ...');
      this.userData['set'] = "vehicle";
      this.connectServer.getData("MemberDetails", this.userData).then(
        resolve => {
          this.loaderView.dismissLoader();
          console.log('Response : ' + resolve);
          if (resolve['success'] == 1) {
            this.message = resolve['response']['message'];
            alert(resolve['response']['message']);
            var p = [];
            var p = [];
            if (this.roleWise == "Admin" || this.roleWise == "SuperAdmin") {
              p['tab'] = 3;
              p['uID'] = this.uID;
            }
            else {
              p['tab'] = 3;
              p['uID'] = 0;
            }
            let navigationExtras: NavigationExtras = {
              queryParams:
              {
                details: p,
              }
            };
            this.navCtrl.navigateRoot(this.ProfilePage, navigationExtras);

          }
          else {
            this.message = resolve['response']['message'];
          }
        }
      );
    }
  }

}

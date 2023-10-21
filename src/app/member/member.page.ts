import { Component, OnInit, CUSTOM_ELEMENTS_SCHEMA, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform, } from '@ionic/angular';
import { NavigationExtras } from '@angular/router';
//import * as moment from 'moment'; 
import { ActivatedRoute } from '@angular/router';
import { GlobalVars } from 'src/service/globalvars';
import { ConnectServer } from 'src/service/connectserver';
import { LoaderView } from 'src/service/loaderview';

@Component({
  selector: 'app-member',
  templateUrl: './member.page.html',
  styleUrls: ['./member.page.scss'],
  standalone: true,
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  imports: [IonicModule, CommonModule, FormsModule]
})
export class MemberPage implements OnInit {
  ProfilePage: any = 'profile';
  page_title: string;
  message: any;
  Block_unit = 0;
  Block_desc = "";
  userData: { member_id: any, mem_other_family_id: any, other_name: any, relation: any, other_mobile: any, other_email: any, other_adhaar: any, other_gender: any, other_publish_contact: number, child_bg: any, other_dob: any, other_wed: any, other_desg: any, ssc: any, other_profile: any, other_publish_profile: number, set: any, coowner: any, send_commu_emails: any };
  max_year: any;
  //todayDate : any;
  mode: number;
  uID: any;
  editable_name: number;
  editable_other: number;
  desg_list: Array<any>;
  role: string;
  roleWise: string;
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) {
    var todayDate = new Date().toISOString();

    this.mode = 0;
    this.uID = 0;
    this.editable_name = 1;
    this.editable_other = 1;
    this.page_title = "";
    this.message = "";
    this.userData = { member_id: 0, mem_other_family_id: 0, other_name: "", relation: "", other_mobile: "", other_email: "", other_adhaar: "", other_gender: "", other_publish_contact: 0, child_bg: 9, other_dob: todayDate, other_wed: todayDate, other_desg: 1, ssc: "", other_profile: "", other_publish_profile: 0, set: "member", coowner: 0, send_commu_emails: 0 };
    this.role = "";
    this.roleWise = "";
    //this.roleWise=this.role;
    //console.log(this.userData);
    this.desg_list = [];

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
    var d = new Date();
    this.max_year = d.getFullYear() + 2;
    let details: any;
    this.route.queryParams.subscribe(params => {
      details = params["details"];
    });

    this.page_title = "Add Member";
    this.mode = 0;
    this.uID = details["unitID"];   // 18
    this.userData['member_id'] = details["member_id"]; //88

    this.fetchDesignations();
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
    if (this.userData.other_name.length == 0) {
      this.message = "Please enter member name";
    }
    else if (this.userData.relation.length == 0) {
      this.message = "Please enter relation with owner";
    }
    else if (this.userData.other_mobile.length == 0) {
      this.message = "Please enter member contact no ";
    }
    else {
      this.loaderView.showLoader('Please Wait ...');
      this.userData['set'] = "member";
      console.log(this.userData['set']);
      this.connectServer.getData("MemberDetails", this.userData).then(
        resolve => {
          this.loaderView.dismissLoader();
          console.log('Response : ' + resolve);
          if (resolve['success'] == 1) {
            this.message = resolve['response']['message'];
            alert(resolve['response']['message']);
            var p = [];
            if (this.roleWise == "Admin" || this.roleWise == "SuperAdmin") {
              p['tab'] = 2;
              p['uID'] = this.uID;
            }
            else {
              p['tab'] = 2;
              p['uID'] = 0;
            }
            let navigationExtras: NavigationExtras = {
              queryParams:
              {
                details: p,
              }
            };
            this.navCtrl.navigateRoot(this.ProfilePage, navigationExtras);
            // this.navCtrl.setRoot(ProfilePage, {details : p});
          }
          else {
            this.message = resolve['response']['message'];
          }
        }
      );
    }
  }
  fetchDesignations() {
    this.loaderView.showLoader('Loading ...');
    var obj = [];
    obj['fetch'] = "desg";
    this.connectServer.getData("MemberDetails", obj).then(
      resolve => {
        console.log("RESOLVE:" + resolve);
        this.loaderView.dismissLoader();
        if (resolve['success'] == 1) {
          console.log(resolve);
          var designationList = resolve['response']['designations'];
          for (var i = 0; i < designationList.length; i++) {
            this.desg_list.push(designationList[i]);
          }
        }
      }
    );
  }
}

import { Component, OnInit, CUSTOM_ELEMENTS_SCHEMA, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { GlobalVars } from 'src/service/globalvars';
import { ConnectServer } from 'src/service/connectserver';
import { LoaderView } from 'src/service/loaderview';
@Component({
  selector: 'app-settings',
  templateUrl: './settings.page.html',
  styleUrls: ['./settings.page.scss'],
  standalone: true,
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  imports: [IonicModule, CommonModule, FormsModule]
})
export class SettingsPage implements OnInit {
  member_contact_array: Array<any>;
  member_Data_arry: Array<any>;
  userData: { member_id: any, other_mem_id: any, notify: any };
  CallbackStatus: any;
  MemberName: any;
  MemberContact: any;
  SendNotification: any;
  CallBackId: any;
  toggleStatus: boolean;
  MemberOtherID: any;
  role: any;
  roleWise: any;
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) {
    this.member_contact_array = [];
    this.member_Data_arry = [];
    this.userData = { member_id: "", other_mem_id: "", notify: "" };
    this.CallbackStatus = "0";
    this.MemberName = "";
    this.MemberContact = "";
    this.SendNotification = "";
    this.CallBackId = "";
    this.toggleStatus = true;
    this.MemberOtherID = "";
    this.role = "";
    this.roleWise = "";

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
    if (this.globalVars.MAP_USER_ROLE == "Admin") {
      this.role = "Admin";
    }
    else if (this.globalVars.MAP_USER_ROLE == "Super Admin") {
      this.role = "SuperAdmin";
    }
    else {
      this.role = this.globalVars.MAP_USER_ROLE;
    }
    this.roleWise = this.role;
    this.fetchMemberContact();
  }
  fetchMemberContact() {
    this.loaderView.showLoader('Loading ...');
    var objData = [];
    objData['fetch'] = "memberNo";
    objData['role'] = this.roleWise;
    this.connectServer.getData("Profile", objData).then(
      resolve => {
        this.loaderView.dismissLoader();
        if (resolve['success'] == 1) {
          var MemberContactNo = resolve['response']['MemberContact'];
          this.member_Data_arry = [];
          this.member_contact_array = [];
          var iIndex = 0;
          for (var i = 0; i < MemberContactNo.length; i++) {
            this.member_Data_arry.push(MemberContactNo[i]);
            this.member_contact_array[i] = MemberContactNo[i];
            this.SendNotification = this.member_contact_array[0]['send_commu_emails'];
            this.MemberOtherID = this.member_contact_array[0]['mem_other_family_id'];
            this.CallBackId = this.member_contact_array[i]['securityCall'];
            if (this.SendNotification == 1) {
              this.toggleStatus = true;
            }
            else {
              this.toggleStatus = false;
            }

            if (this.CallBackId > 0) {
              iIndex = i;
            }
            else {
              this.MemberName = this.member_contact_array[0]['other_name'];
              this.MemberContact = this.member_contact_array[0]['other_mobile'];
            }
          }
          this.MemberName = this.member_contact_array[iIndex]['other_name'];
          this.MemberContact = this.member_contact_array[iIndex]['other_mobile'];

        }
      }
    );
  }
  SelectContact() {
    this.update();
  }
  change() {
    this.CallbackStatus = "1";
  }
  update() {
    this.loaderView.showLoader('Loading ...');
    var objData = [];
    objData['fetch'] = "updateContact";
    objData['role'] = this.roleWise;
    objData['otherID'] = this.userData['other_mem_id'];
    this.connectServer.getData("Profile", objData).then(
      resolve => {
        this.loaderView.dismissLoader();
        console.log('Response : ' + resolve);
        if (resolve['success'] == 1) {
          alert("Security callback number successfully set");
          this.CallbackStatus = "0";
          this.fetchMemberContact();
        }
        else {
          alert("Security callback number not set");
        }
      }
    );
  }
  Change_Toggle() {
    this.loaderView.showLoader('Loading ...');
    var objData = [];
    objData['fetch'] = "notify";
    if (this.toggleStatus == true) {
      objData['notify'] = 1;
    }
    else {
      objData['notify'] = 0;
    }
    objData['MemOtherID'] = this.MemberOtherID;
    this.connectServer.getData("Profile", objData).then(
      resolve => {
        this.loaderView.dismissLoader();
        console.log('Response : ' + resolve);
        if (resolve['success'] == 1) {
          // alert("Security call number successfully set");
        }
        else {
          //alert("Security call number not set");
        }
      }
    );
  }
} //end 


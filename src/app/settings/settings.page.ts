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
  DND_msg_array: Array<any>;
  userData: { member_id: any, other_mem_id: any, notify: any };
  dndData: { unit_id: any, unit_no: any, dnd_type: any, dnd_msg: any }
  CallbackStatus: any;
  dnddata : any;
  CallbackDND: any;
  CallbackRadio: any;
  MemberName: any;
  MemberContact: any;
  dnd_msg: any;
  securityMessage: string = '';
  SendNotification: any;
  CallBackId: any;
  toggleStatus: boolean;
  MemberOtherID: any;
  role: any;
  iSocietyID: any;
  iUnitID: any;
  iUnitNo: any;
  selectedDndType: number = 1;
  dnd_type: any;//this.selectedDndType;
  selectedOption: string;
  radiochecked:number;
  roleWise: any;
  ProfilePage: any = 'profile';
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
    this.dndData = { unit_id: "", unit_no: "", dnd_type: "", dnd_msg: "" };
    this.CallbackStatus = "0";
    this.CallbackRadio = "0";
    this.CallbackDND = "0";
    this.MemberName = "";
    this.MemberContact = "";
    this.SendNotification = "";
    this.CallBackId = "";
    this.DND_msg_array= [];
    this.toggleStatus = true;
    this.MemberOtherID = "";
    this.role = "";
    this.dnd_msg = "";
    this.iSocietyID = "";
    this.roleWise = "";
    this.iUnitID = "";//this.globalVars.MAP_UNIT_ID;
    this.iUnitNo = "";//this.globalVars.MAP_UNIT_NO;
    this.dnd_type = 1;
    this.selectedDndType = 0;
    this.radiochecked=0;
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
    // this.globalVars.MAP_SOCIETY_ID = this.iSocietyID;
    
    this.iSocietyID = this.globalVars.MAP_SOCIETY_ID;
    this.iUnitID = this.globalVars.MAP_UNIT_ID;
    this.iUnitNo = this.globalVars.MAP_UNIT_NO;
    console.log(this.iSocietyID);
    console.log(this.iUnitID);
    console.log(this.iUnitNo);
    this.fetchMemberContact();
    
  }

  fetchMemberContact() {
    this.loaderView.showLoader('Loading ...');
    var objData = [];
    this.fetchMessage();

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
            console.log("member array", this.member_Data_arry)
            this.member_contact_array[i] = MemberContactNo[i];
            this.SendNotification = this.member_contact_array[0]['send_commu_emails'];
            this.MemberOtherID = this.member_contact_array[0]['mem_other_family_id'];
            console.log("other family", this.member_contact_array);
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
          console.log("other phone", this.member_contact_array[iIndex]['other_mobile']);
        }
      }
    );
    
  }
  SelectContact() {
    this.update();
    // console.log("printing select contact");
    // console.log("select contact ",this.member_Data_arry);

  }

  showradio() {
    this.CallbackRadio = "1";
  }
  Readless() {
    this.CallbackStatus = "0";
  }
  changednd() {
    this.CallbackDND = "1";
  }
  UpdateContact() {
    this.navCtrl.navigateRoot(this.ProfilePage);

  }
  hideoptions() {
    this.CallbackDND = '0'; // Set CallbackDND to '0'
    this.securityMessage = '';
  }
  selectmessage() {
    const data = {
      // message: this.message,
      // option: this.selectedOption


    };

  }
  radioGroupChange(event) {
    // Handle the selected option here
    const selectedOption = event.detail.value;
    console.log('Selected option:', selectedOption);
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
  SaveMessage() {

    this.loaderView.showLoader('Loading ...');
    var objData = [];
    objData['fetch'] = "UpdateDndData";
    objData['iSocietyID'] = this.iSocietyID;
    objData['unitId'] = this.iUnitID;
    objData['unit_no'] = this.iUnitNo;
    objData['dnd_msg'] = this.dndData['dnd_msg'];

    objData['dnd_type'] = this.selectedDndType;
    console.log("objdata", objData);

    // objData['society_id'] = this.iSocietyID;
    this.connectServer.getData("Profile", objData).then(
      resolve => {
        this.loaderView.dismissLoader();
        console.log('Response : ' + resolve);
        if (resolve['success'] == 1) {

          alert("DND message added successfully");
          var message = resolve['response']['objdata'];


        }
      }
    )

  }
  // getDndmessage
  fetchMessage() {
    var objData = [];
    objData['fetch'] = "fetchDndData";
     objData['iSocietyID'] = this.iSocietyID;
     objData['unitId'] = this.iUnitID;
     objData['dnd_type'] = this.dnd_type;
     console.log("objdata", objData);

    this.connectServer.getData("Profile", objData).then(
      resolve => {
        this.loaderView.dismissLoader();

        console.log('Response : ' + resolve);
        if(resolve['success'] == 1)
        {
           var dnddata = resolve['response']['DND_message'];
           console.log("dnd_data",dnddata);
          //  this.DND_msg_array = [];
          //  this.DND_msg_array.push(message);
          
            this.selectedDndType = dnddata[0]['dnd_type'];
            if( this.selectedDndType == 1)
            {
              this.radiochecked=1;
            }
            if( this.selectedDndType == 2)
            {
              this.radiochecked=2;
            }
            if( this.selectedDndType == 3)
            {
              this.radiochecked=3;
              this.CallbackDND = '1';
            }
            else
            {

            }
            this.dnd_msg = dnddata[0]['dnd_msg'];
            console.log(this.dnd_msg);
              console.log(this.selectedDndType);
          //if(this.dnd_type === 3){
            //this.CallbackDND = '1'; // Set CallbackDND to '1'
            

          //}
        }




      })
}
} //end 


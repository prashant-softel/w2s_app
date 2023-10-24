import { Component, OnInit, CUSTOM_ELEMENTS_SCHEMA, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NavController, NavParams, ActionSheetController, ToastController, Platform, LoadingController, IonicModule } from '@ionic/angular';
// import { File } from '@ionic-native/file/ngx';
// import { FileTransfer, FileTransferObject } from '@ionic-native/file-transfer/ngx';
// import { FilePath } from '@ionic-native/file-path/ngx';
// import { Camera } from '@ionic-native/camera/ngx';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
// import { HTTP } from '@ionic-native/http/ngx';
// import { HttpClient, HttpHeaders } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';


declare var cordova: any;
enum statusEnum { "Raised" = 1, "Waiting", "In progress", "Completed", "Cancelled" }
enum priorityEnum { "Critical" = 1, "High", "Medium", "Low" }
@Component({
  selector: 'app-addaddressproof',
  templateUrl: './addaddressproof.html',
  styleUrls: ['./addaddressproof.scss'],
  standalone: true,
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ],
  imports: [IonicModule, CommonModule, FormsModule,],
  providers: [
    // Camera, FileTransfer, File, FilePath, HTTP
  ]
})

export class AddAddressProof implements OnInit {
  ServiceRequestPage: any = 'servicerequest';
  data: any = {};
  worklist_array: any;
  memberList: Array<any>;
  details: {};
  checkedMemberList: Array<boolean>;
  selectedMemberList: Array<{}>;
  userData: { srTitle: any, srCategory: any, srPriority: any, purpose: any, stayingSince: any, address: any, ownerName: any, note: any, unitId: any };
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    // private http: HTTP,
    private navParams: NavParams,
    private params: NavParams,
    private route: ActivatedRoute,
    private http: HttpClient,
    private loadingCtrl: LoadingController) {
    // this.http = http;
    //this.getMemberDetails();
    this.worklist_array = [];
    this.memberList = [];
    this.details = [];
    this.checkedMemberList = [];
    this.selectedMemberList = [];
    this.userData = { srTitle: "", srCategory: "", srPriority: "", purpose: "", stayingSince: "", address: "", ownerName: "", note: "", unitId: 0 };

  }

  ngOnInit() {
    console.log('ionViewDidLoad AddAddressProof');
    this.loaderView.dismissLoader();
    //alert(this.globalVars.LoginID);
    this.details = this.navParams.get("details");
    console.log("serviceRequest Details", this.details);
    this.userData.srTitle = this.details['title'];
    this.userData.srPriority = this.details['priority'];
    this.userData.unitId = this.details['unitId'];
    this.userData.srCategory = this.globalVars.ADDRESS_PROOF_REQUEST_ID;
    this.getMemberDetails();
    this.getMemberAddress();
  }
  @HostListener('document:ionBackButton', ['$event'])
  overrideHardwareBackAction(event: any) {
    event.detail.register(100, async () => {
      event.stopImmediatePropagation();
      event.stopPropagation();
      event.preventDefault();
    });
  }
  public getMemberDetails() {
    console.log("this.userData.unitId :", this.userData.unitId);
    // console.log("this.userData.unitId :",this.userData.unitId);
    var data = [];
    var link = this.globalVars.HOST_NAME + 'api.php';
    // const headers = {
    //   'Content-Type': 'application/json', // Adjust the content type as needed
    //   // Add any other headers you require
    // };
    // const httpOptions = {
    //   headers: new HttpHeaders({
    //     'Content-Type': 'application/json'
    //   })
    // }
    // var myData = JSON.stringify({
    //   method: "getMemberDetails", societyId: this.globalVars.MAP_SOCIETY_ID, unitId: this.userData.unitId, role: this.globalVars.MAP_USER_ROLE
    // });
    const myData = {
      method: "getMemberDetails",
      societyId: this.globalVars.MAP_SOCIETY_ID,
      unitId: this.userData.unitId,
      role: this.globalVars.MAP_USER_ROLE
    }
    var myDataJson = JSON.stringify(myData);
    this.http.post(link, myDataJson, {
      headers: {
        'Content-Type': 'application/json'
      }
    })
      .subscribe(data => {
        this.data.response = data["_body"];
        var parsedData = JSON.parse(this.data.response);
        console.log(parsedData);
        const member_details = parsedData['response']['member_details'];
        console.log("MemberDetails ", member_details);
        console.log("Length ", member_details.length);//5
        member_details.forEach((member: any) => this.memberList.push(member))
        this.userData.ownerName = member_details[0]['primary_owner_name']
        for (var i = 0; i < member_details.length; i++) {
          this.checkedMemberList[i] = false;
        }
        console.log("memberList", this.memberList);

      }, error => {
        console.log("Oooops!");
      });
  }
  public getMemberAddress() {
    var data = [];
    var link = this.globalVars.HOST_NAME + 'api.php';
    const myData = {
      method: "getMemberDetails",
      societyId: this.globalVars.MAP_SOCIETY_ID,
      unitId: this.userData.unitId,
      role: this.globalVars.MAP_USER_ROLE
    }
    var myDataJson = JSON.stringify(myData);
    // const httpOptions = {
    //   headers: new HttpHeaders({
    //     'Content-Type': 'application/json'
    //   })
    // }
    this.http.post(link, myDataJson, {
      headers: {
        'Content-Type': 'application/json'
      }
    })
      .subscribe(data => {
        this.data.response = data["_body"];
        var parsedData = JSON.parse(this.data.response);
        console.log(parsedData);
        this.userData.address = parsedData['response']['memberAddress'];

        console.log("Address : ", this.userData.address);

      }, error => {
        console.log("Oooops!");
      });
  }
  public createPassportRequest() {
    console.log("this.selectedMemberList.length : ", this.selectedMemberList.length);
    if (this.selectedMemberList.length == 0) {
      alert("Please select at least one member to apply.");
    }
    else if (this.userData.stayingSince.length == 0) {
      alert("Please provide Staying Since date.");
    }
    else if (this.userData.purpose == 0) {
      alert("Please select appropriate purpose.");
    }
    else {
      var data = [];
      var link = this.globalVars.HOST_NAME + 'api.php';
      const myData = {
        method: "getMemberDetails",
        societyId: this.globalVars.MAP_SOCIETY_ID,
        unitId: this.userData.unitId,
        role: this.globalVars.MAP_USER_ROLE
      }
      var myDataJson = JSON.stringify(myData);
      // const httpOptions = {
      //   headers: new HttpHeaders({
      //     'Content-Type': 'application/json'
      //   })
      // }
      this.http.post(link, myDataJson, {
        headers: {
          'Content-Type': 'application/json'
        }
      })
        .subscribe(data => {
          this.data.response = data["_body"]; //https://stackoverflow.com/questions/39574305/property-body-does-not-exist-on-type-response
          var passportData = JSON.parse(this.data.response);
          var resultStatus = passportData['success'];
          if (resultStatus == "1") {
          }
          else {
            alert("We are unable to connect our server.Please try after some time.");
          }
          var p = [];
          if (this.globalVars.MAP_USER_ROLE == "Member") {
            p['dash'] = "society";
            //console.log(p);
          }
          else {
            p['dash'] = "admin";
          }
          let navigationExtras: NavigationExtras = {
            queryParams:
            {
              details: p,
            }
          };
          this.navCtrl.navigateRoot(this.ServiceRequestPage, navigationExtras);
          // this.navCtrl.navigateRoot(ServiceRequestPage, {details : p});
        }, error => {
          console.log("Oooops!");
        });
    }
  }
  public CheckMemberlist(value, i) {
    this.checkedMemberList[i] = !this.checkedMemberList[i];
    this.selectedMemberList.push(value);
  }



}

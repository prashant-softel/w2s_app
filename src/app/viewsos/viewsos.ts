import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { ConnectServer } from 'src/service/connectserver';
import { LoaderView } from 'src/service/loaderview';
import { NavController, NavParams, ActionSheetController } from '@ionic/angular';
import { ActivatedRoute } from '@angular/router';



@Component({
  selector: 'app-viewsos',
  templateUrl: './viewsos.html',
  styleUrls: ['./viewsos.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ViewsosPage implements OnInit {
  displayData: Array<any>;
  Respons_Array: Array<any>;
  sosFlag: any;
  WingData: any;
  UnitData: any;
  ResolverName: any;
  ResolverRole: any;
  ResolvedStatus: any;
  AlertCreationId: any;
  ResponseValue: Number;
  LoaderIcon: any;
  disabled: any;
  disabled1: any;
  sos_acked_by: any;
  ButtonByStatus: any;
  OwnerName: any;
  ack_Role: any;
  MemberContact: any;
  FloorNo: any;
  connectDB: any;
  sendIssue: any;
  maxTime: any = 5;
  timer: any;
  sendMedicalInterval: any;
  hidevalue: boolean;
  timercall: any;
  ResponseData: Array<any>;

  constructor(public globalVars: GlobalVars,
    public connectServer: ConnectServer,
    private loaderView: LoaderView,
    private params: NavParams,
    private navCtrl: NavController,
    private route: ActivatedRoute) {
    this.displayData = [];
    this.Respons_Array = [];
    this.sosFlag = "";
    this.WingData = "";
    this.UnitData = "";
    this.ResolverName = "";
    this.ResolverRole = "";
    this.ResolvedStatus = "0";
    this.AlertCreationId = "";
    this.ResponseValue = 0;
    this.LoaderIcon = "";
    this.disabled = false;
    this.disabled1 = false;
    this.ButtonByStatus = "0";
    this.OwnerName = "";
    this.MemberContact = "";
    this.FloorNo = "";
    this.connectDB = "0";
    this.sendIssue = "0";
    // this.displayData = params.get("details");
    console.log(this.displayData);
    this.timercall = "0";
    this.sos_acked_by = "";
    this.ack_Role = "";

  }
  ngOnInit(): void {
    let details: any;
    this.route.queryParams.subscribe(params => {
      details = params["details"];

    });
    this.displayData = details;
    console.log('ionViewDidLoad ViewsosPage');
    this.sosFlag = this.displayData['sos'];

    console.log("details", details);
    console.log("sosflag", this.sosFlag);
    this.fetchMemberData();
  }
  fetchMemberData() {
    this.loaderView.showLoader('Loading ...');
    var objData = [];
    this.GetAccess();
    console.log("getaccess", this.GetAccess);
    objData['fetch'] = "5";
    this.connectServer.getData("SOSAlert", objData).then(
      resolve => {
        this.loaderView.dismissLoader();
        //this.CallbackStatus = "1";
        console.log('Response : ' + resolve['details']);
        if (resolve['success'] == "1") {
          var manberData = resolve['response']['memberdetails'];
          this.WingData = manberData[0]['wing'];
          this.UnitData = manberData[0]['unit_no'];
          this.OwnerName = manberData[0]['owner_name'];
          this.MemberContact = manberData[0]['MemberContact'];
          this.FloorNo = manberData[0]['floor_no'];
          console.log("menbaerdata", manberData);

        }
        else {

        }
      }
    );
  }

  GetAccess() {
    var objData = [];
    objData['fetch'] = "7";
    this.connectServer.getData("SOSAlert", objData).then(
      resolve => {
        //this.loaderView.dismissLoader();
        console.log('Response : ' + resolve);
        if (resolve['success'] == 1) {
          var Databases = resolve['response']['smDatabse'];
          this.connectDB = "1";

        }
        else {
          this.connectDB = "0";
        }
      }
    );

  }

  SendMedical() {
    //this.disabled = true;
    this.timer = setTimeout(x => {
      if (this.maxTime <= 0) { }
      this.maxTime -= 1;

      if (this.maxTime > 0) {
        this.timercall = "1";
        this.hidevalue = false;
        this.SendMedical();
        console.log("Medical", this.SendMedical());
      }
      else {
        this.disabled1 = true;
        this.hidevalue = true;
        console.log(this.maxTime);
        this.timercall = "0";
        this.loaderView.showLoader('Loading ...');
        var objData = [];
        objData['fetch'] = "1";
        objData['AlertType'] = "Medical Emergency";
        objData['sosType'] = "1";
        objData['Alertstatus'] = "0";
        objData['UnitNo'] = this.UnitData;
        objData['OwnerName'] = this.OwnerName;
        objData['OwneContact'] = this.MemberContact;
        objData['FloorNo'] = this.FloorNo;
        objData['Wing'] = this.WingData;
        this.connectServer.getData("SOSAlert", objData).then(
          resolve => {
            this.loaderView.dismissLoader();
            //this.CallbackStatus = "1";
            console.log('Response : ' + resolve);
            if (resolve['success'] == 1) {
              this.AlertCreationId = resolve['response']['MedicalAlert'];
              // this.ButtonByStatus="1";
              setInterval(() => {
                if (this.ResponseValue == 0) {
                  this.LoaderIcon = "1";
                  this.sendIssue = "1";
                  this.GetResponse(this.AlertCreationId);
                }
                else {
                  this.LoaderIcon = "0";
                  // this.loaderView.dismissLoader();	
                }

              }, 5000);
            }
            else {

            }
          }
        );
      }

    }, 1000);
    // clearInterval(this.sendMedicalInterval);

    // // Set up a new interval to call SendMedical every 1000 milliseconds (1 second)
    // this.sendMedicalInterval = setInterval(() => {
    //   if (this.maxTime <= 0) {
    //     clearInterval(this.sendMedicalInterval); // Stop the interval when maxTime reaches 0
    //   } else {
    //     this.timercall = "1";
    //     this.hidevalue = false;
    //     console.log("Medical");

    //     // API request logic here
    //     this.loaderView.showLoader('Loading ...');
    //     var objData = [];
    //       objData['fetch'] ="1";
    //           objData['AlertType']="Medical Emergency";
    //              objData['sosType']="1";
    //            objData['Alertstatus']="0";
    //            objData['UnitNo']=this.UnitData;
    //            objData['OwnerName']=this.OwnerName;
    //          objData['OwneContact']=this.MemberContact;
    //           objData['FloorNo']=this.FloorNo;
    //             objData['Wing']=this.WingData;


    //     this.connectServer.getData("SOSAlert", objData).then(
    //       resolve => {
    //         this.loaderView.dismissLoader();
    //         console.log('Response: ' + JSON.stringify(resolve));

    //         if (resolve['success'] === 1) {
    //           this.AlertCreationId =  resolve['response']['MedicalAlert'];

    //           // Set up another interval to check for ResponseValue
    //           const responseInterval = setInterval(() => {
    //             if (this.ResponseValue === 0) {
    //               this.LoaderIcon = "1";
    //               this.sendIssue = "1";
    //               this.GetResponse(this.AlertCreationId);
    //             } else {
    //               this.LoaderIcon = "0";
    //               clearInterval(responseInterval); // Stop the response check interval
    //             }
    //           }, 5000);

    //         } else {
    //           // Handle the case when the API request fails
    //         }
    //       }
    //     );
    //   }
    // }, 1000);
  }


  SendFire() {
    this.timer = setTimeout(x => {
      if (this.maxTime <= 0) { }
      this.maxTime -= 1;

      if (this.maxTime > 0) {
        this.timercall = "1";
        this.hidevalue = false;
        this.SendMedical();
      }
      else {
        this.disabled1 = true;
        this.hidevalue = true;
        console.log(this.maxTime);
        this.timercall = "0";
        this.loaderView.showLoader('Loading ...');
        var objData = [];
        objData['fetch'] = "2";
        objData['AlertType'] = "Fire or Gas Emergency";
        objData['sosType'] = "2";
        objData['Alertstatus'] = "0";
        objData['UnitNo'] = this.UnitData;
        objData['OwnerName'] = this.OwnerName;
        objData['OwneContact'] = this.MemberContact;
        objData['FloorNo'] = this.FloorNo;
        objData['Wing'] = this.WingData;
        this.connectServer.getData("SOSAlert", objData).then(
          resolve => {
            this.loaderView.dismissLoader();
            //this.CallbackStatus = "1";
            console.log('Response : ' + resolve);
            if (resolve['success'] == 1) {
              //this.ButtonByStatus="1";
              this.AlertCreationId = resolve['response']['FireAlert'];
              setInterval(() => {
                if (this.ResponseValue == 0) {
                  this.LoaderIcon = "1";
                  this.sendIssue = "1";
                  this.GetResponse(this.AlertCreationId);
                }
                else {
                  this.LoaderIcon = "0";

                }

              }, 5000);
            }
            else {

            }
          }
        );
      }

    }, 1000);
  }

  SendLift() {
    this.timer = setTimeout(x => {
      if (this.maxTime <= 0) { }
      this.maxTime -= 1;

      if (this.maxTime > 0) {
        this.timercall = "1";
        this.hidevalue = false;
        this.SendMedical();
      }
      else {
        this.disabled1 = true;
        this.hidevalue = true;
        console.log(this.maxTime);
        this.timercall = "0";
        this.loaderView.showLoader('Loading ...');
        var objData = [];
        objData['fetch'] = "3";
        objData['AlertType'] = "Lift Emergency";
        objData['sosType'] = "3";
        objData['Alertstatus'] = "0";
        objData['UnitNo'] = this.UnitData;
        objData['OwnerName'] = this.OwnerName;
        objData['OwneContact'] = this.MemberContact;
        objData['FloorNo'] = this.FloorNo;
        objData['Wing'] = this.WingData;
        this.connectServer.getData("SOSAlert", objData).then(
          resolve => {
            this.loaderView.dismissLoader();
            //this.CallbackStatus = "1";
            // console.log('Response : ' + resolve);
            if (resolve['success'] == 1) {
              // this.ButtonByStatus="1";
              this.AlertCreationId = resolve['response']['LiftAlert'];
              setInterval(() => {
                if (this.ResponseValue == 0) {
                  this.LoaderIcon = "1";
                  this.sendIssue = "1";
                  this.GetResponse(this.AlertCreationId);
                }
                else {
                  this.LoaderIcon = "0";
                  // this.loaderView.dismissLoader();	
                }

              }, 5000);
            }
            else {
              // alert("Security callback number not set");
            }
          }
        );
      }

    }, 1000);
  }


  SendOther() {
    this.timer = setTimeout(x => {
      if (this.maxTime <= 0) { }
      this.maxTime -= 1;

      if (this.maxTime > 0) {
        this.timercall = "1";
        this.hidevalue = false;
        this.SendMedical();
      }
      else {
        this.disabled1 = true;
        this.hidevalue = true;
        console.log(this.maxTime);
        this.timercall = "0";
        this.loaderView.showLoader('Loading ...');
        var objData = [];
        objData['fetch'] = "4";
        objData['AlertType'] = "Theft or Others";
        objData['Alertstatus'] = "0";
        objData['sosType'] = "4";
        objData['UnitNo'] = this.UnitData;
        objData['OwnerName'] = this.OwnerName;
        objData['OwneContact'] = this.MemberContact;
        objData['FloorNo'] = this.FloorNo;
        objData['Wing'] = this.WingData;
        this.connectServer.getData("SOSAlert", objData).then(
          resolve => {
            this.loaderView.dismissLoader();
            //this.CallbackStatus = "1";
            console.log('Response : ' + resolve);
            if (resolve['success'] == 1) {
              // this.ButtonByStatus="1";
              this.AlertCreationId = resolve['response']['OtherAlert'];
              setInterval(() => {
                if (this.ResponseValue == 0) {
                  this.LoaderIcon = "1";
                  this.sendIssue = "1";
                  this.GetResponse(this.AlertCreationId);
                }
                else {
                  this.LoaderIcon = "0";
                  // this.loaderView.dismissLoader();	
                }

              }, 5000);
            }
            else {

            }
          }
        );
      }

    }, 1000);
  }
  GetResponse(alertID) {
    //this.loaderView.showLoader('Loading ...');
    var objData = [];
    objData['fetch'] = "6";
    objData['AlertID'] = alertID;
    this.connectServer.getData("SOSAlert", objData).then(
      resolve => {
        //this.loaderView.dismissLoader();
        console.log('Response : ' + resolve);
        if (resolve['success'] == 1) {
          this.Respons_Array = [];
          var ResolveData = resolve['response']['AlertStatus']['AlertDetails'];
          this.ResolvedStatus = ResolveData[0]['AlertStatus'];
          var ResponseData = resolve['response']['AlertStatus']['AckdDeatils'];
          for (var iCnt = 0; iCnt < ResponseData.length; iCnt++) {
            this.Respons_Array.push(ResponseData[iCnt]);
            console.log("Rdata", ResponseData[iCnt]);
          }
          this.ResponseValue = 1;
          this.LoaderIcon = "0";
          this.ButtonByStatus = "1"
          this.sos_acked_by = this.ResponseData['sos_acked_by'];
          this.ack_Role = this.ResponseData['ack_Role']

        }
        else {
          this.ResponseValue = 0;
          this.LoaderIcon = "1";
          this.ButtonByStatus = "0"
        }
      }
    );

  }

  ResolvedByMe() {
    this.disabled = true;
    var objData = [];
    objData['fetch'] = "8";
    objData['AlertID'] = this.AlertCreationId;
    objData['MemberName'] = this.OwnerName;
    objData['Role'] = "Self";
    objData['AlertStatus'] = "2";
    //objData['']=''
    // alert(objData['MemberName']);
    //alert(objData['Role']);
    this.connectServer.getData("SOSAlert", objData).then(
      resolve => {
        //this.loaderView.dismissLoader();
        console.log('Response : ' + resolve);
        if (resolve['success'] == 1) {
          setInterval(() => {
            if (this.ResponseValue == 0) {
              this.LoaderIcon = "1";
              this.GetResponse(this.AlertCreationId);
            }
            else {
              this.LoaderIcon = "0";
              // this.loaderView.dismissLoader();  
            }

          }, 5000);

        }
        else {

        }
      }
    );
  }

  CancleRequest() {
    clearInterval(this.timer);

    // this.hidevalue=false;
    this.timercall = "0";
    this.maxTime = 5;
  }


}

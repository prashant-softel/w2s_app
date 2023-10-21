import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform, LoadingController } from '@ionic/angular';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { GlobalVars } from 'src/service/globalvars';
import { ConnectServer } from 'src/service/connectserver';
import { LoaderView } from 'src/service/loaderview';
@Component({
  selector: 'app-dues',
  templateUrl: './dues.page.html',
  styleUrls: ['./dues.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class DuesPage implements OnInit {
  //ViewBillPage='viewbill';
  ViewBillPage: any = 'viewbill';
  ViewreceiptPage: any = 'viewreceipt';
  CnotePage: any = 'cnote';
  particulars: Array<{ mode: string, date: string, debit: string, credit: string, balance: number, IsOpeningBill: any, period: any, billtype: any, Unit: any, showIn: string, cdnid: any, inv_number: any, inv_id: any }>;
  role: any;
  roleWise: any;
  showIn: any;
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute,
    private loadingCtrl: LoadingController) {
    this.particulars = [];
    this.role = "";
    this.roleWise = '';
    this.showIn = "0";
    if (this.globalVars.MAP_USER_ROLE == "Member") {
      this.role = "Member";
    }
    else if (this.globalVars.MAP_USER_ROLE == "Admin Member") {

      this.role = "AdminMember";
    }
    else {
      this.role = "Admin";
    }

    this.roleWise = this.role;
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



    let data: any;
    this.route.queryParams.subscribe(params => {
      data = params["details"];

    });
    console.log("data", data);

    console.log("unit", this.globalVars.MAP_UNIT_ID);
    let details: [];
    var objData = [];
    objData['fetch'] = 1;   /// New Version
    if (data == undefined) {
      objData['UnitID'] = this.globalVars.MEMBER_UNIT_ID;
    }
    else {
      objData['UnitID'] = data;
    }

    this.connectServer.getData("MemberLedger", objData).then(
      resolve => {
        this.loaderView.dismissLoader();
        if (resolve['success'] == 1) {
          details = resolve['response']
          var receiptData = details['BillnReceipts'];
          console.log('RD1 ' + receiptData);
          console.log('RD 2' + receiptData.length);
          for (var i = (receiptData.length - 1); i >= 0; i--) {
            var sMode = receiptData[i]['Mode'];
            var sDate = receiptData[i]['Date'];
            var cdnid1 = 0;
            var inv_number = 0;
            var inv_id = 0;
            if (receiptData[i]['Type'] == 0) {
              if (receiptData[i]['IsOpeningBill'] == 1) {
                //var Isopening =receiptData[i]['IsOpeningBill'];
                sMode = "Opening Balance";
                this.showIn = "1";
                //sDate = "No date";
              }
              else {
                if (receiptData[i]['BillType'] == 1 || receiptData[i]['BillType'] == true) {
                  sMode = "S-" + sMode;
                  cdnid1 = receiptData[i]['Cd_id'];
                  this.showIn = "2";
                }
                else {
                  sMode = "M-" + sMode;
                  this.showIn = "3";
                  cdnid1 = receiptData[i]['Cd_id'];
                }
              }
            }
            else {
              sMode = receiptData[i]['Mode'];
              inv_number = receiptData[i]['Inv_Number'];
              inv_id = receiptData[i]['Inv_id']
              this.showIn = "4";
            }
            // alert( this.showIn);
            var objData = { mode: sMode, date: sDate, debit: receiptData[i]['Debit'], credit: receiptData[i]['Credit'], balance: receiptData[i]['Balance'], IsOpeningBill: receiptData[i]['IsOpeningBill'], period: receiptData[i]['PeriodID'], billtype: receiptData[i]['BillType'], Unit: receiptData[i]['UnitID'], showIn: this.showIn, cdnid: cdnid1, inv_number: inv_number, inv_id: inv_id };
            this.particulars.push(objData);
          }
          console.log('obj', objData);

          var iBalance = 0;
          for (var i = 0; i < receiptData.length; i++) {
            iBalance += receiptData[i]['Debit'] - receiptData[i]['Credit'];
            this.particulars[receiptData.length - 1 - i]['balance'] = iBalance;
          }
        }
      }
    );
    //this.route.queryParams.subscribe(params => {
    //details = params["details"];

    //});
    ///pdf viewer 

  }

  viewDetails(particular) {
    if ((particular.mode == "M-Bill" || particular.mode == "S-Bill") && particular.IsOpeningBill == 0) {
      this.showLoading();
      this.loaderView.showLoader('Loading ...');
      var objData1 = [];
      objData1['PeriodID'] = particular.period;
      objData1['BT'] = particular.billtype;
      objData1['fetch'] = 1;
      if (this.roleWise == "Admin" || this.roleWise == "AdminMember") {
        objData1['UnitId'] = particular.Unit;

      }
      else {
        objData1['UnitId'] = 0;
      }
      this.connectServer.getData("Bill", objData1).then(
        resolve => {
          this.loaderView.dismissLoader();
          if (resolve['success'] == 1) {
            let navigationExtras: NavigationExtras = {
              queryParams:
              {
                details: resolve['response'],
              }
            };
            this.navCtrl.navigateRoot(this.ViewBillPage, navigationExtras);
            // this.navCtrl.push(ViewBillPage, {details : resolve['response']});
          }
        }
      );
    }
    else if (particular.mode == "M-Receipt" || particular.mode == "S-Receipt") {
      let navigationExtras: NavigationExtras = {
        queryParams:
        {
          period_id: particular.period,
          Unit: particular.Unit,
        }
      };
      this.navCtrl.navigateRoot(this.ViewreceiptPage, navigationExtras);
      //this.navCtrl.push(ViewreceiptPage, {period_id : particular.period,Unit: particular.Unit});
    }
    else if (particular.mode == "M-DNote" || particular.mode == "S-DNote" || particular.mode == "M-CNote" || particular.mode == "S-CNote") {
      let navigationExtras: NavigationExtras = {
        queryParams:
        {
          Unit: particular.Unit,
          BT: particular.billtype,
          cdnid: particular.cdnid,
          mode1: particular.mode,
          inv_number: particular.inv_number,
          inv_id: particular.inv_id,
        }
      };
      this.navCtrl.navigateRoot(this.CnotePage, navigationExtras);
      //this.navCtrl.push(CnotePage, {Unit: particular.Unit,BT: particular.billtype,cdnid : particular.cdnid, mode1 : particular.mode,inv_number : particular.inv_number,inv_id : particular.inv_id});
    }
    else if (particular.mode == "SInvoice") {
      let navigationExtras: NavigationExtras = {
        queryParams:
        {
          Unit: particular.Unit,
          BT: particular.billtype,
          cdnid: particular.cdnid,
          mode1: particular.mode,
          inv_number: particular.inv_number,
          inv_id: particular.inv_id,
        }
      };
      this.navCtrl.navigateRoot(this.CnotePage, navigationExtras);
      //this.navCtrl.push(CnotePage, {Unit: particular.Unit,BT: particular.billtype,cdnid : particular.cdnid, mode1 : particular.mode,inv_number : particular.inv_number,inv_id : particular.inv_id});
    }

  }
  async showLoading() {
    const loading = await this.loadingCtrl.create({
      message: 'Please wait...',
      duration: 5000,
      cssClass: 'loader-css-class'
    });
    loading.present();
  }
}

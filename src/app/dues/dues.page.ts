import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform, LoadingController } from '@ionic/angular';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { GlobalVars } from 'src/service/globalvars';
import { ConnectServer } from 'src/service/connectserver';
import { LoaderView } from 'src/service/loaderview';
import { HttpClient } from '@angular/common/http';
// import axios from 'axios';
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
  particulars: Array<{
    mode: string, date: string, debit: string, credit: string, balance: number, IsOpeningBill: any, period: any, billtype: any, Unit: any, showIn: string, cdnid: any, inv_number: any, inv_id: any, SocietyCode: any, BillFor: any, BillYear: any, UnitID: any
  }>;
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
    private loadingCtrl: LoadingController,
    private http: HttpClient,

  ) {
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
            var objData = {
              mode: sMode, date: sDate, debit: receiptData[i]['Debit'], credit: receiptData[i]['Credit'], balance: receiptData[i]['Balance'], IsOpeningBill: receiptData[i]['IsOpeningBill'], period: receiptData[i]['PeriodID'], billtype: receiptData[i]['BillType'], Unit: receiptData[i]['UnitID'], showIn: this.showIn, cdnid: cdnid1, inv_number: inv_number, inv_id: inv_id,
              SocietyCode: receiptData[i]['SocietyCode'], BillFor: receiptData[i]['BillFor'], BillYear: receiptData[i]['BillYear'], UnitID: receiptData[i]['UnitID']
            };
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

  public openPdf(myData, particular) {
    // console.log("this.userData.unitId :",this.userData.unitId);
    var link = this.globalVars.HOST_NAME + 'pdf_api.php';


    var myDataJson = {
      "method": "getBillPdf",
      "society_code": "RHG_TEST",
      "Period_id": "116",
      "Unit_no": "202",
      "Bill_type": "0",
      "societyId": "59",
      "unitid": "16",
      "role": "Member"
    };//JSON.stringify(myData);
    this.http.post(link, myDataJson, {
      headers: {
        'Content-Type': 'application/json'
      }
    })
      .subscribe(data => {
        console.log({ "responseData": data });
        if (data["url"] != "" && data["url"] != "") {
          window.open(data["url"], '_blank', 'location=no');

        } else {
          this.onMBillClick(particular);

        }
        return true;
      }, error => {
        console.log("Oooops!");
        this.onMBillClick(particular);
        return false;

      });
  }
  onMBillClick(particular) {
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

  async viewDetails(particular) {
    if (particular.mode == "M-Bill") {
      // var pdfUrl: string = "https://way2society.com/maintenance_bills/RHG_TEST/October-December%202022/bill-RHG_TEST-202-October-December%202022-0.pdf";

      this.globalVars.getMapDetails().then(
        async value => {
          var body = {
            'method': "getBillPdf",
            "society_code": "RHG_TEST",//particular.SocietyCode,
            "Period_id": "116",//particular.period,
            "Unit_no": "203",//value.MAP_UNIT_NO,
            "Bill_type": "0",//particular.billtype,
            "societyId": "59",//value.MAP_SOCIETY_ID,
            "unitid": "17",//particular.UnitID,
            "role": "Member",//value.MAP_USER_ROLE,
          }
            ;
          console.log({ "body": body });
          await this.openPdf(body, particular);

        }
      );
      return;
    }
    // if (particular.mode == "M-Bill") {
    //   console.log("particular.BillFor", particular.BillFor);
    //   // var billforeBufferList = particular.BillFor.split('-');
    //   // console.log("billforeBuffer:", billforeBufferList);
    //   // billforeBufferList.pop();
    //   // var billforeBuffer = billforeBufferList.join('-');
    //   var s: string = "https://way2society.com/maintenance_bills/RHG_TEST/October-December%202022/bill-RHG_TEST-202-October-December%202022-0.pdf";
    //   // https://way2society.com/maintenance_bills/SocietyCode/BillFor%20BillYear/bill-SocietyCode-202-BillFor%20 BillYear-0.pdf
    //   // var s: string = `https://way2society.com/maintenance_bills/${particular.SocietyCode}/${particular.BillFor} ${particular.BillYear}/bill-${particular.SocietyCode}-${particular.UnitID}-${particular.BillFor} ${particular.BillYear}-0.pdf`;
    //   console.log({ "viewaDetails": s });
    //   // if (this.isValidUrl(s)) {
    //   //   window.open(s, '_blank', 'location=no');
    //   // }
    //   this.isValidUrl("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf").then((isValid) => {
    //     if (isValid) {
    //       window.open(s, '_blank', 'location=no');
    //     }
    //   });
    //   return;
    //   //check pdf url 
    // }
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
  // async checkPDFExistence(pdfUrl: string): Promise<boolean> {
  //   try {
  //     const response = await axios.head(pdfUrl);
  //     // Check if the response status is in the 2xx range, which indicates success
  //     return response.status >= 200 && response.status < 300;
  //   } catch (error) {
  //     // If an error occurs (e.g., 404 Not Found), return false
  //     return false;
  //   }
  //   return false;
  // }
  // async isValidUrl(string): Promise<boolean> {
  //   this.checkPDFExistence(string)
  //     .then(exists => {
  //       if (exists) {
  //         console.log('PDF exists.');
  //       } else {
  //         console.log('PDF does not exist or could not be accessed.');
  //       }
  //     })
  //     .catch(error => {
  //       console.error('Error checking PDF existence:', error);
  //     });
  //   // const isValid = await this.connectServer.checkUrlValidity(string);//.subscribe((isValid) => {
  //   //   console.log({ "isValid": isValid });
  //   //   if (isValid) {
  //   //     console.log('URL is valid');
  //   //   } else {
  //   //     console
  //   //       .log('URL is not valid');
  //   //   }
  //   // }, (onError) => {
  //   //   console.log({ "onError": onError });

  //   // });

  //   return false;


  //   // try {
  //   //   new URL(string);
  //   //   return true;
  //   // } catch (err) {
  //   //   return false;
  //   // }
  // }
  async showLoading() {
    const loading = await this.loadingCtrl.create({
      message: 'Please wait...',
      duration: 5000,
      cssClass: 'loader-css-class'
    });
    loading.present();
  }
}

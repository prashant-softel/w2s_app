import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { NavigationExtras } from '@angular/router';
import { GlobalVars } from 'src/service/globalvars';
import { ConnectServer } from 'src/service/connectserver';
import { LoaderView } from 'src/service/loaderview';
import {HttpClient} from "@angular/common/http"; // Import AlertController

@Component({
  selector: 'app-payment',
  templateUrl: './payment.page.html',
  styleUrls: ['./payment.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class PaymentPage implements OnInit {
  RecordneftPage: any = 'recordneft';
  PaymentgatewayPage: any = 'paymentgateway';
  paytm: boolean = false;
  PaymentLink: any;
  AllowPayment: any;
  EnablePaytm: any;
  Enable_NEFT: any;
  // paymentData: any = {
  //     ownerName: 'John Doe',
  //     unitNo: 'A-101',
  //     totalAmount: 3000,
  //     bills: [
  //       { type: 'Maintenance', amount: 1500, payAmount: 1500, comments: '' },
  //       { type: 'Supplementary', amount: 500, payAmount: 500, comments: '' },
  //       { type: 'Invoice', amount: 1000, payAmount: 1000, comments: '' }
  //     ]
  //   };
  /*options : InAppBrowserOptions = {
      location : 'yes',//Or 'no'
      hidden : 'no', //Or  'yes'
      clearcache : 'yes',
      clearsessioncache : 'yes',
      zoom : 'yes',//Android only ,shows browser zoom controls
      hardwareback : 'yes',
      mediaPlaybackRequiresUserAction : 'no',
      shouldPauseOnSuspend : 'no', //Android only
      closebuttoncaption : 'Close', //iOS only
      disallowoverscroll : 'no', //iOS only
      toolbar : 'yes', //iOS only
      enableViewportScale : 'no', //iOS only
      allowInlineMediaPlayback : 'no',//iOS only
      presentationstyle : 'pagesheet',//iOS only
      fullscreen : 'yes',//Windows only
      );*/
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    public http: HttpClient,
    private params: NavParams) {
    this.PaymentLink = "";
    this.AllowPayment = "";
    this.EnablePaytm = "0";
    this.Enable_NEFT = "0";
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
    this.loaderView.showLoader('Loading ...');
    this.connectServer.getData("Society", null).then(
      resolve => {
        this.loaderView.dismissLoader();
        if (resolve['success'] == 1) {

          var temp = resolve['response']['PaymentGateway']['0'];
          this.Enable_NEFT = temp['Record_NEFT'];
          this.AllowPayment = temp['PaymentGateway'];

          //alert(this.AllowPayment);
          // if (this.AllowPayment == '1' && temp['Paytm_Link'] != '') {
          if (this.AllowPayment == '1') {
            this.paytm = true;
            // this.PaymentLink = temp['Paytm_Link'];
            this.EnablePaytm = "1";
          }
          else {
            this.EnablePaytm = "0";

          }
          //alert(this.EnablePaytm);
          console.log("paytm = " + this.paytm);
        }
      }
    );
  }

  neftPayment() {
    //alert("Comming Soon!");
    this.navCtrl.navigateRoot(this.RecordneftPage);
  }
  getPaymentDues() {
    //alert("Comming Soon!");

    var link = this.globalVars.HOST_NAME + 'api.php';
    var myData = {
      method: "getPaymentDues",
      societyId: this.globalVars.MAP_SOCIETY_ID,
      unitId: this.globalVars.MAP_UNIT_ID,
      role: this.globalVars.MAP_USER_ROLE,
      loginId: this.globalVars.MAP_LOGIN_ID
    };
    this.http.post<any>(link, myData)
      .subscribe(data => {
        console.log("data received:", data); // Log the received json

        try {
          this.navCtrl.navigateRoot(this.PaymentgatewayPage, {
            state: { paymentData: data } // Pass data using NavigationExtras
          });
        } catch (error) {
          console.error("Error processing form:", error);
          // Display an error message to the user if something goes wrong
          // this.presentAlert('Error', 'Could not process the payment form. Please try again later.');

        } finally {  // Ensure the loader is dismissed even if there's an error
          this.loaderView.dismissLoader();
        }


      }, error => {
        // console.error("HTTP Error:", error);  // Log the full error object
        // this.presentAlert('Error', 'Could not connect to the payment gateway. Please check your internet connection.');
        this.loaderView.dismissLoader();
      });

    // this.navCtrl.navigateRoot(this.PaymentgatewayPage, {
    //   state: { paymentData: this.paymentData } // Pass data using NavigationExtras
    // });
  }
}

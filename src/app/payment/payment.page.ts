import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { InAppBrowser, InAppBrowserOptions } from '@ionic-native/in-app-browser/ngx';
@Component({
  selector: 'app-payment',
  templateUrl: './payment.page.html',
  styleUrls: ['./payment.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class PaymentPage implements OnInit {
  RecordneftPage:any='recordneft';
  paytm : boolean = false;
  PaymentLink : any;
  AllowPayment : any;
  EnablePaytm : any;
  Enable_NEFT : any;
	options : InAppBrowserOptions = {
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
  };
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private iab: InAppBrowser) {
    this.PaymentLink="";
    this.AllowPayment ="";
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
                      if(resolve['success'] == 1)
                      {

                      	var temp = resolve['response']['PaymentGateway']['0'];
                        this.Enable_NEFT = temp['Record_NEFT'];
                      	this.AllowPayment= temp['PaymentGateway'];

                        //alert(this.AllowPayment);
                        if(this.AllowPayment=='1' && temp['Paytm_Link'] !='')
                        {
                      		this.paytm = true;
                          this.PaymentLink=temp['Paytm_Link'];
                          this.EnablePaytm = "1";
                      	}
                        else
                        {
                          this.EnablePaytm = "0";
                         
                        }
                        //alert(this.EnablePaytm);
                      	console.log("paytm = "+this.paytm);
                      }
                    }
     );
  }
  public openWithSystemBrowser(){
 
    if(this.paytm == false || this.EnablePaytm =='0')
    {
 
    alert('Please contact Management to enable payment gateway!');
      
    }
    else
    {
      var httpUrl =this.PaymentLink;
      let target = "_system";
      //window.open(httpUrl, '_blank', 'location=no'); 
      const browser = this.iab.create(httpUrl,target,this.options);
       browser.show()
      //var ref = cordova.InAppBrowser.open(url, target, options);
     // this.theInAppBrowser.create(httpUrl,target,this.options);
    }
  
 }
 neftPayment() {
   //alert("Comming Soon!");
   this.navCtrl.navigateRoot(this.RecordneftPage);
  }
}

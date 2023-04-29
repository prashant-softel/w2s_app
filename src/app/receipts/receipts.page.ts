import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
@Component({
  selector: 'app-receipts',
  templateUrl: './receipts.page.html',
  styleUrls: ['./receipts.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ReceiptsPage implements OnInit {
  ViewreceiptPage:any='viewreciept';
  particulars : Array<{period: string, date: string, amount: string, mode: string}>;

  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) { 

      this.particulars = [];
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
    let details:any;
    this.route.queryParams.subscribe(params => {
      details = params["details"];
        
      });
    var receiptData = details['receipt'];
    console.log("Receipt Data : " + receiptData);
      for(var i = 0; i < receiptData.length; i++)
      {
        var objData = {period : receiptData[i]['PeriodID'], date : receiptData[i]['ChequeDate'], amount : receiptData[i]['Amount'], mode : receiptData[i]['mode']};
  
        this.particulars.push(objData);
      }
  }
  viewReceipt(period){
    alert("Comming soon");
    /*let navigationExtras: NavigationExtras = {
      queryParams: 
      {
        period_id :period,
      }
    };
    this.navCtrl.navigateRoot(this.ViewreceiptPage,navigationExtras);*/
  	//this.navCtrl.push(ViewreceiptPage, {period_id : period});
  }
}

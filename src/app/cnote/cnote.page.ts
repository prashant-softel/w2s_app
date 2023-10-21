import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { GlobalVars } from 'src/service/globalvars';
import { ConnectServer } from 'src/service/connectserver';
import { LoaderView } from 'src/service/loaderview';

@Component({
  selector: 'app-cnote',
  templateUrl: './cnote.page.html',
  styleUrls: ['./cnote.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class CnotePage implements OnInit {
  displayData: Array<any>;
  particulars1: Array<any>;
  sgst: any;
  cgst: any;
  inv_number: any;
  inv_id: any;
  subtotal: any;
  balance: any;
  notes: any;
  particulars: any;
  bill_date: any;
  billtype_text: any;
  iBillType: any;
  start_date: any;
  end_date: any;
  iUnitID: any;
  cdnid: any;
  mode: any;
  role: any;
  roleWise: any;
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) {
    this.particulars1 = [];
    this.subtotal = "";
    this.inv_number = "";
    this.inv_id = "";
    this.billtype_text = "";
    this.bill_date = "";
    this.balance = "";
    this.notes = "";
    this.iBillType = "";// params.get("BT");
    this.iUnitID = "";//params.get("Unit");
    this.cdnid = "";//params.get("cdnid");
    this.mode = "";//params.get("mode1");
    this.inv_number = "";//params.get("inv_number");
    this.inv_id = "";//params.get("inv_id");
    this.start_date = "";
    //console.log("iBillType : " + this.iBillType + " unitID : " + this.iUnitID + " cdnid : " + this.cdnid + " mode : " + this.mode + " inv_number : " + this.inv_number + " .inv_id : " + this.inv_id);
    this.end_date = "";
    this.cgst = "";
    this.sgst = "";
    let details: any;
    this.route.queryParams.subscribe(params => {
      this.iBillType = params["BT"];
      this.iUnitID = params["Unit"];
      this.cdnid = params["cdnid"];
      this.mode = params["mode1"];
      this.inv_number = params["inv_number"];
      this.inv_id = params["inv_id"];
    });
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
    var objData = [];
    objData["unit"] = this.iUnitID;
    objData["cdnid"] = this.cdnid;
    objData["inv_number"] = this.inv_number;
    objData["inv_id"] = this.inv_id;

    objData["mode"] = this.mode;
    if (this.mode == 'SInvoice') {
      objData['fetch'] = 3;
    }
    else {
      objData['fetch'] = 2;
    }
    this.connectServer.getData("Receipts", objData).then(
      resolve => {
        this.loaderView.dismissLoader();
        if (resolve['success'] == 1) {
          var cdnote = resolve['response']['cdnote'];
          var cdnotevoucher = resolve['response']['cdnotevoucher'];
          if (this.mode == 'M-CNote') {
            this.billtype_text = "M - Credit Note";
          }
          else if (this.mode == 'M-DNote') {
            this.billtype_text = "M - Debit Note";
          }
          else if (this.mode == "S-DNote") {
            this.billtype_text = "S - Debit Note";
          }
          else if (this.mode == "S-CNote") {
            this.billtype_text = "S - Credit Note";
          }
          else if (this.mode == "SInvoice") {
            this.billtype_text = "Invoice";
          }

          if (this.mode == "SInvoice") {
            this.subtotal = cdnote[0]['InvSubTotal'];
          }
          else {
            this.subtotal = cdnote[0]['Note_Sub_Total'];
          }
          if (this.mode == "SInvoice") {
            this.bill_date = cdnote[0]['Inv_Date'];
          }
          else {
            this.bill_date = cdnote[0]['Date'];
          }
          this.balance = cdnote[0]['TotalPayable'];
          this.notes = cdnote[0]['Note'];
          this.cgst = cdnote[0]['CGST'];
          this.sgst = cdnote[0]['SGST'];
          this.particulars1.push(cdnotevoucher);
        }
        else {
          //console.log(resolve['response']);
        }
      }
    );
  }

}

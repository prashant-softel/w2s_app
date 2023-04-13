import { Component , OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
@Component({
  selector: 'app-viewbill',
  templateUrl: './viewbill.page.html',
  styleUrls: ['./viewbill.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ViewbillPage implements OnInit {
  PaymentPage:any ='payment';
  billtype_text : string;
  billfor : string;
  billdate : string;
  duedate : string;
  subtotal : string;
  adjustment : string;
  interest : string;
  previous_principle : string;
  previous_interest : string;
  previous_arrears : string;
  balance : string;
  notes : string;
  sgst : any;
  cgst : any;
  igst : any;
  cess : any;
  role : string;
  roleWise : string;
  particulars : Array<{ledger: string, amount: string}>;
  period_id : any;
  bill_type : any;
  unitId : any;
  periodId : any;
  data:any = {};
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) {
      this.sgst= 0;
      this.igst= 0;
      this.cess= 0;
      this.cgst= 0;
      this.role="";
      this.roleWise='';
      if(this.globalVars.MAP_USER_ROLE == "Member")
      {
        this.role = "Member";
      }
      else if(this.globalVars.MAP_USER_ROLE == "Admin Member")
      {
        
        this.role = "AdminMember";
      }
     else
      {
        this.role= "Admin";
      }
     
     this.roleWise=this.role;
     }

  ngOnInit() {

    let details:any;
    this.route.queryParams.subscribe(params => {
      details = params["details"];
        
      });
    //  console.log("Display Data",details);
    this.unitId = details['UnitId'];
    this.billfor = details['details'][0]['BillFor'];
    this.billdate = details['details'][0]['BillDate'];
    this.duedate = details['details'][0]['DueDate'];
    this.notes = details['details'][0]['Notes'];
    this.bill_type=details['details'][0]['BillType'];
    this.periodId=details['details'][0]['PeriodID']
    this.billtype_text = (details['details'][0]['BillType'] == 0) ? "Maintenance Bill" : "Supplementary Bill";
    this.particulars = [];
    var particulars2 = details['particulars2'];
    this.subtotal = particulars2['BillSubTotal'];
    this.adjustment = particulars2['AdjustmentCredit'];
    this.interest = particulars2['InterestOnArrears'];
    this.sgst =  particulars2['SGST'];
    this.cgst =  particulars2['CGST'];
    this.igst  = particulars2['IGST'];
    this.cess  = particulars2['CESS'];
    this.previous_principle = particulars2['PreviousPrincipalArrears'];

    this.previous_interest = particulars2['PreviousInterestArrears'];
    this.previous_arrears = particulars2['PreviousArrears'];

    this.balance = particulars2['Payable'];

    var particularData = details['particulars'][0];

    console.log("Particular Data : " + particularData);

    for (var key in particularData) 
    {
      if (particularData.hasOwnProperty(key)) 
      {
        if(particularData[key] != 0)
        {
            var objData = {ledger : key, amount : particularData[key]};
            this.particulars.push(objData);
        }
      }
    }

    document.getElementById("bill_notes").innerHTML =  this.notes; 
  }
  MakePayment()
  {
    this.navCtrl.navigateRoot(this.PaymentPage);
  }
}

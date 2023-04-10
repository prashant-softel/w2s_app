import { Component , OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';

@Component({
  selector: 'app-dues',
  templateUrl: './dues.page.html',
  styleUrls: ['./dues.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class DuesPage implements OnInit {
  
  particulars : Array<{mode : string, date : string, debit : string, credit : string, balance : number, IsOpeningBill : any, period : any, billtype:any,Unit:any,cdnid :any,inv_number : any ,inv_id : any}>;
  role:any;
  roleWise:any;
  showIn :any;
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams) { 
    this.particulars = [];
  	this.role="";
    this.roleWise='';
    this.showIn="0";
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
    var receiptData = this.params.get("details")['BillnReceipts'];
      console.log('RD ' + receiptData);
	  	for(var i = (receiptData.length - 1); i >= 0; i--)
	    {
	    	var sMode = receiptData[i]['Mode'];
	    	var sDate = receiptData[i]['Date'];
        var cdnid1 = 0;
        var inv_number = 0;
        var inv_id = 0;
        if(receiptData[i]['Type'] == 0)
          {
            
	    	    if(receiptData[i]['IsOpeningBill'] == 1)
	    	    {
             //var Isopening =receiptData[i]['IsOpeningBill'];
	    		    sMode = "Opening Balance";
               this.showIn="1";
	    		  //sDate = "No date";
			      }
			      else
			      {
               if(receiptData[i]['BillType'] == 1 || receiptData[i]['BillType'] == true )
				      {
					      sMode = "S-" + sMode;
                 cdnid1 = receiptData[i]['Cd_id'];
                 this.showIn="2";
				      }
				      else
				      {
					      sMode = "M-" + sMode;
                 this.showIn="3";
                 cdnid1 = receiptData[i]['Cd_id'];
				      }
            }
          }
        else
        {
          sMode =receiptData[i]['Mode'];
          inv_number = receiptData[i]['Inv_Number'];
          inv_id =  receiptData[i]['Inv_id']
           this.showIn="4";
        }
       // alert( this.showIn);
	        var objData = {mode : sMode, date : sDate, debit : receiptData[i]['Debit'], credit : receiptData[i]['Credit'], balance : receiptData[i]['Balance'], IsOpeningBill : receiptData[i]['IsOpeningBill'], period : receiptData[i]['PeriodID'],billtype : receiptData[i]['BillType'],Unit : receiptData[i]['UnitID'],showIn : this.showIn,cdnid : cdnid1,inv_number : inv_number , inv_id : inv_id};
	        this.particulars.push(objData);
	    }
	    console.log('obj',objData);
	        
	    var iBalance = 0;
	    for(var i = 0; i < receiptData.length; i++)
	    {
	        iBalance += receiptData[i]['Debit'] - receiptData[i]['Credit'];
	        this.particulars[receiptData.length - 1 - i]['balance'] = iBalance;
	    }
  }

	viewDetails(particular)
	{
		if((particular.mode == "M-Bill"||particular.mode == "S-Bill") && particular.IsOpeningBill == 0)
		{
      	this.loaderView.showLoader('Loading ...');
	    var objData1 = [];
      	objData1['PeriodID'] = particular.period;
      	objData1['BT'] = particular.billtype;
        objData1['fetch'] = 1;
      	if(this.roleWise == "Admin" ||  this.roleWise == "AdminMember")
      	{
      		objData1['UnitId'] = particular.Unit;
      		
      	}
      	else
      	{
      		objData1['UnitId'] =0;
      	}
      	this.connectServer.getData("Bill", objData1).then(
        resolve => { 
                    this.loaderView.dismissLoader();
                    if(resolve['success'] == 1)
                    {
                     // this.navCtrl.push(ViewbillPage, {details : resolve['response']});
                    }
                 }
     		);
    	}
		else if(particular.mode == "M-Receipt"||particular.mode == "S-Receipt")
		{
			//this.navCtrl.push(ViewreceiptPage, {period_id : particular.period,Unit: particular.Unit});
		}
    else if(particular.mode == "M-DNote"||particular.mode == "S-DNote" || particular.mode == "M-CNote" || particular.mode == "S-CNote")
    {
      //this.navCtrl.push(CnotePage, {Unit: particular.Unit,BT: particular.billtype,cdnid : particular.cdnid, mode1 : particular.mode,inv_number : particular.inv_number,inv_id : particular.inv_id});
    }
   else if(particular.mode == "SInvoice")
    {
      //this.navCtrl.push(CnotePage, {Unit: particular.Unit,BT: particular.billtype,cdnid : particular.cdnid, mode1 : particular.mode,inv_number : particular.inv_number,inv_id : particular.inv_id});
    }
     
	}
}

import { Component, HostListener, OnInit,CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
@Component({
  selector: 'app-recordneft',
  templateUrl: './recordneft.page.html',
  styleUrls: ['./recordneft.page.scss'],
  standalone: true,
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ],
  imports: [IonicModule, CommonModule, FormsModule]
})
export class RecordneftPage implements OnInit {
  ReceiptsPage:any='receipts';
  userData: {bank_id: string, payer_bank: any, payer_branch: any, amount: any, trnx_date: any, trnx_id: any, bill_type: any, note: any,bank_acc:any, set:""};
  max_year: any;
  message : any;
  bank_acc : any;
  bank_ifsc : any;
  bank_account_no : any;
  bank_ifsc_code : any;
  bank_list : Array<any>;
  date:any;
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams) { 

    var todayDate = new Date().toISOString();
  	this.userData = {bank_id: "", payer_bank: "", payer_branch: "", amount: "", trnx_date: todayDate, trnx_id: "", bill_type: 0, note: "",bank_acc:0,set: ""};
    this.bank_acc = "";
    this.bank_ifsc = "";
    this.bank_account_no = '';
    this.bank_ifsc_code='';
    this.bank_list = [];
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
    this.fetchBanks();
    this.fetchPayerBanks();
    var d= new Date();
    this.max_year= d.getFullYear()+2;
  }

  fetchBanks() {
    this.loaderView.showLoader('Loading ...');  
    var obj = [];
    obj['fetch'] = "";
    this.connectServer.getData("Bank", obj).then(
      resolve => { 
              this.loaderView.dismissLoader();
              if(resolve['success'] == 1)
              {
                  console.log(resolve);
                  var bankDetails = resolve['response']['banks'];
                  for(var i = 0; i < bankDetails.length; i++)
                  {
                    if(i == 0)
                    {
                      //this.bank_account_no = bankDetails[0]['AcNumber'];
                      //this.bank_ifsc_code = bankDetails[0]['IFSC_Code'];
                    }
                    this.bank_list.push(bankDetails[i]);
                  }
                  if(bankDetails.length==1)
                  {
                    this.userData.bank_id= bankDetails[0]['BankID'];
                    this.bank_account_no = bankDetails[0]['AcNumber'];
                    this.bank_ifsc_code = bankDetails[0]['IFSC_Code'];
                    console.log("Bank:"+ this.userData.bank_id);
                  }
              } 
          }
      );
    }
    onSelectBank(selectedValue: any) {
     // alert("select bank"+selectedValue.detail.value);
      console.log('Selected', selectedValue);
      for(var i = 0; i < this.bank_list.length; i++)
      {
        if(this.bank_list[i]['BankID'] == selectedValue.detail.value)
        {
          this.bank_account_no = this.bank_list[i]['AcNumber'];
          this.bank_ifsc_code=this.bank_list[i]['IFSC_Code'];
          break;
        }
      }
    }
    fetchPayerBanks() {
      var obj = [];
      obj['fetch_payer'] = "";
      this.connectServer.getData("Bank", obj).then(
        resolve => { 
              //this.loaderView.dismissLoader();
              if(resolve['success'] == 1)
              {
                console.log(resolve);
                var bankDetails = resolve['response']['banks'];
                this.userData.payer_bank= bankDetails[0]['Payer_Bank'];
                this.userData.payer_branch= bankDetails[0]['Payer_Cheque_Branch'];
              } 
           }
        );
      }
      submit() {
        this.loaderView.showLoader('Please Wait ...');  
        this.connectServer.getData("Bank", this.userData).then(
          resolve => { 
                this.loaderView.dismissLoader();
                if(resolve['success'] == 1)
                {
                  this.message = resolve['response']['message'];
                  this.bank_list = [];
                  alert(resolve['response']['message']);
                  //this.navCtrl.setRoot(ReceiptsPage);
                  this.loaderView.showLoader('Loading ...');  
    						  this.connectServer.getData("Receipts", null).then(
      						 resolve => 
      						 	{ 
                    	this.loaderView.dismissLoader();
							 	      if(resolve['success'] == 1)
                    	{
                        let navigationExtras: NavigationExtras = {
                        queryParams: 
                        {
                          details :resolve['response'],
                        }
                      };
                      //alert("call3");
                        this.navCtrl.navigateRoot(this.ReceiptsPage,navigationExtras);
                        //this.navCtrl.setRoot(ReceiptsPage, {details : resolve['response']});
                    	} 
                 		}
    						  );
                } 
                else
                {
                    this.message = resolve['response']['message'];
                }
              }
          );
        }
}

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
  selector: 'app-viewreceipt',
  templateUrl: './viewreceipt.page.html',
  styleUrls: ['./viewreceipt.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ViewreceiptPage implements OnInit {
  particulars : any;
  iPeriodID : any;
  start_date : any;
  end_date : any;
  iUnitID :any;
  role:any;
  roleWise:any;
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) { 

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
     this.particulars = [];
     
    this.route.queryParams.subscribe(params => {
      this.iPeriodID = params["period_id"];
      this.iUnitID = params["period_id"];
      });
    //// this.iPeriodID = params.get("period_id");
     //this.iUnitID = params.get("Unit"); 
     this.start_date = "";
     this.end_date = "";
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
    var objData = [];
    objData["PeriodID"] = this.iPeriodID;
    objData['fetch'] = 1;
    if(this.roleWise == "Admin" || this.roleWise== "AdminMember")
    {
      objData["UnitID"] = this.iUnitID;
    }
    else
    {
      objData["UnitID"] = 0;
    }
    this.connectServer.getData("Receipts", objData).then(
        resolve => { 
                    this.loaderView.dismissLoader();
                    if(resolve['success'] == 1)
                    {
                      var Receipts = resolve['response']['receipt'];
                      console.log(Receipts);
                      for(var r = 0;r < Receipts.length; r++)
                      {
                        this.particulars.push(Receipts[r]);
                      }  
                      this.start_date = resolve['response']['start_date'];
                      this.end_date = resolve['response']['end_date'];

                      //console.log(resolve['response']);
                    }
                    else
                    {
                      console.log(resolve['response']);
                    } 
                 }
    );
  }
  IsValidMode(mode, text)
  {
    var sReturn = false;
    if(text == "HIDEFORCASH")
    {
        if(mode == "NEFT" || mode == "CHEQUE")
        {
            sReturn = true;
        }
    }
    else if(mode == text)
    {
        sReturn = true;
    }

    return sReturn;
  }
}

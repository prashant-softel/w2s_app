import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform,} from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-updatefine',
  templateUrl: './updatefine.page.html',
  styleUrls: ['./updatefine.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class UpdatefinePage implements OnInit {
  ViewimposefinePage:any='viewimposefine';
  updateData :{ rev_id : any, period_id : any, Ledger_id:any, unit_id : any, amount: any, desc : any, member_name:any,sendEmail:any};
  details:{};
  message : string;
  UnitNo:any;
  type:any;
  checked :boolean =true;
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) { 
    this.updateData = {rev_id : "",period_id : "",Ledger_id:"", unit_id : "",amount:"", desc : "", member_name:"", sendEmail : "" };
    this.details=[];
    this.UnitNo="";
    this.type="";
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
    console.log('ionViewDidLoad UpdatefinePage');
    let details:any;
    this.route.queryParams.subscribe(params => {
      details = params["details"];
        
      });
      this.displayData(details);
    //this.displayData(this.navParams.get("details"));
    if(this.checked==true)
    {
      this.updateData['sendEmail']= "1";
    }
    else
    {
      this.updateData['sendEmail']= "0";
    }
    
  }
  displayData(details)
  {
    this.details=details;
    this.UnitNo=this.details['unit_no'];
    this.updateData.rev_id=this.details['ID'];
    this.updateData.Ledger_id=this.details['LedgerID'];
    this.updateData.period_id=this.details['PeriodID'];
    this.updateData.unit_id=this.details['UnitID'];
    this.updateData.amount=this.details['amount'];
    this.updateData.desc=this.details['Comments']
    this.updateData.member_name=this.details['owner_name'];
    this.type=this.details['Type'];
    console.log(this.details);
  }
  
  getSelect(isChecked)
  {
    if(isChecked === true)
    {
      this.updateData['sendEmail']= "1";  
    }
    else
    {
      this.updateData['sendEmail']= 0;
    }
  }
  update()
  {
    this.updateData['fetch'] = "updatefine";
    this.connectServer.getData("ImposeFineServlet", this.updateData).then(
      resolve => {  
          console.log('Response : ' + resolve);
          if(resolve['success'] == 1)
          {
            this.message = resolve['response']['message'];
            this.navCtrl.navigateRoot(this.ViewimposefinePage);
          } 
          else
          {
            this.message = resolve['response']['message'];
          }
        }
      );
    }
}

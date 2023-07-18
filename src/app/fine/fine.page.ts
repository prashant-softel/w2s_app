import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform,} from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';

@Component({
  selector: 'app-fine',
  templateUrl: './fine.page.html',
  styleUrls: ['./fine.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class FinePage implements OnInit {

userData :{  period_id : any, Ledger_id:any, unit_id : any, amount: any, desc : any, img : any, periodDate:any, sendEmail:any };
FetchPeriod : Array <any>;
UnitList :Array <any>;
//UnitList : Array<{}>;
message : string;
options:any;
base64Image:any;
lastImage: string = null;
//loading: Loading;
LedgerName : string;
LedgerId : any;
LedgerDetail: Array<any>;
//LedgerDetail: Array<{}>;
fine_id : any;
type : string;
update:any;
setMassege:any;
Block_unit=0;
Block_desc="";
//sendcheck: num=1;
checked :boolean =true;
disabled : any;
ViewimposefinePage:any='viewimposefine';
 
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams) { 

      this.userData = {period_id : "",Ledger_id:"", unit_id : "",amount:"", desc : "", img : "", periodDate:"", sendEmail : ""};
     
      this.FetchPeriod = [];
      this.LedgerDetail=[];
      this.LedgerName="";
      this.UnitList =[];
      this.fine_id = 0;
      this.type="";
      this.update=true;
      this.setMassege ="";
      this.disabled=false;

    }

  ngOnInit() {
    this.fetchPeriodDetails();
    if(this.checked==true)
  	{
  		this.userData['sendEmail']= "1";
  	}
  	else
  	{
  		this.userData['sendEmail']= "0";
  	}
  
    this.Block_unit=this.globalVars.MAP_UNIT_BLOCK;
    this.Block_desc=this.globalVars.MAP_BLOCK_DESC;
    
  }
  
  fetchPeriodDetails()
  {
    this.loaderView.showLoader('Loading ...');
    var objData = [];
    objData['fetch'] ="Fetchperiod";
    this.connectServer.getData("ImposeFineServlet", objData).then(
      resolve => {
        this.fetchLedgerName();
        this.loaderView.dismissLoader();
        if(resolve['success'] == 1)
        {
          console.log(resolve);
          var PeriodList = resolve['response']['Period'];
          if(PeriodList['PeriodID'] == -1)
          {
            this.setMassege= PeriodList['PeriodID'];
            //alert(this.setMassege);
          }
          else
          {
            this.setMassege= 1;
            this.userData.period_id=PeriodList['PrvePeriodID'];
            this.type = PeriodList['Type'];
            this.userData.periodDate = PeriodList['PrevPeriodDate'];
          }
        }
      }
    );
  }

  fetchLedgerName()
  {
	  //this.loaderView.showLoader('Loading ...');
	  var objData = [];
	  objData['fetch'] ="FetchLedger";
	  this.connectServer.getData("ImposeFineServlet", objData).then(
	    resolve => {
	      this.FetchUnitDetails();
        //this.loaderView.dismissLoader();
        if(resolve['success'] == 1)
        {
          console.log(resolve);
          this.LedgerDetail = resolve['response']['Fine'];
          //alert(this.LedgerDetail.length);
          this.userData.Ledger_id= this.LedgerDetail[0]['FineID'];
        }
        else
        {
          //alert("Please select default fine ledger");
					//this.navCtrl.setRoot(ViewimposefinePage);
        }
      }
    );
  }

  FetchUnitDetails()
  {
    //this.loaderView.showLoader('Loading ...');
    var objData = [];
    objData['fetch'] ="FetchUnits";
    this.connectServer.getData("ImposeFineServlet", objData).then(
      resolve => {
        // this.loaderView.dismissLoader();
        if(resolve['success'] == 1)
        {
          console.log(resolve);
          var UnitList = resolve['response']['Units'];
          //this.userData.memberEmail=UnitList['email'];
          //alert(this.userData.memberEmail);
          //  console.log(this.userData.memberEmail);
          for(var i = 0; i < UnitList.length; i++)
          {
            this.UnitList.push(UnitList[i]);
  	  		}
          // this.userData.unit_no=this.UnitList[0]['unit_no'];
          //	 this.userData.owner_name=this.UnitList[0]['owner_name'];
        }
      }
    );
	}


  getSelect(isChecked)
	{
    alert(isChecked);
	  if(isChecked === true)
	  {
		  this.userData['sendEmail']= "1";
	  }
	  else
	  {
		  this.userData['sendEmail']= 0;
	  }
	  alert(this.userData['sendEmail']);
  }

  submit()
  {
    this.disabled = true;
    //alert(this.userData['sendEmail']);
    this.userData['set'] = "addImposeFine";
    this.connectServer.getData("ImposeFineServlet", this.userData).then(
      resolve => {
        //this.loaderView.dismissLoader();
        console.log('Response : ' + resolve);
        if(resolve['success'] == 1)
        {
          this.message = resolve['response']['message'];
          this.fine_id = resolve['response']['new_fine_id'];
          if(this.lastImage === null)
          {
            alert("Fine would be added in next bill.");
            this.navCtrl.navigateRoot(this.ViewimposefinePage);
            //this.navCtrl.setRoot(ViewimposefinePage);
          }
          else
          {
            //this.uploadImage();
          }
        }
        else
        {
          this.message = resolve['response']['message'];
        }
      }
    );
  }

}

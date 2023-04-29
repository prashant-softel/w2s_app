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
  selector: 'app-imposedetail',
  templateUrl: './imposedetail.page.html',
  styleUrls: ['./imposedetail.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ImposedetailPage implements OnInit {
  ViewimposefinePage:any='viewimposefine';
  UpdatefinePage:any='updatefine';
  displayData = {};
  ImposeDetails = {};
  hasLink : any;
  member_name : any;
  impose_date : any;
  unit_no : any;
  amount : any;
  type : any;
  description : any;
  image_Link : any;
  latest:any;
  old_rev_id : any;
  reportedBy : any;
  unit_presantation: any;
  role : string;
  roleWise : string;
  updatedTime: any;
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) { 
    this.role="";
    this.roleWise="";

   /*this.displayData = navParams.get("details");
   console.log(this.displayData);
   if(this.displayData['latest'] == 'previous')
   {
      this.latest=false;
   }
   else{
   this.latest=true;
   }*/
   
    this.hasLink = false;
   this.ImposeDetails = [];
   this.image_Link = "";
   this.member_name = "";
   this.impose_date = "";
   this.unit_no = "";
   this.amount = "";
   this.description =""; 
   this.type = "";  
   this.old_rev_id="";
   this.unit_presantation="";  
   this.reportedBy="";
   this.updatedTime="";
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
      this.displayData = details;//navParams.get("details");
      console.log(this.displayData);
      if(this.displayData['latest'] == 'previous')
      {
         this.latest=false;
      }
      else{
      this.latest=true;
      }
    console.log('ionViewDidLoad ImposedetailPage');
    console.log(this.globalVars);
    if(this.globalVars.MAP_USER_ROLE == "Member") 
    {
      this.role = "Member";
    }
    else
    {
      this.role = "AdminMember";
    }
    this.roleWise=this.role;
    this.fetchImposeHistory();
  }

  fetchImposeHistory() {
    this.loaderView.showLoader('Loading ...');
    var objData = [];
    objData['fetch'] = "imposehistory";
    objData['rev_id'] = this.displayData['ID'];
    this.connectServer.getData("ImposeFineServlet", objData).then(
        resolve => {
          this.loaderView.dismissLoader();
          if(resolve['success'] == 1)
          {
            console.log("SUCCESS")
            var imposeHistoryDetails = resolve['response']['ImposeHistory'][0];
            this.ImposeDetails = imposeHistoryDetails; 
            this.member_name  = this.ImposeDetails['owner_name'] ;  
            this.impose_date = this.ImposeDetails['UpdateTime'] ; 
            this.unit_no = this.ImposeDetails['unit_no'] ;  
            this.amount = this.ImposeDetails['Amount'] ; 
            this.description= this.ImposeDetails['Comments'] ;
            this.type= this.ImposeDetails['Type'] ; 
            this.image_Link= this.ImposeDetails['Link'] ; 
            this.old_rev_id=this.ImposeDetails['ID'];
            this.unit_presantation=this.ImposeDetails['unit_type'];
            this.reportedBy=this.ImposeDetails['ReportedBy']
            if(this.image_Link.length > 0)
            {
              this.hasLink = true;
            }
            document.getElementById("additional_text").innerHTML = this.description;
          }
          else
          {
            console.log("FAIL")
          }
      }
    );     
}

openLink()
{
  window.open(this.image_Link, '_blank', 'location=no'); 
  return false;
}
EditFine()
{
  //alert("Comming Soon");
  console.log(this.displayData);
  let navigationExtras: NavigationExtras = {
    queryParams: 
    {
      details :this.displayData,
    }
  };
  this.navCtrl.navigateRoot(this.UpdatefinePage,navigationExtras);
 //this.navCtrl.push(UpdatefinePage,{details : this.displayData});
}

DeleteFine()
{
  var objData = [];
  objData['fetch'] = "deleteFine";
  objData['rev_id'] = this.displayData['ID'];
  objData['UnitID'] = this.displayData['UnitID'];
  objData['Amount'] = this.displayData['amount'];
  objData['desc'] = this.displayData['Comments'];
  objData['sendEmail'] = "1";
  console.log(objData);
 this.connectServer.getData("ImposeFineServlet", objData).then(
      resolve => { 
        this.loaderView.dismissLoader();
        if(resolve['success'] == 1)
        {
          this.navCtrl.navigateRoot(this.ViewimposefinePage);
        }
        else
        {
          alert("Don't delete fine");
        }
      }
    );
  }
}

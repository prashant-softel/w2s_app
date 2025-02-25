import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform,} from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';

@Component({
  selector: 'app-viewimposefine',
  templateUrl: './viewimposefine.page.html',
  styleUrls: ['./viewimposefine.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ViewimposefinePage implements OnInit {
  ImposedetailPage:any ='imposedetail';
  FinePage:any='fine';
  role : string;
  roleWise : string;
  imposeDetails : any;
  particulars_latest : any;
  particulars_previous : any;
  tab: string = "letest";
  hasLatestFine : any;
  hasPreviousFine : any;
  Showmassage:any;
  temp_latest_fine_array : Array<any>;
  temp_previous_fine_array : Array<any>;
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams) { 
    this.role="";
    this.roleWise="";
    this.imposeDetails=[];
    this.particulars_latest= [];
    this.particulars_previous= [];
    this.Showmassage="";
    this.hasLatestFine = false;
    this.hasPreviousFine = false;
    this.temp_latest_fine_array =[] ;
    this.temp_previous_fine_array =[] ;
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
    if(this.globalVars.MAP_USER_ROLE == "Member")
    {
      this.role = "Member";
      objData['fetch'] = "imposelist";
    }
    else
    {
      this.role = "AdminMember";
      objData['fetch'] = "imposelistall";
    }
    this.connectServer.getData("ImposeFineServlet", objData).then(
      resolve => {
        this.loaderView.dismissLoader();
        if(resolve['success'] == 1)
        {
          var hasFine = false;
          var FineList = resolve['response']['FineList'];
          if(FineList[0]== -1)
          {
            this.Showmassage=FineList[0];
          }
          else
          {
            for(var i = 0; i < FineList.length; i++)
            {
              hasFine = true;
              this.imposeDetails.push(FineList[i]);
              if(FineList[i]['IsPreviousFine'] == 0)
              {
                this.hasLatestFine = true;
                this.particulars_latest.push(FineList[i]);
                this.temp_latest_fine_array= this.particulars_latest;
              }
              else
              {
                this.hasPreviousFine = true;
                this.particulars_previous.push(FineList[i]);
                this.temp_previous_fine_array=this.particulars_previous;
              }
            }
          }
        }
        this.roleWise=this.role;
      }
    );
  }

  getItems1(ev: any)
  {
    this.temp_latest_fine_array= this.particulars_latest;
    let val = ev.target.value;
    if(val && val.trim() != '')
    {
      this.temp_latest_fine_array= this.particulars_latest.filter(
        (p) => {
          let name: any = p;
          if (name.owner_name.toLowerCase().indexOf(val.toLowerCase()) > -1) { return (name.owner_name.toLowerCase().indexOf(val.toLowerCase()) > -1); }
          return null;
          //if(name.owner_name.toLowerCase().indexOf(val.toLowerCase()) > -1)
            //return (name.owner_name.toLowerCase().indexOf(val.toLowerCase()) > -1);
          }
      );
    }
  }
  getItems2(ev: any)
  {
    this.temp_previous_fine_array=this.particulars_previous;
    let val = ev.target.value;
    if(val && val.trim() != '')
    {
      this.temp_previous_fine_array=this.particulars_previous.filter(
        (p) => {
          let name: any = p;
          if (name.owner_name.toLowerCase().indexOf(val.toLowerCase()) > -1) { return (name.owner_name.toLowerCase().indexOf(val.toLowerCase()) > -1); }
          return null;
          /*if(name.owner_name.toLowerCase().indexOf(val.toLowerCase()) > -1)
            return (name.owner_name.toLowerCase().indexOf(val.toLowerCase()) > -1);*/
        }
      );
    }
  }

  addFine()  
  {
    //alert("comming soon!");
    this.navCtrl.navigateRoot(this.FinePage);
    //this.navCtrl.push(FinePage);
  }

selectItems(p)
{
  //alert("comming soon!");
  p['latest'] = this.tab;
  let navigationExtras: NavigationExtras = {
    queryParams: 
    {
      details :p,
    }
  };
  this.navCtrl.navigateRoot(this.ImposedetailPage,navigationExtras);
  //this.navCtrl.push(this.ImposedetailPage,{details : p});
  }
}

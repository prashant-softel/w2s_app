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
  selector: 'app-serviceprovider',
  templateUrl: './serviceprovider.page.html',
  styleUrls: ['./serviceprovider.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ServiceproviderPage implements OnInit {
  ProviderDetailsPage:any='providerdetails';
  AddproviderPage:any='addprovider';
  particulars : any;
  particulars_all : any;
  particulars_my : any;
  particulars_pending : any;
  tab: string = "";
  hasAllProvider : any;
  hasMyProvider : any;
  hasPendingProvider : any;
	providerDetails : any;
	catID : any;
	flag : any;
  displayData = {};
  selected : any;
  my_provider_array_loaded : number;
  all_provider_array_loaded : number;
  pending_provider_array_loaded : number;
  temp_particulars_my_array : Array<any>;
  temp_particulars_all_array : Array<any>;
  temp_particulars_pending_array : Array<any>;
  review : any;
  indexedTeams :any;
  showSearch  :any = 1;
  pending_roviderID : any;
  message : string = "";
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) { 
    this.particulars = [];
    this.particulars_all= [];
    this.particulars_my= [];
    this.particulars_pending= [];
    this.hasAllProvider = false;
    this.hasMyProvider = false;
    this.hasPendingProvider = false;
    this.providerDetails = [];
    this.catID = 0;
    this.flag = 0;
    this.selected="";
    this.my_provider_array_loaded  = 0;
    this.all_provider_array_loaded = 0;
    this.pending_provider_array_loaded = 0;
    this.temp_particulars_my_array= [];
    this.temp_particulars_all_array = [];
    this.temp_particulars_pending_array=[];
    this.pending_roviderID = 0;
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

    this.message = "";
    this.showSearch = 1;
    let details:any;
    this.route.queryParams.subscribe(params => {
      details = params["details"];
        
      });
    this.displayData = details;//this.navParams.get("details");
    if(this.displayData['dash']=="society")
    {
      this.tab = "0";
      this.selected= 1;
      this.fetchProviders(0);
    }
    else
    {
      this.displayData = details;
      //this.displayData =this.navParams.get("details");
      this.tab = this.displayData['tab'];
      this.selected = 2; 
      this.fetchProviders(this.tab);
    }
  }

  fetchProviders(tab){
    alert("call");
    this.message = "";
    this.showSearch = 1;
    if(tab == 0)
    {
      if(!this.my_provider_array_loaded)
      {
        this.loaderView.showLoader('Loading ...');
        var objData = [];
        this.connectServer.getData("ServiceProvider/selectmy", objData).then(
          resolve => {
          this.loaderView.dismissLoader();
          if(resolve['success'] == 1)
          {
            console.log("SUCCESS")
            var providers = resolve['response']['providers'];
            for(var iCnt = 0; iCnt < Object.keys(providers).length; iCnt++)
            {
              this.particulars_my.push(providers[iCnt]);
              this.temp_particulars_my_array = this.particulars_my;
              this.review=providers[iCnt]['Review'][0]['Rating'];
              this.my_provider_array_loaded = 1;
            }
          }
          else
          {
            console.log("FAIL");
            this.showSearch = 0;
            this.message=" No Service Provider To Display."
          }
        });
      }
    }
    else if(tab == 1)
    {
      if(!this.all_provider_array_loaded)
      {
        this.message = "";
        this.showSearch =1;
        this.loaderView.showLoader('Loading ...');
        var objData = [];
        this.connectServer.getData("ServiceProvider/selectall", objData).then(
          resolve => {
            this.loaderView.dismissLoader();
            if(resolve['success'] == 1)
            {
              console.log("SUCCESS : ", resolve);
              var providers = resolve['response']['providers'];
              for(var iCnt = 0; iCnt < Object.keys(providers).length; iCnt++)
              {
                this.particulars_all.push(providers[iCnt]);
                this.temp_particulars_all_array = this.particulars_all;
                this.review=providers[iCnt]['Review'][0]['Rating'];
                this.all_provider_array_loaded=1;
              }              
            }
            else
            {
              console.log("FAIL");
              this.showSearch = 0;
              this.message=" No Service Provider To Display."
            }
          });
        }
      }
      else if(tab == 2)
      {
        this.message = "";
        this.showSearch = 1;
        if(!this.pending_provider_array_loaded)
        {
          this.loaderView.showLoader('Loading ...');
          var objData = [];
          this.connectServer.getData("ServiceProvider/pending", objData).then(
          resolve => {
            this.loaderView.dismissLoader();
            if(resolve['success'] == 1)
            {
              console.log("SUCCESS")
              var providers = resolve['response']['providers'];
              for(var iCnt = 0; iCnt < Object.keys(providers).length; iCnt++)
              {
                this.particulars_pending.push(providers[iCnt]);
                this.temp_particulars_pending_array = this.particulars_pending;
                this.pending_provider_array_loaded=1;
                this.pending_roviderID=this.particulars_pending[iCnt]['service_prd_reg_id'];
              }
            }
            else
            {
              console.log("FAIL");
              this.showSearch = 0;
              this.message=" No Service Provider To Display."
            }
            console.log('Pending : ');
            console.log(this.particulars_pending);
         });
      }
    }
  }
  compare(id)
  {
	  if(id!=this.catID){
		  this.catID = id;
		  this.flag = 0;
	  }
	  else{
	  this.flag = 1;
	  }
  }
  selectItems(p)
  {
    /*p['pending'] = this.tab;
    let navigationExtras: NavigationExtras = {
      queryParams: 
      {
        details :p,
      }
    };
	  this.navCtrl.navigateRoot(this.ProviderDetailsPage,navigationExtras);*/
    
  }
  addServiceProvider()  
  {
    this.navCtrl.navigateRoot(this.AddproviderPage);
  }
  getItems1(ev: any)
  {
    this.temp_particulars_my_array = this.particulars_my;
    let val = ev.target.value;
    if (val && val.trim() != '')
    {
      this.temp_particulars_my_array = this.particulars_my.filter(
      (p) =>{
          let name: any = p;
          if(name.cat.toLowerCase().indexOf(val.toLowerCase()) > -1)
          return (name.cat.toLowerCase().indexOf(val.toLowerCase()) > -1);
        }
      );
    }
  }
  getItems2(ev: any)
  {
    this.temp_particulars_all_array = this.particulars_all;
    let val = ev.target.value;
    if (val && val.trim() != '')
    {
      this.temp_particulars_all_array = this.particulars_all.filter(
      (p) => {
          let name: any = p;
          if(name.cat.toLowerCase().indexOf(val.toLowerCase()) > -1)
            return (name.cat.toLowerCase().indexOf(val.toLowerCase()) > -1);
        }
      );
    }
  }
  getItems3(ev: any)
  {
    this.temp_particulars_pending_array = this.particulars_pending;
    let val = ev.target.value;
    if (val && val.trim() != '')
    {
      this.temp_particulars_pending_array = this.particulars_pending.filter(
      (p) => {
          let name: any = p;
          if(name.cat.toLowerCase().indexOf(val.toLowerCase()) > -1)
            return (name.cat.toLowerCase().indexOf(val.toLowerCase()) > -1);
        }
      );
    }
  }
}

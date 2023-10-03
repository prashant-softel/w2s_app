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
  selector: 'app-tenants',
  templateUrl: './tenants.page.html',
  styleUrls: ['./tenants.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class TenantsPage implements OnInit {
  TenantsdetailsPage:any='tenantsdetails';
	tab : string;
	message : string = "";
	Tenants_active :  Array<any>;
	Tenants_pending :  Array<any>;
	temp_Tenants_active_array : Array<any>;
	temp_Tenants_pending_array : Array<any>;
  Tenants :  Array<any>;
	objData : any;
	selected : any = 0;
	Block_unit : any = 0;
	Block_desc : String = "";
	getParam : {}; 
	constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) { 
    this.tab = "active";
  	this.objData = [];
  	this.Tenants = [];
  	this.temp_Tenants_active_array =[] ;
    this.temp_Tenants_pending_array =[] ;
  	
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
    this.getParam = details;//this.navParams.get("details");
    this.Block_unit=this.globalVars.MAP_UNIT_BLOCK;
    this.Block_desc=this.globalVars.MAP_BLOCK_DESC;
    if(this.getParam['dash']=="admin")
    {
      this.selected = 1; //show pending tab
    }
    this.fetchTenants();
    
  }
  fetchTenants(){
    this.loaderView.showLoader('Loading');
    this.objData['flag'] = 1;
    this.connectServer.getData("TenantServlet", this.objData).then(
        resolve => { 
          this.loaderView.dismissLoader();
          if(resolve['success'] == 1)
          {
            this.message = "";
            this.Tenants = resolve['response']['tenants'] ;	
            this.Tenants_active = [];
            this.Tenants_pending = [];
            for (var i = 0; i < this.Tenants.length; i++)
            {
              if(this.Tenants[i]['active'] == 1){
                this.Tenants_active.push(this.Tenants[i]);
                this.temp_Tenants_active_array= this.Tenants_active;
              }
              else if(this.Tenants[i]['active'] == 0 || this.Tenants[i]['active'] == 2){
                this.Tenants_pending.push(this.Tenants[i]);
                this.temp_Tenants_pending_array= this.Tenants_pending;
              }
            }
          }
          else
          {
            this.message = resolve['message'];
          }
        }
      );  	
  }
  selectActiveTenants(p)
	{
		p['admin'] = 0;
    let navigationExtras: NavigationExtras = {
      queryParams: 
      {
        details :p,
      }
    };
    this.navCtrl.navigateRoot(this.TenantsdetailsPage,navigationExtras);
		//this.navCtrl.push(TenantsdetailsPage,{details : p});
	}
  selectPendingTenants(p)
	{
		p['admin'] = 1;
    let navigationExtras: NavigationExtras = {
      queryParams: 
      {
        details :p,
      }
    };
    this.navCtrl.navigateRoot(this.TenantsdetailsPage,navigationExtras);
		//this.navCtrl.push(TenantsdetailsPage,{details : p});
	}
  getItems1(ev: any)
  {
    this.temp_Tenants_active_array= this.Tenants_active;
    let val = ev.target.value;
    if (val && val.trim() != '')
    {
      this.temp_Tenants_active_array= this.Tenants_active.filter(
      (p) => {
        let name: any = p;
        if (name.unit_no.toLowerCase().indexOf(val.toLowerCase()) > -1) { return (name.unit_no.toLowerCase().indexOf(val.toLowerCase()) > -1); }
        return null;
        /*if(name.unit_no.toLowerCase().indexOf(val.toLowerCase()) > -1)
            return (name.unit_no.toLowerCase().indexOf(val.toLowerCase()) > -1);*/
        }
      );
    }
  }
  getItems2(ev: any)
  {
    this.temp_Tenants_pending_array= this.Tenants_pending;
    let val = ev.target.value;
    if (val && val.trim() != '')
    {
      this.temp_Tenants_pending_array=this.Tenants_pending.filter(
        (p) => {
          let name: any = p;
          if (name.unit_no.toLowerCase().indexOf(val.toLowerCase()) > -1) { return (name.unit_no.toLowerCase().indexOf(val.toLowerCase()) > -1); }
        return null;
         /* if(name.unit_no.toLowerCase().indexOf(val.toLowerCase()) > -1)
            return (name.unit_no.toLowerCase().indexOf(val.toLowerCase()) > -1);*/
          }
        );
      }
  }
}

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-servicerequest',
  templateUrl: './servicerequest.page.html',
  styleUrls: ['./servicerequest.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ServicerequestPage implements OnInit {
  ViewServiceRequestPage:any='viewservicerequest';
  AddservicerequestPage:any='addservicerequest';
  particulars : any;
	particulars_active : any;
	particulars_expired : any;
	particulars_assign : any;
	tab: string = "active";
	hasActiveSRL : any;
	hasExpiredSRL : any;
	hasAssign : any;
	role : string;
	roleWise : string;
	displayData = {};
	dashCall:any;
	logRole : any;
	action: any;
	admin :any;
	dashboard:any;
  MyUnit:any;
  NewRole:any;
  data:any;
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute,
    public http: HttpClient) {
    this.particulars = [];
    this.data=[];
    this.particulars_active= [];
    this.particulars_expired= [];
    this.particulars_assign= [];
    this.hasActiveSRL = false;
    this.hasExpiredSRL = false;
    this.hasAssign = false;
    this.role="";
    this.roleWise='';
    this.action=0;
    this.MyUnit=0;
    this.NewRole="";
   }

  ngOnInit() {
    this.fetchData('list');
	  this.fetchData('assigned');
    this.MyUnit=this.globalVars.MAP_UNIT_ID;
 		this.NewRole = this.globalVars.MAP_USER_ROLE; // == "Contractor"
 		//this.getCategoryId()

  }
  getCategoryId()
	{
	 	var data = [];
    var link = this.globalVars.HOST_NAME+'api.php';
    var myData = JSON.stringify({method:"getRequestId",societyId:this.globalVars.MAP_SOCIETY_ID,unitId:this.globalVars.MAP_UNIT_ID,role:this.globalVars.MAP_USER_ROLE,loginId:this.globalVars.MAP_LOGIN_ID});
    this.http.post(link, myData)
      .subscribe(data => 
      {
          this.data.response = data["_body"]; 
          var parsedData = JSON.parse(this.data.response);
          console.log(parsedData);
          var details = parsedData['response']['details'];
          console.log(details);
          this.globalVars.ADDRESS_PROOF_REQUEST_ID = details['AddressProofId'];
          this.globalVars.TENANT_REQUEST_ID = details['TenantRequestId'];
          this.globalVars.RENOVATION_REQUEST_ID = details['RenovationRequestId'];
          
      }, error => {
      console.log("Oooops!");
    });  
	}

  fetchData(type)
	{
    let details:any;
    this.route.queryParams.subscribe(params => {
      details = params["details"];
        
      });
	  this.displayData =details;//this.navParams.get("details");
	  console.log(this.displayData);
	  var objData = [];
    if(type == 'list') {
	      if(this.globalVars.MAP_USER_ROLE == "Member" || this.displayData['dash']=="society")
	      {
	    		this.role = "Member";
	   			objData['fetch'] = "srlist";
      	}
	      else if(this.globalVars.MAP_USER_ROLE == "AdminMember" || this.displayData['dash']=="admin")
	      {
	      	this.role = "AdminMember";
	      	objData['fetch'] = "srlistall";
	      }
	      else if(this.globalVars.MAP_USER_ROLE == "Contractor" || this.displayData['dash']=="contractor")
	      {
	      	this.role = "Contractor";
	      	objData['fetch'] = "srlistcontractor";
	      }
	    }
      else if(type == 'assigned')
      {
        objData['fetch'] = "assignme";
      }
      this.connectServer.getData("ServiceRequest", objData).then(
	        resolve => {
	            if(resolve['success'] == 1)
		          {
                var hasSR = false;
                var SRList = resolve['response']['sr'];
		            for(var n = 0;n < SRList.length; n++)
		            {
		              hasSR = true;
		              this.particulars.push(SRList[n]);
		              if(type == 'list') {
	 		              if(SRList[n]['status']== 'Closed' || SRList[n]['status']== 'Resolved')
			              {
			                this.hasExpiredSRL = true;
			                this.particulars_expired.push(SRList[n]);
			              }
			              else if(SRList[n]['status']!= 'Closed' || SRList[n]['status']!= 'Resolved')
			              {
			                this.hasActiveSRL = true;
			                this.particulars_active.push(SRList[n]);
			              }
		              }
		              else if(type == 'assigned')
		              {
		                this.hasAssign = true;
                    if(this.MyUnit==SRList[n]['AssignID'])
                    {
		                  this.particulars_assign.push(SRList[n]);
                    }
		              }
                }
                if(!hasSR)
		            {
		              //document.getElementById('msg').innerHTML = 'No Service Requests To Display';
		            }
		            console.log(resolve['response']);
              }
		          else
		          {
		            //document.getElementById('msg').innerHTML = 'No Service Requests To Display';
		            console.log(resolve['response']);
		          }
		          this.roleWise=this.role;
		          //alert(this.roleWise);
		        }
	      );
	}
  viewSR(p) 
  {
    let navigationExtras: NavigationExtras = {
      queryParams: 
      {
        details :p,
      }
    };
    this.navCtrl.navigateRoot(this.ViewServiceRequestPage,navigationExtras);
    //this.navCtrl.push(ViewServiceRequestPage, {details : p});
  }
  addSR()  
  {
    this.navCtrl.navigateRoot(this.AddservicerequestPage);
  }
}

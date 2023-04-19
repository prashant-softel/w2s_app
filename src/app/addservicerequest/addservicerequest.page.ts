import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-addservicerequest',
  templateUrl: './addservicerequest.page.html',
  styleUrls: ['./addservicerequest.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class AddservicerequestPage implements OnInit {
  ServiceRequestPage:any='servicerequest';
  userData : {title : any, details : any, priority : any, category : any, sr_id :any;unit_id:any};
  cat_list : Array <any>;
  message : string;
  options : any;
  base64Image :any;
  lastImage : string = null;
  loading : any ;//Loading;
  servicerequest_id : any;
  Block_unit=0;
  Block_desc="";
  Unit_list : Array <any>;
  rolewise:any;
  role:any;
  renovationRequestId = 0;
  tenantRequestId = 0;
  AddressProofId = 0;
  
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute,
 
  ) {
    this.userData = {title : "", details : "", priority : "1-Low", category : 1, sr_id :"",unit_id: 0};
    this.cat_list = [];
    this.Unit_list = [];
    this.fetchCategories();
    this.message = "";
    this.servicerequest_id = 0;
    this.role= "";
    this.rolewise ="";
    this.AddressProofId = this.globalVars.ADDRESS_PROOF_REQUEST_ID;
    this.renovationRequestId = this.globalVars.RENOVATION_REQUEST_ID;
    this.tenantRequestId = this.globalVars.TENANT_REQUEST_ID;

   }

  ngOnInit() {
    if(this.globalVars.MAP_UNIT_BLOCK == undefined)
    {
      this.Block_unit = 0
    }
    else
    {
      this.Block_unit=this.globalVars.MAP_UNIT_BLOCK;
      this.Block_desc=this.globalVars.MAP_BLOCK_DESC;
    }
    if(this.globalVars.MAP_USER_ROLE == "Admin" || this.globalVars.MAP_USER_ROLE == "Super Admin")
    {
      this.role ="Admin";
    }
    else
    {
      this.role="Member";
    }
    this.rolewise= this.role;
  }

  fetchCategories() {
    this.loaderView.showLoader('Loading ...');
    var obj = [];
    obj['fetch'] = "srcategory";
    this.connectServer.getData("ServiceRequest", obj).then(
        resolve => {
            this.loaderView.dismissLoader();
            if(resolve['success'] == 1)
            {
              console.log(resolve);
              var categoryList = resolve['response']['category'];
              for(var i = 0; i < categoryList.length; i++)
              {
                this.cat_list.push(categoryList[i]);
              }
            }
          }
      );
      this.fetchUnitList();
  }
  fetchUnitList()
  {
    var objData = [];
    this.connectServer.getData("ServiceProvider/fetchUnits", objData).then(
        resolve => {
            if(resolve['success'] == 1)
            {
              console.log(resolve);
              var UnitList = resolve['response']['member']['0'];
              for(var i = 0; i < UnitList.length; i++)
              {
                this.Unit_list.push(UnitList[i]);
              }
            }
          }
        );
    }

    create() 
    {
     
      if(this.rolewise == "Member")
      {
        this.userData.unit_id = this.globalVars.MAP_UNIT_ID;
      }
      if(this.userData.unit_id == 0 && this.rolewise == "Admin" )
      {
        alert("Please select unit no.");
      }
      else
      {
        this.loaderView.showLoader('Please Wait ...');
        this.userData['set'] = "sr";
        console.log("this.userData.unit_id : "+this.userData.unit_id);
        if(this.userData.category == this.globalVars.RENOVATION_REQUEST_ID || this.userData.category == this.globalVars.ADDRESS_PROOF_REQUEST_ID || this.userData.category == this.globalVars.TENANT_REQUEST_ID)
        {
          var p=[];
          p['title'] = this.userData.title;
          p['category'] = this.userData.category;
          p['priority'] = this.userData.priority;
          p['loginId'] = this.globalVars.MAP_LOGIN_ID;
          p['unitId'] = this.userData.unit_id;
          console.log("details : ",p);
          if(this.userData.category == this.globalVars.RENOVATION_REQUEST_ID)
          {
            //this.navCtrl.setRoot(AddRenovationRequest,{details : p});
          }
          else if(this.userData.category == this.globalVars.ADDRESS_PROOF_REQUEST_ID)   
          {
            //this.navCtrl.setRoot(AddAddressProof,{details : p});
          }
          else
          {
            //this.navCtrl.setRoot(AddtenantPage,{details : p});
            this.loaderView.dismissLoader();
          }     
        }//End
        else
        {  
          this.connectServer.getData("ServiceRequest", this.userData).then(
          resolve => {
              this.loaderView.dismissLoader();
              console.log('Response : ' + resolve);
              if(resolve['success'] == 1)
              {
                this.message = resolve['response']['message'];
                this.servicerequest_id = resolve['response'] ['new_sr_id'];
                if(this.lastImage === null)
                {
                  var p=[];
                  if(this.globalVars.MAP_USER_ROLE == "Member")
                  {
                    p['dash']="society";
                    console.log(p);
                  }
                  else
                  {
                    p['dash']="admin";
                  }
                  let navigationExtras: NavigationExtras = {
                    queryParams: 
                    {
                      details :p,
                    }
                  };
                  this.navCtrl.navigateRoot(this.ServiceRequestPage,navigationExtras);
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
  }
  public selectItems()
  {
    this.loaderView.showLoader('Loading ...');
  }
 
}

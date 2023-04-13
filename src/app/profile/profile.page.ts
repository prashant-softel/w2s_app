import { Component, OnInit,CUSTOM_ELEMENTS_SCHEMA, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import * as moment from 'moment'; 

@Component({
  selector: 'app-profile',
  templateUrl: './profile.page.html',
  styleUrls: ['./profile.page.scss'],
  standalone: true,
  schemas: [ CUSTOM_ELEMENTS_SCHEMA ],
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ProfilePage implements OnInit {
  tab: string = "home";
  displayData = {};
  member_id : any;
  unit_id :any;
	unit_details_array : Array<any>;
	
  contact_details_array : Array<any>;
	
  member_details_owner_array : Array<any>;
  member_details_coowner_array : Array<any>;
  member_details_other_array : Array<any>;

	vehicle_details_car_array : Array<any>;
  vehicle_details_bike_array : Array<any>;
  Lien_details_array : Array<any>;
  /* tenant detals */
  Tenant_details_array : Array<any>;
  NameOfLease : any;
  LeaseStartDate :any;
  LeaseEndDate: any;
  AgentName :any;
  AgentContact :any;
  Members :any;
  detailsTenant :any;
  role : string;
  roleWise : string;
  duesAmount:any;
  Address:any;
  ShowAddressField:any;
  userData:{OwnerAddress:any};
  memberUnitID:any;
  
  renew: any;
  currentDate:any;
  renewDate :any;
  renewSociety:any;
  buttonText:any;
  enablebutton:any;
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams) 
    { 
    this.userData={OwnerAddress:""};
    this.unit_details_array = [];
    this.contact_details_array = [];
    this.member_details_owner_array = [];
    this.member_details_coowner_array = [];
    this.member_details_other_array = [];
    
    this.vehicle_details_car_array = [];
    this.vehicle_details_bike_array = [];
    this.Lien_details_array = [];
    /* tenant detals */
    this.Tenant_details_array=[];
    this.NameOfLease ="";
    this.LeaseStartDate ="";
    this.LeaseEndDate ="";
    this.AgentName = "";
    this.AgentContact = "";
    this.Members = "";
    this.detailsTenant ="";
    this.duesAmount=this.globalVars.MEMBER_DUES_AMOUNT;
    this.role="";
    this.roleWise='';
    this.Address='';
    this.ShowAddressField = '0';
    this.memberUnitID="";
    
    var todayDate = new Date().toISOString();
    this.currentDate=moment(todayDate).format('DD-MM-YYYY');
    //this.renewDate='31-12-2019';
    this.renew = "0";
    this.renewSociety="";
    this.buttonText="";
    this.enablebutton="0";
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
   //alert(this.globalVars.MAP_SOCIETY_ID);
   this.roleWise=this.role;
  // alert(this.roleWise);
  // 288
 /* if(this.globalVars.MAP_SOCIETY_ID == 59)
     {
       if(this.currentDate <= this.renewDate)
         {
            this.renew = "1";
         }
         else
         {
            this.renew = "2";
         }
     }*/
  // alert(this.renew);
  ///this.route.queryParams.subscribe(params => {
    //let userName = params["userName"];
    //alert(userName);

 // });
  var displayData = this.params.get("details");
   // this.displayData = navParams.get("details");
  
    //console.log("navParams : ",this.displayData);
   
    if(this.displayData['tab'] != null)   // my code
    {
      switch(this.displayData['tab'])
      {
        case 1:
          this.tab = "home";
          break;
        case 2:
          this.tab = "person";
          break;
        case 3:
          this.tab = "car";
          break;
          
      }
     
    }
    //this.fetchData();
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

    var obj = [];
    obj['uID'] = 26;//this.displayData['uID'];
    obj['fetch'] = "profileData";
    this.loaderView.showLoader('Loading ...'); 
    //this.VehicleRenewFlag();
    this.connectServer.getData("Profile", obj).then(
        resolve => { 
                      this.loaderView.dismissLoader();
                      if(resolve['success'] == 1)
                      {
                          var profileDetails = resolve['response']['memberdetails'];
                          var unitDetails = profileDetails['unit'][0];
                          this.memberUnitID=unitDetails['unit'];
                          for (var key in unitDetails) 
                          {
                            this.unit_details_array[key] = unitDetails[key];
                          }
                          this.member_id = this.unit_details_array['member_id'];
                          var memberDetails = profileDetails['members'];
                          for(var iMemCnt = 0; iMemCnt < memberDetails.length; iMemCnt++)
                          {
                            if(memberDetails[iMemCnt]['coowner'] == 1)
                            { 
                              this.Address = memberDetails[0]['other_address'];
                              this.member_details_owner_array.push(memberDetails[iMemCnt]);
                            }
                            else if(memberDetails[iMemCnt]['coowner'] == 2)
                            {
                              this.member_details_coowner_array.push(memberDetails[iMemCnt]);
                            }
                            else
                            {
                              this.member_details_other_array.push(memberDetails[iMemCnt]);
                            }
                          }

                          var carDetails = profileDetails['car'];

                          for(var iCarCnt = 0; iCarCnt < carDetails.length; iCarCnt++)
                          {
                            carDetails[iCarCnt]['type'] = 4;
                            this.vehicle_details_car_array.push(carDetails[iCarCnt]);
                            
                          }
                          var bikeDetails = profileDetails['bike'];

                          for(var iBikeCnt = 0; iBikeCnt < bikeDetails.length; iBikeCnt++)
                          {
                            bikeDetails[iBikeCnt]['type'] = 2;
                            this.vehicle_details_bike_array.push(bikeDetails[iBikeCnt]);
                          }
                          var LeanDetails =profileDetails['lien'];
                          for(var iLienCnt = 0; iLienCnt < LeanDetails.length; iLienCnt++)
                          {
                            this.Lien_details_array.push(LeanDetails[iLienCnt]);
                          }
                          var TenantDetails = profileDetails['Tenant'];
                          this.detailsTenant = TenantDetails; 
                          if(this.detailsTenant != '' )
                          {
                            this.NameOfLease = TenantDetails[0]['tenant_name'];
                            this.LeaseStartDate = TenantDetails[0]['start_date'];
                            this.LeaseEndDate = TenantDetails[0]['end_date'];
                            this.AgentName = TenantDetails[0]['agent_name'];
                            this.AgentContact = TenantDetails[0]['agent_no'];
                            this.Members = TenantDetails[0]['members'];
                            var TenantOccDetails = TenantDetails[0]['OccupyDetails'];
                            for(var iTenantCnt = 0; iTenantCnt <  Object.keys(TenantOccDetails).length; iTenantCnt++)
                            {
                              this.Tenant_details_array.push(TenantOccDetails[iTenantCnt]);
                            }
                            console.log("Occupy :",this.Tenant_details_array);
                          }
                          else
                          {

                          }
                      } 
                   }
      );
  }
  UpdateAdd()
  {
    this.ShowAddressField = '1';
  }
  UpdateAddress()
  {
    var obj = [];
    obj['memberID'] = this.member_id;
    obj['ownerAdd']=this.userData.OwnerAddress;
   // alert(obj['memberID']);
    //alert(obj['ownerAdd']);
    obj['fetch'] = "UpdateAddress";
    this.loaderView.showLoader('Loading ...'); 
    this.connectServer.getData("Profile", obj).then(
    resolve => { 
                  this.loaderView.dismissLoader();
                  if(resolve['success'] == 1)
                  {
                     // var profileDetails = resolve['response']['memberdetails'];
                     this.ShowAddressField = '0';
                      //this.fetchData();
                     //this.fetchData();
                  }
                  else
                  {
  
                  }
                }
           );
  }
  ViewLedger()
  {
  this.loaderView.showLoader('Loading ...');  
  var objData = [];
   objData['fetch'] = 1;
  if(this.roleWise == this.roleWise)
  {
    objData['UnitID'] = this.displayData['uID'];
  }
  else
  {
    objData['UnitID'] = 0;
  }
  this.connectServer.getData("MemberLedger", objData).then(
    resolve => { 
                  this.loaderView.dismissLoader();
                  if(resolve['success'] == 1)
                  {
                    
                      //this.navCtrl.push(DuesPage, {details : resolve['response']});
                  }
               }
  );
  }
  SendActivation(MemOtherID)
  {
   
   var objData = [];
   objData['UnitID'] = this.displayData['uID'];
   objData['Member_OtherID'] = MemOtherID;
   objData['fetch'] = 11;
       this.connectServer.getData("Directory", objData).then(
          resolve => { 
                   if(resolve['success'] == 1)
                      {
                        alert("Activation code successfully send !");
                      }
                      else
                      {
                        alert("Activation code not send");
                      }
                    }
               );
}
showMemberDetails(p) {
  if(this.globalVars.PROFILE_EDIT_MEMBER == 1)
   {
    p['unitID']=this.displayData['uID'];
  //this.navCtrl.push(MemberPage, {show : 1, data : p});
   }
  else 
 {
     p['unitID']=this.displayData['uID'];
   //this.navCtrl.push(MemberPage, {show : 1, data : p});
  }
 }
 renewVehcle(p)
 { 
   p['VehiclType'] ='car';
  // this.navCtrl.push(RenewvehiclePage, {details : p});
 }
 renewVehcle1(p)
 { 
   p['VehiclType'] ='bike';
   //this.navCtrl.push(RenewvehiclePage, {details : p});
 }
 renewVehcle2(p)
 { 
    alert("The last date for submission is over. Please contact society manager/MC");
 }
 addTenant()
{
   var p = [];
   p['unit_id']=this.memberUnitID;
   console.log(p);
   //this.navCtrl.push(AddtenantPage, {details :p});
}
addMember() {
alert("Comming Soon !");
  //this.navCtrl.push(MemberPage, {add : 1, member_id : this.member_id, unitID : this.displayData['uID']});
}
addVehicle() {
  //p
  alert("Comming Soon !");
  //this.navCtrl.push(VehiclePage, {add : 1, member_id : this.member_id, unitID : this.displayData['uID']});
}
} // End Tags

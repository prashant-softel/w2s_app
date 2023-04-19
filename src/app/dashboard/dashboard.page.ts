import { Component, OnInit,CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';


enum statusEnum  { "Raised" = 1,  "Waiting",  "In progress",  "Completed", "Cancelled"}
enum priorityEnum  { "Critical" = 1,  "High",  "Medium",  "Low"}
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.page.html',
  styleUrls: ['./dashboard.page.scss'],
  standalone: true,
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ],
  imports: [IonicModule, CommonModule, FormsModule]
})
export class DashboardPage implements OnInit {
  SocietyPage: any = 'society';
  DuesPage :any ='dues';
  FeaturesPage: any ='features';
  EventsPage :any='events';
  NoticesPage:any='notices';
  tab: string = "society";
  ServiceRequestPage:any='servicerequest';
  PaymentPage:any='payment';
  PhotoAlbumPage:any='photoalbum';
  ProfilePage:any='profile';
  DirectoryPage :any='directory';
  ClassifiedsPage:any='classifieds';
  PollPage :any='poll';
  ServiceproviderPage:any='serviceprovider';
  data : any;
  //selection:any;
  bill_amount : any;
  bill_billdate : any;
  bill_duedate : any;
  receipt_amount : any;
  receipt_date : any;
  due_amount : any;
  event_title : any;
  event_date : any;
  end_date : any;
  event_time : any;
  notice_title : any;
  notice_post : any;
  notice_expiry : any;
  event_charges: any;
 // event_detail : any;
  event_details_array : Array<any>;
  notice_details_array : Array<any>;
  SRequest_details_array :Array<any>;
  Classified_details_array : Array<any>;
  Poll_details_array : Array<any>;
  task_by_me_array :Array <any>;
  Feature_member_array :Array <any>;
  Feature_Admin_array :Array <any>;
  Feature_All_array :Array <any>;
  Services_array :Array <any>;
  role : string;
  roleWise : string;
  status : any; 
  priority : any;
  img_link :any;
  VisitorEntryId:any;
  SocietySubscription:any;
  SocietySubscriptionNote:any;
  SocietySubscriptionDate:any;
  currentDate:any;

  AccessUI:any;
  SocietySubscriptionDate1:any;
  blockAcount :any;
  flash: boolean;
  router: any;
  society:any;

  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams
  ) {
    this.bill_amount = "0.00";
      this.bill_billdate = "00-00-0000";
      this.bill_duedate = "00-00-0000";
      this.receipt_amount = "0.00";
      this.receipt_date = "00-00-0000";
      this.due_amount = "0.00";
      this.event_title = "No Upcoming Event";
      this.event_date = "0";
      this.end_date = "0";
      this.event_time = "0";
      this.notice_title = "No Active Notice";
      this.notice_post = "0";
      this.notice_expiry = "0";
      this.event_details_array = [];
      this.notice_details_array = [];
      this.SRequest_details_array =[];
      this.Classified_details_array=[];
      this.Poll_details_array = [];
      this.task_by_me_array = [];
      this.Feature_member_array=[];
      this.Feature_Admin_array=[];
      this.Feature_All_array=[];
      this.Services_array=[];
      //this.event_details = params.get("details");
      this.event_charges = 0;
      this.role="";
      this.roleWise="";
      this.status = statusEnum;
      this.priority = priorityEnum;
      this.img_link="https://way2society.com/images/"
      this.VisitorEntryId="";
      this.SocietySubscription="";
      this.SocietySubscriptionNote="";
      this.SocietySubscriptionDate="";
      this.SocietySubscriptionDate1 = "";
      var todayDate = new Date().toISOString();
      //this.currentDate=moment(todayDate).format('DD-MM-YYYY');
     // alert( this.currentDate);
     this.society = "";
      this.AccessUI="0";
      this.blockAcount = "0";


  }

  ngOnInit() {
    this.loaderView.showLoader('Loading ...');
    this.event_details_array = [];
    this.notice_details_array = [];
    this.SRequest_details_array =[];
    this.Classified_details_array=[];
    this.Poll_details_array = [];
    this.task_by_me_array = [];
    this.Feature_member_array=[];
    this.Feature_Admin_array=[];
    this.Feature_All_array=[];
    this.Services_array=[];
    this.society = this.globalVars.MAP_SOCIETY_NAME;

   if(this.globalVars.MAP_USER_ROLE == "Admin Member"){
      this.role = "AdminMember";
    }
    else if(this.globalVars.MAP_USER_ROLE == "Super Admin" )
    {
      this.role = "SuperAdmin";
    }
    
    else if(this.globalVars.MAP_USER_ROLE == "Contractor")
    {
      this.role = "Contractor";
    }
    else
    {
      this.role = this.globalVars.MAP_USER_ROLE;
    }


    //this.role = this.globalVars.MAP_USER_ROLE;
    this.roleWise=this.role;
    console.log(this.role);
    this.connectServer.getData("Dashboard", null).then(
      resolve => { 
                    this.loaderView.dismissLoader();
                    
                    if(resolve['success'] == 1)
                    {
                     

                        if(this.role == "Admin" || this.role == "SuperAdmin" || this.role == "Manager")
                        {

                          var Advertise =resolve['response']['Advertisements'];
                           for(var iCount =0 ; iCount < Object.keys(Advertise).length; iCount++)
                           {   
                            this.Feature_All_array.push(Advertise[iCount]);
                            }
                            
                         /* ------------------- Tasks  (Admin section) ------------------ */
                            if(resolve['response']['taskByMe'] != "")
                            {
                              var TaskByMe = resolve['response']['taskByMe'];
                              //var length =4;
                              for(var i=0;i <TaskByMe.length;i++)
                              {
                                this.task_by_me_array.push(TaskByMe[i]);
                              }
                            }

                         /* ------------------- Event  (Admin section) ------------------ */
                            if(resolve['response']['events'] != "")
                            {
                              var upEvents = resolve['response']['events'];
                              for(var i=0;i<upEvents.length;i++)
                              {
                                this.event_details_array.push(upEvents[i]);
                              }
                            }
                          /* ------------------- Notice (Admin section)------------------ */
                            if(resolve['response']['notice'] != "")
                            {
                              var upNotice = resolve['response']['notice'];
                              for(var iCn=0;iCn<upNotice.length;iCn++)
                              {
                                this.notice_details_array.push(upNotice[iCn])
                              }
                            }
                          /* -------------------  Service Requst (Admin section) ------------------ */
                            if(resolve['response']['Srequest'] != "")
                            {
                              var upRequest = resolve['response']['Srequest'];
                              var count =5;
                           /*upRequest.length*/
                              for(var iReq=0;iReq<5;iReq++)
                              {
                                this.SRequest_details_array.push(upRequest[iReq]);
                               }
                            }

                        /* -------------------  Classified (Admin section)------------------ */

                           if(resolve['response']['classified'] != "")
                            {
                              var upClassified = resolve['response']['classified'];
                              for(var iClass=0; iClass<  Object.keys(upClassified).length; iClass++)
                              {
                                this.Classified_details_array.push(upClassified[iClass]);
                         
                              }
                            }
                        /* -------------------  Poll (Admin section)------------------ */
                            if(resolve['response']['poll'] != "")
                            {
                              var upPoll = resolve['response']['poll'];
                              for(var iPoll= 0; iPoll <  Object.keys(upClassified).length; iPoll++)
                              {
                                this.Poll_details_array.push(upPoll[iPoll]);
                         
                              }
                            }

                        }
                       /* ------------------- Contractor Sections------------------ */
                        else if(this.role == "Contractor")
                        {
                           //if(resolve['response']['Srequest'] != "")
                         // {
                       
                            var upRequest = resolve['response']['Srequest'];
                            console.log(upRequest);
                            for(var iReq = 0; iReq < Object.keys(upRequest).length; iReq++)
                            {
                              this.SRequest_details_array.push(upRequest[iReq]);
                         
                            }
                        //}
                         }

                       /* ------------------- Member Sections------------------ */
                      else
                      {
                          var Advertise =resolve['response']['Advertisements'];
                         for(var iCount =0 ; iCount < Object.keys(Advertise).length; iCount++)
                         {   
                            this.Feature_All_array.push(Advertise[iCount]);
                          }
                        
                          /* -------------------  Bill (Member section)------------------ */
                          if(resolve['response']['bill'] != "")
                          {
                            var billDetails = resolve['response']['bill'];
                            this.bill_amount = billDetails[0]['TotalBillPayable'];
                            this.bill_billdate = billDetails[0]['bill_date'];
                            this.bill_duedate = billDetails[0]['due_date'];

                          }
                          /* -------------------  Reciept (Member section)------------------ */
                          if(resolve['response']['receipt'] != "")
                          {
                            var receiptDetails = resolve['response']['receipt'];
                            this.receipt_amount = receiptDetails[0]['Amount'];
                            this.receipt_date = receiptDetails[0]['ChequeDate'];

                          }
                         /* -------------------  Dues (Member section)------------------ */
                          if(resolve['response']['dues'] != "")
                          {
                              var dueDetails = resolve['response']['dues'];
                              this.due_amount = dueDetails[0]['Total'];
                              if(this.due_amount<0){this.flash = true;}
                              //alert(billDetails[0]['TotalBillPayable']);
                              //this.globalVars.setMemberDuesAmount(dueDetails[0]['Total']);
                          }
                         /* -------------------  Events (Member section)------------------ */
                          if(resolve['response']['events'] != "")
                          {
                            var upEvents = resolve['response']['events'];
                            for(var i=0; i < Object.keys(upEvents).length;i++)
                            {
                              this.event_details_array.push(upEvents[i]);
                            }
                          }
                        /* -------------------  Notices (Member section)------------------ */
                          if(resolve['response']['notice'] != "")
                          {
                            var upNotice = resolve['response']['notice'];
                            for(var iCn=0;iCn<upNotice.length;iCn++)
                            {
                              this.notice_details_array.push(upNotice[iCn]);
                         
                            }
                          }
                        /*   ----------------   Service Request --------------*/
                          if(resolve['response']['Srequest'] != "")
                          {
                            var upRequest = resolve['response']['Srequest'];
                            for(var iReq = 0; iReq <  Object.keys(upRequest).length; iReq++)
                            {
                              this.SRequest_details_array.push(upRequest[iReq]);
                         
                            }
                          }

                        /* -------------------  Classified ------------------ */

                          if(resolve['response']['classified'] != "")
                          {
                            var upClassified = resolve['response']['classified'];
                            for(var iClass=0; iClass< Object.keys(upClassified).length; iClass++)
                            {
                              this.Classified_details_array.push(upClassified[iClass]);
                         
                            }
                          }
                        /* -------------------  Poll ------------------ */
                          if(resolve['response']['poll'] != "")
                          {
                            var upPoll = resolve['response']['poll'];
                            for(var iPoll= 0; iPoll < Object.keys(upPoll).length; iPoll++)
                            {
                              this.Poll_details_array.push(upPoll[iPoll]);
                         
                            }
                          }
                      //}
                     // alert("test");
                           var BlockDesc = resolve['response']['BlockedUnit'][0];
                           this.globalVars.MAP_UNIT_BLOCK = BlockDesc['block_unit'];
                           this.globalVars.MAP_BLOCK_DESC = BlockDesc['block_desc'];
                       }
                     //  ----  Services Feature -------------------//
                        if(this.roleWise== 'Member' || this.role == "Admin" || this.role == "AdminMember" || this.role == "Admin" || this.role == "SuperAdmin" || this.role == "Manager" || this.role == "Tenant" )
                       {
                        if(resolve['response']['SocietySubscription'] != "")
                        {
                          var SocietySubs = resolve['response']['SocietySubscription'];
                        // console.log('FeatureSetting ' + FeatureSetting[0]['send_approval_tovisitor']);
                          this.SocietySubscriptionNote= SocietySubs[0]['subscription_alert_notes'];
                          this.SocietySubscription = SocietySubs[0]['software_subscription'];
                          this.SocietySubscriptionDate = SocietySubs[0]['subscription_alert_date'];
                         
                           if(this.currentDate <= this.SocietySubscriptionDate)
                           {
                              if(this.SocietySubscription != '0')
                             {
                                //this.presentAlert(); 
                             }
                             this.AccessUI = "0";
                            // alert("inside if ");
                          }
                          else
                          {
                         
                           
                          // alert(this.SocietySubscription);
                            if((this.SocietySubscription == '1')  && (this.roleWise == "Admin" || this.roleWise == "SuperAdmin" ))
                            {
                               this.AccessUI = "0";
                               this.blockAcount = "0";
                               //this.presentAlert(); 
                               // alert("inside else1 ");
                            }
                            else if((this.SocietySubscription == '2') &&  (this.roleWise == "Admin" || this.roleWise == "SuperAdmin" || this.roleWise == "Manager" || this.roleWise == "Member" ||  this.roleWise == "AdminMember") )
                            {
                              this.blockAcount = "1";
                              this.AccessUI = "0";
                              //this.presentAlert(); 
                              // alert("inside else2 ");
                            }
                            else if(this.SocietySubscription == '0')
                            {
                              this.AccessUI = "0";
                              this.blockAcount = "0";
                             // alert("inside else3 ");
                            }
                            else
                            {
                               this.AccessUI = "1";
                                this.blockAcount = "0";
                              // alert("inside els4 ");
                            }
                          }
                        }
                   
                     
                     }

                        var Services = resolve['response']['MobileServices']; 
                       for(var iService = 0; iService < Object.keys(Services).length; iService++)
                       {
                         this.Services_array.push(Services[iService]);
                       }

                    if(this.role == "Admin" || this.role == "AdminMember" )
                     {
                        var ProfileFLag =resolve['response']['ProfileFlags'][0];
                        this.globalVars.setUserProfileDetails(ProfileFLag['PROFILE_APPROVALS_LEASE'], ProfileFLag['PROFILE_CLASSIFIED'], 
                          ProfileFLag['PROFILE_CREATE_ALBUM'], ProfileFLag['PROFILE_CREATE_POLL'], ProfileFLag['PROFILE_EDIT_MEMBER'],
                          ProfileFLag['PROFILE_MANAGE_LIEN'], ProfileFLag['PROFILE_PHOTO'], ProfileFLag['PROFILE_SEND_EVENT'],
                          ProfileFLag['PROFILE_SEND_NOTICE'], ProfileFLag['PROFILE_SERVICE_PROVIDER'], ProfileFLag['PROFILE_SEND_NOTIFICATION'],
                          ProfileFLag['PROFILE_USER_MANAGEMENT'] );
                      }
                      /// alert(ProfileFLag['PROFILE_CLASSIFIED']);
                        //this.loaderView.dismissLoader();
                      
                      if(resolve['response']['FetureSetting'] != "")
                      {
                        var FeatureSetting = resolve['response']['FetureSetting'];
                       //console.log('FeatureSetting ' + FeatureSetting[0]['send_approval_tovisitor']);
                        var visitor_approval_status = FeatureSetting[0]['send_approval_tovisitor'];
                         //this.globalVars.setVisitor_Approval_Status(visitor_approval_status);
                      }
                      
                          
                      }
                    
                      
                   // }

                     //this.loaderView.dismissLoader();
                 }
    ).catch((err) => {
    console.log(err)
   // this.loaderView.dismissLoader();
    });
  }
  selectSociety() {
    this.navCtrl.navigateRoot(this.SocietyPage);
  }
  viewDues() {
    //alert("call");
  if(this.AccessUI == "0" && this.blockAcount=="0" )  ///   -----------------------------------------------------------------
   {
    //alert("call1");
   this.loaderView.showLoader('Loading ...');  
    var objData = [];
    objData['fetch'] =1;   /// New Version
     objData['UnitID'] = 0;

   this.connectServer.getData("MemberLedger", objData).then(
     resolve => { 
                   this.loaderView.dismissLoader();
                   if(resolve['success'] == 1)
                   {
                    //alert("call2");
                       //this.navCtrl.push(this.DuesPage, {details : resolve['response']});
                       //this.navCtrl.navigateRoot(this.DuesPage, {details : resolve['response']});
                       let navigationExtras: NavigationExtras = {
                        queryParams: 
                        {
                          details :resolve['response'],
                        }
                      };
                      //alert("call3");
                        this.navCtrl.navigateRoot(this.DuesPage,navigationExtras);
                        //}
                      // this.router.navigate(this.DuesPage, {details : resolve['response']});
                   }
                }
   );
 }
 else
 {
   //this.presentAlert(); 
   /*let navigationExtras: NavigationExtras = {
    queryParams: {
        userName:'TESTTSTSTS' ,
       
    }
};
  this.navCtrl.navigateRoot(this.SocietyPage,navigationExtras);*/
 }
 }
 viewFeature()
   {
     this.navCtrl.navigateRoot(this.FeaturesPage);
   }
   viewAllEvents() {
    if(this.AccessUI == "0")
    {
      this.navCtrl.navigateRoot(this.EventsPage);
    }
    else{
     // this.presentAlert(); 
    }
    
  }
  viewAllNotices() {
    if(this.AccessUI == "0")
   {
     this.navCtrl.navigateRoot(this.NoticesPage);
   }
   else{
     //this.presentAlert(); 
   }
   
 }
 sRequest()
  {
    var p=[];
    p['dash']="society";
     if(this.AccessUI == "0")
    {
      let navigationExtras: NavigationExtras = {
        queryParams: 
        {
          details :p,
        }
      };
      this.navCtrl.navigateRoot(this.ServiceRequestPage,navigationExtras);
      //this.navCtrl.navigateRoot(this.ServiceRequestPage, {details : p});  //issue please check
      //this.navCtrl.navigateRoot(this.ServiceRequestPage); 
    }
    else
    {
      //this.presentAlert(); 
    }
    
  }
  viewNotices()
   {
      var p=[];
      p['dash']="society";
       if(this.AccessUI == "0")
       {
          //this.navCtrl.navigateRoot(this.NoticesPage, {details : p});
          this.navCtrl.navigateRoot(this.NoticesPage);
        }
        else
        {
          //this.presentAlert(); 
        }
      
   }
   payment()
   {
    //alert("payment");
     var p=[];
     p['dash']="society";
     //alert(this.AccessUI);
     //alert(this.blockAcount);
      if( this.AccessUI == "0" && this.blockAcount=="0")
     {
      //alert("navigate");
      // this.navCtrl.navigateRoot(PaymentPage, {details : p});
       this.navCtrl.navigateRoot(this.PaymentPage);
     }
     else
     {
        //this.presentAlert(); 
     }
   }
   viewAlbums()
   {
     var p=[];
     p['dash']="society";
      if(this.AccessUI == "0")
      {
         //this.navCtrl.navigateRoot(this.PhotoAlbumPage, {details : p});
         this.navCtrl.navigateRoot(this.PhotoAlbumPage);
      }
      else
      {
        // this.presentAlert(); 
      }
   }
   profile()
   {
     var p=[];
     p['dash']="society";
     p['uID']=0;
    // alert(this.AccessUI);
     if(this.AccessUI == "0")
     {
      let navigationExtras: NavigationExtras = {
        queryParams: 
        {
          details : p,
                   
        }
      };
        this.navCtrl.navigateRoot(this.ProfilePage,navigationExtras);
       //if(this.roleWise =="Member" )
       //{
         //this.navCtrl.navigateRoot(this.ProfilePage, {details : p});  
         //this.navCtrl.navigateRoot(this.ProfilePage);  
      // }
      /* else if(this.roleWise =="Tenant")
       {
         this.navCtrl.push(this.TenantProfilePage, {details : p});  
       }
       else
       {
         this.navCtrl.push(this.UpdateprofilePage);
       }*/
      
    }
     else
     {
         //this.presentAlert(); 
     }
     //if(this.roleWise =="Member" || this.roleWise =="AdminMember")
    
   }
   viewEvents()
   {
     var p=[];
     p['dash']="society";
      if(this.AccessUI == "0")
     {
       //this.navCtrl.navigateRoot(this.EventsPage, {details : p});
       this.navCtrl.navigateRoot(this.EventsPage);
     }
     else
     {
       //this.presentAlert(); 
     }
     
   }
   directory()
  {
    var p=[];
    p['dash']="society";
     if(this.AccessUI == "0")
    {
      this.navCtrl.navigateRoot(this.DirectoryPage);
      //this.navCtrl.push(DirectoryPage, {details : p}); 
    }
    else
    {
      //this.presentAlert(); 
    }
    
  }
  viewPoll()
  {
    var p=[];
    p['dash']="society";
     if(this.AccessUI == "0")
    {
      let navigationExtras: NavigationExtras = {
        queryParams: 
        {
          details :p,
        }
      };
      //this.navCtrl.navigateRoot(PollPage, {details : p});
      this.navCtrl.navigateRoot(this.PollPage,navigationExtras);
    }
    else
    {
      //this.presentAlert(); 
    }
   
  }
  sProvider()
  {

    var p=[];
    p['dash']="society";
    if(this.AccessUI == "0")
    {
      let navigationExtras: NavigationExtras = {
        queryParams: 
        {
          details :p,
        }
      };
      //this.navCtrl.push(ServiceproviderPage, {details : p});  
      this.navCtrl.navigateRoot(this.ServiceproviderPage,navigationExtras);
    }
    else
    {
      //this.presentAlert(); 
    }
    
  }

  viewClassified(){
   
    this.navCtrl.navigateRoot(this.ClassifiedsPage);
  }
  
} // end tag

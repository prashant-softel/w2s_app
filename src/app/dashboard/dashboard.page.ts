import { Component, OnInit ,CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { ApiProvider } from 'src/service/api';
import { Observable } from 'rxjs';

enum statusEnum  { "Raised" = 1,  "Waiting",  "In progress",  "Completed", "Cancelled"}
enum priorityEnum  { "Critical" = 1,  "High",  "Medium",  "Low"}
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.page.html',
  styleUrls: ['./dashboard.page.scss'],
  standalone: true,
  schemas: [ CUSTOM_ELEMENTS_SCHEMA ],
  imports: [IonicModule, CommonModule, FormsModule]
})
export class DashboardPage implements OnInit {
  flash : boolean = false;
  tab: string = "society";
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
  event_details_array: Array<{}> = [];
  notice_details_array: Array<{}> = [];
  SRequest_details_array: Array<{}> = [];
  Classified_details_array: Array<{}> = [];
  Poll_details_array: Array<{}> = [];
  task_by_me_array: Array<{}> = [];
  Feature_member_array: Array<{}> = [];
  Feature_Admin_array: Array<{}> = [];
  Feature_All_array: Array<{}> = [];
  role: any;
  roleWise: any;
  status: any;
  priority: any;
  img_link: any;
  films: any | [];
  constructor(public apiProvider: ApiProvider
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
      //this.event_details = params.get("details");
      this.event_charges = 0;
      this.role="";
      this.roleWise="";
      this.status = statusEnum;
      this.priority = priorityEnum;
     // this.img_link="https://way2society.com/Avd_img/"
      this.img_link="http://localhost/master_file/Avd_img/"
    //  this.userRole="";


  }

  ngOnInit() {
    // console.clear();
    this.apiProvider.getFilms().subscribe({
      next: (response: any) => {
        this.films = response.results;
        console.log(this.films);
        alert(this.films.length)
      },
      error: (err) => {
        alert('There was an error in retrieving data from the server');
      }
    });
    //alert(this.films)
  }
    /* ----------Admin section -----------------*/
    viewServiceInAdmin() {
      //var p=[];
      //p['dash']=this.tab;
      //console.log(p);
      //this.navCtrl.push(ServiceRequestPage,{details : p});
    }
     viewPollAdmin() {
      //var p=[];
      //p['dash']=this.tab;
      //console.log("tab : "+this.tab);
      //this.navCtrl.push(PollPage,{details : p});
    }
    
    viewProvider()
    {
     //var p=[];
      //p['dash']=this.tab;
      //p['flag'] = flag;
      //console.log(p);
      //this.navCtrl.push(ServiceproviderPage,{details : p});
    }
    viewAllProvider()
    {
     //var p=[];
     // p['tab']='1';
      //p['dash']= "admin";
      ////p['flag'] = flag;
     // this.navCtrl.push(ServiceproviderPage,{details : p});
    }
    viewPendingProvider()
    {
     //var p=[];
      //p['tab']='2';
      //p['dash']= "admin";
     // p['flag'] = flag;
      //this.navCtrl.push(ServiceproviderPage,{details : p});
    }
    viewTenantsInAdmin(){
      //var p=[];
      //p['dash']=this.tab;
     // console.log(p);
     // this.navCtrl.push(TenantsPage,{details : p});
    }
    viewClassifiedInAdmin(){
      // var p=[];
      // p['dash']=this.tab;
      // console.log(p);
     // this.navCtrl.push(ClassifiedsPage);
    }
    /*-----------------Member section ------------------*/ 
    viewService() {
     // var p=[];
     // p['dash']="society";
      ///console.log(p);
     // this.navCtrl.push(ServiceRequestPage,{details : p});
    }
  
    /* viewClassified() {
      this.navCtrl.push(ClassifiedsPage);
    }*/
    /* viewPoll() {
      var p=[];
      p['dash']="society";
      console.log(p);
      this.navCtrl.push(PollPage,{details : p});
    }*/
     viewImpose() {
     // this.navCtrl.push(ViewimposefinePage);
    }
    neftPayment() {
   // alert("tesh");
      //this.navCtrl.push(RecordneftPage);
    }
  
   /*  payment(){
       this.navCtrl.push(PaymentPage);
     }
     */
     viewDirectory() {
     // this.navCtrl.push(DirectoryPage);
    }
    viewAllEvents() {
     // this.navCtrl.push(EventsPage);
    }
  
    viewAllNotices() {
      //this.navCtrl.push(NoticesPage);
    }
  
    selectSociety() {
      //this.navCtrl.setRoot(SocietyPage);
    }
  
    viewTask(){
     // this.navCtrl.push(TaskPage);
    }
  
    viewAdminAlbums() {
      //this.navCtrl.push(AlbumApprovalPage);
    }
  Visitor()
  {
  
    // this.navCtrl.push(VisitorInPage);
  }
  /* -------    Header Icons Function ---    */

  profile()
  {
   /* var p=[];
    p['dash']="society";
    p['uID']=0;
    if(this.roleWise =="Member" || this.roleWise =="AdminMember")
    {
      this.navCtrl.push(ProfilePage, {details : p});  
    }
    else
    {
      this.navCtrl.push(UpdateprofilePage);
    }*/
    
  }
  payment()
  {
    //var p=[];
    //p['dash']="society";
    //this.navCtrl.push(PaymentPage, {details : p});
  }
  viewAlbums()
  {
    //var p=[];
    //p['dash']="society";
    //this.navCtrl.push(PhotoAlbumPage, {details : p});
  }
  sProvider()
  {
    //var p=[];
    //p['dash']="society";
    //this.navCtrl.push(ServiceproviderPage, {details : p});
  }
  sRequest()
  {
   //var p=[];
   // p['dash']="society";
    //this.navCtrl.push(ServiceRequestPage, {details : p});
  }
  directory()
  {
    //var p=[];
    //p['dash']="society";
    //this.navCtrl.push(DirectoryPage, {details : p});
  }
  viewClassified()
  {
   // var p=[];
    //p['dash']="society";
   // this.navCtrl.push(ClassifiedsPage, {details : p});
  }
  viewPoll()
  {
    //var p=[];
    //p['dash']="society";
    //this.navCtrl.push(PollPage, {details : p});
  }
  viewEvents()
  {
    //var p=[];
    //p['dash']="society";
    //this.navCtrl.push(EventsPage, {details : p});
  }
   viewNotices()
   {
      //var p=[];
      //p['dash']="society";
      //this.navCtrl.push(NoticesPage, {details : p});
   }
   viewFeature()
   {
     //this.navCtrl.push(FeaturesPage);
   }
}


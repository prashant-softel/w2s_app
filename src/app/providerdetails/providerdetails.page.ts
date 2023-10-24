import { Component, OnInit, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AlertController, IonicModule, NavController, NavParams, Platform, } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras, Router } from '@angular/router';

@Component({
  selector: 'app-providerdetails',
  templateUrl: './providerdetails.page.html',
  styleUrls: ['./providerdetails.page.scss'],
  standalone: true,
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ],
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ProviderdetailsPage implements OnInit {
  tab: string = "personal";
  UpdateproviderPage: any = 'updateprovider';
  ServiceproviderPage: any = 'serviceprovider';
  image_id: any;
  dbname: any;
  imagestring: any;
  checkoutdesc: any;
  checkoutnote: any;
  profileImage: string = null;
  staffitemlended = {};
  displayData = {};
  errorlended: any;
  providerData = {};
  userData: { startDate: any, endDate: any, comment: any };
  ReportDetails: any;
  img_src: string;
  assigned: any;
  AssignUnit: any;
  unitList: Array<any>;
  totalunit: Array<any>;
  review_array: Array<any>;
  Document_array: Array<any>;
  Rate: any;
  UnitReport_array: Array<any>;
  role: string;
  roleWise: string;
  DateList: any;
  Attendance: any;
  InTime: any;
  TotalHours: any;
  OutTime: any;
  Listing: any;
  toggleStatus: boolean;
  notifysearch: any;
  RefreshCalender: any;
  moreInfo: any;
  profile_flag: any;
  DateCount: any;
  OutDate: any;
  count: any;
  dateMulti: string[];
  typeMulti: 'string'; // 'string' | 'js-date' | 'moment' | 'time' | 'object'
  /* optionsMulti: CalendarComponentOptions = {
     pickMode: 'multi',
     color: 'primary',
     daysConfig:[]
   };
   */
  /*options : InAppBrowserOptions = {
      location : 'yes',//Or 'no'
      hidden : 'no', //Or  'yes'
      clearcache : 'yes',
      clearsessioncache : 'yes',
      zoom : 'yes',//Android only ,shows browser zoom controls
      hardwareback : 'yes',
      mediaPlaybackRequiresUserAction : 'no',
      shouldPauseOnSuspend : 'no', //Android only
      closebuttoncaption : 'Close', //iOS only
      disallowoverscroll : 'no', //iOS only
      toolbar : 'yes', //iOS only
      enableViewportScale : 'no', //iOS only
      allowInlineMediaPlayback : 'no',//iOS only
      presentationstyle : 'pagesheet',//iOS only
      fullscreen : 'yes',//Windows only
  };*/
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private router: Router,
    private alertCtrl: AlertController

  ) {

    //this.displayData = navParams.get("details");
    let data_details: any;
    data_details = this.router.getCurrentNavigation()?.extras?.state;
    this.displayData = data_details.details;
    console.log("navParams : ", this.displayData);

    this.userData = { startDate: "", endDate: "", comment: "" };
    this.providerData = [];
    this.staffitemlended = [];
    this.ReportDetails = [];
    this.img_src = "https://way2society.com/upload/main";
    this.checkoutdesc = "0";
    this.checkoutnote = "";
    this.errorlended = "";
    this.image_id = "";
    this.dbname = "";
    this.imagestring = "";
    this.assigned = 0;
    this.AssignUnit = "";
    this.unitList = [];
    this.totalunit = [];
    this.review_array = [];
    this.Document_array = [];
    this.Rate = 0;
    this.UnitReport_array = [];
    this.role = "";
    this.roleWise = '';
    this.DateList = "";
    this.Attendance = "";
    this.InTime = "";
    this.TotalHours = "";
    this.OutTime = "";
    this.Listing = '0';
    this.toggleStatus = false;
    this.notifysearch = "0";
    this.RefreshCalender = '1';
    this.moreInfo = '0';
    this.DateCount = 0;
    this.OutDate = "";
    this.count = 0;
    this.dateMulti = [];
  }

  ngOnInit() {
    if (this.globalVars.MAP_USER_ROLE == "Member") {
      this.role = "Member";
      this.profile_flag = 0;
    }
    else if (this.globalVars.MAP_USER_ROLE == "Admin Member") {
      this.profile_flag = this.globalVars.PROFILE_SERVICE_PROVIDER;
      this.role = "AdminMember";
    }
    else if (this.globalVars.MAP_USER_ROLE == "Admin") {
      this.profile_flag = this.globalVars.PROFILE_SERVICE_PROVIDER;
      this.role = "Admin";
    }
    else {
      this.role = "SuperAdmin";
      this.profile_flag = 2;
    }
    this.roleWise = this.role;
    this.fetchProvidersData();
  }
  fetchProvidersData() {
    this.loaderView.showLoader('Loading ...');
    var objData = [];
    objData['societyid'] = this.displayData['society_id'];
    objData['providerid'] = this.displayData['service_prd_reg_id'];
    this.connectServer.getData("ServiceProvider/selectprovider", objData).then(
      resolve => {
        this.loaderView.dismissLoader();
        if (resolve['success'] == 1) {
          console.log("SUCCESS")
          var providers = resolve['response']['providerDetails'][0];
          if (Object.keys(resolve['response']['servicedata']).length > 0) {
            if (Object.keys(resolve['response']['servicedata']['Staff_Lended']).length > 0) {
              this.staffitemlended = resolve['response']['servicedata']['Staff_Lended'];
              console.log(this.staffitemlended);
            }
            else {
              console.log('hello');
              this.errorlended = "No Data Available";
            }
          }
          else {
            console.log('hello1');
            this.errorlended = "No Data Available";
          }
          this.providerData = providers;
          var units = providers['UnitID'].split(',');
          var myUnit = this.globalVars.MAP_UNIT_ID;
          for (var iUnit = 0; iUnit < units.length; iUnit++) {
            if (units[iUnit].trim() == myUnit) {
              this.assigned = 1;
            }
            else {
              //this.AssignUnit= this.globalVars.MAP_UNIT_NO;
            }
          }
          var Review = providers['Review'];
          for (var iCnt = 0; iCnt < Object.keys(Review).length; iCnt++) {
            this.review_array.push(Review[iCnt]);
          }
          this.Rate = Review[0]['Rate'];
          var Document = providers['Documents'];
          for (var i = 0; i < Object.keys(Document).length; i++) {
            this.Document_array.push(Document[i]);
          }
          var UnitDetail = providers['UnitsDetails'];
          for (var iDetail = 0; iDetail < Object.keys(UnitDetail).length; iDetail++) {
            this.UnitReport_array.push(UnitDetail[iDetail]);
          }
        }
        else {
          console.log("FAIL")
        }
      });
  }
  /* -------------------------  Image View ---------------------------- */
  viewImage() {
    //this.navCtrl.push(ServiceproviederimageviewPage, {details: this.navParams.get("details") });
    //return false;
  }

  /* -------------------------------  Assign Provider In Unit -------------------*/
  Assignme() {
    var objData = [];
    objData['providerid'] = this.displayData['service_prd_reg_id'];
    this.connectServer.getData("ServiceProvider/assign", objData).then(
      resolve => {
        console.log('Response : ' + resolve);
        if (resolve['success'] == 1) {
          alert("Added to unit successfully");
          var p = [];
          p['tab'] = '0';
          p['dash'] = "society";
          p['flag'] = 1;
          // this.navCtrl.setRoot(ServiceproviderPage, {details :p});
        }
        else {
          // this.message = resolve['response']['message'];
        }
      }
    );
  }
  getMoreInfo() {

    this.moreInfo = '1';
  }
  /*  ---------------------------  Remove Provider In Assign unit --------------------- */

  Remove() {
    var objData = [];
    objData['providerid'] = this.displayData['service_prd_reg_id'];
    this.connectServer.getData("ServiceProvider/remove", objData).then(
      resolve => {
        //this.loaderView.dismissLoader();
        console.log('Response : ' + resolve);
        if (resolve['success'] == 1) {
          alert("Removed from unit successfully ");
          var p = [];
          p['tab'] = '0';
          p['dash'] = "society";
          // this.navCtrl.setRoot(ServiceproviderPage, {details :p});
        }
        else {
          // this.message = resolve['response']['message'];
        }
      }
    );
  }
  /*  ---------------------------  Get Provider Report  --------------------- */
  search() {
    //this.providerReport();
    this.notifysearch = "1";
    // alert(this.dateMulti.length);
  }
  searchagain() {
    this.notifysearch = "0";
  }
  /*providerReport()
  {
    var objData=[];
    objData['providerid'] = this.displayData['service_prd_reg_id']; 
    objData['StartDate'] = this.userData['startDate'];
    objData['EndDate'] = this.userData['endDate'];
    this.RefreshCalender = '0';
    this.loaderView.showLoader('Loading ...');
    this.connectServer.getData("ServiceProvider/ProviderReport",  objData).then(
        resolve => {
            this.loaderView.dismissLoader();
            this.RefreshCalender = '1';                  ///  reload calander 
            console.log('Response : ' + resolve);
            if(resolve['success'] == 1)
            {
              var Report=resolve['response']['AttendanceReport'];
              console.log("Reports:",Report);
              //let _daysConfig: DayConfig[] = [];
              for(var iCnt  = 0; iCnt <Report.length; iCnt++)
              {
                // alert(Report[iCnt]['0']);
                this.ReportDetails[iCnt]=Report[iCnt];
                this.ReportDetails[iCnt]['DateList']=Report[iCnt]['0'];
                this.ReportDetails[iCnt]['Attendance'] = Report[iCnt]['1'];
                this.ReportDetails[iCnt]['InTime']=Report[iCnt]['2'];
                this.ReportDetails[iCnt]['TotalHours']=Report[iCnt]['3'];
                this.ReportDetails[iCnt]['OutTime']=Report[iCnt]['4'];
                this.ReportDetails[iCnt]['OutDate']=Report[iCnt]['5'];
                this.dateMulti[iCnt]= moment(this.ReportDetails[iCnt]['DateList']).format('YYYY-MM-DD'); 
                if(Report[iCnt]['1'] == 'A') 
                {
                  _daysConfig.push({
                  date: new Date(moment(this.ReportDetails[iCnt]['DateList']).format('YYYY-MM-DD')),
                  //marked: true,
                  cssClass: 'redButton'
                  })
                }
              }
              this.optionsMulti['daysConfig'] = _daysConfig;
            }
            else
            {
              // this.message = resolve['response']['message'];
            }
          } 
      );
    // this.ShowCalander();
    }*/
  /* ------------------------------  Toogle Implements -----------------------  */

  Change_Toggle() {
    //alert(this.toggleStatus);
    if (this.toggleStatus == true) {
      this.Listing = '1';
    }
    else {
      this.Listing = '0';
    }
  }
  cancle() {
    this.moreInfo = '0';
  }
  AddMoreInfo() {
    var objData = [];
    objData['providerid'] = this.displayData['service_prd_reg_id'];
    objData['comment'] = this.userData['comment'];
    this.connectServer.getData("ServiceProvider/addMoreInfo", objData).then(
      resolve => {
        //this.loaderView.dismissLoader();
        console.log('Response : ' + resolve);
        if (resolve['success'] == 1) {
          alert("Comments added successfully ");
          var p = [];
          p['tab'] = '1';
          p['dash'] = "admin";
          p['flag'] = '2';
          let navigationExtras: NavigationExtras = {
            queryParams:
            {
              details: p,
            }
          };
          this.navCtrl.navigateRoot(this.ServiceproviderPage, navigationExtras);
          // this.navCtrl.navigateRoot(this.ServiceproviderPage, { details: p });
        }
        else {
          // this.message = resolve['response']['message'];
        }
      }
    );

  }

  /* ------------------   Provider Balck Listed ----------------- */
  Block(p) {
    //this.navCtrl.push(BlockproviderPage,{details: this.providerData});
  }

  /* ------------------   Provider Edit Personal details ----------------- */
  Edit_persional() {

    this.providerData['dash'] = "society";
    this.providerData['call'] = "1";
    let navigationExtras: NavigationExtras = {
      queryParams:
      {
        details: this.providerData,

      }
    };
    this.navCtrl.navigateRoot(this.UpdateproviderPage, navigationExtras);
    // this.navCtrl.navigateRoot(this.UpdateproviderPage, {details: this.providerData});
  }

  /* ------------------   Add Provider Review ----------------- */
  Review() {
    //this.navCtrl.push(ReviewPage, {details: this.providerData});
  }

  /* ------------------   Edit Provider Contact Details ----------------- */
  Edit_Contact() {
    this.providerData['dash'] = "society";
    this.providerData['call'] = "3";
    //this.navCtrl.push(EditproviderPage, {details: this.providerData});
  }

  /* ------------------  Edit Provider Refrence Details----------------- */
  Edit_Reference() {
    this.providerData['dash'] = "society";
    this.providerData['call'] = "4";
    //this.navCtrl.push(EditproviderPage, {details: this.providerData});
  }

  /* ------------------  Edit Provider Family Details----------------- */
  Edit_Family() {
    this.providerData['dash'] = "society";
    this.providerData['call'] = "5";
    //this.navCtrl.push(EditproviderPage, {details: this.providerData});
  }

  /* ------------------  Edit Provider Document Details----------------- */
  Edit_Document() {
    this.providerData['dash'] = "society";
    this.providerData['call'] = "6";
    /// this.navCtrl.push(EditproviderPage, {details: this.providerData});
  }

  /* ------------------  Open Provider Document ----------------- */
  public openWithSystemBrowser(documents) {
    var url = "";
    url = "https://way2society.com/W2S_DocViewer.php?url=Uploaded_Documents/" + documents + "&doc_version=1";
    let target = "_system";
    ///this.theInAppBrowser.create(url,target,this.options);
  }
  async confirmApprove() {
    let alert = await this.alertCtrl.create({
      header: 'Approve Provider?',
      message: '',
      buttons: [
        {
          text: 'Cancel',
          role: 'cancel',
          handler: () => {
            console.log('Cancel clicked');
          }
        },
        {
          text: 'Approve',
          handler: () => {
            this.AproveIt();
          }
        }
      ]
    });
    await alert.present();
  }


  /* ------------------  Provider Remove  ----------------- */
  async confirmRemove() {
    let alert = await this.alertCtrl.create({
      header: 'Remove Provider?',
      message: 'Do you really want to remove Provider',
      buttons: [
        {
          text: 'Cancel',
          role: 'cancel',
          handler: () => {
            console.log('Cancel clicked');
          }
        },
        {
          text: 'Remove',
          handler: () => {
            this.RemoveIt();
          }
        }
      ]
    });
    await alert.present();
  }

  /* ------------------  Provider Approve  ----------------- */
  AproveIt() {
    var objData = [];
    objData['providerid'] = this.displayData['service_prd_reg_id'];
    this.connectServer.getData("ServiceProvider/Approved", objData).then(
      resolve => {
        //this.loaderView.dismissLoader();
        console.log('Response : ' + resolve);
        if (resolve['success'] == 1) {
          alert("Provider Approved successfully ");
          var p = [];
          p['tab'] = '1';
          p['dash'] = "admin";
          p['flag'] = '2';
          let navigationExtras: NavigationExtras = {
            queryParams:
            {
              details: p,
            }
          };
          this.navCtrl.navigateRoot(this.ServiceproviderPage, navigationExtras);
          // this.navCtrl.setRoot(ServiceproviderPage, {details :p});
        }
        else {
          // this.message = resolve['response']['message'];
        }
      }
    );
  }

  /* ------------------  Provider Remove  ----------------- */
  RemoveIt() {
    var objData = [];
    objData['providerid'] = this.displayData['service_prd_reg_id'];
    this.connectServer.getData("ServiceProvider/Removeit", objData).then(
      resolve => {
        //this.loaderView.dismissLoader();
        console.log('Response : ' + resolve);
        if (resolve['success'] == 1) {
          alert("Provider Remove successfully ");
          var p = [];
          p['tab'] = '1';
          p['dash'] = "admin";
          p['flag'] = '2';
          let navigationExtras: NavigationExtras = {
            queryParams:
            {
              details: p,
            }
          };
          this.navCtrl.navigateRoot(this.ServiceproviderPage, navigationExtras);
          // this.navCtrl.navigateRoot(this.ServiceproviderPage, {details :p});
        }
        else {
          // this.message = resolve['response']['message'];
        }
      }
    );

  }
}

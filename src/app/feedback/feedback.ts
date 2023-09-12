import { Component } from '@angular/core';
import { IonicModule, NavController, NavParams } from '@ionic/angular';

// import { Geolocation } from '@ionic-native/geolocation/ngx';
//import { Storage } from '@ionic/storage';

//import { Thanks } from '../thanks/thanks';
import { Survey } from '../survey/survey';
//import { Surveylist } from '../surveylist/surveylist';
import { Dashboard } from '../dashboard/dashboard';
import { Router } from '@angular/router';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { GlobalVars } from 'src/service/globalvars';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

/**
 * Generated class for the Feedback page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@Component({
  templateUrl: 'feedback.html',
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class Feedback {

  details: Array<{}>;
  surveyDetails: { society_name: any, society_add: any, landmark: any, area: any, locality: any, pin_code: any, email: any, no_of_units: any, billingcycle: any, managerName: any, manager_contactNo: any, secretoryName: any, secretory_contactNo: any, Amount: any, perCycle: any, portalName: any, chargespaying: any, notes: any };
  particulars: Array<{ ID: number, question: string, rating: number }>;
  count: number = 0;
  message: any;
  showStar: number;
  hideMR: number;
  remarks: any;

  latitude: number;
  longitude: number;
  displayData: any;
  loginId: number;
  area_Array: Array<any>;
  Locality_array: Array<any>;
  constructor(
    public navCtrl: NavController,
    public navParams: NavParams,
    private loaderView: LoaderView,
    private connectServer: ConnectServer,
    // private geolocation: Geolocation,
    private router: Router,
    public globalVars: GlobalVars) {
    this.particulars = [];
    this.details = [];
    this.area_Array = [];
    this.Locality_array = [];
    this.surveyDetails = { society_name: "", society_add: "", landmark: "", area: "", locality: "", pin_code: "", email: "", no_of_units: "", billingcycle: "", managerName: "", manager_contactNo: "", secretoryName: "", secretory_contactNo: "", Amount: "", perCycle: "", portalName: "", chargespaying: "", notes: "" };
    if (this.globalVars.USER_ROLE == "2") {
      this.hideMR = 1;
    }

    // this.displayData = navParams.get("details");
    let data_details: any;
    data_details = this.router.getCurrentNavigation()?.extras?.state;
    this.displayData = data_details.details;
    this.loginId = this.displayData['loginID'];

    // this.geolocation.getCurrentPosition().then(
    //   (resp) => {

    //   })
    //   .catch((error) => {
    //     console.log('Error getting location', error);
    //   });

    // let watch = this.geolocation.watchPosition(

    // );
    // watch.subscribe((data) => {

    //   this.latitude = 41.2;//data.coords.latitude;
    //   this.longitude = 43.4;//data.coords.longitude;
    // });
    // this.geolocation.getCurrentPosition().then((resp) => {
    //   this.latitude = resp.coords.latitude;
    //   this.longitude = resp.coords.longitude;
    // }).catch((error) => {
    //   console.log('Error getting location', error);
    // });
    this.latitude = 41.2;
    this.longitude = 42.3;

    // storage.set('area' , this.surveyDetails['area'],);
    //storage.set( 'locality', this.surveyDetails['locality']);
    // storage.set('pincode', this.surveyDetails['pin_code']);

    // Or to get a key/value pair
    //storage.get('area').then((val) => {
    // this.surveyDetails['area']=val;
    //console.log('Your age is', val);
    //});
    // storage.get('locality').then((val) => {
    //  this.surveyDetails['locality']=val;
    //console.log('Your age is', val);
    // });
    // storage.get('pincode').then((val) => {
    // console.log('Your age is', val);
    // });

  }


  ionViewDidLoad() {
    console.log('ionViewDidLoad Feedback');
    // this.fetchSurvey(); 
    this.GetArea();

  }



  /* sendLocationToDatabase () {
    var objData = {
    login_id : this.loginId,
    lat: this.latitude,
    lng: this.longitude,
    flag: 1
  };
  this.loaderView.showLoader('Loading');

  this.connectServer.getData("FeedbackServlet/AddLocation", objData).then
  (
      resolve => 
      {
          this.loaderView.dismissLoader();
          console.log(resolve);
          if(resolve['success'] == 1)
          {
            console.log('Location added successfully');
          }
          else
          {
            console.log("FAIL");
          }
      }
  ).catch((err) => {
    console.log("Error: ", err);
  });
   
}*/

  GetArea() {
    var objData = [];
    //this.loaderView.showLoader('Loading');
    this.connectServer.getData("FeedbackServlet/fetcharea", objData).then
      (
        resolve => {
          // this.loaderView.dismissLoader();

          if (resolve['success'] == 1) {
            this.area_Array = [];

            var AreaList = resolve['response']['areaDetails'];
            console.log("AreaList", AreaList);
            for (var iCnt = 0; iCnt < Object.keys(AreaList).length; iCnt++) {
              this.area_Array.push(AreaList[iCnt]);
            }
            console.log("Area", this.area_Array);
          }
          else {
            console.log("FAIL");
          }
        }
      ).catch((err) => {
        console.log("Error: ", err);
      });
  }

  fetchLocality() {

    var objData = [];
    objData['areaid'] = this.surveyDetails['area'];
    // alert(objData['areaid']);
    this.loaderView.showLoader('Loading ...');
    this.connectServer.getData("FeedbackServlet/fetchlocality", objData).then(
      resolve => {
        this.loaderView.dismissLoader();
        if (resolve['success'] == 1) {

          var localityList = resolve['response']['localityDetails'];
          this.Locality_array = [];
          for (var i = 0; i < Object.keys(localityList).length; i++) {
            this.Locality_array.push(localityList[i]);
          }
        }
        else {

        }
      }
    );
  }
  fetchPincode() {

    var objData = [];
    objData['localityid'] = this.surveyDetails['locality'];
    // alert(objData['areaid']);
    this.loaderView.showLoader('Loading ...');
    this.connectServer.getData("FeedbackServlet/fetchpincode", objData).then(
      resolve => {
        this.loaderView.dismissLoader();
        if (resolve['success'] == 1) {

          var localityPinCodeList = resolve['response']['localityDetails'];
          if (localityPinCodeList[0]['pincode'] != undefined) {
            this.surveyDetails['pin_code'] = localityPinCodeList[0]['pincode'];
          }
          else {
            this.surveyDetails['pin_code'] = "0";
          }
          // this.surveyDetails['pin_code']= localityPinCodeList[0]['pincode'];
          // alert( this.surveyDetails['pin_code']);
        }
        else {
          this.surveyDetails['pin_code'] = "0";
        }

      }

    );
  }

  submit() {
    //alert(this.surveyDetails['pin_code']);
    this.message = "";
    if (this.surveyDetails.society_name.length == 0) {
      this.message = "Please enter Society Name";
    }
    else if (this.surveyDetails.society_add.length == 0) {
      this.message = "Please enter Society Address";
    }
    else if (this.surveyDetails.area.length == 0) {
      this.message = "Please select area";
    }
    else if (this.surveyDetails.locality.length == 0) {
      this.message = "Please select locality";
    }
    //else if(this.surveyDetails.pin_code.length == 0)
    // {
    //  this.message = "Please enter pincode";
    // }
    else if (this.surveyDetails.no_of_units.length == 0) {
      this.message = "Please enter no of units";
    }

    else {
      this.surveyDetails['loginid'] = this.loginId;
      this.connectServer.getData("FeedbackServlet/insert", this.surveyDetails).then(
        resolve => {
          //this.loaderView.dismissLoader();
          console.log('Response : ' + resolve);
          if (resolve['success'] == 1) {
            this.message = resolve['response']['message'];
            alert("added successfully");

            //  this.sendLocationToDatabase();
            this.navCtrl.navigateRoot("Survey", { state: { details: { loginID: this.loginId } } });
          }
          else {
            this.message = resolve['message'];


            //this.navCtrl.setRoot(Thanks,  {surveyDetails : this.surveyDetails, doctorDetails : this.details});
            alert("not addded");
          }
        }
      );
    }
  }


  gotoSurvey() {
    this.navCtrl.navigateRoot("Survey", { state: { details: this.surveyDetails } });
  }
}
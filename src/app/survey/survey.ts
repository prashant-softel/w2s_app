import { Component } from '@angular/core';
import { IonicModule, NavController, NavParams } from '@ionic/angular';

import { Feedback } from '../feedback/feedback';
import { Router } from '@angular/router';
import { GlobalVars } from 'src/service/globalvars';
import { ConnectServer } from 'src/service/connectserver';
import { LoaderView } from 'src/service/loaderview';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
/**
 * Generated class for the Survey page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@Component({
  templateUrl: 'survey.html',

  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class Survey {
  message: any;
  userData: { Code: any };
  doctorDetails: {};
  surveyDetails: any;
  surveyID: number;
  doctorID: number;
  loginID: number;
  latitude: number;
  longitude: number;


  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private loaderView: LoaderView,
    private router: Router,

    // private geolocation: Geolocation
  ) {

    this.userData = { Code: "" };

    let data_details: any;
    data_details = this.router.getCurrentNavigation()?.extras?.state;
    // this.displayData = data_details.details;
    this.surveyDetails = data_details.details;
    // this.surveyDetails = navParams.get("details");
    // console.log('Survey Details', this.surveyDetails);
    this.loginID = this.surveyDetails['loginID'];
    this.doctorDetails = [];
    // this.geolocation.getCurrentPosition().then((resp) => {
    // }).catch((error) => {
    //   console.log('Error getting location', error);
    // });

    // let watch = this.geolocation.watchPosition();
    // watch.subscribe((data) => {
    //   this.latitude = data.coords.latitude;
    //   this.longitude = data.coords.longitude;
    //   console.log('Latitude: ', this.latitude);
    //   console.log('Longitude: ', this.longitude);
    // });

  }
  ionViewDidLoad() {
    console.log('ionViewDidLoad Survey');
    // this.sendLocationToDatabase(); 
    // this.start();
  }

  ionViewWillLeave() {

  }

  ionViewDidLeave() {
    console.log('ionViewDidLeave Survey');
    this.sendLocationToDatabase();
  }



  fetchDetails1() {
    this.navCtrl.navigateForward("Feedback", { state: { details: { loginID: this.loginID } } });
  }

  viewServey() {
    this.navCtrl.navigateForward("ViewsurveyPage", { state: { details: { loginID: this.loginID } } });
  }

  viewReminders() {
    this.navCtrl.navigateForward("ViewRemindersPage", { state: { details: { loginID: this.loginID } } });
  }

  sendLocationToDatabase() {
    var objData = {
      login_id: this.loginID,
      lat: this.latitude ?? 40.3,
      lng: this.longitude ?? 43.3,
      flag: 0
    };
    // this.loaderView.showLoader('Verifying details');
    this.connectServer.getData("FeedbackServlet/AddLocation", objData).then(
      resolve => {
        //this.loaderView.dismissLoader();
        console.log(resolve);
        if (resolve['success'] == 1) {
          console.log('Location added successfully');
        }
        else {
          console.log("FAIL");
        }
      }
    ).catch((err) => {
      console.log("Error: ", err);
      this.loaderView.dismissLoader();
    });
  }
}

import { Component } from '@angular/core';
import { IonicModule, NavController, NavParams } from '@ionic/angular';
import { ViewsurveyPage } from '../viewsurvey/viewsurvey';
import { Router } from '@angular/router';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { GlobalVars } from 'src/service/globalvars';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
/**
 * Generated class for the UpdatesurveyPage page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@Component({

  templateUrl: 'updatesurvey.html',

  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class UpdatesurveyPage {
  updateSurvey: { survey_id: any, societyName: any, societyAdd: any, Landmark: any, Area: any, locality: any, pincode: any, email: any, no_units: any, BillingCycle: any, managerName: any, managerContact: any, secretaryName: any, secContact: any, service_amount: any, cycle: any, portalName: any, charges: any, note: any, pin_code: any };
  details: {};
  message: string;
  area_Array: Array<any>;
  Locality_array: Array<any>;
  constructor(public navCtrl: NavController,
    public router: Router,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private loaderView: LoaderView
  ) {

    this.updateSurvey = { survey_id: "", societyName: "", societyAdd: "", Landmark: "", Area: "", locality: "", pincode: "", email: "", no_units: "", BillingCycle: "", managerName: "", managerContact: "", secretaryName: "", secContact: "", service_amount: "", cycle: "", portalName: "", charges: "", note: "", pin_code: '' };
    this.details = [];
    this.area_Array = [];
    this.Locality_array = [];
  }

  displayData(details) {
    this.details = details;
    console.log("update", this.details);
    this.updateSurvey.survey_id = this.details['id'];
    this.updateSurvey.societyName = this.details['society_name'];
    this.updateSurvey.societyAdd = this.details['society_add'];
    this.updateSurvey.Landmark = this.details['landmark'];
    this.updateSurvey.Area = this.details['area_id'];
    // alert(this.updateSurvey.Area);
    this.updateSurvey.locality = this.details['locality_id'];

    if (this.details['Pincode1'] != undefined) {
      this.updateSurvey.pincode = this.details['Pincode1'];
    }
    else {
      this.updateSurvey.pincode = "";
    }
    this.updateSurvey.email = this.details['society_email'];
    this.updateSurvey.no_units = this.details['no_of_units'];

    this.updateSurvey.BillingCycle = this.details['billingCycle'];
    this.updateSurvey.managerName = this.details['manager_name'];
    this.updateSurvey.managerContact = this.details['manager_contact_no'];
    this.updateSurvey.secretaryName = this.details['secretory_name']
    this.updateSurvey.secContact = this.details['sec_contact_no'];

    this.updateSurvey.service_amount = this.details['ac_service_amount'];
    this.updateSurvey.cycle = this.details['per_cycle'];
    this.updateSurvey.portalName = this.details['name_of_portal'];
    this.updateSurvey.charges = this.details['paying_charges']
    this.updateSurvey.note = this.details['notes'];
    console.log(this.details);

  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad UpdatesurveyPage');
    // this.displayData(this.navParams.get("details"));

    let data_details: any;
    data_details = this.router.getCurrentNavigation()?.extras?.state;
    this.displayData(data_details.details);
    this.GetArea();
  }

  GetArea() {
    var objData = [];
    this.loaderView.showLoader('Loading');
    this.connectServer.getData("FeedbackServlet/fetcharea", objData).then
      (
        resolve => {
          this.loaderView.dismissLoader();

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
    objData['areaid'] = this.updateSurvey['Area'];
    // alert(objData['areaid']);
    // this.loaderView.showLoader('Loading ...');
    this.connectServer.getData("FeedbackServlet/fetchlocality", objData).then(
      resolve => {
        // this.loaderView.dismissLoader();
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
    objData['localityid'] = this.updateSurvey['locality'];
    // alert(objData['areaid']);
    // this.loaderView.showLoader('Loading ...');
    this.connectServer.getData("FeedbackServlet/fetchpincode", objData).then(
      resolve => {
        // this.loaderView.dismissLoader();
        if (resolve['success'] == 1) {

          var localityPinCodeList = resolve['response']['localityDetails'];

          this.updateSurvey['pin_code'] = localityPinCodeList[0]['pincode'];

        }
        else {

        }
      }
    );
  }
  Update() {

    this.connectServer.getData("FeedbackServlet/Update", this.updateSurvey).then(
      resolve => {
        //this.loaderView.dismissLoader();
        console.log('Response : ' + resolve);
        if (resolve['success'] == 1) {
          this.message = resolve['response']['message'];
          alert("Record Updatetd successfully");
          this.navCtrl.navigateRoot("ViewsurveyPage");
        }
        else {
          this.message = resolve['message'];
        }
      }
    );
  }
}

//}

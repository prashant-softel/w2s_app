import { Component } from "@angular/core";
import { IonicModule, NavController, NavParams } from "@ionic/angular";
// import { DatePicker } from "@ionic-native/date-picker";
// import { Geolocation } from "@ionic-native/geolocation";

import { UpdatesurveyPage } from "../updatesurvey/updatesurvey";

import * as _ from "lodash";
import { Router } from "@angular/router";
import { GlobalVars } from "src/service/globalvars";
import { ConnectServer } from "src/service/connectserver";
import { LoaderView } from "src/service/loaderview";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
// import moment from "moment";

@Component({
  templateUrl: "surveyhistory.html",

  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class SurveyhistoryPage {
  displayData = {};
  SurveyDetails = {};
  society_name: any;
  society_address: any;
  landmark: any;
  area: any;
  locality: any;
  pin_code: any;
  no_of_unit: any;
  billing_cycle: any;
  manager_name: any;
  manager_contact: any;
  secretory_name: any;
  secretory_contact: any;
  amount: any;
  per_cycle: any;
  portal_name: any;
  payingcharges: any;
  reportedby: any;
  date: any;
  email: any;
  addcomment: any;
  message: string;
  todayDate: any;
  selectedDate: any;
  sr_history_details: Array<any>;
  addreminder: any;
  username: any;
  latitude: number;
  longitude: number;
  loginId: number;
  area_id: any;
  NewComment: {
    id: any;
    surveyid: any;
    reminder_date: any;
    note: any;
    reminder_time: any;
    reminder_note: any;
    isset: any;
    status: any;
  };
  constructor(
    public navCtrl: NavController,
    public navParams: NavParams,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private loaderView: LoaderView,
    // private geolocation: Geolocation,
    private router: Router
    // private datePicker: DatePicker
  ) {
    this.SurveyDetails = [];
    this.society_name = "";
    this.loginId = 0;
    this.latitude = 0;
    this.longitude = 0;
    this.society_address = "";
    this.landmark = "";
    this.area = "";
    this.area_id = "";
    this.locality = "";
    this.pin_code = "";
    this.no_of_unit = "";
    this.billing_cycle = "";
    this.manager_name = "";
    this.manager_contact = "";
    this.secretory_name = "";
    this.secretory_contact = "";
    this.amount = "";
    this.per_cycle = "";
    this.portal_name = "";
    this.payingcharges = "";
    this.reportedby = "";
    this.date = "";
    this.email = "";
    this.addcomment = 0;
    this.addreminder = 0;
    this.todayDate = new Date().toISOString();
    this.sr_history_details = [];
    this.selectedDate = null;
    this.username = this.globalVars.USER_NAME;
    this.NewComment = {
      id: 0,
      surveyid: 0,
      note: "",
      reminder_date: "",
      reminder_note: "",
      reminder_time: "",
      isset: "",
      status: ""
    };
    // this.displayData = navParams.get("details");
    let data_details: any;
    data_details = this.router.getCurrentNavigation()?.extras?.state;
    this.displayData = data_details.details;

    this.loginId = this.displayData["loginID"];

    console.log("Display Data", this.displayData);


  }

  ionViewDidLoad() {
    console.log("ionViewDidLoad SurveyhistoryPage");
    this.fetchSurveyHistory();
  }

  fetchSurveyHistory() {
    this.loaderView.showLoader("Loading ...");
    var objData = [];

    objData["surveyID"] = this.displayData["id"];
    this.connectServer
      .getData("FeedbackServlet/details", objData)
      .then(resolve => {
        this.loaderView.dismissLoader();
        if (resolve["success"] == 1) {
          //console.log(resolve);
          //console.log("SUCCESS");
          var surveyHistoryDetails = resolve["response"]["surveyDetails"][0];
          this.SurveyDetails = surveyHistoryDetails;
          this.society_name = this.SurveyDetails["society_name"];
          this.society_address = this.SurveyDetails["society_add"];
          this.landmark = this.SurveyDetails["landmark"];
          this.area = this.SurveyDetails["Area1"];
          this.locality = this.SurveyDetails["Locality"];
          this.pin_code = this.SurveyDetails["Pincode1"];
          this.no_of_unit = this.SurveyDetails["no_of_units"];
          this.billing_cycle = this.SurveyDetails["billingCycle"];
          this.manager_name = this.SurveyDetails["manager_name"];
          this.manager_contact = this.SurveyDetails["manager_contact_no"];
          this.secretory_name = this.SurveyDetails["secretory_name"];
          this.secretory_contact = this.SurveyDetails["sec_contact_no"];
          this.amount = this.SurveyDetails["ac_service_amount"];
          this.per_cycle = this.SurveyDetails["per_cycle"];
          this.portal_name = this.SurveyDetails["name_of_portal"];
          this.payingcharges = this.SurveyDetails["paying_charges"];
          this.reportedby = this.SurveyDetails["LoginName"];
          this.date = this.SurveyDetails["UpdatedTimestamp"];
          this.email = this.SurveyDetails["society_email"];
          this.area_id = this.SurveyDetails["area_id"];
          this.NewComment.surveyid = this.SurveyDetails["id"];

          this.loginId = this.SurveyDetails["login_id"];
          var reminderHistoryDetails = resolve["response"]["activityDetails"];
          // this.NewComment.
          //document.getElementById("additional_text").innerHTML = this.description;

          //console.log("Reminder Histroy:", reminderHistoryDetails);

          for (
            var iCnt = 0;
            iCnt < Object.keys(reminderHistoryDetails).length;
            iCnt++
          ) {
            this.sr_history_details.push(reminderHistoryDetails[iCnt]);
          }

          //console.log(this.sr_history_details);
          this.sr_history_details = _.orderBy(
            this.sr_history_details,
            reminder => {
              return 'moment(reminder.timestamp).format("LLL") need to';
            },
            ["desc"]
          );
        } else {
          console.log("FAIL");
        }
      });
  }
  updateSurvey() {
    //console.log(this.displayData);

    this.navCtrl.navigateForward("UpdatesurveyPage", { state: { details: this.displayData } });
  }

  addComment() {
    this.addcomment = 1;
  }

  addReminder() {
    this.addreminder = 1;
  }

  setStatus(noteId) {
    this.NewComment.status = true;
    var objData = [];
    objData["id"] = noteId;
    objData["status"] = 1;

    this.connectServer
      .getData("FeedbackServlet/status", objData)
      .then(resolve => {
        // console.log("Response : " + resolve);
        if (resolve["success"] == 1) {
          alert("Added Successfully!");
          // this.fetchSurveyHistory();
        } else {
          alert("Something went wrong! Please try again after some time!");
        }
      });
  }

  sendLocationToDatabase(flag) {
    console.log("location:", this.longitude, this.loginId, this.latitude);
    var objData = {
      login_id: this.loginId,
      lat: this.latitude ?? 40.3,
      lng: this.longitude ?? 43.3,
      flag
    };

    this.connectServer
      .getData("FeedbackServlet/AddLocation", objData)
      .then(resolve => {
        this.loaderView.dismissLoader();
        console.log(resolve);
        if (resolve["success"] == 1) {
          console.log("Location added successfully");
        } else {
          console.log("FAIL");
        }
      })
      .catch(err => {
        console.log("Error: ", err);
      });
  }

  /*submitComment(id) {
    var objData = {
      survey_id: id,
      comment: this.NewComment.note
    };
    this.message = "";
    if (this.NewComment.note.length == 0) {
      this.message = "Please enter comment";
    } else {
      this.connectServer
        .getData("FeedbackServlet/addComment", objData)
        .then(resolve => {
          console.log("Response : " + resolve);
          if (resolve["success"] == 1) {
            alert("Added Successfully!");
            this.addcomment = 0;
            // this.fetchSurveyHistory();
            this.sendLocationToDatabase(3);
          } else {
            alert("Something went wrong! Please try again after some time!");
          }
        });
    }
  }*/


  submit() {
    this.message = "";
    if (this.NewComment.reminder_date.length != 0 && this.NewComment.reminder_note.length == 0) {
      alert("Please enter note");

    }
    else if (this.NewComment.reminder_date.length != 0 && this.NewComment.reminder_time.length == 0) {
      alert("Please select time ");
    }
    else {
      // alert("2");
      this.connectServer.getData("FeedbackServlet/activity_insert", this.NewComment).then(
        resolve => {
          console.log("Response : " + resolve);
          if (resolve["success"] == 1) {
            this.message = resolve["response"]["message"];
            alert("Added Successfully!");
            //this.sendLocationToDatabase(2);
            this.addcomment = 0;
            // this.navCtrl.setRoot(Survey);
          }
          else {
            this.message = resolve["message"];
            alert("Something went wrong! Please try again after some time!");
          }
        })
    }

  }

  cancelComment() {
    this.addcomment = 0;
  }
}

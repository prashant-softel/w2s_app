import { Component } from '@angular/core';
import { IonicModule, NavController, NavParams } from '@ionic/angular';


import { SurveyhistoryPage } from '../surveyhistory/surveyhistory';

import * as _ from 'lodash';
import { Router } from '@angular/router';
import { GlobalVars } from 'src/service/globalvars';
import { ConnectServer } from 'src/service/connectserver';
import { LoaderView } from 'src/service/loaderview';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
// import moment from 'moment';

@Component({
  selector: 'page-viewreminders',
  templateUrl: 'viewreminders.html',

  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})

export class ViewRemindersPage {

  reminders: Array<any>;
  completedReminders: Array<any>;
  remainingReminders: Array<any>;
  notCompletedReminders: Array<any>;

  completedShow: Array<any>;
  remainingShow: Array<any>;
  notCompletedShow: Array<any>;

  todayDate: any;
  loginID: number;
  data: any;
  tab: string;

  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private loaderView: LoaderView,
    private router: Router
  ) {

    this.todayDate = new Date().toISOString();
    // this.data = navParams.get("details");
    let data_details: any;
    data_details = this.router.getCurrentNavigation()?.extras?.state;
    this.data = data_details.details;
    // console.log(this.data);
    this.loginID = this.data['loginID'];
    // console.log(typeof(this.loginID), this.loginID);

    this.reminders = [];
    this.completedReminders = [];
    this.remainingReminders = [];

    this.completedShow = [];
    this.remainingShow = [];
    this.notCompletedShow = [];
  }

  ionViewDidLoad() {
    this.tab = "remaining";
    console.log('ionViewDidLoad ViewRemindersPage');
    this.fetchReminders();
    this.remaining();
  }

  callSocietyPage(page) {
    var details = page;
    details['id'] = page['surveyid']
    this.navCtrl.navigateForward("SurveyhistoryPage", { state: { details } });
  }

  fetchReminders() {
    // this.loaderView.showLoader('Loading ...');
    var objData = { login_id: this.loginID };

    this.connectServer.getData("FeedbackServlet/activitydetails", objData).then
      (
        resolve => {
          this.loaderView.dismissLoader();
          console.log(resolve);
          if (resolve['success'] == 1) {
            var reminderHistoryDetails = resolve['response']['activityDetails'];

            for (var i = Object.keys(reminderHistoryDetails).length - 1; i >= 0; i--) {
              this.reminders.push(reminderHistoryDetails[i]);
            }

            console.log(this.reminders);

            for (let i = 0; i < this.reminders.length; i++) {
              this.reminders[i]['reminder_date'] = 'nee to'; //moment(this.reminders[i]['reminder_date']).format('MMM Do YY');
              if (this.reminders[i]['status'] === 0) {


                this.remainingReminders.push(this.reminders[i]);
              }
              else if (this.reminders[i]['status'] === 1) {
                //this.reminders[i]['reminder_date'] = moment(this.reminders[i]['reminder_date']).format('MMM Do YY');
                this.completedReminders.push(this.reminders[i]);
              }

              // if (this.reminders[i]['reminder_date'] < this.todayDate)
              // {
              //   this.notCompletedReminders.push(this.reminders[i]);
              // }
            }

            this.remainingReminders = _.orderBy(this.remainingReminders, (reminder) => {
              return 'need to';//moment(reminder.reminder_date);
            }, ['asc']);

            this.completedReminders = _.orderBy(this.completedReminders, (reminder) => {
              return 'need to'; //moment(reminder.reminder_date);
            }, ['asc']);

            console.log(this.remainingReminders);
            console.log(this.completedReminders);
          }
          else {
            console.log("FAIL")
          }
        }
      ).catch((err) => {
        console.log("Error: ", err);
        this.loaderView.dismissLoader();
      });
  }

  completed() {
    this.completedShow = this.completedReminders;
  }

  remaining() {
    this.remainingShow = this.remainingReminders;
  }



}

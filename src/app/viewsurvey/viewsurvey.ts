import { Component } from '@angular/core';
import { IonicModule, NavController, NavParams } from '@ionic/angular';
import { SurveyhistoryPage } from '../surveyhistory/surveyhistory';
import { GlobalVars } from 'src/service/globalvars';
import { ConnectServer } from 'src/service/connectserver';
import { LoaderView } from 'src/service/loaderview';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
/**
 * Generated class for the ViewsurveyPage page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */

@Component({

  templateUrl: 'viewsurvey.html',

  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ViewsurveyPage {
  surveyList_array: Array<any>;
  temp_surveyList_array: Array<any>;
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private loaderView: LoaderView
  ) {
    this.surveyList_array = [];
    this.temp_surveyList_array = [];
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad ViewsurveyPage');
    this.fetchSurveyList();
  }

  fetchSurveyList() {
    this.loaderView.showLoader('Loading ...');
    var objData = [];
    // objData['fetch'] = "surveyList";
    this.connectServer.getData("FeedbackServlet/check", objData).then(
      resolve => {
        //this.fetchDocumentsList();
        this.loaderView.dismissLoader();
        if (resolve['success'] == 1) {
          console.log(resolve);
          var allList = resolve['response']['surveylist'];
          for (var iCnt = 0; iCnt < Object.keys(allList).length; iCnt++) {
            this.surveyList_array.push(allList[iCnt]);
            this.temp_surveyList_array = this.surveyList_array;
          }
        }
      }
    );

  }
  getItems1(ev: any) {
    this.temp_surveyList_array = this.surveyList_array;
    let val = ev.target.value;

    if (val && val.trim() != '') {
      this.temp_surveyList_array = this.surveyList_array.filter(
        (p) => {
          let name: any = p;
          if (name.society_name.toLowerCase().indexOf(val.toLowerCase()) > -1) {
            return (name.society_name.toLowerCase().indexOf(val.toLowerCase()) > -1);
          }
          return null;

        }
      );
    }
  }
  viewSurveDetails(p) {
    this.navCtrl.navigateForward("SurveyhistoryPage", { state: { details: p } });
  }
}

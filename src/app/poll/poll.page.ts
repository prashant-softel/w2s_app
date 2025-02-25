import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { GlobalVars } from 'src/service/globalvars';
import { ConnectServer } from 'src/service/connectserver';
import { LoaderView } from 'src/service/loaderview';

@Component({
  selector: 'app-poll',
  templateUrl: './poll.page.html',
  styleUrls: ['./poll.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class PollPage implements OnInit {
  TakepollPage: any = 'takepoll';
  tab: string = "active";
  message1: string;
  message2: string;
  active_polls_array: Array<any>;
  inactive_polls_array: Array<any>;
  temp_active_polls_array: Array<any>;
  temp_inactive_polls_array: Array<any>;
  active_polls_array_loaded: number;
  inactive_polls_array_loaded: number;
  pollmessage: any;
  displayData = {};
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) {
    this.active_polls_array = [];
    this.inactive_polls_array = [];
    this.temp_active_polls_array = [];
    this.temp_inactive_polls_array = [];
    this.active_polls_array_loaded = 0;
    this.inactive_polls_array_loaded = 0;
    this.message1 = "";
    this.message2 = "";
    this.pollmessage = "";
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
    console.log('ionViewDidLoad PollPage');
    let details: any;
    this.route.queryParams.subscribe(params => {
      details = params["details"];

    });
    this.displayData = details;//this.navParams.get("details");
    console.log(this.displayData);
    if (this.displayData['dash'] == "society") {
      this.fetchActive();
      this.pollmessage = 1;
    }
    else {
      this.pollmessage = 2;
    }
  }

  fetchActive() {
    this.message1 = "";
    if (!this.active_polls_array_loaded) {
      var obj = [];
      obj['fetch'] = 0;
      this.connectServer.getData("Polls", obj).then(
        resolve => {
          this.loaderView.dismissLoader();
          if (resolve['success'] == 1) {
            var ActivePollList = resolve['response']['vote'];
            for (var i = 0; i < ActivePollList.length; i++) {
              this.active_polls_array.push(ActivePollList[i])
              this.temp_active_polls_array = this.active_polls_array;
              this.active_polls_array_loaded = 1;
            }
          }
          else {
            this.active_polls_array_loaded = 1;
            this.message1 = "Cannot find Active Polls";
          }
        }
      );
    }
  }

  fetchClose() {
    this.message2 = "";
    if (!this.inactive_polls_array_loaded) {
      var obj = [];
      obj['fetch'] = 1;
      this.connectServer.getData("Polls", obj).then(
        resolve => {
          this.loaderView.dismissLoader();
          if (resolve['success'] == 1) {
            var InactivePolls = resolve['response']['vote'];
            for (var iCnt = 0; iCnt < InactivePolls.length; iCnt++) {
              this.inactive_polls_array.push(InactivePolls[iCnt]);
              this.temp_inactive_polls_array = this.inactive_polls_array;
              this.inactive_polls_array_loaded = 1;
            }
          }
          else {
            this.message2 = "Cannot find Inactive Polls";
            //this.inactive_polls_array_loaded = 1;	
          }
        }
      );
    }
  }
  getItems1(ev: any) {
    this.temp_active_polls_array = this.active_polls_array;
    let val = ev.target.value;
    if (val && val.trim() != '') {
      this.temp_active_polls_array = this.active_polls_array.filter(
        (p) => {
          let name: any = p;
          if (name.question.toLowerCase().indexOf(val.toLowerCase()) > -1) { return (name.question.toLowerCase().indexOf(val.toLowerCase()) > -1); }
          return null;
        }
      );
    }
  }

  getItems2(ev: any) {
    this.temp_inactive_polls_array = this.inactive_polls_array;
    let val = ev.target.value;
    if (val && val.trim() != '') {
      this.temp_inactive_polls_array = this.inactive_polls_array.filter(
        (p) => {
          let name: any = p;
          if (name.question.toLowerCase().indexOf(val.toLowerCase()) > -1) { return (name.question.toLowerCase().indexOf(val.toLowerCase()) > -1); }
          return null;
        }
      );
    }
  }

  takepoll(p) {
    if (this.tab == "active") {
      p['active'] = true;
    }
    else {
      p['active'] = false;
    }
    let navigationExtras: NavigationExtras = {
      queryParams:
      {
        details: p,
      }
    };
    this.navCtrl.navigateForward(this.TakepollPage, { state: { details: p } });

    //this.navCtrl.navigateRoot(this.ProviderDetailsPage,navigationExtras);
    // this.navCtrl.navigateRoot(this.TakepollPage, navigationExtras);
  }
}

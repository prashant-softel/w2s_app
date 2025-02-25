import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { AddclassifiedPage } from '../addclassified/addclassified';

@Component({
  selector: 'app-classifieds',
  templateUrl: './classifieds.page.html',
  styleUrls: ['./classifieds.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ClassifiedsPage implements OnInit {
  ClassifiedsdetailsPage: any = 'classifiedsdetails';
  AddclassifiedPage: any = 'addclassified';
  tab: string;
  img: string;
  message: string = "ADD remove classified";
  selected: number = 0;
  pending: number = 0;
  classifieds_array: Array<any>;
  temp_classifieds_array: Array<any>;
  temp_pending_classifieds_array: Array<any>;
  pending_classifieds_array: Array<any>;
  classifieds_array_loaded: number;
  image_array: Array<any>;
  image_array2: Array<any>;
  img_src: string;
  role: any;
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) {
    this.tab = "active";
    this.img = "";
    this.classifieds_array = [];
    this.temp_classifieds_array = [];
    this.pending_classifieds_array = [];
    this.temp_pending_classifieds_array = [];
    this.classifieds_array_loaded = 0;
    this.image_array = [];
    this.image_array2 = [];
    this.img_src = "https://way2society.com/ads/";
    this.role = "";
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
    this.role = this.globalVars.MAP_USER_ROLE;
    if (this.globalVars.MAP_USER_ROLE == "Admin Member" || this.globalVars.MAP_USER_ROLE == "Admin" || this.globalVars.MAP_USER_ROLE == "Super Admin") {
      this.selected = 2;
    }
    else {
      this.selected = 0;
    }
    console.log("selected :", this.selected);
    this.fetchData();
  }
  fetchData() {
    var obj = [];
    obj['fetch'] = "classifieds";
    this.loaderView.showLoader('Loading');
    this.connectServer.getData("Classifieds", obj).then(
      resolve => {
        this.loaderView.dismissLoader();
        console.log(resolve);
        if (resolve['success'] == 1) {
          var classifiedList = resolve['response']['classified'];
          this.temp_classifieds_array = [];
          this.classifieds_array = [];
          for (var i = 0; i < classifiedList.length; i++) {
            if (classifiedList[i]['IsClassifiedExpired'] == 0 && classifiedList[i]['active'] == 1) {
              this.classifieds_array.push(classifiedList[i]);
              this.temp_classifieds_array = this.classifieds_array;
            }
          }
          console.log(this.temp_classifieds_array);
          this.classifieds_array_loaded = 1;
          for (var i = 0; i < this.temp_classifieds_array.length; i++) {
            this.image_array = this.temp_classifieds_array[i]['img'].split(",");
            this.temp_classifieds_array[i]['img_array'] = [];
            this.temp_classifieds_array[i]['img_array1'] = "";
            for (var j = 0; j < this.image_array.length; j++) {
              this.temp_classifieds_array[i]['img_array'][j] = (this.img_src) + this.image_array[j];
            }
            if (this.temp_classifieds_array[i]['img_array'][0] == this.img_src) {
              this.temp_classifieds_array[i]['img_array1'] = (this.img_src) + "Noimage.gif";
            }
            else {
              this.temp_classifieds_array[i]['img_array1'] = (this.temp_classifieds_array[i]['img_array'][0]);
            }
          }
        }
      }
    );
  }

  fetchPending() {
    console.log('PendingClassifiedsPage');
    var obj = [];
    this.loaderView.showLoader('Loading');
    obj['flag'] = "3";
    this.connectServer.getData("TenantServlet", obj).then(
      resolve => {
        this.loaderView.dismissLoader();
        console.log("in approval servlet");
        console.log(resolve);
        if (resolve['success'] == 1) {
          this.pending = 1;
          this.pending_classifieds_array = resolve['response']['classified'];
          this.temp_pending_classifieds_array = [];
          for (var i = 0; i < this.pending_classifieds_array.length; i++) {
            this.temp_pending_classifieds_array.push(this.pending_classifieds_array[i]);
          }
          console.log(this.temp_pending_classifieds_array);
          for (var i = 0; i < this.temp_pending_classifieds_array.length; i++) {
            this.image_array2 = this.temp_pending_classifieds_array[i]['img'].split(",");
            this.temp_pending_classifieds_array[i]['img_array'] = [];
            this.temp_pending_classifieds_array[i]['img_array1'] = "";
            for (var j = 0; j < this.image_array2.length; j++) {
              this.temp_pending_classifieds_array[i]['img_array'][j] = (this.img_src) + this.image_array2[j];
            }
            if (this.temp_pending_classifieds_array[i]['img_array'][0] == this.img_src) {
              this.temp_pending_classifieds_array[i]['img_array1'] = (this.img_src) + "Noimage.gif";
            }
            else {
              this.temp_pending_classifieds_array[i]['img_array1'] = (this.temp_pending_classifieds_array[i]['img_array'][0]);
            }
          }
        }
      }
    );
  }
  getMoreInfo() {
    //alert("Coming soon!");
  }
  getItems(ev: any) {
    this.temp_classifieds_array = this.classifieds_array;
    let val = ev.target.value;
    if (val && val.trim() != '') {
      this.temp_classifieds_array = this.classifieds_array.filter(
        (p) => {
          let name: any = p;
          if (name.ad_title.toLowerCase().indexOf(val.toLowerCase()) > -1) { return (name.ad_title.toLowerCase().indexOf(val.toLowerCase()) > -1); }
          return null;
          /*if(name.ad_title.toLowerCase().indexOf(val.toLowerCase()) > -1)
            return (name.ad_title.toLowerCase().indexOf(val.toLowerCase()) > -1);*/
        }
      );
    }
  }

  selectItems(p) {
    let navigationExtras: NavigationExtras = {
      queryParams:
      {
        details: p,
      }
    };
    this.navCtrl.navigateRoot(this.ClassifiedsdetailsPage, navigationExtras);
    //this.navCtrl.push(this.ClassifiedsdetailsPage,{details : p});
  }
  addClassified() {
    this.navCtrl.navigateRoot(this.AddclassifiedPage);
  }
}

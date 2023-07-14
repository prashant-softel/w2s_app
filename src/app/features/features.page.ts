import { Component, OnInit, CUSTOM_ELEMENTS_SCHEMA, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-features',
  templateUrl: './features.page.html',
  styleUrls: ['./features.page.scss'],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class FeaturesPage implements OnInit {
  ViewfeaturePage: any = 'viewfeature';
  particulars_feature: any;
  temp_Feature_All_array: Array<any>;
  particulars_previous_feature: any;
  temp_Feature_previous_array: Array<any>;
  more: any;
  note: any;
  desc: any;
  tab: string = "letest";
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) {
    this.particulars_feature = [];
    this.temp_Feature_All_array = [];
    this.particulars_previous_feature = [];
    this.temp_Feature_previous_array = [];
    this.more = '0';
    this.note = "";
    this.desc = "";
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
    this.fetchLetestFetures();
  }

  fetchLetestFetures()
  {
    this.loaderView.showLoader('Loading');
    var objData =[];
    objData['fetch'] = 7;
    this.connectServer.getData("Directory", objData).then(
      resolve => {    
            this.loaderView.dismissLoader();
            if(resolve['success'] == 1)
            {
              var FeatureList = resolve['response']['LatestFeature'];
              for(var i =0; i < FeatureList.length; i++)
              {
                if(FeatureList[i]['status'] == 1)
                  {
                    this.particulars_feature.push(FeatureList[i]);
                    this.temp_Feature_All_array= this.particulars_feature;
                  }
                  else
                  {
                      this.particulars_previous_feature.push(FeatureList[i]);
                      this.temp_Feature_previous_array=this.particulars_previous_feature;
                  }
              
              }
            }
          }
      );
  }

  ViewFeature(details)
  {
    let navigationExtras: NavigationExtras = {
      queryParams: 
      {
        details :details,
      }
    };
    this.navCtrl.navigateRoot(this.ViewfeaturePage,navigationExtras);
    //this.navCtrl.push(ViewfeaturePage, {details : details});
  }
  getItems(ev: any)
  {
    this.temp_Feature_All_array= this.particulars_feature;
    let val = ev.target.value;
    if(val && val.trim() != '')
    {
      this.temp_Feature_All_array= this.particulars_feature.filter(
      (p) => {
          let name: any = p;
          if (name.title.toLowerCase().indexOf(val.toLowerCase()) > -1) { return (name.title.toLowerCase().indexOf(val.toLowerCase()) > -1); }
          return null;
        }
      );
    }
  }

  getItems1(ev: any)
  {
    this.temp_Feature_previous_array=this.particulars_previous_feature;
    let val = ev.target.value;
    if(val && val.trim() != '')
    {
      this.temp_Feature_previous_array=this.particulars_previous_feature.filter(
      (p) => {
          let name: any = p;
          if (name.title.toLowerCase().indexOf(val.toLowerCase()) > -1) { return (name.title.toLowerCase().indexOf(val.toLowerCase()) > -1); }
          return null;
        }
      );
    }
  }
}

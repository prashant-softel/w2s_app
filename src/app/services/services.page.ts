import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform,} from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
@Component({
  selector: 'app-services',
  templateUrl: './services.page.html',
  styleUrls: ['./services.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ServicesPage implements OnInit {
  displayData : any;
  Description : any;
  OldVersion :any;
  LatestVersion:any;
  app_link : any;
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) { 
    this.displayData= [];
  	this.Description="";
    this.OldVersion= "";
    this.LatestVersion="";
    this.app_link="https://play.google.com/store/apps/details?id=com.ionicframework.way2society869487&rdid=com.ionicframework.way2society869487";
   
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
    let details:any;
    this.route.queryParams.subscribe(params => {
      details = params["details"];
        
      });
    this.displayData = details;//this.params.get("details");  
    this.OldVersion=this.displayData.Version;
    this.LatestVersion=this.globalVars.LATEST_APP_VERSION;
    this.Description = this.displayData.Desc;
    document.getElementById("notice_details").innerHTML = this.Description;
  }
  downloadApp()
  {
    window.open(this.app_link, '_blank', 'location=no'); 
    return false;
  }
}

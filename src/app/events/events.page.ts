import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-events',
  templateUrl: './events.page.html',
  styleUrls: ['./events.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class EventsPage implements OnInit {
  VieweventPage : any ='viewevent';
  particulars : any;
  particulars_active : any;
  particulars_expired : any;
  tab: string = "active";
  hasActiveEvent : any;
  hasExpiredEvent : any;
  
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) {
    this.particulars= [];
    this.particulars_active= [];
    this.particulars_expired= [];
    this.hasActiveEvent = false;
    this.hasExpiredEvent = false;
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
    var objData = [];
    this.loaderView.showLoader('Loading ...');
    this.connectServer.getData("Events", objData).then(
      resolve => { 
              this.loaderView.dismissLoader();
              if(resolve['success'] == 1)
              {
                var EventsData = resolve['response']['events'];
                for(var m = 0; m < EventsData.length; m++)
                {
                  this.particulars.push(EventsData[m]);
                  if(EventsData[m]['IsEventExpired'] == 0)
                  {
                    this.hasActiveEvent = true;
                    this.particulars_active.push(EventsData[m]);
                  }
                  else 
                  {
                    this.hasExpiredEvent = true;
                    this.particulars_expired.push(EventsData[m]);
                  }
                }
                console.log(this.particulars);
              }
              else
              {
                console.log(resolve['response']);
              } 
            }
        );
  }
  viewEvent(details)
  {
    let navigationExtras: NavigationExtras = {
      queryParams: 
      {
        details :details,
      }
    };
    this.navCtrl.navigateRoot(this.VieweventPage,navigationExtras);
    //this.navCtrl.push(VieweventPage, {details : details});
  }
}

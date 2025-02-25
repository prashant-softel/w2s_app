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
  selector: 'app-notices',
  templateUrl: './notices.page.html',
  styleUrls: ['./notices.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class NoticesPage implements OnInit {
  ViewnoticePage:any='viewnotice';
  tab: string = "active";
  hasActiveNotices : any;
  hasExpiredNotices : any;
  particulars_active : any;
  particulars_expired : any;
  role : string;
  roleWise : string;
  
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) { 
    this.particulars_active = [];
    this.particulars_expired = [];
    this.hasActiveNotices = false;
    this.hasExpiredNotices = false;
    this.role="";
    this.roleWise="";
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
    if(this.globalVars.MAP_USER_ROLE == "Admin Member"){
      this.role = "AdminMember";
    }
    else if(this.globalVars.MAP_USER_ROLE == "Super Admin")
    {
      this.role = "SuperAdmin";
    }
    else
    {
      this.role = this.globalVars.MAP_USER_ROLE;
    }
    this.roleWise=this.role;
    var objData = [];
    this.connectServer.getData("Notices", objData).then(
      resolve => { 
            if(resolve['success'] == 1)
            {
              var hasNotice = false;
              var Notices = resolve['response']['notices'];
              for(var n = 0;n < Notices.length; n++)
              {
                hasNotice = true;
                if(Notices[n]['IsNoticeExpired'] == 0)
                {   
                    this.hasActiveNotices = true; 
                    this.particulars_active.push(Notices[n]);
                }
                else
                {
                    this.hasExpiredNotices = true;
                    this.particulars_expired.push(Notices[n]);
                }
              }  
              if(!hasNotice)
              {
                //document.getElementById('msg').innerHTML = 'No Notice To Display';
              }
              console.log(resolve['response']);
            }
            else
            {
              //document.getElementById('msg').innerHTML = 'No Notice To Display';
              console.log(resolve['response']);
            } 
          }
      );
  }
  setDescription(id, text)
  {
  // alert("test : " + id + " : " + text);
    document.getElementById("desc20").innerHTML = text;
    return true;
  }

  hasLink(link)
  {
    if(link.length > 0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  openLink(link)
  {
    window.open(link, '_blank', 'location=no'); 
    return false;
  }

  viewNotice(details)
  {
    let navigationExtras: NavigationExtras = {
      queryParams: 
      {
        details :details,
      }
    };
    this.navCtrl.navigateRoot(this.ViewnoticePage,navigationExtras);
    //this.navCtrl.push(ViewnoticePage, {details : details});
  }

  addNotice()  {
        //this.navCtrl.push(AddnoticePage);
    }

}

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
//import { PhotoViewer } from '@ionic-native/photo-viewer';
@Component({
  selector: 'app-viewevent',
  templateUrl: './viewevent.page.html',
  styleUrls: ['./viewevent.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class VieweventPage implements OnInit {
  event_details : any;
	event_title : any;
	event_date : any;
	event_time : any;
	event_detail : any;
	event_link : any;
  event_attachment : any;
  event_charges: any;
  end_date: any;
  hasAttachment : any;
  hasLink : any;
  Event_ver : any;
  GdriveAttachId: any;
  /*options : InAppBrowserOptions = {
    location : 'yes',//Or 'no'
    hidden : 'no', //Or  'yes'
    clearcache : 'yes',
    clearsessioncache : 'yes',
    zoom : 'yes',//Android only ,shows browser zoom controls
    hardwareback : 'yes',
    mediaPlaybackRequiresUserAction : 'no',
    shouldPauseOnSuspend : 'no', //Android only
    closebuttoncaption : 'Close', //iOS only
    disallowoverscroll : 'no', //iOS only
    toolbar : 'yes', //iOS only
    enableViewportScale : 'no', //iOS only
    allowInlineMediaPlayback : 'no',//iOS only
    presentationstyle : 'pagesheet',//iOS only
    fullscreen : 'yes',//Windows only
};*/
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) {
    this.event_charges = 0;

    this.event_details = [];
    //this.event_details = params.get("details");
    //alert(this.event_details);
    this.hasAttachment = false;
    this.hasLink = false;
   }

  ngOnInit() {
    let details:any;
    this.route.queryParams.subscribe(params => {
      details = params["details"];
        
      });
      this.event_details=details;
      console.log(details);
    this.event_title = this.event_details.events_title;
    this.event_date = this.event_details.events_date;
    this.end_date = this.event_details.end_date;
    this.event_time = this.event_details.event_time;
    this.event_detail = this.event_details.events;
    this.event_link = this.event_details.events_url;
    this.event_attachment = this.event_details.Link;
    this.event_charges = this.event_details.event_charges;
    this.Event_ver = this.event_details.event_version;
    this.GdriveAttachId =this.event_details.attachment_gdrive_id;
    if(this.event_link.length > 0)
    {
      this.hasLink = true;
    }

    if(this.event_attachment.length > 0)
    {
      this.hasAttachment = true;
    }

    document.getElementById("event_details").innerHTML = this.event_detail;
  }

  
  public openWithSystemBrowser(){
    var url ="";
      if(this.Event_ver == 1)
      {
        
        url="https://way2society.com/W2S_DocViewer.php?url="+this.event_attachment+"&doc_version=1";
      }
      else if(this.Event_ver == 2)
      {
        if(this.GdriveAttachId == "" || this.GdriveAttachId == "-")
        {
          url="https://way2society.com/W2S_DocViewer.php?url="+this.event_attachment+"&doc_version=1";
        }
        else
        {
          url="https://way2society.com/W2S_DocViewer.php?url="+this.GdriveAttachId+"&doc_version=2";
        }
      }
        let target = "_system"
        window.open(url, '_blank', 'location=no');
      //  PhotoViewer.show(url, 'Optional Title');
        //this.theInAppBrowser.create(url,target,this.options);
   }

  openLink()
  {
    window.open(this.event_link, '_blank', 'location=no');
    return false;
  }
}

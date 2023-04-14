import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-viewnotice',
  templateUrl: './viewnotice.page.html',
  styleUrls: ['./viewnotice.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ViewnoticePage implements OnInit {
	notice_details : any;
  notice_subject : any;
  notice_date : any;
  notice_expiry : any;
  notice_description : any;
  notice_link : any;
  notice_expired : any;
  hasLink : any;
  Notice_ver : any;
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
    this.hasLink = false;
    this.notice_details = [];
   // this.notice_details = this.params.get("details");
   }

  ngOnInit() {
    let details:any;
    this.route.queryParams.subscribe(params => {
      details = params["details"];
        
      });
    this.notice_details=details;
    this.notice_subject = this.notice_details.subject;
    this.notice_date = this.notice_details.creation_date;
    this.notice_expiry = this.notice_details.exp_date;
    this.notice_description = this.notice_details.description;
    this.notice_link = this.notice_details.Link;
    this.Notice_ver = this.notice_details.notice_version;
    this.GdriveAttachId =this.notice_details.attachment_gdrive_id;
  
    if(this.notice_link.length > 0)
    {
      this.hasLink = true;
    }
    this.notice_expired = this.notice_details.IsNoticeExpired;
   // alert(this.notice_description);
    document.getElementById("notice_details").innerHTML = this.notice_description;
  }

  public openWithSystemBrowser(){
    // alert(this.Notice_ver);
     var url ="";
       if(this.Notice_ver == 1)
       {
         url="https://way2society.com/W2S_DocViewer.php?url="+this.notice_link+"&doc_version=1";
       }
       else if(this.Notice_ver == 2)
       {
         if(this.GdriveAttachId == "" || this.GdriveAttachId == "-")
         {
           url="https://way2society.com/W2S_DocViewer.php?url="+this.notice_link+"&doc_version=1";
         }
         else
         {
           url="https://way2society.com/W2S_DocViewer.php?url="+this.GdriveAttachId+"&doc_version=2";
         }
       }
         let target = "_system";
        // this.theInAppBrowser.create(url,target,this.options);
        // alert(url);
        // alert(this.theInAppBrowser);
    }
}

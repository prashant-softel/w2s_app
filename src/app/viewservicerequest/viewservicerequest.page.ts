import { Component, OnInit,CUSTOM_ELEMENTS_SCHEMA, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-viewservicerequest',
  templateUrl: './viewservicerequest.page.html',
  styleUrls: ['./viewservicerequest.page.scss'],
  standalone: true,
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ],
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ViewservicerequestPage implements OnInit {
  ServiceRequestPage:any='servicerequest';
  sr_details : Array<any>;
  sr_history_details : Array<any>;
  sr_status : any;
  userData : {status : any, summary : any, unit_id:any};
  addcomment : number;
	sr_attachment : any;
	hasAttachment : any;
	img_array:Array<any>;
  img_src="";
  folder;
  role : string;
  superwise : any;
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) { 
   
    this.sr_details = [];
    this.sr_history_details = [];
    this.sr_status = "";
    this.userData = {status : "", summary : "",unit_id : ""};
    this.img_array=[];
    this.superwise="";
    this.folder= "";
    this.role = "";
    this.img_src="https://way2society.com/upload/main/";
    this.addcomment = 0;
    this.hasAttachment = false;
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
      console.log("details",details);
      this.sr_details=details;
    //this.sr_details = params.get("details");
    this.fetchSRHistory();

    this.sr_attachment = this.sr_details['img'];
    if(this.sr_details['img'].length > 0)
    {
      this.hasAttachment = true;
    }
    document.getElementById('sr_details_id').innerHTML = this.sr_details['details'];
  }
  selectItems()
  {
    //this.loaderView.showLoader('Loading ...');
    //this.navCtrl.push(ServicerequestimageviewPage, {details: this.navParams.get("details") });
  }
  fetchSRHistory() {
    this.loaderView.showLoader('Loading ...');
    var objData = [];
    objData['fetch'] = "srhistory";
    objData['request_no'] = this.sr_details['request_no'];
    objData['category'] = this.sr_details['category_id'];
    objData['priority'] = this.sr_details['priority'];
    this.connectServer.getData("ServiceRequest", objData).then(
        resolve => {
            this.loaderView.dismissLoader();
            if(resolve['success'] == 1)
            {
              console.log(resolve['response']);
              var hasSR = false;
              var SRList = resolve['response']['sr'];
              var iLatest = 1;
              for(var n = (SRList.length - 1); n >= 0; n--)
              {
                hasSR = true;
                if(iLatest == 1)
                {
                  this.sr_status = SRList[n]['status'];
                  this.userData['status'] = SRList[n]['status'];
                  iLatest = 0;
                }
                if(n == 0)
                {
                  SRList[n]['raised_text'] ="Raised";
                }
                else
                {
                  SRList[n]['raised_text'] = "Updated";
                }
                this.sr_history_details.push(SRList[n]);
              }
              this.superwise= SRList[0]['Supervised_by'];
              if(!hasSR)
              {
                //document.getElementById('msg').innerHTML = 'No History To Display';
              }
              console.log(resolve['response']);
            }
            else
            {
              //document.getElementById('msg').innerHTML = 'No History To Display';
              console.log(resolve['response']);
            }
            if(this.globalVars.MAP_USER_ROLE == "Member")
            {
              this.role = "Member";
            }
            else if(this.globalVars.MAP_USER_ROLE == "Contractor")
            {
              this.role = "Contractor";
            }
            else
            {
              this.role = "AdminMember";
            }
          }
      );
  }
  addComment() 
  {
    this.addcomment = 1;
  }
  cancelComment() 
  {
    this.addcomment = 0;
  }
  submit() {
  	this.loaderView.showLoader('Please Wait ...');
    this.userData['set'] = "comment";
    this.userData['unit_id'] = this.sr_details['unit_id'];
    this.userData['request_no'] = this.sr_details['request_no'];
    this.connectServer.getData("ServiceRequest", this.userData).then(
        resolve => {
            this.loaderView.dismissLoader();
            console.log('Response : ' + resolve);
            if(resolve['success'] == 1)
            {
              alert(resolve['response']['message']);
              var p=[];
              if(this.role == "AdminMember")
              {
                p['dash']="admin";
                console.log(p);
              }
              else if(this.role == "Contractor")
              {
                p['dash']="contractor";
              }
              else
              {
                p['dash']="society";
              }
              let navigationExtras: NavigationExtras = {
                queryParams: 
                {
                  details :p,
                }
              };
              this.navCtrl.navigateRoot(this.ServiceRequestPage,navigationExtras);
              //this.navCtrl.setRoot(this.ServiceRequestPage,{details : p});
            }
            else
            {
              //this.message = resolve['response']['message'];
            }
          }
      );
  }
  openAttachment()
  {
    window.open(this.img_src + this.sr_details['img'] , '_blank', 'location=no');
    //return false;
  }
}

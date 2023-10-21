import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform, AlertController } from '@ionic/angular';

import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { GlobalVars } from 'src/service/globalvars';
import { ConnectServer } from 'src/service/connectserver';
import { LoaderView } from 'src/service/loaderview';

enum statusEnum { "Pending" = 0, "Active", "Need Information", "Rejected" }
@Component({
  selector: 'app-tenantsdetails',
  templateUrl: './tenantsdetails.page.html',
  styleUrls: ['./tenantsdetails.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class TenantsdetailsPage implements OnInit {
  TenantsPage: any = 'tenants';
  message: string = "";
  tenant: any;
  status: any;
  admin: number = 0;
  tenant_note: string = "";
  info: any;
  profile_flag: any;
  userInfo: { comment: any };
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    public alertCtrl: AlertController,
    private params: NavParams,
    private route: ActivatedRoute) {
    this.tenant = [];

    this.status = statusEnum;
    this.info = '0';
    this.userInfo = { comment: "" }
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
    let details: any;
    this.route.queryParams.subscribe(params => {
      details = params["details"];

    });
    this.tenant = details;//this.navParams.get("details");
    if (this.tenant.hasOwnProperty('note')) {
      this.tenant_note = this.tenant['note'];
      this.tenant_note = this.tenant_note.replace(new RegExp('<p>', 'g'), '');
      this.tenant_note = this.tenant_note.replace(new RegExp('<div>', 'g'), '');
      this.tenant_note = this.tenant_note.replace(new RegExp('</p>', 'g'), '');
      this.tenant_note = this.tenant_note.replace(new RegExp('</div>', 'g'), '');
    }

    if (this.globalVars.MAP_USER_ROLE == "Admin") {
      this.profile_flag = this.globalVars.PROFILE_SERVICE_PROVIDER;
    }
    else if (this.globalVars.MAP_USER_ROLE == "Admin Member") {
      this.profile_flag = this.globalVars.PROFILE_SERVICE_PROVIDER;
    }
    else {
      this.profile_flag = 2;
    }

    //console.log(this.navParams.get("details")+"  desc : "+this.tenant_note);
    this.admin = this.tenant['admin'];
    console.log(JSON.stringify(this.tenant) + " & admin : " + this.admin);
  }

  confirmApprove(p) {
    let alert = this.alertCtrl.create({
      //title: "Approve this Tenant?',
      message: '',
      buttons: [
        {
          text: 'Cancel',
          role: 'cancel',
          handler: () => {
            console.log('Cancel clicked');
          }
        },
        {
          text: 'Approve',
          handler: () => {
            this.ApproveIt(p);
          }
        }
      ]
    });
    //alert.present();
  }

  confirmRemove(p) {
    let alert = this.alertCtrl.create({
      ///title: 'Remove this Tenant?',
      message: '',
      buttons: [
        {
          text: 'Cancel',
          role: 'cancel',
          handler: () => {
            console.log('Cancel clicked');
          }
        },
        {
          text: 'Remove',
          handler: () => {
            this.RemoveIt(p);
          }
        }
      ]
    });
    //alert.present();
  }

  ApproveIt(p) {
    var objData = [];
    objData['flag'] = "2";
    objData['tenant_id'] = p;
    this.connectServer.getData("TenantServlet", objData).then(
      resolve => {
        console.log('Response : ' + resolve);
        if (resolve['success'] == 1) {
          alert("Tenant Approved successfully ");
          var p = [];
          p['dash'] = "admin";
          let navigationExtras: NavigationExtras = {
            queryParams:
            {
              details: p,
            }
          };
          //this.navCtrl.navigateRoot(this.TenantsPage,navigationExtras);
          this.navCtrl.navigateRoot(this.TenantsPage, navigationExtras);
          //this.navCtrl.setRoot(this.TenantsPage,{details : p});
        }
        else {
          this.message = resolve['response']['message'];
        }
      }
    ).catch((e) => console.log(e));
  }
  getInfo() {
    this.info = '1';
  }
  cancle() {
    this.info = '0';
  }

  getMoreInfo(tenant_id) {
    var objData = [];
    objData['flag'] = "7";
    objData['tenant_id'] = tenant_id;
    objData['comment'] = this.userInfo['comment'];
    this.loaderView.showLoader('Loading ...');
    this.connectServer.getData("TenantServlet", objData).then(
      resolve => {
        this.loaderView.dismissLoader();
        console.log('Response : ' + resolve);
        if (resolve['success'] == 1) {
          alert("Comment added successfully");
          var p = [];
          p['dash'] = "admin";
          let navigationExtras: NavigationExtras = {
            queryParams:
            {
              details: p,
            }
          };
          this.navCtrl.navigateRoot(this.TenantsPage, navigationExtras);
          //this.navCtrl.setRoot(TenantsPage,{details : p});
        }
        else {
          this.message = resolve['response']['message'];
        }
      }
    ).catch((e) => console.log(e));
  }

  RemoveIt(p) {
  }
}

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
  selector: 'app-classifiedsdetails',
  templateUrl: './classifiedsdetails.page.html',
  styleUrls: ['./classifiedsdetails.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ClassifiedsdetailsPage implements OnInit {
  ClassifiedsPage:any='classifieds';
  ad_title :string;
  desp :string;
  exp_date :string;
  img :string;
  location :string;
  email :string;
  phone :string;
  category_name :string;
  post_date :string;
  id :string;
  profile_flag : any;
  active : any;
  alertCtrl: any;
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) { }

  @HostListener('document:ionBackButton', ['$event'])
  overrideHardwareBackAction(event: any) {
    event.detail.register(100, async () => {
      event.stopImmediatePropagation();
      event.stopPropagation();
      event.preventDefault();
    });
  }
  ngOnInit() {
    if(this.globalVars.MAP_USER_ROLE == "Super Admin")
    {
      this.profile_flag = 2
    }
    else
    {
      this.profile_flag = this.globalVars.APPROVALS_CLASSIFIED;
    }
    let details:any;
    this.route.queryParams.subscribe(params => {
      details = params["details"];
        
      });
    this.displayData(details);
  }

  displayData(details)
  {
    this.ad_title = details['ad_title'];	
    this.desp = details['desp'];
    this.exp_date = details['exp_date'];
    this.location = details['location'];
    this.email = details['email'];
    this.phone = details['phone'];
    this.category_name = details['category_name'];
    this.post_date=details['post_date'];
    this.id = details['id'];
    this.img=details['img_array1'];
    this.active=details['active'];
   document.getElementById("desp1").innerHTML=this.desp;
 }
 selectItems()
 {
    //this.navCtrl.push(ClassifiedsimageviewPage, {details: this.navParams.get("details") });
 }
   


confirmApprove(p) {
  let alert = this.alertCtrl.create({
  title: 'Approve this Ad?',
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
alert.present();
}

ApproveIt(p)
{
  var objData=[];
  objData['flag'] = "4"; 
  objData['id'] = p;   
  this.connectServer.getData("TenantServlet",  objData).then(
    resolve => {
      if(resolve['success'] == 1)
      {
        alert("Classified Approved successfully ");
        var p=[];
        p['tab']='1';
        p['dash']= "admin";
        let navigationExtras: NavigationExtras = {
          queryParams: 
          {
            details :p,
          }
        };
        this.navCtrl.navigateRoot(this.ClassifiedsPage,navigationExtras);
        //this.navCtrl.setRoot(this.ClassifiedsPage , {details :p});
      }
      else
      {
      }
    }
  );
}

confirmRemove(p) {
  let alert = this.alertCtrl.create({
  title: 'Reject this ad?',
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
alert.present();
}
RemoveIt(p)
{
  var objData=[];
  objData['flag'] = "8"; 
  objData['id'] = p;   
  this.connectServer.getData("TenantServlet",  objData).then(
    resolve => {
      //this.loaderView.dismissLoader();
      console.log('Response : ' + resolve);
      if(resolve['success'] == 1)
      {
        alert("Classified Reject successfully ");
        var p=[];
        p['tab']='1';
        p['dash']= "admin";
        let navigationExtras: NavigationExtras = {
          queryParams: 
          {
            details :p,
          }
        };
        this.navCtrl.navigateRoot(this.ClassifiedsPage,navigationExtras);
        //this.navCtrl.setRoot(ClassifiedsPage , {details :p});
      }
      else
      {
      }
    }
  );
}
}

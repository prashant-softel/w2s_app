import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams } from '@ionic/angular';
import { ConnectServer } from 'src/service/connectserver';
import { LoaderView } from 'src/service/loaderview';
import { GlobalVars } from 'src/service/globalvars';
import { ActivatedRoute, NavigationExtras } from '@angular/router';


@Component({
  selector: 'app-updateprovider',
  templateUrl: './updateprovider.page.html',
  styleUrls: ['./updateprovider.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class UpdateproviderPage implements OnInit {
  updateData :{ service_prd_id : any, provider_name : any, Cat_id:any, DateOfBirth : any, IdentyMark: any, WorkingSince : any, MarritalStatus:any,MobileNo:any,AltMobileNo :any};
  details:{};
  message : string;
  CategoryName : any;
  callForm:any;
  ProviderDetailsPage: any = "providerdetails";
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    private connectServer: ConnectServer,
    private loaderView : LoaderView,
    private route: ActivatedRoute,
    private globalVars : GlobalVars) {
    this.updateData = {service_prd_id : "",provider_name : "",Cat_id:"", DateOfBirth : "",IdentyMark:"", WorkingSince : "", MarritalStatus:"", MobileNo : "",AltMobileNo : "" };
    this.details=[];
     this.CategoryName="";
     this.callForm=""; }

  ngOnInit() {
    console.log('ionViewDidLoad UpdateproviderPage');
    let details: any ;
    this.route.queryParams.subscribe(params =>{
      details = params["details"];
    }
      );
      this.displayData = details;
      console.log(details);
      console.log(this.displayData);

    // this.displayData(this.navParams.get("details"));
  }
  displayData()
  {
    // this.details=details;
    this.CategoryName=this.details['cat'];
    console.log(this.CategoryName);
    this.callForm = this.displayData['call'];
    //alert(this.callForm);
       this.updateData.service_prd_id=this.details['service_prd_reg_id'];
       this.updateData.provider_name=this.details['full_name'];
       this.updateData.Cat_id=this.details['cat_id'];
       this.updateData.DateOfBirth=this.details['dob'];
       this.updateData.IdentyMark=this.details['identy_mark'];
         this.updateData.WorkingSince=this.details['since'];
       this.updateData.MarritalStatus=this.details['marry'];
       this.updateData.MobileNo=this.details['cur_con_1'];
       this.updateData.AltMobileNo=this.details['cur_con_2'];
       //this.updateData.UnitID=this.details['GROUP_CONCAT(su.`unit_id`)'];

    console.log(this.details);

  }
  updatPresonal()
	{

  		//var objData = [];
      	this.connectServer.getData("ServiceProvider/updatePersonal",  this.updateData).then(
        resolve => {
                //this.loaderView.dismissLoader();
            console.log('Response : ' + resolve);
                        if(resolve['success'] == 1)
                        {
                            this.message = resolve['response']['message'];
                           // this.service_prd_reg_id = resolve['response']['new_rev_id'];

                              alert("Profile details successfully update");
                              let navigationExtras: NavigationExtras = {
                                queryParams:
                                {
                                  details: this.details,
                          
                                }
                              };
                              this.navCtrl.navigateRoot(this.ProviderDetailsPage, navigationExtras);
                              // this.navCtrl.navigateRoot(ProviderDetailsPage, {details : this.details});

                        }
                        else
                        {
                            this.message = resolve['response']['message'];
                        }
                     }
        );
}
keyUpChecker(ev) {
  let elementChecker: string;
  let format = /^[a-zA-Z ]*$/i;
  elementChecker = ev.target.value;
  console.log(ev.target.value);
  if(!format.test(elementChecker)){
  this.updateData.provider_name = elementChecker.slice(0, -1);
  }
  }

}

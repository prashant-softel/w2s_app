import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
// import { Contacts, Contact, ContactField, ContactName } from '@ionic-native/contacts';

import { NavController, NavParams, ActionSheetController } from '@ionic/angular';
import { NavigationExtras } from '@angular/router';
import { GlobalVars } from 'src/service/globalvars';
import { ConnectServer } from 'src/service/connectserver';
import { LoaderView } from 'src/service/loaderview';



@Component({
  selector: 'app-expectedvisitor',
  templateUrl: './expectedvisitor.html',
  styleUrls: ['./expectedvisitor.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ExpectedvisitorPage implements OnInit {
  todayDate: any;
  Purpose_list: Array<any>;
  contactsfound = [];
  MyvisitorsPage = 'myvisitors';
  ExpVisitor: { ContactNo: any, fName: any, lName: any, Expdate: any, ExpTime: any, ExpPurpose_id: any, vNote: any };
  constructor(public globalVars: GlobalVars,
    public connectServer: ConnectServer,
    private loaderView: LoaderView,
    public navParams: NavParams,
    // private contacts: Contacts,
    private navCtrl: NavController) {
    this.todayDate = new Date().toISOString();
    this.ExpVisitor = { ContactNo: "", fName: "", lName: "", Expdate: "", ExpTime: "", ExpPurpose_id: "", vNote: "" };
    this.Purpose_list = [];
  }
  @HostListener('document:ionBackButton', ['$event'])
  overrideHardwareBackAction(event: any) {
    event.detail.register(100, async () => {
      event.stopImmediatePropagation();
      event.stopPropagation();
      event.preventDefault();
    });
  }
  ngOnInit(): void {
    console.log('ionViewDidLoad ExpectedvisitorPage');
    this.fetchPurposeList();

  }
  fetchPurposeList() {
    var objData = [];

    objData['fetch'] = 19;
    this.connectServer.getData("Directory", objData).then(
      resolve => {
        if (resolve['success'] == 1) {
          console.log(resolve);
          this.Purpose_list = [];
          var PurposeList = resolve['response']['purpose'];
          for (var i = 0; i < PurposeList.length; i++) {
            this.Purpose_list.push(PurposeList[i]);
          }
        }
      }
    );

  }

  async getContact() {
    try {
      // const SelectedContact = await this.contacts.pickContact();
      // this.ExpVisitor['ContactNo']= SelectedContact['phoneNumbers'][0]['value']; 
      // this.ExpVisitor['fName'] = SelectedContact['displayName'];
    }
    catch (e) {
      console.error(e);
    }


  }
  submit() {
    if (this.ExpVisitor.ContactNo.length < 10) {
      alert("Please enter valid number");
    }
    else if (this.ExpVisitor.fName.length == '') {
      alert("Please enter visitor name");
    }
    else if (this.ExpVisitor.Expdate.length == '') {
      alert("Please enter expected date");
    }
    else if (this.ExpVisitor.ExpTime.length == '') {
      alert("Please enter expected time");
    }
    else {
      if (this.ExpVisitor.ContactNo.length > 10) {
        this.ExpVisitor.ContactNo = this.ExpVisitor.ContactNo.substr(this.ExpVisitor.ContactNo.length - 10);
      }
      else {
        this.ExpVisitor.ContactNo = this.ExpVisitor.ContactNo;
      }
      this.loaderView.showLoader('Loading');
      var objData = [];
      this.ExpVisitor['fetch'] = 18;
      this.connectServer.getData("Directory", this.ExpVisitor).then(
        resolve => {
          this.loaderView.dismissLoader();
          if (resolve['success'] == 1) {
            alert("Visitor added successfully");
            this.navCtrl.navigateRoot(this.MyvisitorsPage);

          }
        }
      );
    }
  }

}






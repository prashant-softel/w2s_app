import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform,} from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import * as moment from 'moment';

@Component({
  selector: 'app-myvisitors',
  templateUrl: './myvisitors.page.html',
  styleUrls: ['./myvisitors.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class MyvisitorsPage implements OnInit {
  tab: string = "current";
  Visitor_Inside_Array : Array<any>;
  Visitor_Past_Array : Array<any>;
  Expected_Visitor_Array : Array<any>;
  InsideVisitor:any;
  img_src :any;
  VisitorFeature :any;
  Past_visitor_temp_array :Array<any>;
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams) {
    this.Visitor_Inside_Array=[];
  	this.Visitor_Past_Array=[];
    this.Expected_Visitor_Array=[];
    this.Past_visitor_temp_array=[];
  	this.InsideVisitor=[];
    this.VisitorFeature="0";
  	this.img_src="https://way2society.com/SecuirityApp/VisitorImage/";
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
    this.fetchCurrentVisitor();
  }
  fetchCurrentVisitor()
  {
    this.loaderView.showLoader('Loading');
    var objData =[];
    objData['fetch'] = 14;
    this.connectServer.getData("Directory", objData).then(
      resolve => {
        this.loaderView.dismissLoader();
        if(resolve['success'] == 1)
        {
          if(resolve['response']['DB'] =="1")
          { 
            this.VisitorFeature="1";
            this.Visitor_Inside_Array = [];
            var VisitorInsideList = resolve['response']['visitorinside'];
            for(var i = 0;i < VisitorInsideList.length; i++)
            {
              this.Visitor_Inside_Array[i]=VisitorInsideList[i];
              this.Visitor_Inside_Array[i]["VisitorName"]=this.Visitor_Inside_Array[i]['Visitor'][0]['FullName'];
              this.Visitor_Inside_Array[i]["VisitorImage"]=this.Visitor_Inside_Array[i]['Visitor'][0]['img'];
              console.log('Visitor in Array : ' + this.Visitor_Inside_Array[i]['Entry_flag']);
            }
          }
          else
          {
            this.VisitorFeature="0";
          }
        }
        else
        {
          this.VisitorFeature="1";
        }
      }
    );
  }
  fetchPastVisitor()
  {
    this.loaderView.showLoader('Loading');
    var objData =[];
    objData['fetch'] = 15;
    this.connectServer.getData("Directory", objData).then(
      resolve => {
        this.loaderView.dismissLoader();
        if(resolve['success'] == 1)
        {
          if(resolve['response']['DB'] =="1")
          { 
            this.VisitorFeature="1";
            this.Visitor_Past_Array=[];
            var VisitorPastList = resolve['response']['visitoroutside'];
            for(var iCnt = 0;iCnt < VisitorPastList.length;iCnt++)
            {
              this.Visitor_Past_Array.push(VisitorPastList[iCnt]);
              console.log("Past Visitor :", );
              this.Visitor_Past_Array[iCnt]["VisitorName"]=this.Visitor_Past_Array[iCnt]["Visitor"][0]['FullName'];
              this.Visitor_Past_Array[iCnt]["VisitorImage"]=this.Visitor_Past_Array[iCnt]["Visitor"][0]['img'];
              this.Visitor_Past_Array[iCnt]['VisitorInDate']=  moment(this.Visitor_Past_Array[iCnt]['otpGtimestamp']).format('ll');
            // alert(this.Visitor_Past_Array[iCnt]['VisitorInDate']);
              this.Past_visitor_temp_array =  this.Visitor_Past_Array;
            }
          }
          else
          {
            this.VisitorFeature="0";
          }
        }
        else
        {
          this.VisitorFeature="1";
        }
      }
    );
  }
  fetchExpectedVisitor()
  {
    this.loaderView.showLoader('Loading');
    var objData =[];
    objData['fetch'] = 17;
    this.connectServer.getData("Directory", objData).then(
        resolve => {
          this.loaderView.dismissLoader();
            if(resolve['success'] == 1)
             {
                if(resolve['response']['DB'] =="1")
                { 
                  this.VisitorFeature="1";
                  this.Expected_Visitor_Array=[];
                  var ExpectedVisitor = resolve['response']['visitorlist'];
                  for(var iCount = 0;iCount < ExpectedVisitor.length;iCount++)
                   {
                     this.Expected_Visitor_Array[iCount]=ExpectedVisitor[iCount];
                     this.Expected_Visitor_Array[iCount]['expected_date']=  moment(ExpectedVisitor[iCount]['expected_date']).format('ll');
                     this.Expected_Visitor_Array[iCount]['expected_time']= moment(this.Expected_Visitor_Array[iCount]['expected_time'], 'HH:mm A').format('LT');
                     
                   }
                 }
                 else
                 {
                    this.VisitorFeature="0";
                 }
              }
              else
              {
                this.VisitorFeature="1";
              }
            }
        );
 }
 VisitorDetails(p)
{
  var viewdata = [];
  viewdata = p;
  console.log('check ' + p);
  viewdata['enumvalue'] = 1;
  if(viewdata['Entry_flag'] == 0)
  {
    viewdata['enumvalue'] = 0;
  }
  //console.log(viewdata);
	//this.navCtrl.push(VisitorInPage,{details : viewdata});
}
addExpectedVisitor()
{
	//this.navCtrl.push(ExpectedvisitorPage);
}
 getItems(ev: any)
{
    this.Past_visitor_temp_array =  this.Visitor_Past_Array;
    let val = ev.target.value;
    if (val && val.trim() != '')
    {
       this.Past_visitor_temp_array =  this.Visitor_Past_Array.filter(
        (p) => {
          let name: any = p;
          if(name.VisitorName.toLowerCase().indexOf(val.toLowerCase()) > -1)
            return (name.VisitorName.toLowerCase().indexOf(val.toLowerCase()) > -1);
          }
        );
      }
  } 
}

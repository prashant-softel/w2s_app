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
  selector: 'app-viewfeature',
  templateUrl: './viewfeature.page.html',
  styleUrls: ['./viewfeature.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ViewfeaturePage implements OnInit {
  displayData : any;
  Description : any;
  Title : any;
  Images :any;
  Attachment :any;
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) { 
    this.displayData= [];
  	this.Description="";
  	this.Title="";
  	this.Images="";
  	this.Attachment="";
  	//this.displayData = this.params.get("details");
  }

  ngOnInit() {
    let details:any;
    this.route.queryParams.subscribe(params => {
      details = params["details"];
        
      });
      this.displayData=details; 
    this.Title = this.displayData.title;
    this.Images = this.displayData.advImage;
    this.Attachment = this.displayData.attachment;
    this.Description = this.displayData.description;
    document.getElementById("notice_details").innerHTML = this.Description;
  }
  viewImage(img)
  {
    var img_link ="https://way2society.com/Avd_img/"+img;	
    window.open(img_link, '_blank', 'location=no');
    return false;
  }
  attachment(attach)
  {
    var attach_link ="https://way2society.com/Avd_img/"+attach;
    window.open(attach_link, '_blank', 'location=no');
    return false;
  }
}

import { Component, OnInit,CUSTOM_ELEMENTS_SCHEMA, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform,} from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';

enum statusEnum  { "Raised" = 1,  "Waiting",  "In progress",  "Completed", "Cancelled"}
enum priorityEnum  { "Critical" = 1,  "High",  "Medium",  "Low"}
@Component({
  selector: 'app-task',
  templateUrl: './addtask.page.html',
  styleUrls: ['./addtask.page.scss'],
  standalone: true,
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ],
  imports: [IonicModule, CommonModule, FormsModule,]
})

export class AddTaskPage implements OnInit {
  userData : {title : any, desc : any, duedate : any,attachment :any, status: any, priority : any, percentcomplete : any, mapping_id : any, notifyByEmail : any};
	mem_list : Array <any>;
  max_year: any;	
	todayDate : any;
	message : string;
  options:any;
	base64Image:any;
	lastImage: string = null;
	//loading: Loading;
	task_id : any;
  TaskPage :any='task';
	
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams) { 

      this.todayDate = new Date().toISOString();		
			this.userData = { title : "", desc : "", duedate : this.todayDate, attachment :"", notifyByEmail : false, percentcomplete : 0, priority : 1, status : 1, mapping_id : 0};
	  	this.mem_list = [];
	  	this.message = "";
			this.task_id = 0;
      var d= new Date();
      this.max_year= d.getFullYear()+2;
			
		
  }

  ngOnInit() {
    this.fetchMembers();
  }
  @HostListener('document:ionBackButton', ['$event'])
  overrideHardwareBackAction(event: any) {
    event.detail.register(100, async () => {
      event.stopImmediatePropagation();
      event.stopPropagation();
      event.preventDefault();
    });
  }
  fetchMembers()
	{
		this.loaderView.showLoader('Loading ...');
    var obj = [];
    obj['flag'] = 1;
    this.connectServer.getData("AddTaskServlet", obj).then(
      resolve => {
        // console.log(obj);
        this.loaderView.dismissLoader();
        if(resolve['success'] == 1)
        {
          var memberList = resolve['response']['assignedMembers'];
          for(var i = 0; i < memberList.length; i++)
          {
            this.mem_list.push(memberList[i]);
          }
          // console.log(this.mem_list);
        }
      }
    );
	}
  createTask()
  {
    this.userData['flag'] = 2;
    this.connectServer.getData("AddTaskServlet", this.userData).then(
      resolve => {
        //this.loaderView.dismissLoader();
        console.log('Response : ' + resolve);
        if(resolve['success'] == 1)
        {
          this.message = resolve['response']['message'];
          this.task_id = resolve['response']['new_id'];

          if(this.lastImage === null)
          {
            this.navCtrl.navigateRoot(this.TaskPage);
          }
          else
          {
            //this.uploadImage();
          }

        }
        else
        {
          this.message = resolve['response']['message'];
        }
      }
    );
  }
}

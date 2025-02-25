import { Component, OnInit,CUSTOM_ELEMENTS_SCHEMA, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform,} from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import * as moment from 'moment';

enum statusEnum  { "Raised" = 1,  "Waiting",  "In progress",  "Completed", "Cancelled"}
enum priorityEnum  { "Critical" = 1,  "High",  "Medium",  "Low"}
@Component({
  selector: 'app-updatetask',
  templateUrl: './updatetask.page.html',
  styleUrls: ['./updatetask.page.scss'],
  standalone: true,
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ],
  imports: [IonicModule, CommonModule, FormsModule]
})
export class UpdatetaskPage implements OnInit {
  TaskPage:any='task';
  commentType : number = 1; //commentType in db.comment Table
  ts_history_details :any;
  timeStmp : any;
  showHistory : boolean = false;
  taskDetails : any;
  userData : {status : any, percentCompleted : any, taskid : any,summary:any};
  message : string = "";
  update : boolean = false;
  hasAttachment : boolean = false;
  img_src: string;
  status : any;
  priority : any;
  raisedBy : string = "";
  assignedTo : string = "";
  TaskUpFlag:any;
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) { 

    this.taskDetails =  [];
    this.ts_history_details = [];
    //this.taskDetails = this.navParams.get("details");
    this.userData = {status : 0, percentCompleted : 0, taskid : 0,summary:""};
    this.img_src="https://way2society.com/upload/";
    this.status = statusEnum;
    this.priority = priorityEnum;
    this.TaskUpFlag="0";

    //this.userData['taskid'] = this.taskDetails['id'];
    //this.userData['status'] = this.taskDetails['Status'];
    //this.userData['percentCompleted'] = this.taskDetails['PercentCompleted'];
    //this.userData['summary'] = "";
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
    this.taskDetails = details;//this.navParams.get("details");
    this.userData['taskid'] = this.taskDetails['id'];
    this.userData['status'] = this.taskDetails['Status'];
    this.userData['percentCompleted'] = this.taskDetails['PercentCompleted'];
    if(this.taskDetails['Attachment'].length > 0){
      this.hasAttachment = true;
    }
    if(this.taskDetails.hasOwnProperty('task_owner_name')){
      this.raisedBy = "Me";
      this.assignedTo = this.taskDetails['task_owner_name'];
    }
    else {
      this.raisedBy = this.taskDetails['raised_by_name'];
      this.assignedTo = "Me";
    }
    this.showTaskHistory();
  }
  addComment() {
    this.update = true;
    console.log(this.update)
  }
  cancelComment() {
    this.update = false;
  }
  set(){
    console.log("userData ",this.userData);

    if(this.userData['status'] == 4) 
    {
      if(this.userData['percentCompleted'] == 100)
      {
        this.TaskUpFlag="1";
      }
      else{
        this.TaskUpFlag="0";
      }
    }
    else if(this.userData['status'] != 4)
    {
      this.TaskUpFlag="1";
    }
    if(this.TaskUpFlag=="1")
    {
      this.loaderView.showLoader('Updating');
      this.userData['flag'] = 3; //flag 3 : update
      this.connectServer.getData("TaskServlet", this.userData).then(
        resolve => { 
          if(resolve['success'] == 1)
          {
            console.log(resolve);
            //response : {"success":1,"response":{"task":[{"Status":3,"TypeID":1,"RaisedBy":169,"Description":"this is task for ankur","Reminder":"1","Priority":1,"Title":"task for ankur","Attachment":"after-banks.png","PercentCompleted":"34","TimeStamp":"Jun 9, 2018 11:11:21 PM","Task_Owner":3669,"Role":"1","TaskType":1,"id":1,"DueDate":"Jun 9, 2018"}]}}
            this.message = "Task updated!";
            this.navCtrl.navigateRoot(this.TaskPage);
           // this.navCtrl.setRoot(this.TaskPage);
          }
        }
      );    
      this.addInComment(); 
      //this.viewCtrl.dismiss();
     }
     else
     {
       alert("Task is Completed, Please set progress as 100%");
     }
   }

   openAttachment()
   {
   // window.open(url, '_blank', 'location=no');
     window.open(this.img_src + this.taskDetails['Attachment'] , '_blank', 'location=no');
     return false;
   }

   showTaskHistory(){
    var objData = [];
    objData['flag'] = 1;
    objData['commentType'] = 1;
    objData['refID'] = this.taskDetails['id'];
    this.connectServer.getData("CommentServlet", objData).then(
      resolve => { 
        if(resolve['success'] == 1)
        {
          this.showHistory = true;
          this.ts_history_details = resolve['response']['comments'];
          for(var i=0; i<this.ts_history_details.length;i++){
            this.ts_history_details[i]['TimeStamp'] = moment(this.ts_history_details[i]['TimeStamp']).zone("+00:00").format("Do MMM YYYY, HH:mm");
          }
        }
        else{
          console.log(resolve['response']);
        }
      }
    );     
  }
  addInComment(){
    console.log("comment", this.userData);
    var objData = [];
    objData['flag'] = 2; //add
    objData['commentType'] = 1;
    objData['refID'] = this.taskDetails['id'];
    var comment = "";
    if(this.userData['status'] != this.taskDetails['Status'] ){
      comment += `Status updated : ${this.status[this.userData['status']]} , `;
    }
    if(this.userData['percentCompleted'] != this.taskDetails['PercentCompleted'])
    {
      comment += `Progress updated : ${this.userData['percentCompleted']} , `;
    }
    comment += `Comment : ${this.userData['summary']}.`;
    console.log("comments : "+comment);
    objData['comments'] = comment;
    this.connectServer.getData("CommentServlet", objData).then(
      resolve => { 
        this.loaderView.dismissLoader();
        if(resolve['success'] == 1)
        {
          console.log("comment added. Response : "+resolve['response']);
          //this.navCtrl.setRoot(this.TaskPage);
          this.navCtrl.navigateRoot(this.TaskPage);
        }
        else{
          console.log(resolve['response']);
        }
      }
    );
  }

}

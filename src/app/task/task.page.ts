import { Component, OnInit, CUSTOM_ELEMENTS_SCHEMA, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform, } from '@ionic/angular';
import { NavigationExtras } from '@angular/router';
import { GlobalVars } from 'src/service/globalvars';
import { ConnectServer } from 'src/service/connectserver';
import { LoaderView } from 'src/service/loaderview';
//import { NgCircleProgressModule } from 'ng-circle-progress';

enum statusEnum { "Raised" = 1, "Waiting", "In progress", "Completed", "Cancelled" }
enum priorityEnum { "Critical" = 1, "High", "Medium", "Low" }
@Component({
  selector: 'app-task',
  templateUrl: './task.page.html',
  styleUrls: ['./task.page.scss'],
  standalone: true,
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ],
  imports: [IonicModule, CommonModule, FormsModule,]
})

export class TaskPage implements OnInit {
  UpdatetaskPage: any = 'updatetask';
  AddtaskPage: any = 'addtask';
  tab: string;
  tasksByMe: Array<any>;
  tasksForMe: Array<any>;
  taskForAllOpen: Array<any>;
  taskForCompleted: Array<any>;
  status: any;
  priority: any;
  objData1: any;
  message: string = "";
  showTask: number = 0;
  toggleStatus: boolean;
  Listing: any;
  temp_Complete_Task_array: Array<any>;
  temp_AllOpen_Task_array: Array<any>;

  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams) {
    this.tasksByMe = [];
    this.taskForAllOpen = [];
    this.taskForCompleted = [];
    this.status = statusEnum;
    this.priority = priorityEnum;
    this.objData1 = [];
    this.toggleStatus = false;
    this.Listing = '0';
    this.temp_Complete_Task_array = [];
    this.temp_AllOpen_Task_array = [];

  }

  ngOnInit() {
    this.tab = "for_me";
    this.fetchTaskForMe();
  }
  @HostListener('document:ionBackButton', ['$event'])
  overrideHardwareBackAction(event: any) {
    event.detail.register(100, async () => {
      event.stopImmediatePropagation();
      event.stopPropagation();
      event.preventDefault();
    });
  }
  fetchTaskByMe() {
    this.loaderView.showLoader('Loading');
    this.objData1['flag'] = 1;
    this.connectServer.getData("TaskServlet", this.objData1).then(
      resolve => {
        this.loaderView.dismissLoader();
        if (resolve['success'] == 1) {
          this.showTask = 1;
          this.message = "";
          this.tasksByMe = resolve['response']['task'];
        }
        else {
          this.showTask = 0;
          this.message = "No Task Assigned";
        }
      }
    );
  }

  fetchTaskForMe() {
    this.loaderView.showLoader('Loading');
    this.objData1['flag'] = 2;
    this.connectServer.getData("TaskServlet", this.objData1).then(
      resolve => {
        this.loaderView.dismissLoader();
        if (resolve['success'] == 1) {
          this.showTask = 1;
          this.message = "";
          this.tasksForMe = resolve['response']['task'];
        }
        else {
          this.showTask = 0;
          this.message = "No Task Assigned";
        }
      }
    );
  }

  fetchTaskAll() {
    this.loaderView.showLoader('Loading');
    this.objData1['flag'] = 4;
    this.connectServer.getData("TaskServlet", this.objData1).then(
      resolve => {
        this.loaderView.dismissLoader();
        if (resolve['success'] == 1) {
          this.showTask = 1;
          this.message = "";
          var openTask = resolve['response']['task'];
          this.taskForCompleted = [];
          this.temp_Complete_Task_array = [];
          this.taskForAllOpen = [];
          this.temp_AllOpen_Task_array = [];
          for (var iCnt = 0; iCnt < Object.keys(openTask).length; iCnt++) {
            if (openTask[iCnt]['Status'] == 4 || openTask[iCnt]['Status'] == 5) {
              this.taskForCompleted.push(openTask[iCnt]);
              this.temp_Complete_Task_array = this.taskForCompleted;
            }
            else {
              this.taskForAllOpen.push(openTask[iCnt]);
              this.temp_AllOpen_Task_array = this.taskForAllOpen;
            }
          }
        }
        else {
          this.showTask = 0;
          this.message = "No Task Assigned";
        }
      }
    );
  }
  public updateTask(taskDetails) {
    let navigationExtras: NavigationExtras = {
      queryParams:
      {
        details: taskDetails,
      }
    };
    this.navCtrl.navigateRoot(this.UpdatetaskPage, navigationExtras);
    //this.navCtrl.push(this.UpdatetaskPage, {details : taskDetails});
  }

  public addTask() {
    this.navCtrl.navigateRoot(this.AddtaskPage);
  }
  Change_Toggle() {
    if (this.toggleStatus == true) {
      this.Listing = '1';
    }
    else {
      this.Listing = '0';
    }
  }

  getItems1(ev: any) {
    this.temp_Complete_Task_array = this.taskForCompleted;
    let val = ev.target.value;
    if (val && val.trim() != '') {
      this.temp_Complete_Task_array = this.taskForCompleted.filter(
        (p) => {
          let name: any = p;
          if (name.Title.toLowerCase().indexOf(val.toLowerCase()) > -1) { return (name.Title.toLowerCase().indexOf(val.toLowerCase()) > -1); }
          return null;
          /*if(name.Title.toLowerCase().indexOf(val.toLowerCase()) > -1)
            return (name.Title.toLowerCase().indexOf(val.toLowerCase()) > -1);*/
        }
      );
    }
  }
  getItems(ev: any) {
    this.temp_AllOpen_Task_array = this.taskForAllOpen;
    let val = ev.target.value;
    if (val && val.trim() != '') {
      this.temp_AllOpen_Task_array = this.taskForAllOpen.filter(
        (p) => {
          let name: any = p;
          if (name.Title.toLowerCase().indexOf(val.toLowerCase()) > -1) { return (name.Title.toLowerCase().indexOf(val.toLowerCase()) > -1); }
          return null;
          /*if(name.Title.toLowerCase().indexOf(val.toLowerCase()) > -1)
            return (name.Title.toLowerCase().indexOf(val.toLowerCase()) > -1);*/
        }
      );
    }
  }
}

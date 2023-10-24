import { Component, OnInit, CUSTOM_ELEMENTS_SCHEMA, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NavController, NavParams, ActionSheetController, ToastController, Platform, LoadingController, IonicModule } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
// import { File } from '@ionic-native/file/ngx';
// import { FileTransfer, FileTransferObject } from '@ionic-native/file-transfer/ngx';
// import { FilePath } from '@ionic-native/file-path/ngx';
// import { Camera } from '@ionic-native/camera/ngx';
import { TaskPage } from '../task/task.page';

declare var cordova: any;
enum statusEnum { "Raised" = 1, "Waiting", "In progress", "Completed", "Cancelled" }
enum priorityEnum { "Critical" = 1, "High", "Medium", "Low" }
@Component({
  selector: 'app-task',
  templateUrl: './addtask.page.html',
  styleUrls: ['./addtask.page.scss'],
  standalone: true,
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ],
  imports: [IonicModule, CommonModule, FormsModule],
  providers: [
    // Camera, FileTransfer, 
    // File,
    // FilePath
  ]
})

export class AddTaskPage implements OnInit {
  userData: { title: any, desc: any, duedate: any, attachment: any, status: any, priority: any, percentcomplete: any, mapping_id: any, notifyByEmail: any };
  mem_list: Array<any>;
  max_year: any;
  todayDate: any;
  message: string;
  options: any;
  base64Image: any;
  lastImage: string = null;
  myImagePath: string = null;
  //loading: Loading;
  task_id: any;
  TaskPage: any = 'task';

  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    // private camera: Camera,
    // private transfer: FileTransfer,
    private file: File,
    // private filePath: FilePath,
    private actionSheetCtrl: ActionSheetController,
    private toastCtrl: ToastController,
    private loadingCtrl: LoadingController) {

    this.todayDate = new Date().toISOString();
    this.userData = { title: "", desc: "", duedate: this.todayDate, attachment: "", notifyByEmail: false, percentcomplete: 0, priority: 1, status: 1, mapping_id: 0 };
    this.mem_list = [];
    this.message = "";
    this.task_id = 0;
    var d = new Date();
    this.max_year = d.getFullYear() + 2;


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
  fetchMembers() {
    this.loaderView.showLoader('Loading ...');
    var obj = [];
    obj['flag'] = 1;
    this.connectServer.getData("AddTaskServlet", obj).then(
      resolve => {
        // console.log(obj);
        this.loaderView.dismissLoader();
        if (resolve['success'] == 1) {
          var memberList = resolve['response']['assignedMembers'];
          for (var i = 0; i < memberList.length; i++) {
            this.mem_list.push(memberList[i]);
          }
          // console.log(this.mem_list);
        }
      }
    );
  }
  createTask() {
    this.userData['flag'] = 2;
    this.connectServer.getData("AddTaskServlet", this.userData).then(
      resolve => {
        //this.loaderView.dismissLoader();
        console.log('Response : ' + resolve);
        if (resolve['success'] == 1) {
          this.message = resolve['response']['message'];
          this.task_id = resolve['response']['new_id'];

          if (true || this.lastImage === null) {
            this.navCtrl.navigateRoot(this.TaskPage);
          }
          else {
            this.uploadImage();
          }

        }
        else {
          this.message = resolve['response']['message'];
        }
      }
    );
  }


  /*  ---------------------------- Image View Functions  -----------------------*/

  public selectItems() {
    this.loaderView.showLoader('Loading ...');

  }

  public async presentActionSheet() {
    //tobeun
    // let actionSheet = await this.actionSheetCtrl.create({
    //   header: 'Select Image Source',
    //   buttons: [{
    //     text: 'Load from Library', handler: () => {
    //       this.takePicture(this.camera.PictureSourceType.PHOTOLIBRARY);
    //     }
    //   },
    //   {
    //     text: 'Use Camera',
    //     handler: () => { this.takePicture(this.camera.PictureSourceType.CAMERA); }
    //   },
    //   {
    //     text: 'Cancel',
    //     role: 'cancel'
    //   }]
    // });
    // actionSheet.present();
  }
  public takePicture(sourceType) {// Create options for the Camera Dialog
    //tobeun
    // var options = {
    //   quality: 100,
    //   sourceType: sourceType,
    //   saveToPhotoAlbum: false,
    //   correctOrientation: true
    // };
    // // Get the data of an image
    // this.camera.getPicture(options).then(
    //   (imagePath) => {
    //     console.log({ "imagePath": imagePath });
    //     this.myImagePath = imagePath;
    //     // Special handling for Android library
    //     if (this.platform.is('android') && sourceType === this.camera.PictureSourceType.PHOTOLIBRARY) {
    //       this.filePath.resolveNativePath(imagePath).then(
    //         filePath => {
    //           let correctPath = filePath.substr(0, filePath.lastIndexOf('/') + 1);
    //           let currentName = imagePath.substring(imagePath.lastIndexOf('/') + 1, imagePath.lastIndexOf('?'));
    //           this.copyFileToLocalDir(correctPath, currentName, this.createFileName());
    //         }
    //       );
    //     }
    //     else {
    //       var currentName = imagePath.substr(imagePath.lastIndexOf('/') + 1);
    //       var correctPath = imagePath.substr(0, imagePath.lastIndexOf('/') + 1);
    //       this.copyFileToLocalDir(correctPath, currentName, this.createFileName());
    //     }
    //   },
    //   (err) => {
    //     this.presentToast('Error while selecting image.');
    //   }
    // );
  }
  // Create a new name for the image
  private createFileName() {
    var d = new Date(),
      n = d.getTime(),
      newFileName = n + ".jpg";
    return newFileName;
  }

  // Copy the image to a local folder
  private copyFileToLocalDir(namePath, currentName, newFileName) {
    //tobeun
    // this.file.copyFile(namePath, currentName, cordova.file.dataDirectory, newFileName).then(
    //   success => {
    //     this.lastImage = newFileName;
    //   },
    //   error => {
    //     this.presentToast('Error while storing file.');
    //   }
    // );
  }

  private async presentToast(text) {
    let toast = await this.toastCtrl.create({
      message: text,
      duration: 3000,
      position: 'top'
    });
    toast.present();
  }
  // Always get the accurate path to your apps folder
  public pathForImage(img) {
    console.log({ "selected img": img });
    if (img === null) {
      return '';
    }
    else {
      let win: any = window;
      console.log({ "pathForImage3": win.Ionic.WebView.convertFileSrc(this.myImagePath) });
      return win.Ionic.WebView.convertFileSrc(this.myImagePath);
      // let win: any = window;
      // return win.Ionic.WebView.convertFileSrc(img)
      //return this.file.dataDirectory + img;

    }
  }
  public pathForImage1(img) {
    if (img === null) {
      return '';
    }
    else {
      let win: any = window;

      console.log({ "hg": win.Ionic.WebView.convertFileSrc("file:///data/user/0/io.ionic.starter/cache/" + img) });

      return "file:///data/user/0/io.ionic.starter/cache/" + img;

    }
  }

  public uploadImage() {
    //tobeun
    // // Destination URL

    // var url = "https://way2society.com/upload_image_from_mobile_new.php";

    // // File for Upload
    // var targetPath = this.pathForImage(this.lastImage);

    // // File name only
    // var filename = this.lastImage;
    // var options = {
    //   fileKey: "file",
    //   fileName: filename,
    //   chunkedMode: false,
    //   mimeType: "multipart/form-data",
    //   params: { 'fileName': filename, 'task_id': this.task_id, 'feature': 2, 'token': this.globalVars.USER_TOKEN, 'tkey': this.globalVars.MAP_TKEY }

    //   //params: { 'fileName': filename, 'fine_id': this.fine_id, 'feature': 3, 'token': this.globalVars.USER_TOKEN, 'tkey': this.globalVars.MAP_TKEY }
    // };

    // const fileTransfer: FileTransferObject = this.transfer.create();

    // // Use the FileTransfer to upload the image
    // fileTransfer.upload(targetPath, encodeURI(url), options).then
    //   (data => {
    //     // this.loading.dismissAll()
    //     this.presentToast('Image successful uploaded.');
    //     alert("Image added successfully.");
    //     this.navCtrl.navigateForward("task");
    //   },
    //     err => {
    //       // this.loading.dismissAll()
    //       this.presentToast('Error while uploading file.');

    //       this.navCtrl.navigateForward("task");
    //     }
    //   );
  }

}

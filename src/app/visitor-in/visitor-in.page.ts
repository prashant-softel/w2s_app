import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { Router } from '@angular/router';
import { finalize } from 'rxjs/operators';
// import { GlobalVars } from '../../providers/globalvars';
// import { GlobalVars } from 'src/service/globalvars';
// import { ConnectServer } from '../../providers/connectserver';
// import { ConnectServer } from 'src/service/connectserver';
// import { LoaderView } from '../../providers/loaderview';
// import { LoaderView } from 'src/service/loaderview';
// import { DashboardPage } from '../dashboard/dashboard';
import { DashboardPage } from '../dashboard/dashboard.page';
// import { MyvisitorsPage } from '../myvisitors/myvisitors';
import { MyvisitorsPage } from '../myvisitors/myvisitors.page';
// import { NavController, NavParams, ActionSheetController, ToastController, Platform, LoadingController ,Loading } from 'ionic-angular';
import { NavController, NavParams, ActionSheetController, ToastController, Platform, LoadingController } from '@ionic/angular';
// import { File } from '@ionic-native/file/ngx';
// import { FileTransfer, FileTransferObject } from '@ionic-native/file-transfer/ngx';
// import { FilePath } from '@ionic-native/file-path/ngx';
// import { Camera } from '@ionic-native/camera/ngx';
import { Camera, CameraResultType, CameraSource, Photo } from '@capacitor/camera';
// import { itemviewpage } from '../itemview/itemview';
import { ViewimagePage } from '../viewimage/viewimage.page';
import { IonCardContent } from '@ionic/angular';
import { ActivatedRoute } from '@angular/router';
import { LoaderView } from 'src/service/loaderview';
import { GlobalVars } from 'src/service/globalvars';
import { ConnectServer } from 'src/service/connectserver';
import { Directory, Filesystem } from '@capacitor/filesystem';
import { HttpClient, HttpClientModule } from '@angular/common/http';
declare var cordova: any;

enum statusEnum { "Approved" = 1, "Denied", "Without Approval" };
enum priorityEnum { "Critical" = 1, "High", "Medium", "Low" }

const IMAGE_DIR = 'stored-images';

interface LocalFile {
  name: string;
  path: string;
  data: string;
}


@Component({
  selector: 'app-visitor-in',
  templateUrl: './visitor-in.page.html',
  styleUrls: ['./visitor-in.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule, HttpClientModule],
  providers: [
    // Camera,
    // FileTransfer,
    // File
    // , FilePath
  ]
})
export class VisitorInPage implements OnInit {
  image_id: any;
  dbname: any;
  imagestring: any;
  approvalmsg: any;
  disabled: boolean = true;
  counterforitem: any;
  profileImage: string = null;
  approvalstatus: any;
  myImagePath: string = null;
  enumvalue: number;
  approvalvalue: any;
  DashboardPage: any;
  MyvisitorsPage: any;
  inserted: any;
  approvalvaluetxt: any;
  checkoutnote: any;
  ApproveMsg: Array<any>;
  ApprovalData: Array<any>;
  VisitorDetails: Array<any>;
  fetchVisitorList: Array<any>;
  displayData: Array<any>;
  FullName: any;
  Contact: any;
  Purpose: any;
  UnitNo: any;
  OwnerName: any;
  EnrtyDate: any;
  EnrtyTime: any;
  Entry_flag: any;
  VehicleNo: any;
  Note: any;
  societyName: any;
  company: any;
  img: any;
  img_src: any;
  inGate: any;
  OutGate: any;
  OutDate: any;
  OutTime: any;
  ReportArray: Array<any>;
  status: any;
  callreport: any;
  checkoutdesc: any;
  ApprovalFlag: any;
  EnableButton: any;
  visitor_approval_value: any;

  images: LocalFile[] = [];

  constructor(
    private navCtrl: NavController,
    // private camera: Camera,
    // private transfer: FileTransfer,
    // private file: File,
    private router: Router,
    // private filePath: FilePath,
    public actionSheetCtrl: ActionSheetController,
    public toastCtrl: ToastController,
    public platform: Platform,
    public loadingCtrl: LoadingController,
    private loaderView: LoaderView,
    public navParams: NavParams,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private route: ActivatedRoute,
    private http: HttpClient,
  ) {
    this.visitor_approval_value = "0";
    this.image_id = "";
    this.dbname = "";
    this.counterforitem = "0";
    this.imagestring = "";
    this.approvalstatus = "";
    this.approvalvalue = "";
    this.approvalmsg = "";
    this.approvalvaluetxt = "";
    this.enumvalue = 0;
    // this.displayData = navParams.get("details");
    this.displayData = [];
    this.ApproveMsg = [];
    this.ApprovalData = [];
    this.VisitorDetails = [];
    this.fetchVisitorList = [];
    this.inserted = "";
    this.FullName = "";
    this.Contact = "";
    this.Purpose = "";
    this.UnitNo = "";
    this.OwnerName = "";
    this.EnrtyDate = "";
    this.EnrtyTime = "";
    this.Entry_flag = "";
    this.VehicleNo = "";
    this.Note = "";
    this.societyName = "";
    this.company = "";
    this.img = "";
    this.inGate = "";
    this.OutDate = "";
    this.OutTime = "";
    this.OutGate = "";

    this.ReportArray = [];
    this.status = statusEnum;
    this.checkoutnote = "";
    this.enumvalue = 0;
    this.callreport = "0";
    this.checkoutdesc = "0";
    this.ApprovalFlag = "";
    //this.outTime="";
    //this.img_src="http://localhost/beta_aws2/SecuirityApp/VisitorImage/";
    this.img_src = "https://way2society.com/SecuirityApp/VisitorImage/";
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
    this.displayData = details;
    console.log(details);
    this.FullName = this.displayData['VisitorName'];
    //  this.FullName = this.displayData.VisitorName;
    this.Contact = this.displayData['visitorMobile'];
    this.img = this.displayData['VisitorImage']
    this.Purpose = this.displayData['purpose_name'];
    this.VehicleNo = this.displayData['vehicle'];
    this.Note = this.displayData['visitor_note'];
    this.status = this.displayData['status']


    console.log(this.displayData);
    this.visitor_approval_value = this.globalVars.Visitor_Approval_Status;
    console.log('ionViewDidLoad VisitorInPage');
    var mySound = new Audio();
    var mySound = new Audio('sound/entering.mp3'); mySound.play();
    if (this.displayData['enumvalue'] == 1) {
      // alert("test");
      this.enumvalue = 1;
      if (this.displayData['Entry_flag'] == 3) {
        this.approvalstatus = "Approved";
      }
      if (this.displayData['Entry_flag'] == 2) {
        this.approvalstatus = "Denied";
      }
      console.log('Length ' + Object.keys(this.displayData['VisitorItem']).length);
      if (Object.keys(this.displayData['VisitorItem']).length > 0) {
        this.counterforitem = "1";
        this.checkoutdesc = "1";
        console.log('Visitor Item ' + this.displayData['VisitorItem'][0]['item_desc']);
        this.checkoutnote = this.displayData['VisitorItem'][0]['item_desc'];
        this.imagestring = this.displayData['VisitorItem'][0]['item_image']
      }
      else {
        console.log('Visitor Item');
      }

      if (this.displayData['company'] != "Select Company") {
        this.company = this.displayData['company'];
      }
      if (this.displayData['approvemsg'] != "NULL" && this.displayData['approvemsg'] != "") {
        this.approvalmsg = this.displayData['approvemsg'];
      }
      else {
        this.approvalmsg = "";
      }

    }
    else {

      this.fetchIncommingandOutgiongVIsitor();
      console.log('Test ' + this.displayData['enumvalue']);
    }
  }
  ionViewDidLoad() {

    //alert(this.globalVars.Visitor_Approval_Status);
    // this.visitor_approval_value = this.globalVars.Visitor_Approval_Status;
    // console.log('ionViewDidLoad VisitorInPage');
    // var mySound = new Audio();
    // var mySound = new Audio('sound/entering.mp3'); mySound.play();
    // if (this.displayData['enumvalue'] == 1) {
    //   // alert("test");
    //   this.enumvalue = 1;
    //   if (this.displayData['Entry_flag'] == 1) {
    //     this.approvalstatus = "Approved";
    //   }
    //   if (this.displayData['Entry_flag'] == 2) {
    //     this.approvalstatus = "Denied";
    //   }
    //   console.log('Length ' + Object.keys(this.displayData['VisitorItem']).length);
    //   if (Object.keys(this.displayData['VisitorItem']).length > 0) {
    //     this.counterforitem = "1";
    //     this.checkoutdesc = "1";
    //     console.log('Visitor Item ' + this.displayData['VisitorItem'][0]['item_desc']);
    //     this.checkoutnote = this.displayData['VisitorItem'][0]['item_desc'];
    //     this.imagestring = this.displayData['VisitorItem'][0]['item_image']
    //   }
    //   else {
    //     console.log('Visitor Item');
    //   }
    //   this.FullName = this.displayData['VisitorName'];
    //   this.Contact = this.displayData['visitorMobile'];
    //   this.img = this.displayData['VisitorImage']
    //   this.Purpose = this.displayData['purpose_name'];
    //   this.VehicleNo = this.displayData['vehicle'];
    //   this.Note = this.displayData['visitor_note'];
    //   if (this.displayData['company'] != "Select Company") {
    //     this.company = this.displayData['company'];
    //   }
    //   if (this.displayData['approvemsg'] != "NULL" && this.displayData['approvemsg'] != "") {
    //     this.approvalmsg = this.displayData['approvemsg'];
    //   }
    //   else {
    //     this.approvalmsg = "";
    //   }

    // }
    // else {

    //   this.fetchIncommingandOutgiongVIsitor();
    //   console.log('Test ' + this.displayData['enumvalue']);
    // }
  }

  fetchIncommingandOutgiongVIsitor() {
    this.loaderView.showLoader('Loading');

    var objData = [];
    objData['fetch'] = 6;
    objData['VisitorID'] = this.displayData['ID'];
    // objData['VisitorID'] = 524;
    // alert(objData['VisitorID']);
    this.connectServer.getData("Directory", objData).then(
      resolve => {

        this.loaderView.dismissLoader();
        console.log('Response : ' + resolve['details']);
        if (resolve['success'] == "1") {

          console.log(resolve['details']);
          console.log('ApprovalMsgDetails : ' + resolve['details']['ApprovalMsgDetails'][0]);
          var VisitorDetailsList = resolve['details']['displayData']['visitors'][0];
          //console.log(VisitorDetailsList);
          this.VisitorDetails = VisitorDetailsList;
          var approveList = resolve['details']['ApprovalMsgDetails'];
          for (var i = 0; i < Object.keys(approveList).length; i++) {
            console.log('Message ' + approveList[i]['approve_msg']);
            this.ApproveMsg[i] = approveList[i]['approve_msg'];
          }
          console.log(this.ApproveMsg);
          this.EnrtyTime = this.displayData['InTime'];
          this.EnrtyDate = this.displayData['Date'];
          this.Entry_flag = this.displayData['Entry_flag'];
          this.Purpose = this.displayData['purpose_name'];
          this.UnitNo = this.displayData['UnitNo'];
          this.OwnerName = this.displayData['OwnerName'];
          this.VehicleNo = this.displayData['vehicle'];
          this.Note = this.displayData['visitor_note'];
          this.inGate = this.displayData['Entry_Gate'];
          this.OutDate = this.displayData['OutDate'];
          this.OutTime = this.displayData['OutTime'];
          this.OutGate = this.displayData['Exit_Gate'];
          this.company = this.displayData['company'];
          this.ApprovalFlag = this.displayData['FlagStatus'];
          console.log('visitor-detail', this.VisitorDetails);
          var VList = VisitorDetailsList['displayData'][0];
          this.fetchVisitorList = VList;
          this.FullName = this.fetchVisitorList['FullName'];

          this.Contact = this.fetchVisitorList['Mobile'];
          this.img = this.fetchVisitorList['img'];


        }
      }
    );

  }

  ViewReport() {
    this.callreport = "1";
    this.fetchVisitorReport();
  }
  CheckOutDesc() {
    this.checkoutdesc = "1";
  }
  fetchVisitorReport() {
    //this.loaderView.showLoader('Loading');
    var objData = [];
    objData['fetch'] = 16;
    objData['VisitorExistID'] = this.displayData['visitor_ID'];
    // alert(objData['VisitorID']);
    this.connectServer.getData("Directory", objData).then(
      resolve => {

        // this.loaderView.dismissLoader();
        if (resolve['success'] == 1) {
          this.ReportArray = [];
          var VisitorReportList = resolve['response']['visitorlist'];
          for (var i = 0; i < VisitorReportList.length; i++) {
            this.ReportArray.push(VisitorReportList[i]);
          }

        }
      }
    );

  }

  GoBack() {
    // this.navCtrl.setRoot(DashboardPage);
    this.navCtrl.navigateRoot(this.DashboardPage);
  }

  Insert(i) {
    var note;
    this.ApprovalData['approvalstatus'] = i;
    if (this.approvalvalue != "") {
      note = this.approvalvalue;
    }
    else if (this.approvalvaluetxt != "") {
      note = this.approvalvaluetxt;
    }
    else {
      note = "NULL";
    }
    var objData = [];
    objData['fetch'] = 20;
    //objData['VisitorID'] = this.displayData['visitor_ID'];
    objData['VisitorID'] = this.displayData['ID'];
    //objData['VisitorID'] = 370;
    objData['approvalstatus'] = i;
    objData['note'] = note;
    console.log(objData);
    this.loaderView.showLoader('Loading');
    this.connectServer.getData("Directory", objData).then(
      resolve => {

        this.loaderView.dismissLoader();
        if (resolve['success'] == 1) {

          if (i == 1) {
            alert("You approved visitor " + this.FullName);
          }
          else if (i == 2) {
            alert("You denied visitor " + this.FullName);
          }
          this.inserted = "1";
          // this.navCtrl.setRoot(MyvisitorsPage);
          this.navCtrl.navigateRoot(this.MyvisitorsPage);
          this.enumvalue = 1;
          this.fetchIncommingandOutgiongVIsitor();
        }
      }
    );

  }
  MovetoDashBoard() {
    // this.navCtrl.push(DashboardPage);
    this.navCtrl.navigateRoot(this.DashboardPage);
  }

  //New Add Section
  // public pathForImage(img) {
  //   if (img === null) {
  //     return '';
  //   }
  //   else {
  //     let win: any = window;
  //     return win.Ionic.WebView.convertFileSrc(this.myIm)
  //     // return cordova.file.dataDirectory + img;
  //     //return "file:///data/user/0/io.ionic.starter/cache/"+img
  //   }
  // }


  public async presentActionSheet() {
    let actionSheet = await this.actionSheetCtrl.create({
      header: 'Select Image Source',
      buttons: [{
        text: 'Load from Library', handler: () => {
          this.takePicture(CameraSource.Photos);
        }
      },
      {
        text: 'Use Camera',
        handler: () => {
          this.takePicture(CameraSource.Photos);
        }
      },
      {
        text: 'Cancel',
        role: 'cancel'
      }]
    });
    actionSheet.present();
  }
  async takePicture(sourceType) {
    const image = await Camera.getPhoto({
      quality: 90,
      allowEditing: false,
      resultType: CameraResultType.Uri,
      source: sourceType // Camera, Photos or Prompt!
    });

    if (image) {
      this.saveImage(image)
    }
  }
  async saveImage(photo: Photo) {
    const base64Data = await this.readAsBase64(photo);

    const fileName = new Date().getTime() + '.jpeg';
    const savedFile = await Filesystem.writeFile({
      path: `${IMAGE_DIR}/${fileName}`,
      data: base64Data,
      directory: Directory.Data
    });

    // Reload the file list
    // Improve by only loading for the new image and unshifting array!
    this.loadFiles();
  }
  async loadFiles() {
    this.images = [];

    const loading = await this.loadingCtrl.create({
      message: 'Loading data...'
    });
    await loading.present();

    Filesystem.readdir({
      path: IMAGE_DIR,
      directory: Directory.Data
    })
      .then(
        (result) => {
          this.loadFileData(result.files.map((x) => x.name));
        },
        async (err) => {
          // Folder does not yet exists!
          await Filesystem.mkdir({
            path: IMAGE_DIR,
            directory: Directory.Data
          });
        }
      )
      .then((_) => {
        loading.dismiss();
      });
  }
  async loadFileData(fileNames: string[]) {
    for (let f of fileNames) {
      const filePath = `${IMAGE_DIR}/${f}`;

      const readFile = await Filesystem.readFile({
        path: filePath,
        directory: Directory.Data
      });

      this.images.push({
        name: f,
        path: filePath,
        data: `data:image/jpeg;base64,${readFile.data}`
      });
    }
  }
  async startUpload() {
    var file: LocalFile = this.images[0];
    const response = await fetch(file.data);
    const blob = await response.blob();
    const formData = new FormData();
    formData.append('file', blob, file.name);
    this.uploadData(formData);
  }

  // Upload the formData to our API
  async uploadData(formData: FormData) {
    const loading = await this.loadingCtrl.create({
      message: 'Uploading image...',
    });
    await loading.present();

    // Use your own API!
    // const url = 'http://localhost:8888/images/upload.php';
    var url = "https://way2society.com/upload_image.php";


    this.http.post(url, formData)
      .pipe(
        finalize(() => {
          loading.dismiss();
        })
      )
      .subscribe(res => {
        if (res['success']) {
          alert("Image added succcessfully");
          var p = [];
          p['tab'] = '0';
          p['dash'] = "society";
          this.presentToast('Image successful uploaded.');
          this.navCtrl.navigateForward("myvisitors");
        } else {
          this.presentToast('Error while uploading file.');
          var p = [];
          p['tab'] = '0';
          p['dash'] = "society";
          this.navCtrl.navigateForward("myvisitors");
        }
      });
  }

  // async deleteImage(file: LocalFile) {
  //   await Filesystem.deleteFile({
  //     directory: Directory.Data,
  //     path: file.path
  //   });
  //   this.loadFiles();
  //   this.presentToast('File removed.');
  // }

  // https://ionicframework.com/docs/angular/your-first-app/3-saving-photos
  private async readAsBase64(photo: Photo) {
    if (this.platform.is('hybrid')) {
      const file = await Filesystem.readFile({
        path: photo.path
      });

      return file.data;
    }
    else {
      // Fetch the photo, read as a blob, then convert to base64 format
      const response = await fetch(photo.webPath);
      const blob = await response.blob();

      return await this.convertBlobToBase64(blob) as string;
    }
  }

  // Helper function
  convertBlobToBase64 = (blob: Blob) => new Promise((resolve, reject) => {
    const reader = new FileReader;
    reader.onerror = reject;
    reader.onload = () => {
      resolve(reader.result);
    };
    reader.readAsDataURL(blob);
  });



  // public takePicture(sourceType) {// Create options for the Camera Dialog
  //   //tobeun
  //   var options = {
  //     quality: 100,
  //     sourceType: sourceType,
  //     saveToPhotoAlbum: false,
  //     correctOrientation: true
  //   };
  //   // Get the data of an image
  //   this.camera.getPicture(options).then(
  //     (imagePath) => {
  //       console.log({ "imagepath": imagePath });
  //       this.myImagePath = imagePath;
  //       // Special handling for Android library
  //       if (this.platform.is('android') && sourceType === this.camera.PictureSourceType.PHOTOLIBRARY) {
  //         this.filePath.resolveNativePath(imagePath).then(
  //           filePath => {
  //             let correctPath = filePath.substr(0, filePath.lastIndexOf('/') + 1);
  //             let currentName = imagePath.substring(imagePath.lastIndexOf('/') + 1, imagePath.lastIndexOf('?'));
  //             this.copyFileToLocalDir(correctPath, currentName, this.createFileName());
  //           }
  //         );
  //       }
  //       else {
  //         var currentName = imagePath.substr(imagePath.lastIndexOf('/') + 1);
  //         var correctPath = imagePath.substr(0, imagePath.lastIndexOf('/') + 1);
  //         this.copyFileToLocalDir(correctPath, currentName, this.createFileName());
  //       }
  //     },
  //     (err) => {
  //       this.presentToast('Error while selecting image.');
  //     }
  //   );
  // }

  // Create a new name for the image
  // private createFileName() {
  //   var d = new Date(),
  //     n = d.getTime(),
  //     newFileName = n + ".jpg";
  //   return newFileName;
  // }

  // Copy the image to a local folder
  // private copyFileToLocalDir(namePath, currentName, newFileName) {
  //   //tobeun
  //   this.file.copyFile(namePath, currentName, cordova.file.dataDirectory, newFileName).then(
  //     success => {
  //       this.profileImage = newFileName;
  //     },


  //     error => {
  //       this.presentToast('Error while storing file.');
  //     }
  //   );
  // }

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
  // public uploadImage() {
  //   //tobeun
  //   // Destination URL
  //   //var url = "http://192.169.1.117/beta_aws_2/ads";
  //   // var url = "http://13.232.206.120/upload_image.php";
  //   var url = "https://way2society.com/upload_image.php";
  //   //  alert(url);
  //   // File for Upload
  //   var targetPath = this.pathForImage(this.profileImage);
  //   alert(targetPath);
  //   // File name only
  //   var filename = this.profileImage;
  //   //alert(filename);
  //   var options = {
  //     fileKey: "file",
  //     fileName: filename,
  //     chunkedMode: false,
  //     mimeType: "multipart/form-data",
  //     params: { 'fileName': filename, 'entry_id': this.image_id, 'feature': 6, 'token': this.globalVars.USER_TOKEN, 'tkey': this.globalVars.MAP_TKEY }
  //   };

  //   const fileTransfer: FileTransferObject = this.transfer.create();

  //   fileTransfer.upload(targetPath, encodeURI(url), options).then
  //     (data => {
  //       //this.loading.dismissAll()
  //       this.presentToast('Image successful uploaded.');
  //       alert("Image added succcessfully");
  //       var p = [];
  //       p['tab'] = '0';
  //       p['dash'] = "society";
  //       this.navCtrl.navigateForward("myvisitors");
  //     },
  //       err => {
  //         //this.loading.dismissAll()
  //         this.presentToast('Error while uploading file.');
  //         var p = [];
  //         p['tab'] = '0';
  //         p['dash'] = "society";
  //         this.navCtrl.navigateForward("myvisitors");
  //       }
  //     );
  // }

  // public uploadImageOld() {
  //   //tobeun
  //   // Destination URL
  //   //var url = "http://192.169.1.117/beta_aws_2/ads";
  //   // var url = "http://13.232.206.120/upload_image.php";
  //   var url = "https://way2society.com/upload_image.php";
  //   //  alert(url);
  //   // File for Upload
  //   var targetPath = this.pathForImage(this.profileImage);
  //   alert(targetPath);
  //   // File name only
  //   var filename = this.profileImage;
  //   //alert(filename);
  //   var options = {
  //     fileKey: "file",
  //     fileName: filename,
  //     chunkedMode: false,
  //     mimeType: "multipart/form-data",
  //     params: { 'fileName': filename, 'entry_id': this.image_id, 'feature': 6, 'token': this.globalVars.USER_TOKEN, 'tkey': this.globalVars.MAP_TKEY }
  //   };

  //   const fileTransfer: FileTransferObject = this.transfer.create();

  //   fileTransfer.upload(targetPath, encodeURI(url), options).then
  //     (data => {
  //       //this.loading.dismissAll()
  //       this.presentToast('Image successful uploaded.');
  //       alert("Image added succcessfully");
  //       var p = [];
  //       p['tab'] = '0';
  //       p['dash'] = "society";
  //       this.navCtrl.navigateForward("myvisitors");
  //     },
  //       err => {
  //         //this.loading.dismissAll()
  //         this.presentToast('Error while uploading file.');
  //         var p = [];
  //         p['tab'] = '0';
  //         p['dash'] = "society";
  //         this.navCtrl.navigateForward("myvisitors");
  //       }
  //     );
  // }



  public selectItems() {
    // const image_id = 'http://13.232.206.120/SecuirityApp/VisitorImage' + this.imagestring;
    // this.navCtrl.push(itemviewpage,{ image_id: "http://13.232.206.120/SecuirityApp/VisitorImage/"+this.imagestring });
    // this.router.navigate(['viewimage'], { image_id: image_id });
    this.loaderView.showLoader("Loading");

  }
  private InsertItemDescription() {

    console.log('imagestring' + this.imagestring);
    console.log('checkoutnote' + this.checkoutnote);
    console.log('ID' + this.displayData['ID']);


    var objData = [];
    objData['fetch'] = 21;
    objData['id'] = this.displayData['ID'];
    objData['imagestring'] = this.imagestring;
    objData['checkoutnote'] = this.checkoutnote;
    this.connectServer.getData("Directory", objData).then(
      resolve => {

        this.loaderView.dismissLoader();
        if (resolve['success'] == "1") {
          alert("Check Out Description Updated");
          this.image_id = resolve['id'];
          this.dbname = resolve['dbname'];
          this.counterforitem = "1";
          console.log(this.image_id + ' ' + this.dbname);
          // this.uploadImage();
          this.startUpload();
        }
      }
    );

  }
  public incert() {
    this.InsertItemDescription();
  }


}

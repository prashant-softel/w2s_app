import { Component, OnInit } from '@angular/core';
import { NavController, NavParams, ActionSheetController, ToastController, Platform, LoadingController, IonicModule } from '@ionic/angular';

import { File } from '@ionic-native/file/ngx';
import { FileTransfer, FileTransferObject } from '@ionic-native/file-transfer/ngx';
import { FilePath } from '@ionic-native/file-path/ngx';
import { Camera } from '@ionic-native/camera/ngx';
import { GlobalVars } from 'src/service/globalvars';
import { ViewimposefinePage } from '../viewimposefine/viewimposefine.page';
import { FineimageviewPage } from '../fineimageview/fineimageview.page';
import { ConnectServer } from 'src/service/connectserver';
import { LoaderView } from 'src/service/loaderview';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';


declare var cordova: any;
@Component({
  selector: 'app-fine',
  templateUrl: './fine.page.html',
  styleUrls: ['./fine.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule],
  providers: [Camera, FileTransfer, File, FilePath]
})
export class FinePage implements OnInit {
  FineimageviewPage: any = 'fineimageview';
  userData: { period_id: any, Ledger_id: any, unit_id: any, amount: any, desc: any, img: any, periodDate: any, sendEmail: any };
  FetchPeriod: Array<any>;
  UnitList: Array<any>;
  //UnitList : Array<{}>;
  message: string;
  options: any;
  base64Image: any;
  lastImage: string = null;
  myImagePath: string = null;
  //loading: Loading;
  LedgerName: string;
  LedgerId: any;
  LedgerDetail: Array<any>;
  //LedgerDetail: Array<{}>;
  fine_id: any;
  type: string;
  update: any;
  setMassege: any;
  Block_unit = 0;
  Block_desc = "";
  //sendcheck: num=1;
  checked: boolean = true;
  disabled: any;
  ViewimposefinePage: any = 'viewimposefine';

  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private camera: Camera,
    private transfer: FileTransfer,
    private file: File,
    private filePath: FilePath,
    private actionSheetCtrl: ActionSheetController,
    private toastCtrl: ToastController,
    private loadingCtrl: LoadingController,
    private sanitizer: DomSanitizer
  ) {

    this.userData = { period_id: "", Ledger_id: "", unit_id: "", amount: "", desc: "", img: "", periodDate: "", sendEmail: "" };

    this.FetchPeriod = [];
    this.LedgerDetail = [];
    this.LedgerName = "";
    this.UnitList = [];
    this.fine_id = 0;
    this.type = "";
    this.update = true;
    this.setMassege = "";
    this.disabled = false;

  }

  ngOnInit() {
    this.fetchPeriodDetails();
    if (this.checked == true) {
      this.userData['sendEmail'] = "1";
    }
    else {
      this.userData['sendEmail'] = "0";
    }

    this.Block_unit = this.globalVars.MAP_UNIT_BLOCK;
    this.Block_desc = this.globalVars.MAP_BLOCK_DESC;

  }

  fetchPeriodDetails() {
    this.loaderView.showLoader('Loading ...');
    var objData = [];
    objData['fetch'] = "Fetchperiod";
    this.connectServer.getData("ImposeFineServlet", objData).then(
      resolve => {
        this.fetchLedgerName();
        this.loaderView.dismissLoader();
        if (resolve['success'] == 1) {
          console.log(resolve);
          var PeriodList = resolve['response']['Period'];
          if (PeriodList['PeriodID'] == -1) {
            this.setMassege = PeriodList['PeriodID'];
            //alert(this.setMassege);
          }
          else {
            this.setMassege = 1;
            this.userData.period_id = PeriodList['PrvePeriodID'];
            this.type = PeriodList['Type'];
            this.userData.periodDate = PeriodList['PrevPeriodDate'];
          }
        }
      }
    );
  }

  fetchLedgerName() {
    //this.loaderView.showLoader('Loading ...');
    var objData = [];
    objData['fetch'] = "FetchLedger";
    this.connectServer.getData("ImposeFineServlet", objData).then(
      resolve => {
        this.FetchUnitDetails();
        //this.loaderView.dismissLoader();
        if (resolve['success'] == 1) {
          console.log(resolve);
          this.LedgerDetail = resolve['response']['Fine'];
          //alert(this.LedgerDetail.length);
          this.userData.Ledger_id = this.LedgerDetail[0]['FineID'];
        }
        else {
          //alert("Please select default fine ledger");
          //this.navCtrl.setRoot(ViewimposefinePage);
        }
      }
    );
  }

  FetchUnitDetails() {
    //this.loaderView.showLoader('Loading ...');
    var objData = [];
    objData['fetch'] = "FetchUnits";
    this.connectServer.getData("ImposeFineServlet", objData).then(
      resolve => {
        // this.loaderView.dismissLoader();
        if (resolve['success'] == 1) {
          console.log(resolve);
          var UnitList = resolve['response']['Units'];
          //this.userData.memberEmail=UnitList['email'];
          //alert(this.userData.memberEmail);
          //  console.log(this.userData.memberEmail);
          for (var i = 0; i < UnitList.length; i++) {
            this.UnitList.push(UnitList[i]);
          }
          // this.userData.unit_no=this.UnitList[0]['unit_no'];
          //	 this.userData.owner_name=this.UnitList[0]['owner_name'];
        }
      }
    );
  }


  getSelect(isChecked) {
    alert(isChecked);
    if (isChecked === true) {
      this.userData['sendEmail'] = "1";
    }
    else {
      this.userData['sendEmail'] = 0;
    }
    alert(this.userData['sendEmail']);
  }

  submit() {
    this.disabled = true;
    //alert(this.userData['sendEmail']);
    this.userData['set'] = "addImposeFine";
    this.connectServer.getData("ImposeFineServlet", this.userData).then(
      resolve => {
        //this.loaderView.dismissLoader();
        console.log('Response : ' + resolve);
        if (resolve['success'] == 1) {
          this.message = resolve['response']['message'];
          this.fine_id = resolve['response']['new_fine_id'];
          if (true || this.lastImage === null) {
            alert("Fine would be added in next bill.");
            this.navCtrl.navigateRoot(this.ViewimposefinePage);
            //this.navCtrl.setRoot(ViewimposefinePage);
          }
          else {
            //this.uploadImage();
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

    // let navigationExtras: NavigationExtras = {
    //   queryParams:
    //   {
    //     details: this.userData['img'],
    //   }
    // };

    // this.navCtrl.navigateRoot(this.FineimageviewPage, navigationExtras);

    //this.navCtrl.navigateRoot(this.FineimageviewPage);
    //this.navCtrl.push(FineimageviewPage,this.userData['img']);
  }



  public async presentActionSheet() {
    let actionSheet = await this.actionSheetCtrl.create({
      header: 'Select Image Source',
      buttons: [{
        text: 'Load from Library', handler: () => {
          this.takePicture(this.camera.PictureSourceType.PHOTOLIBRARY);
        }
      },
      {
        text: 'Use Camera',
        handler: () => { this.takePicture(this.camera.PictureSourceType.CAMERA); }
      },
      {
        text: 'Cancel',
        role: 'cancel'
      }]
    });
    actionSheet.present();
  }

  public takePicture(sourceType) {// Create options for the Camera Dialog
    var options = {
      quality: 100,
      sourceType: sourceType,
      saveToPhotoAlbum: false,
      correctOrientation: true
    };
    // Get the data of an image
    this.camera.getPicture(options).then(
      (imagePath) => {
        console.log({ "imagePath": imagePath });
        this.myImagePath = imagePath;
        // Special handling for Android library
        if (this.platform.is('android') && sourceType === this.camera.PictureSourceType.PHOTOLIBRARY) {
          this.filePath.resolveNativePath(imagePath).then(
            filePath => {
              let correctPath = filePath.substr(0, filePath.lastIndexOf('/') + 1);
              let currentName = imagePath.substring(imagePath.lastIndexOf('/') + 1, imagePath.lastIndexOf('?'));
              this.copyFileToLocalDir(correctPath, currentName, this.createFileName());
            }
          );
        }
        else {
          var currentName = imagePath.substr(imagePath.lastIndexOf('/') + 1);
          var correctPath = imagePath.substr(0, imagePath.lastIndexOf('/') + 1);
          this.copyFileToLocalDir(correctPath, currentName, this.createFileName());
        }
      },
      (err) => {
        this.presentToast('Error while selecting image.');
      }
    );
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
    this.file.copyFile(namePath, currentName, cordova.file.dataDirectory, newFileName).then(
      success => {
        this.lastImage = newFileName;
      },
      error => {
        this.presentToast('Error while storing file.');
      }
    );
  }

  private async presentToast(text) {
    let toast = await this.toastCtrl.create({
      message: text,
      duration: 3000,
      position: 'top'
    });
    toast.present();
  }

  public pathForImage(img) {
    if (img === null) {
      return '';
    }
    else {
      let win: any = window;
      console.log({ "pathForImage3": win.Ionic.WebView.convertFileSrc(this.myImagePath) });
      return win.Ionic.WebView.convertFileSrc(this.myImagePath);
    }
  }
  public uploadImage() {
    // Destination URL
    //var url = "http://localhost/beta_aws_3";
    var url = "https://way2society.com/upload_image_from_mobile_new.php";

    // File for Upload
    var targetPath = this.pathForImage(this.lastImage);

    // File name only
    var filename = this.lastImage;
    var options = {
      fileKey: "file",
      fileName: filename,
      chunkedMode: false,
      mimeType: "multipart/form-data",
      params: { 'fileName': filename, 'fine_id': this.fine_id, 'feature': 3, 'token': this.globalVars.USER_TOKEN, 'tkey': this.globalVars.MAP_TKEY }
    };

    const fileTransfer: FileTransferObject = this.transfer.create();
    // this.loading = this.loadingCtrl.create({ content: 'Uploading...', });
    // this.loading.present();

    //alert(targetPath);

    //this.presentToast(targetPath);

    // Use the FileTransfer to upload the image
    fileTransfer.upload(targetPath, encodeURI(url), options).then
      (data => {
        // this.loading.dismissAll()
        this.presentToast('Image successful uploaded.');
        alert("Fine added successfully.");
        var p = [];
        p['tab'] = '1';
        p['dash'] = "admin";

        // this.navCtrl.setRoot(ViewimposefinePage, { details: p });
        //need to handle next page state data
        this.navCtrl.navigateForward("viewimposefine", { state: p });


      },
        err => {
          // this.loading.dismissAll()
          this.presentToast('Error while uploading file.');
          var p = [];
          p['tab'] = '1';
          p['dash'] = "admin";

          // this.navCtrl.setRoot(ViewimposefinePage, { details: p });

          //need to handle next page state data
          this.navCtrl.navigateForward("viewimposefine", { state: p });
        }
      );
  }

}




import { Component, OnInit, CUSTOM_ELEMENTS_SCHEMA, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActionSheetController, IonicModule, LoadingController, NavController, NavParams, Platform, ToastController } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
// import { Camera } from '@ionic-native/camera/ngx';
// import { File } from '@ionic-native/file/ngx';
// import { FilePath } from '@ionic-native/file-path/ngx';
// import { FileTransfer, FileTransferObject } from '@ionic-native/file-transfer/ngx';

import { Camera, CameraResultType, CameraSource, Photo } from '@capacitor/camera';
import { Filesystem, Directory } from '@capacitor/filesystem';
import { finalize } from 'rxjs';
import { HttpClient } from '@angular/common/http';

declare var cordova: any;
const IMAGE_DIR = 'stored-images';

enum statusEnum { "Raised" = 1, "Waiting", "In progress", "Completed", "Cancelled" }
enum priorityEnum { "Critical" = 1, "High", "Medium", "Low" }

interface LocalFile {
  name: string;
  path: string;
  data: string;
}
@Component({
  selector: 'app-addclassified',
  templateUrl: './addclassified.html',
  styleUrls: ['./addclassified.scss'],
  standalone: true,
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ],
  imports: [IonicModule, CommonModule, FormsModule],
  providers: [
    // Camera, FileTransfer, File, FilePath
  ]

})
export class AddclassifiedPage implements OnInit {
  userData: { ad_title: any, desp: any, exp_date: any, img: any, location: any, email: any, phone: any, cat_id: any; };
  cat_list: Array<any>;
  ClassifiedsPage: any = "classifieds";
  ClassifiedsimageviewPage: any = "classifiedimageview";
  message: string;
  options: any;
  myImagePath: string = null;
  base64Image: any;
  // lastImage: string = null;
  // loading: Loading;
  classified_id: any;
  Block_unit = 0;
  Block_desc = "";
  selectedImageName;
  images: LocalFile[] = [];


  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute,
    // private camera: Camera,
    // private transfer: FileTransfer,
    // private file: File,
    // private filePath: FilePath,
    private http: HttpClient,
    public actionSheetCtrl: ActionSheetController,
    public toastCtrl: ToastController,

    public loadingCtrl: LoadingController) {
    var todayDate = new Date().toISOString();
    this.userData = { ad_title: "", desp: "", exp_date: todayDate, img: "", location: "", email: "", phone: "", cat_id: "" };

    this.cat_list = [];
    // this.fetchCategories();
    this.message = "";
    this.classified_id = 0;
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
    console.log('ionViewDidLoad AddclassifiedPage');
    this.fetchCategories();
    if (this.globalVars.MAP_UNIT_BLOCK == undefined) {
      this.Block_unit = 0
    }
    else {
      this.Block_unit = this.globalVars.MAP_UNIT_BLOCK;
      this.Block_desc = this.globalVars.MAP_BLOCK_DESC;
    }
  }
  fetchCategories() {
    this.loaderView.showLoader('Loading ...');
    var obj = [];
    obj['fetch'] = "classifiedscategory";

    this.connectServer.getData("Classifieds", obj).then(
      resolve => {
        this.loaderView.dismissLoader();
        if (resolve['success'] == 1) {
          console.log(resolve);
          var categoryList = resolve['response']['cat_id'];
          for (var i = 0; i < categoryList.length; i++) {
            this.cat_list.push(categoryList[i]);
          }
        }
      }
    );
  }
  create() {
    //this.loaderView.showLoader('Please Wait ...');
    this.userData['set'] = "addclassifieds";

    this.connectServer.getData("Classifieds", this.userData).then(
      resolve => {
        //this.loaderView.dismissLoader();
        console.log('Response : ' + resolve);
        if (resolve['success'] == 1) {
          this.message = resolve['response']['message'];
          this.classified_id = resolve['response']['new_c_id'];

          if (this.images.length < 1) {
            alert("We have received your request. Please wait for approval.");
            this.navCtrl.navigateRoot(this.ClassifiedsPage);
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

  public selectItems() {
    this.loaderView.showLoader('Loading ...');
    this.navCtrl.navigateRoot(this.ClassifiedsimageviewPage, this.userData['img']);
  }
  public async presentActionSheet() {
    let actionSheet = await this.actionSheetCtrl.create({
      header: 'Select Image Source',
      buttons: [{
        text: 'Load from Library', handler: () => {
          // this.takePicture(this.camera.PictureSourceType.PHOTOLIBRARY);
          this.selectImage(CameraSource.Photos);

        }
      },
      {
        text: 'Use Camera',
        handler: () => {
          // this.takePicture(this.camera.PictureSourceType.CAMERA); 
          this.selectImage(CameraSource.Camera);

        }
      },
      {
        text: 'Cancel',
        role: 'cancel'
      }]
    });
    actionSheet.present();
  }
  async selectImage(cameraSource: CameraSource) {
    const image = await Camera.getPhoto({
      quality: 90,
      allowEditing: false,
      resultType: CameraResultType.Uri,
      source: cameraSource // Camera, Photos or Prompt!
    });

    if (image) {
      this.saveImage(image)
      // const base64Data = await this.readAsBase64(image);

      // const fileName = new Date().getTime() + '.jpeg';

      // this.images = [];
      // this.images.push({
      //   name: fileName,
      //   path: image.path, //filePath,
      //   data: `${base64Data}` //`data:image/jpeg;base64,${readFile.data}`
      // });


    }
  }
  private async readAsBase64(photo: Photo) {
    if (this.platform.is('hybrid')) {
      const file = await Filesystem.readFile({
        path: photo.path
      });

      return file.data;
    }
    else {
      const response = await fetch(photo.webPath);
      const blob = await response.blob();
      return await this.convertBlobToBase64(blob) as string;
    }
  }

  convertBlobToBase64 = (blob: Blob) => new Promise((resolve, reject) => {
    const reader = new FileReader;
    reader.onerror = reject;
    reader.onload = () => {
      resolve(reader.result);
    };
    reader.readAsDataURL(blob);
  });
  public takePicture(sourceType) {// Create options for the Camera Dialog
    // var options = {
    //   quality: 100,
    //   sourceType: sourceType,
    //   saveToPhotoAlbum: false,
    //   correctOrientation: true
    // };
    // // Get the data of an image
    // this.camera.getPicture(options).then(
    //   (imagePath) => {
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
  // public pathForImage(img) {
  //   if (img === null) {
  //     return '';
  //   }
  //   else {
  //     let win: any = window;
  //     console.log({ "pathForImage3": win.Ionic.WebView.convertFileSrc(this.myImagePath) });
  //     return win.Ionic.WebView.convertFileSrc(this.myImagePath);
  //     // return cordova.file.dataDirectory + img;
  //     //return "file:///data/user/0/io.ionic.starter/cache/"+img
  //   }
  // }
  // public pathForImage1(img) {
  //   if (img === null) {
  //     return '';
  //   }
  //   else {
  //     let win: any = window;

  //     console.log({ "hg": win.Ionic.WebView.convertFileSrc("file:///data/user/0/io.ionic.starter/cache/" + img) });

  //     return "file:///data/user/0/io.ionic.starter/cache/" + img;

  //   }
  // }
  async saveImage(photo: Photo) {
    const base64Data = await this.readAsBase64(photo);

    const fileName = new Date().getTime() + '.jpeg';
    this.selectedImageName = fileName;
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
      if (this.selectedImageName == f) {
        this.images.push({
          name: f,
          path: filePath,
          data: `data:image/jpeg;base64,${readFile.data}`
        });
      }
    }
  }
  // public uploadImageOld() {
  //   // Destination URL
  //   //var url = "http://localhost/beta_aws_2/ads";
  //   var url = "https://way2society.com/upload_image_from_mobile_new.php";

  //   // File for Upload
  //   var targetPath = this.pathForImage(this.lastImage);

  //   // File name only
  //   var filename = this.lastImage;
  //   var options = {
  //     fileKey: "file",
  //     fileName: filename,
  //     chunkedMode: false,
  //     mimeType: "multipart/form-data",
  //     params: { 'fileName': filename, 'classified_id': this.classified_id, 'feature': 1, 'token': this.globalVars.USER_TOKEN, 'tkey': this.globalVars.MAP_TKEY }
  //   };

  //   const fileTransfer: FileTransferObject = this.transfer.create();
  //   // this.loading = this.loadingCtrl.create({'Uploading...' });
  //   // this.loading.present();

  //   //alert(targetPath);

  //   //this.presentToast(targetPath);

  //   // Use the FileTransfer to upload the image
  //   fileTransfer.upload(targetPath, encodeURI(url), options).then
  //     (data => {
  //       // this.loading.dismissAll()
  //       this.presentToast('Image successful uploaded.');
  //       alert("We have received your request. Please wait for approval.");
  //       this.navCtrl.navigateRoot(this.ClassifiedsPage);
  //     },
  //       err => {
  //         // this.loading.dismissAll();
  //         this.presentToast('Error while uploading file.');
  //         this.navCtrl.navigateRoot(this.ClassifiedsPage);
  //       }
  //     );
  // }
  async uploadImage() {
    const file = this.images[0];
    const response = await fetch(file.data);
    const blob = await response.blob();
    const formData = new FormData();
    formData.append('file', blob, file.name);
    formData.append('filename', file.name);
    const loading = await this.loadingCtrl.create({
      message: 'Uploading image...',
    });
    await loading.present();
    var url = "https://way2society.com/upload_image_from_mobile.php";
    this.http.post(url, formData,
      {
        params: { 'fileName': file.name, 'classified_id': this.classified_id, 'feature': 1, 'token': this.globalVars.USER_TOKEN, 'tkey': this.globalVars.MAP_TKEY },
      })
      .pipe(
        finalize(() => {
          loading.dismiss();
        })
      )
      .subscribe(res => {
        this.presentToast('Image successful uploaded.');
        this.navCtrl.navigateForward(this.ClassifiedsPage);
      },
        err => {
          // this.presentToast('Error while uploading file.');
          this.presentToast('Image successful uploaded.');
          this.navCtrl.navigateForward(this.ClassifiedsPage);
        }
      );
  }






}

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NavController, NavParams, ActionSheetController, ToastController, Platform, LoadingController, IonicModule } from '@ionic/angular';
// import { File } from '@ionic-native/file/ngx';
// import { FileTransfer, FileTransferObject } from '@ionic-native/file-transfer/ngx';
// import { FilePath } from '@ionic-native/file-path/ngx';
// import { Camera } from '@ionic-native/camera/ngx';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { ServicerequestPage } from '../servicerequest/servicerequest.page';
import { Camera, CameraResultType, CameraSource, Photo } from '@capacitor/camera';
import { Filesystem, Directory } from '@capacitor/filesystem';
import { finalize } from 'rxjs';
import { HttpClient } from '@angular/common/http';


declare var cordova: any;
const IMAGE_DIR = 'stored-images';

interface LocalFile {
  name: string;
  path: string;
  data: string;
}

@Component({
  selector: 'app-addservicerequest',
  templateUrl: './addservicerequest.page.html',
  styleUrls: ['./addservicerequest.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule],
  providers: [
    // Camera, FileTransfer,
    // File
    // , FilePath
  ]
})
export class AddservicerequestPage implements OnInit {
  ServiceRequestPage: any = 'servicerequest';
  AddtenantPage: any = 'addtenant';
  AddAddressProof: any = 'addaddressproof';

  userData: { title: any, details: any, priority: any, category: any, sr_id: any; unit_id: any };
  cat_list: Array<any>;
  message: string;
  options: any;
  base64Image: any;
  // lastImage: string = null;
  // myImagePath: string = null;
  loading: any;//Loading;
  servicerequest_id: any;
  Block_unit = 0;
  Block_desc = "";
  Unit_list: Array<any>;
  rolewise: any;
  role: any;
  renovationRequestId = 0;
  tenantRequestId = 0;
  AddressProofId = 0;
  images: LocalFile[] = [];
  selectedImageName;



  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private plt: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute,
    // private camera: Camera,
    // private transfer: FileTransfer,
    // private file: File,
    // private filePath: FilePath,
    private actionSheetCtrl: ActionSheetController,
    private toastCtrl: ToastController,
    private loadingCtrl: LoadingController,
    private http: HttpClient,


  ) {
    this.userData = { title: "", details: "", priority: "1-Low", category: 1, sr_id: "", unit_id: 0 };
    this.cat_list = [];
    this.Unit_list = [];
    this.fetchCategories();
    this.message = "";
    this.servicerequest_id = 0;
    this.role = "";
    this.rolewise = "";
    this.AddressProofId = this.globalVars.ADDRESS_PROOF_REQUEST_ID;
    this.renovationRequestId = this.globalVars.RENOVATION_REQUEST_ID;
    this.tenantRequestId = this.globalVars.TENANT_REQUEST_ID;

  }

  ngOnInit() {
    this.loadFiles();
    if (this.globalVars.MAP_UNIT_BLOCK == undefined) {
      this.Block_unit = 0
    }
    else {
      this.Block_unit = this.globalVars.MAP_UNIT_BLOCK;
      this.Block_desc = this.globalVars.MAP_BLOCK_DESC;
    }
    if (this.globalVars.MAP_USER_ROLE == "Admin" || this.globalVars.MAP_USER_ROLE == "Super Admin") {
      this.role = "Admin";
    }
    else {
      this.role = "Member";
    }
    this.rolewise = this.role;
  }

  fetchCategories() {
    this.loaderView.showLoader('Loading ...');
    var obj = [];
    obj['fetch'] = "srcategory";
    this.connectServer.getData("ServiceRequest", obj).then(
      resolve => {
        this.loaderView.dismissLoader();
        if (resolve['success'] == 1) {
          console.log(resolve);
          var categoryList = resolve['response']['category'];
          for (var i = 0; i < categoryList.length; i++) {
            this.cat_list.push(categoryList[i]);
          }
        }
      }
    );
    this.fetchUnitList();
  }
  fetchUnitList() {
    var objData = [];
    this.connectServer.getData("ServiceProvider/fetchUnits", objData).then(
      resolve => {
        if (resolve['success'] == 1) {
          console.log(resolve);
          var UnitList = resolve['response']['member']['0'];
          for (var i = 0; i < UnitList.length; i++) {
            this.Unit_list.push(UnitList[i]);
          }
        }
      }
    );
  }

  create() {
    console.log({ "this.rolewise": this.rolewise, "userData.unit_id": this.userData.unit_id, "this.globalVars.MAP_UNIT_ID": this.globalVars.MAP_UNIT_ID });
    if (this.rolewise == "Member") {
      this.userData.unit_id = this.globalVars.MAP_UNIT_ID;
    }
    if (this.userData.unit_id == 0 && this.rolewise == "Admin") {
      alert("Please select unit no.");
    }
    else {
      if (!this.userData.unit_id) {
        alert('Somting went wrong for proper working of app please login again!');
        return;
      }
      this.loaderView.showLoader('Please Wait ...');
      this.userData['set'] = "sr";

      console.log("this.userData.unit_id : " + this.userData.unit_id);
      if (this.userData.category == this.globalVars.RENOVATION_REQUEST_ID || this.userData.category == this.globalVars.ADDRESS_PROOF_REQUEST_ID || this.userData.category == this.globalVars.TENANT_REQUEST_ID) {
        console.log(this.userData.category);
        console.log(this.globalVars.ADDRESS_PROOF_REQUEST_ID);
        var p = [];
        p['title'] = this.userData.title;
        p['category'] = this.userData.category;
        p['priority'] = this.userData.priority;
        p['loginId'] = this.globalVars.MAP_LOGIN_ID;
        p['unitId'] = this.userData.unit_id;
        console.log("details : ", p);
        if (this.userData.category == this.globalVars.ADDRESS_PROOF_REQUEST_ID) {
          let navigationExtras: NavigationExtras = {
            queryParams:
            {
              details: p,
            }
          };
          this.navCtrl.navigateRoot(this.AddAddressProof, navigationExtras);
          //this.navCtrl.setRoot(AddAddressProof,{details : p});
        }
        else {
          let navigationExtras: NavigationExtras = {
            queryParams:
            {
              details: p,
            }
          };
          this.navCtrl.navigateRoot(this.AddtenantPage, navigationExtras);
          this.loaderView.dismissLoader();
        }
      }//End
      else {
        this.connectServer.getData("ServiceRequest", this.userData).then(
          resolve => {
            this.loaderView.dismissLoader();
            console.log('Response : ' + resolve);
            if (resolve['success'] == 1) {
              this.message = resolve['response']['message'];
              this.servicerequest_id = resolve['response']['new_sr_id'];
              if (this.images.length < 1) {
                var p = [];
                if (this.globalVars.MAP_USER_ROLE == "Member") {
                  p['dash'] = "society";
                  console.log(p);
                }
                else {
                  p['dash'] = "admin";
                }
                let navigationExtras: NavigationExtras = {
                  queryParams:
                  {
                    details: p,
                  }
                };

                // this.navCtrl.navigateForward(this.ServiceRequestPage, { state: { details: p } });
                this.navCtrl.navigateRoot(this.ServiceRequestPage, navigationExtras);
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
    }
  }

  public selectItems() {
    this.loaderView.showLoader('Loading ...');

  }
  public async presentActionSheet() {
    let actionSheet = await this.actionSheetCtrl.create({
      header: 'Select Image Source',
      buttons: [{
        text: 'Load from Library', handler: () => {
          //tobeun     this.takePicture(this.camera.PictureSourceType.PHOTOLIBRARY);
          this.selectImage(CameraSource.Photos);
        }
      },
      {
        text: 'Use Camera',
        handler: () => {
          this.selectImage(CameraSource.Camera);

          //tobeun  this.takePicture(this.camera.PictureSourceType.CAMERA);
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
    if (this.plt.is('hybrid')) {
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
  // public pathForImage(img) {
  //   console.log({ "selected img": img });
  //   if (img === null) {
  //     return '';
  //   }
  //   else {
  //     let win: any = window;
  //     // console.log({ "pathForImage3": win.Ionic.WebView.convertFileSrc(this.myImagePath) });
  //     return win.Ionic.WebView.convertFileSrc(this.myImagePath);
  //   }
  // }

  // Always get the accurate path to your apps folder
  // public pathForImage(img) {
  //   console.log({ "selected img": img });
  //   if (img === null) {
  //     return '';
  //   }
  //   else {
  //     let win: any = window;
  //     console.log({ "pathForImage3": win.Ionic.WebView.convertFileSrc(this.myImagePath) });
  //     return win.Ionic.WebView.convertFileSrc(this.myImagePath);
  //     //let win: any = window;
  //     // return win.Ionic.WebView.convertFileSrc(img)
  //     //return this.file.dataDirectory + img;
  //     // console.log({"hg": win.Ionic.WebView.convertFileSrc("file:///data/user/0/io.ionic.starter/cache/" + img)});
  //     // console.log({ "hgshjsj": win.Ionic.WebView.convertFileSrc(img) });
  //     // return win.Ionic.WebView.convertFileSrc(img);
  //   }
  // }
  // public pathForImageUpload(img) {
  //   //tobeun
  //   // console.log({ "selected img": img });
  //   // if (img === null) {
  //   //   return '';
  //   // }
  //   // else {
  //   //   let win: any = window;
  //   //   console.log({ "imgUpload": this.file.dataDirectory + img });
  //   //   return this.file.dataDirectory + img;

  //   //   // return win.Ionic.WebView.convertFileSrc(this.myImagePath);
  //   //   //let win: any = window;
  //   //   // return win.Ionic.WebView.convertFileSrc(img)
  //   //   //return this.file.dataDirectory + img;
  //   //   // console.log({"hg": win.Ionic.WebView.convertFileSrc("file:///data/user/0/io.ionic.starter/cache/" + img)});
  //   //   // console.log({ "hgshjsj": win.Ionic.WebView.convertFileSrc(img) });
  //   //   // return win.Ionic.WebView.convertFileSrc(img);
  //   // }
  // }
  /* public pathForImage1(img) {
     if (img === null) {
       return '';
     }
     else {
       let win: any = window;
       // return win.Ionic.WebView.convertFileSrc(img)
       // return cordova.file.dataDirectory + img;
       console.log({ "hg": win.Ionic.WebView.convertFileSrc("file:///data/user/0/io.ionic.starter/cache/" + img) });
       // console.log({"hgshjsj": win.Ionic.WebView.convertFileSrc( img)});
       // return win.Ionic.WebView.convertFileSrc("file:///data/user/0/io.ionic.starter/cache/" + img);
       return "file:///data/user/0/io.ionic.starter/cache/" + img;

     }
   }*/

  // public uploadImageOld() {

  //   // Destination URL
  //   //var url = "http://localhost/beta_aws_3";
  //   var url = "https://way2society.com/upload_image_from_mobile_new.php";

  //   // File for Upload
  //   var targetPath = this.pathForImageUpload(this.lastImage);

  //   // File name only
  //   var filename = this.lastImage;
  //   var options = {
  //     fileKey: "file",
  //     fileName: filename,
  //     chunkedMode: false,
  //     mimeType: "multipart/form-data",
  //     params: { 'fileName': filename, 'service_request_id': this.servicerequest_id, 'feature': 2, 'token': this.globalVars.USER_TOKEN, 'tkey': this.globalVars.MAP_TKEY }
  //   };

  //   const fileTransfer: FileTransferObject = this.transfer.create();
  //   // this.loading = this.loadingCtrl.create({ content: 'Uploading...', });
  //   // this.loading.present();

  //   //alert(targetPath);

  //   //this.presentToast(targetPath);

  //   // Use the FileTransfer to upload the image
  //   fileTransfer.upload(targetPath, encodeURI(url), options).then
  //     (data => {
  //       // this.loading.dismissAll()
  //       this.presentToast('Image successful uploaded.');
  //       this.navCtrl.navigateForward("ServicerequestPage");
  //       //need to handle next page state data
  //       //this.navCtrl.navigateForward("viewimposefine", { state: p });


  //     },
  //       err => {
  //         // this.loading.dismissAll()
  //         console.log({ "error": err });
  //         this.presentToast('Error while uploading file.');


  //         //need to handle next page state data
  //         this.navCtrl.navigateForward("ServicerequestPage");
  //       }
  //     );
  // }

  async uploadImage() {
    const file = this.images[0];

    const response = await fetch(file.data);
    const blob = await response.blob();

    const formData = new FormData();
    formData.append('file', blob, file.name);
    formData.append('fileName', file.name);
    formData.append('service_request_id', this.servicerequest_id);
    formData.append('feature', '2');
    formData.append('token', this.globalVars.USER_TOKEN);
    formData.append('tkey', this.globalVars.MAP_TKEY);

    const loading = await this.loadingCtrl.create({
      message: 'Uploading image...',
    });

    await loading.present();

    const url = "https://way2society.com/upload_image_from_mobile.php";

    this.http.post(url, formData)
      .pipe(finalize(() => loading.dismiss()))
      .subscribe(res => {
        this.presentToast('Image successfully uploaded.');

        var p = [];
        if (this.globalVars.MAP_USER_ROLE == "Member") {
          p['dash'] = "society";
          console.log(p);
        }
        else {
          p['dash'] = "admin";
        }
        let navigationExtras: NavigationExtras = {
          queryParams:
            {
              details: p,
            }
        };

        // this.navCtrl.navigateForward(this.ServiceRequestPage, { state: { details: p } });
        this.navCtrl.navigateRoot(this.ServiceRequestPage, navigationExtras);
      }, err => {
          this.presentToast('Image successful uploaded.');
          this.navCtrl.navigateForward(this.ServiceRequestPage);
      });
  }

}

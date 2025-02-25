import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AlertController, IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras, Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { ActionSheetController, ToastController, LoadingController } from '@ionic/angular';
import { Camera, CameraResultType, CameraSource, Photo } from '@capacitor/camera';
import { Directory, Filesystem } from '@capacitor/filesystem';
import { HttpClient } from '@angular/common/http';
import { finalize } from 'rxjs';
// import { File } from '@ionic-native/file/ngx';
// import { FileTransfer, FileTransferObject } from '@ionic-native/file-transfer/ngx';
// import { FilePath } from '@ionic-native/file-path/ngx';
// import { Camera } from '@ionic-native/camera/ngx';
// import { PhotoViewer } from "@ionic-native/photo-viewer/ngx";
// import { ImagePicker } from "@ionic-native/image-picker/ngx";



declare var cordova: any;
const IMAGE_DIR = 'stored-images';

interface LocalFile {
  name: string;
  path: string;
  data: string;
}


enum statusEnum { "Approved" = 1, "Denied", "Without Approval" };
enum priorityEnum { "Critical" = 1, "High", "Medium", "Low" };
@Component({
  selector: 'app-imageview',
  templateUrl: './imageview.page.html',
  styleUrls: ['./imageview.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule],
  providers: [
    // Camera, FileTransfer, File, FilePath, ImagePicker, PhotoViewer
  ]

})
export class ImageviewPage implements OnInit {
  img_array: Array<any>;
  ViewImagePage: any = 'viewimage';
  PhotoAlbumPage: any = 'photoalbum';
  img_src = "";
  folder;
  photos: any;
  albumID: any;
  displayData: Array<any>;

  image: string = null;
  images: LocalFile[] = [];
  selectedImageName;

  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute,
    private router: Router,
    // private camera: Camera,
    // private transfer: FileTransfer,
    // private file: File,
    // private filePath: FilePath,
    public actionSheetCtrl: ActionSheetController,
    public toastCtrl: ToastController,
    public navParams: NavParams,
    // private imagePicker: ImagePicker,
    // private photoViewer: PhotoViewer,
    public alertCtrl: AlertController,
    private loadingCtrl: LoadingController,
    private http: HttpClient,




  ) {

    this.img_array = [];
    // this.img_src = "";
    this.img_src = "https://way2society.com/uploads";
    // this.img_src = "http://localhost/master4/uploads";
    this.folder = "";
    this.albumID = 0;
    this.image = "";
    this.displayData = [];
    // let details: any;
    // details = this.router.getCurrentNavigation()?.extras?.state;
    // console.log(details);
    // this.displayData();
    // this.FetchImageInAlbumID(details);
  }

  ngOnInit() {
    let details: any;
    this.route.queryParams.subscribe(params => {
      details = params["details"];
    });
    this.displayData = details;
    console.log(details);

    console.log("displaydata", this.displayData);
    this.FetchImageInAlbumID();
  }
  FetchImageInAlbumID() {

    this.loaderView.showLoader('Loading ...');
    var obj = [];
    obj["fetch"] = "photoByAlbumID";
    obj["AlbumID"] = this.displayData['album_id'];
    console.log("ALbumID", this.displayData['album_id']);
    console.log("folder Nmae", this.displayData['folder']);
    this.connectServer.getData("Photoalbum", obj).then(
      resolve => {
        this.loaderView.dismissLoader();
        if (resolve['success'] == 1) {
          var AllImageData = resolve['response']['PhotoDetails'];
          for (var iCnt = 0; iCnt < Object.keys(AllImageData).length; iCnt++) {
            this.img_array.push(AllImageData[iCnt]);
          }

        }
      }
    );
  }
  getImage() {
    this.presentActionSheet();
  }

  public async presentActionSheet() {
    let actionSheet = await this.actionSheetCtrl.create({
      header: "Select Image Source",
      buttons: [
        {
          text: "Load from Library",
          handler: () => {
            this.selectImage(CameraSource.Photos);

            // this.getPictures();
            // this.takePicture(this.camera.PictureSourceType.PHOTOLIBRARY);
          }
        },
        {
          text: "Use Camera",
          handler: () => {
            this.selectImage(CameraSource.Camera);

            // this.takePicture(this.camera.PictureSourceType.CAMERA);
          }
        },
        {
          text: "Cancel",
          role: "cancel"
        }
      ]
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

  public viewImage(image) {
    var obj = {
      folder: this.folder,
      img: image
    };
    let navigationExtras: NavigationExtras = {
      queryParams:
      {
        details: obj,
      }
    };
    // this.navCtrl.navigateForward(this.ViewImagePage, { state: { details: obj} });
    this.navCtrl.navigateForward(this.ViewImagePage, { state: { details: image, details1: this.img_array } });

    //alert(image);
    // this.navCtrl.push(this.ViewImagePage, { details: image,details1 : this.img_array});
    // this.navCtrl.push(ViewImagePage, { details: image });
  }
  public saveMemberId() {
    console.log(this.globalVars.MAP_ID);
  }

  public saveTimeStamp() { }

  // public async getPictures() {
  //   // let options = {
  //   //   maximumImagesCount: 5
  //   // };
  //   // this.photos = new Array<string>();
  //   // this.imagePicker
  //   //   .getPictures(options)
  //   //   .then(results => {
  //   //     let alert =  this.confirmApprove();
  //   //     alert.present();

  //   //     alert.onDidDismiss(data => {
  //   //       for (let i = 0; i < results.length; i++) {
  //   //         this.uploadImage(results[i]);
  //   //       }
  //   //     });
  //   //   })
  //   //   .catch(err => alert(err));
  //   try {
  //     let options = {
  //       maximumImagesCount: 5
  //     };
  //     this.photos = new Array<string>();

  //     // Assuming that this.imagePicker.getPictures() returns a promise
  //     const results = await this.imagePicker.getPictures(options);

  //     let alert = await this.confirmApprove(); // Use await here to make sure the alert is created and presented
  //     await alert.present();
  //     const data = await alert.onDidDismiss();

  //     for (let i = 0; i < results.length; i++) {
  //       await this.uploadImage(results[i]);
  //     }
  //   } catch (err) {
  //     console.error(err);
  //     // Handle any errors here
  //   }
  // }
  public takePicture(sourceType) {
    // Create options for the Camera Dialog
    // var options = {
    //   quality: 100,
    //   saveToPhotoAlbum: true,
    //   sourceType: sourceType,
    //   correctOrientation: true,
    //   encodingType: this.camera.EncodingType.JPEG,
    //   mediaType: this.camera.MediaType.PICTURE
    // };

    // // Get the data of an image
    // this.camera.getPicture(options).then(
    //   (imagepath) => {
    //     // this.uploadImage(image);
    //     this.image = imagepath;
    //     if (this.platform.is('android') && sourceType === this.camera.PictureSourceType.PHOTOLIBRARY) {
    //       this.filePath.resolveNativePath(imagepath).then(
    //         filePath => {
    //           let correctPath = filePath.substr(0, filePath.lastIndexOf('/') + 1);
    //           let currentName = imagepath.substring(imagepath.lastIndexOf('/') + 1, imagepath.lastIndexOf('?'));
    //           this.copyFileToLocalDir(correctPath, currentName, this.createFileName());
    //         }
    //       );
    //     } else {
    //       var currentName = imagepath.substr(imagepath.lastIndexOf('/') + 1);
    //       var correctPath = imagepath.substr(0, imagepath.lastIndexOf('/') + 1);
    //       this.copyFileToLocalDir(correctPath, currentName, this.createFileName());

    //     }
    //   },
    //   err => {
    //     this.presentToast(`Error while getting image. ${err}`);
    //   }
    // );
  }
  private createFileName() {
    var d = new Date(),
      n = d.getTime(),
      newFileName = n + ".jpg";
    return newFileName;
  }
  // Copy the image to a local folder

  // private copyFileToLocalDir(namePath, currentName, newFileName) {
  //   this.file.copyFile(namePath, currentName, cordova.file.dataDirectory, newFileName).then(
  //     success => {
  //       this.image = newFileName;
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
      position: "top"
    });
    toast.present();
  }
  // public pathForImage(img) {
  //   console.log({ "selected img": img });
  //   if (img === null) {
  //     return '';
  //   }
  //   else {
  //     let win: any = window;
  //     console.log({ "pathForImage3": win.Ionic.WebView.convertFileSrc(this.image) });
  //     return win.Ionic.WebView.convertFileSrc(this.image);
  //     // let win: any = window;
  //     // return win.Ionic.WebView.convertFileSrc(img)
  //     //return this.file.dataDirectory + img;

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
  // public uploadImageOld(image) {
  //   this.confirmApprove();
  //   // alert("ALbum Id"+ this.displayData['album_id']);
  //   // alert("Folder"+ this.displayData['folder']);
  //   // Destination URL
  //   var url = "https://way2society.com/upload_image_from_mobile_new.php";
  //   //var url = "https://ef2a152f.ngrok.io/master4/upload_image_from_mobile_new.php";
  //   var targetPath = this.pathForImage(this.image);
  //   var filename = image;
  //   var options = {
  //     fileKey: "file",
  //     fileName: filename,
  //     chunkedMode: false,
  //     mimeType: "multipart/form-data",
  //     params: {
  //       file: filename,
  //       album_id: this.displayData['album_id'],
  //       album_name: this.displayData['folder'],
  //       feature: 6,
  //       token: this.globalVars.USER_TOKEN,
  //       tkey: this.globalVars.MAP_TKEY
  //     }
  //   };

  //   const fileTransfer: FileTransferObject = this.transfer.create();

  //   this.loaderView.showLoader("Uploading..");

  //   fileTransfer.upload(targetPath, encodeURI(url), options).then(
  //     data => {
  //       this.loaderView.dismissLoader();
  //       this.presentToast("Image successful uploaded.");
  //       // alert(JSON.stringify(data, null, 4));
  //       // alert("We have received your request. Please wait for approval.");
  //       this.navCtrl.navigateRoot(this.PhotoAlbumPage);
  //       return;
  //     },
  //     err => {
  //       this.loaderView.dismissLoader();
  //       // alert(JSON.stringify(err));
  //       this.presentToast("Error while uploading file.");
  //       this.navCtrl.navigateRoot(this.PhotoAlbumPage);
  //       return;
  //     }
  //   );
  //   this.loaderView.dismissLoader();
  // }
  async uploadImage() {
    this.confirmApprove();

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
        params:
        {
          file: file.name,
          album_id: this.displayData['album_id'],
          album_name: this.displayData['folder'],
          feature: 6,
          token: this.globalVars.USER_TOKEN,
          tkey: this.globalVars.MAP_TKEY
        }
      })
      .pipe(
        finalize(() => {
          loading.dismiss();
        })
      )
      .subscribe(res => {
        this.presentToast('Image successful uploaded.');
        this.navCtrl.navigateForward(this.PhotoAlbumPage);
      },
        err => {
          // this.presentToast('Error while uploading file.');
          this.presentToast('Image successful uploaded.');
          this.navCtrl.navigateForward(this.PhotoAlbumPage);
        }
      );
  }


  confirmApprove() {
    let alert = this.alertCtrl.create({
      header: "Upload Image",
      message: this.globalVars.USER_NAME + ", you want to upload this image?",
      buttons: [
        {
          text: "Cancel",
          role: "cancel",
          handler: () => {
            // alert.dismiss(true);
            //return false;
          }
        },
        {
          text: "Approve",
          handler: () => {
            // alert.dismiss(true);
            return true;
          }
        }
      ]
    });
    return alert;
  }
}

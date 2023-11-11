import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActionSheetController, IonicModule, LoadingController, NavController, NavParams, Platform, ToastController } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { ActivatedRoute, NavigationExtras } from '@angular/router';
import { HTTP } from '@ionic-native/http/ngx';
import { HttpClient, HttpClientModule, HttpHeaders} from '@angular/common/http';
import { FilePath } from '@ionic-native/file-path/ngx';
import { Camera } from '@ionic-native/camera/ngx';
import { ImagePicker } from '@ionic-native/image-picker/ngx';
import { File } from '@ionic-native/file/ngx';
import { FileTransfer, FileTransferObject } from '@ionic-native/file-transfer/ngx';
declare var cordova: any;


@Component({
  selector: 'app-addrenovationrequest',
  templateUrl: './addrenovationrequest.html',
  styleUrls: ['./addrenovationrequest.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule, HttpClientModule],
  providers: [HTTP, Camera , FileTransfer, File, FilePath, ImagePicker]
})
export class AddRenovationRequest implements OnInit {
  worklist_array :Array <any>;
  checkedworklist:Array <boolean>;
  uploadwork:any;
  base64Image:any;
  lastImage: string = null;
  // loading: Loading;
  details :any;
  serviceRequestId : any;
  data:any ;
  myImagePath: string = null;
  userData : {srTitle : any,srCategory : any,srPriority : any,template_id : any, unit_id : any, workStartDate : any, workEndDate :any,workType:any,location:any,workDetails:any,contractorName:any,contractorContact:any,contractorAddress:any,maxLabourer:any,labourerName:any};
  selectedWorkList: Array<any>;
  maxLabourer: number;
  labourers: Array<any>;
  renovationId : any;
  drawingFiles : Array<String>;  
  drawingFiles1 :any;  
  ServiceRequestPage: any = 'servicerequest';
  getdetails: {};
 
  httpHeader = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

 
  constructor(private navCtrl: NavController,
    public http: HttpClient,
    private globalVars: GlobalVars,
    private camera: Camera,
    private transfer: FileTransfer,
    private file: File,
    private filePath: FilePath,
    public actionSheetCtrl: ActionSheetController,
    public navParams: NavParams,
    public toastCtrl: ToastController,
    private loaderView : LoaderView,
    private platform: Platform,
    private route : ActivatedRoute,
    public loadingCtrl: LoadingController,
    private imagePicker: ImagePicker) {

      this.userData = {srTitle:"",srCategory:this.globalVars.RENOVATION_REQUEST_ID,srPriority:"",template_id : "", unit_id : "", workStartDate : "", workEndDate :"",workType:"",location:"",workDetails:"",contractorName:"",contractorContact:"",contractorAddress:"",maxLabourer:0,labourerName:[]};
        this.worklist_array =[];
       
        this.checkedworklist=[];
        this.uploadwork = '0';

        this.selectedWorkList = [];
        this.maxLabourer = 0;
        this.labourers = [];
        this.renovationId = 0;
        this.drawingFiles = [];
        this.drawingFiles1 ="";
        this.getdetails = [];
      
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
    console.log('ionViewDidLoad AddRenovationRequest');
    this.loaderView.dismissLoader();
    //alert(this.globalVars.LoginID);
    // this.details =this.navParams.get("details");
    let details: any;
    this.route.queryParams.subscribe(params => {
     details = params["details"];
     console.log("Query Parameters:", params);
     console.log(this.details);
     this.getdetails = details;
     this.getWorkTypeList();

    });
    // console.log(this.details);
    // this.userData.srTitle = this.details['title'];
    // this.userData.srPriority = this.details['priority'];
    // this.userData.unit_id = this.details['unitId'];
    // this.getWorkTypeList();

  }
  public getWorkTypeList() {
    var data = [];
    var link = this.globalVars.HOST_NAME + 'api.php';
    var myData = JSON.stringify({ method: "getWorkTypeList", societyId: this.globalVars.MAP_SOCIETY_ID, unitId: this.userData.unit_id, role: this.globalVars.MAP_USER_ROLE, login_id: this.globalVars.LoginID });
    this.http.post(link, myData, this.httpHeader)
      .subscribe(data => {
        this.data.response = data["_body"]; //https://stackoverflow.com/questions/39574305/property-body-does-not-exist-on-type-response
        //this.data.response = data["_body"]; //https://stackoverflow.com/questions/39574305/property-body-does-not-exist-on-type-response
        //console.log(this.data.response);
        var parsedData = JSON.parse(this.data.response);
        console.log(parsedData);
        var workListDetails = parsedData['response']['WorkList'];
        console.log("worK List ", workListDetails);
        for (var i = 0; i < workListDetails.length; i++) {
          // alert(workListDetails[i]);
          // //// this.work = workListDetails[i]['work'];
          //this.work_require = workListDetails[i]['drawingReq'];
          this.worklist_array.push(workListDetails[i]);
          this.checkedworklist.push(false);
        }
        console.log("workLIstArray", this.worklist_array);

      }, error => {
        console.log("Oooops!");
      });
  }
  public setMaximumLabourer() {
    // this.maxLabourer = max;
    // alert(this.maxLabourer);
    // let items = [];
    for (let i = 0; i < this.maxLabourer; i++) {
      this.labourers[i] = i + 1;
    }
  }

  CheckWorklist(value, i) {
    this.checkedworklist[i] = !this.checkedworklist[i];
    this.selectedWorkList.push(value);
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
  public takePicture(sourceType) {
    // Create options for the Camera Dialog
    var options =
    {
      quality: 100,
      sourceType: sourceType,
      saveToPhotoAlbum: false,
      correctOrientation: true
    };
    // Get the data of an image
    this.camera.getPicture(options).then(
      (imagePath) => {
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
        this.drawingFiles.push(newFileName);
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
  // Always get the accurate path to your apps folder
  public pathForImage(img) {
    if (img === null) {
      return '';
    }
    else {
      // return cordova.file.dataDirectory + img;
      let win: any = window;
      console.log({ "pathForImage3": win.Ionic.WebView.convertFileSrc(this.myImagePath) });
      return win.Ionic.WebView.convertFileSrc(this.myImagePath);
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
  public createRenovationRequest() {
    console.log("drawing file length : ", this.drawingFiles.length);
    if (this.userData.workStartDate.length == 0) {
      alert("Please provide work start date.");
    }
    else if (this.userData.workEndDate.length == 0) {
      alert("Please provide work end date.");
    }
    else if (this.selectedWorkList.length == 0) {
      alert("Please select at least one Work Type.");
    }
    else if (this.userData.location.length == 0) {
      alert("Please provide work location.");
    }
    else if (this.userData.contractorName.length == 0) {
      alert("Please provide Contractor Name.");
    }
    else if (this.userData.contractorContact.length == 0) {
      alert("Please provide Contractor Contact.");
    }
    else if (this.userData.maxLabourer == 0) {
      alert("Please provide maximum laburer.");
    }
    //Uncomment this code for file validation
    /*else if(this.drawingFiles.length == 0)
    {
       for(var workId in this.selectedWorkList)
       {
         if(this.selectedWorkList[workId] == "Civil")
         {
           alert ("Please attach drawing file for Civil.")
         }
         else if(this.selectedWorkList[workId] == "Plumbing")
         {
           alert ("Please attach drawing file for Plumbing.")
         }
         else if(this.selectedWorkList[workId] == "Kitchen")
         {
          alert ("Please attach drawing file for Kitchen.")
         }
         else
         {

         }
       }
     }*/
    else {
      console.log("Unit Id : ", this.userData.unit_id);
      var data = [];
      var link = this.globalVars.HOST_NAME + 'api.php';
      var myData = JSON.stringify({ method: "submitRenovationDetails", srTitle: this.userData.srTitle, srPriority: this.userData.srPriority, srCategory: this.userData.srCategory, loginId: this.globalVars.MAP_LOGIN_ID, societyId: this.globalVars.MAP_SOCIETY_ID, unitId: this.userData.unit_id, role: this.globalVars.MAP_USER_ROLE, login_id: this.globalVars.LoginID, serviceRequestId: this.serviceRequestId, startDate: this.userData.workStartDate, endDate: this.userData.workEndDate, workDetails: this.userData.workDetails, workType: this.selectedWorkList, location: this.userData.location, contractorName: this.userData.contractorName, contractorContact: this.userData.contractorContact, contractorAddress: this.userData.contractorAddress, maxLabourer: this.userData.maxLabourer, labourerName: this.userData.labourerName });
      this.http.post(link, myData)
        .subscribe(data => {
          this.data.response = data["_body"]; //https://stackoverflow.com/questions/39574305/property-body-does-not-exist-on-type-response
          console.log("createRenovation :", this.data.response);
          var renovationResult = JSON.parse(this.data.response);
          var resultStatus = renovationResult['success'];
          console.log("resultStatus :", resultStatus);
          var rId = renovationResult['response']['renovationId'];
          this.renovationId = rId;
          if (resultStatus == "1") {
          }
          else {
            alert("We are unable to connect our server.Please try after some time.");
          }
          var url = "https://way2society.com/upload_image_from_mobile.php";
          // File for Upload
          //alert(url);
          var options: any;
          if (this.drawingFiles.length > 0) {
            //alert("If Condition");
            for (var file in this.drawingFiles) {
              // alert("file"+this.drawingFiles[file]);
              var targetPath = this.pathForImage(this.drawingFiles[file]);
              var filename = this.drawingFiles[file];
              options = {
                fileKey: "file",
                fileName: filename,
                chunkedMode: false,
                mimeType: "multipart/form-data",
                params: { 'fileName': filename, "renovationId": this.renovationId, 'feature': 6, 'token': this.globalVars.USER_TOKEN, 'tkey': this.globalVars.MAP_TKEY, 'unitId': this.userData.unit_id }
              };
              const fileTransfer: FileTransferObject = this.transfer.create();
              // this.loading = this.loadingCtrl.create({ content: 'Uploading...', });
              // this.loading.present();
              // Use the FileTransfer to upload the image
              fileTransfer.upload(targetPath, encodeURI(url), options).then
                (data => {
                  // this.loading.dismissAll()
                  this.presentToast('Image successful uploaded.');
                  this.navCtrl.navigateRoot(this.ServiceRequestPage);
                },
                  err => {
                    // this.loading.dismissAll()
                    this.presentToast('Error while uploading file.');
                    this.navCtrl.navigateRoot(this.ServiceRequestPage);
                  }
                );
            }
          }
          var p = [];
          if (this.globalVars.MAP_USER_ROLE == "Member") {
            p['dash'] = "society";
            //console.log(p);
          }
          else {
            p['dash'] = "admin";
          }
          let navigationExtras: NavigationExtras = {
            queryParams: 
            {
              details :p,
            }
          };

          this.navCtrl.navigateRoot(this.ServiceRequestPage, navigationExtras);
        }, error => {
          console.log("Oooops!");
        });
    }
    //console.log("Drawing files : ",this.drawingFiles);
  }
  
}

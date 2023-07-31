import { Component, OnInit,CUSTOM_ELEMENTS_SCHEMA, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NavController, NavParams, ActionSheetController, ToastController, Platform, LoadingController, IonicModule } from '@ionic/angular';
import { File } from '@ionic-native/file/ngx';
import { FileTransfer, FileTransferObject } from '@ionic-native/file-transfer/ngx';
import { FilePath } from '@ionic-native/file-path/ngx';
import { Camera } from '@ionic-native/camera/ngx';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';

declare var cordova: any;
enum statusEnum  { "Raised" = 1,  "Waiting",  "In progress",  "Completed", "Cancelled"}
enum priorityEnum  { "Critical" = 1,  "High",  "Medium",  "Low"}
@Component({
  selector: 'app-tenant',
  templateUrl: './addtenant.page.html',
  styleUrls: ['./addtenant.page.scss'],
  standalone: true,
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ],
  imports: [IonicModule, CommonModule, FormsModule,],
  providers: [Camera, FileTransfer, File, FilePath]
})

export class AddTenantPage implements OnInit {
  DashboardPage:any='dashboard';
  addType :any;
	profession_array:Array<any>;
	todayDate : any;
	ShowCIN : any;
	CheckMoreTanent:boolean;
	ShowMoreTenant:any;
	Tenantindex:number=0;
	docindex:number=0;
	documentImage : Array <any>;
	addmoredoc : number;
	base64Image:any;
	//loading: Loading;
	displayData : any;
	unitid :any;
	TenantFamilyDetail : {Name:any, Relation: any, DOB:any, Contact: any,Email:any};
	TenantData : {TFirstName :any,TMiddleName:any,TLastName:any,TDob:any,TContact:any,TEmail:any,Tprofession:any,LeaseStart:any,LeaseEnd:any,TenantFamily : Array <{}>,TAddress:any,TCity:any,TPincode:any,TNote:any,TAgentName:any,TAgentNumber:any,TenantFamilyJSON:any};
	uploadDoc: {DocName:any,Doc_img:any}
	userData :{uploadDoc : Array<any>};

	
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute,
    private camera: Camera,
    private transfer: FileTransfer,
    private file: File,
    private filePath: FilePath,
    private actionSheetCtrl: ActionSheetController,
    private toastCtrl: ToastController,
    private loadingCtrl: LoadingController) { 
      
      this.displayData= [];
      
      this.addType="1";
      this.profession_array=[];
      this.todayDate = new Date().toISOString();
      this.ShowCIN = '0';
      this.CheckMoreTanent=false;
      this.ShowMoreTenant ='0';
      this.documentImage = [];
      this.addmoredoc = 0;
      this.TenantFamilyDetail = {Name:"", Relation:"", DOB:"", Contact:"", Email: ""};
      this.TenantData ={TFirstName :"",TMiddleName:"",TLastName:"",TDob:"",TContact:"",TEmail:"" ,Tprofession:"1",LeaseStart:"",LeaseEnd:"",TenantFamily : [],TAddress:"",TCity:"",TPincode:"",TNote:"",TAgentName:"",TAgentNumber:"",TenantFamilyJSON:""};
      this.TenantData.TenantFamily[this.Tenantindex] =  {Name:"", Relation:"", DOB:"", Contact:"", Email: ""};
      this.uploadDoc= {DocName:"",Doc_img:""};
      this.userData = {uploadDoc : []};
      this.userData.uploadDoc[this.docindex]={DocName:"",Doc_img:""};
		
  }

  ngOnInit() {
    let details:any;
    this.route.queryParams.subscribe(params => {
      details = params["details"];
    });
    this.displayData =details; //this.params.get("details");
      //console.log("displat", this.displayData);
    this.unitid =this.displayData['unit_id'];
    
    this.fetchProfession();
  }
  @HostListener('document:ionBackButton', ['$event'])
  overrideHardwareBackAction(event: any) {
    event.detail.register(100, async () => {
      event.stopImmediatePropagation();
      event.stopPropagation();
      event.preventDefault();
    });
  }
  
  addStatus(valueType)
  {
   // alert(valueType);
    if(valueType == 2)
    {
    	this.addType="2";
      //this.navCtrl.setRoot(ViewregistrationPage);
      this.loaderView.dismissLoader();
    }
    else
    {
      this.addType="1";
    	this.loaderView.dismissLoader();
    }
  }

  fetchProfession()
  {
  	var obj = [];
  	obj['fetch'] = "Profession";
	  this.loaderView.showLoader('Loading ...'); 
  	this.connectServer.getData("LeaseServlet", obj).then(
  	  resolve => { 
        this.loaderView.dismissLoader();
        if(resolve['success'] == 1)
        {
          var ProfessionDetails = resolve['response']['Profession'];
          console.log("profession0 :" , ProfessionDetails);
          for(var iCnt=0; iCnt < Object.keys(ProfessionDetails).length; iCnt++)
          {
            this.profession_array.push(ProfessionDetails[iCnt]);
          }
          console.log("profession :" , this.profession_array);
        }
        else
        {}
      }
    );
  }

  CheckMT()
  {
    if(this.CheckMoreTanent == true)
    {
      this.ShowMoreTenant ='1';  
    }
    else
    {
     this.ShowMoreTenant ='0';      
    }
  }

  AddMoreTenamtFamily()
  {
    this.Tenantindex= this.Tenantindex + 1;
    this.TenantData.TenantFamily[this.Tenantindex] = {Name:"", Relation:"", DOB:"", Contact:"", Email: ""};
     //this.TenantData.TenantFamily[this.Tenantindex] = {Name:"", Relation:"", DOB:"", Contact:"", Email: "",Adhaar:"", CoSingAgreement:0};
  }

  submit()
  {
  	if(this.TenantData.TFirstName.length == 0)
    {
      alert("Please enter tenant first name");
    }
    else if(this.TenantData.TLastName.length == 0)
    {
      alert("Please enter tenant last name");
    }
    else if(this.TenantData.TDob.length == 0)
    {
      alert("Select Tenant date of birth");
    }
    else if(this.TenantData.LeaseStart.length == 0)
    {
      alert("Select Lease start date");
    }
    else if(this.TenantData.LeaseEnd.length == 0)
    {
      alert("Select Lease start date");
    }
    else
    {
  	  this.loaderView.showLoader('Loading ...'); 
  	  this.TenantData.TenantFamilyJSON =JSON.stringify(this.TenantData.TenantFamily);
      this.TenantData.TenantFamilyJSON = encodeURIComponent( this.TenantData.TenantFamilyJSON);
      this.TenantData['fetch'] ="addTenant";
      var FamilyData =this.TenantData.TenantFamily; 
      this.TenantData.TenantFamily=null;
  	  this.connectServer.getData("LeaseServlet", this.TenantData).then(
  	    resolve => { 
          this.loaderView.dismissLoader();
          if(resolve['success'] == 1)
          {
            this.TenantData.TenantFamily= FamilyData;  
            this.navCtrl.navigateRoot(this.DashboardPage);
		        //this.navCtrl.setRoot(DashboardPage);  
          }
          else
          {}
        }
      );
    }
  }

  AddMoreDoc()
	{
		this.docindex= this.docindex + 1;
		alert(this.docindex);
		this.userData.uploadDoc[this.docindex]={DocName:"",Doc_img:""};
  }

  /*  ---------------------------- Image View Functions  -----------------------*/

  public selectItems() {
    this.loaderView.showLoader('Loading ...');

  }
  
 
  public async presentActionSheet(documentType, documentIndex)
  {
		let actionSheet = await this.actionSheetCtrl.create({
    header: 'Select Image Source',
		buttons: [{text: 'Load from Library',handler: () => {
        this.takePicture(this.camera.PictureSourceType.PHOTOLIBRARY, documentType, documentIndex);}},
		    {
			    text: 'Use Camera',
			    handler: () => {this.takePicture(this.camera.PictureSourceType.CAMERA, documentType, documentIndex);}
			  },
			  {
			    text: 'Cancel',
			    role: 'cancel'
			  }]
		  });
		  actionSheet.present();
		}

   public takePicture(sourceType, documentType, documentIndex)
		{// Create options for the Camera Dialog
		  var options = {
		    quality: 100,
		    sourceType: sourceType,
		    saveToPhotoAlbum: false,
		    correctOrientation: true
		  };
		  // Get the data of an image
		  this.camera.getPicture(options).then(
			  (imagePath) => {
          // Special handling for Android library
          if (this.platform.is('android') && sourceType === this.camera.PictureSourceType.PHOTOLIBRARY)
          {
            this.filePath.resolveNativePath(imagePath).then(
              filePath => {
                  let correctPath = filePath.substr(0, filePath.lastIndexOf('/') + 1);
                  let currentName = imagePath.substring(imagePath.lastIndexOf('/') + 1, imagePath.lastIndexOf('?'));
                  this.copyFileToLocalDir(correctPath, currentName, this.createFileName(), documentType, documentIndex);
                }
              );
          }
          else
          {
            var currentName = imagePath.substr(imagePath.lastIndexOf('/') + 1);
            var correctPath = imagePath.substr(0, imagePath.lastIndexOf('/') + 1);
            this.copyFileToLocalDir(correctPath, currentName, this.createFileName(), documentType, documentIndex);
          }
        },
  			(err) => {
					this.presentToast('Error while selecting image.');
        }
    );
  }

	// Create a new name for the image
	private createFileName()
  {
		var d = new Date(),
		n = d.getTime(),
		newFileName =  n + ".jpg";
		return newFileName;
  }

	// Copy the image to a local folder
	private copyFileToLocalDir(namePath, currentName, newFileName, documentType, documentIndex)
  {
		this.file.copyFile(namePath, currentName, cordova.file.dataDirectory, newFileName).then(
      success => {
        //if(documentType == 'profile') {
        //	this.profileImage = newFileName;
        //}
        //else if(documentType == 'document') {
        var objFileDetail = [];
        objFileDetail['documentId'] = this.userData.uploadDoc['DocName'][this.docindex];
        this.userData.uploadDoc[this.docindex]['Doc_img'] = newFileName;
        objFileDetail['documentImage'] = newFileName;
        this.documentImage.push(objFileDetail);
      //}
      },
      error => {
        this.presentToast('Error while storing file.');
      }
    );
  }

	private async presentToast(text)
  {
		let toast = await this.toastCtrl.create({
      message: text,
      duration: 3000,
      position: 'top'
    });
		toast.present();
  }

	// Always get the accurate path to your apps folder
	/*--------------------------------  fetch image --------------------------*/
	public pathForImage(img)
  {
		if (img === null)
    {
			return '';
    }
    else
    {
			return cordova.file.dataDirectory + img;
			//return "file:///data/user/0/io.ionic.starter/cache/"+img
    }
  }
  public pathForImage1(img) {
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
  }
	/*-------------------------------- fetch Doc image --------------------------*/
 /* public pathForDoc(Doc_img)
  {
		if (Doc_img === null)
    {
      return '';
    }
    else if(this.platform.is('core') || this.platform.is('mobileweb'))
    {
      return '';
    }
    else
    {
			return cordova.file.dataDirectory + Doc_img;
			//return "file:///data/user/0/io.ionic.starter/cache/"+Doc_img
    }
  }*/

  public uploadDocument(documentId, documentName)
  {
		// Destination URL
		//var url = "http://192.169.1.117/beta_aws_2/ads";
		var url = "https://way2society.com/upload_image_from_mobile_new1.php";

		// File for Upload
		var targetPath = this.pathForImage(documentName);

		// File name only
		var filename =documentName;
		var options = {
			fileKey: "file",
			fileName: filename,
			chunkedMode: false,
			mimeType: "multipart/form-data",
			params : {'fileName': filename, 'unitid' : this.unitid, 'feature' : 8, 'token' : this.globalVars.USER_TOKEN, 'tkey' : this.globalVars.MAP_TKEY, 'DocName' : documentId}
		};
    //const fileTransfer: FileTransferObject = this.transfer.create();
		const fileTransfer: FileTransferObject = this.transfer.create();
		//this.loading = this.loadingCtrl.create({content: 'Please wait... \nUploading document(s) will take some time.',});
		//this.loading.present();
    // Use the FileTransfer to upload the image
    
		fileTransfer.upload(targetPath, encodeURI(url), options).then
			(data =>{
					//	this.loading.dismissAll()
						this.presentToast('Image successful uploaded.');
					
					},
          err => {
					//	this.loading.dismissAll()
						this.presentToast('Error while uploading file.');
					}
			);
		}

}

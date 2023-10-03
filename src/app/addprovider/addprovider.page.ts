import { Component, OnInit,CUSTOM_ELEMENTS_SCHEMA, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { NavController, NavParams, ActionSheetController, ToastController, Platform, LoadingController, IonicModule } from '@ionic/angular';

import { File } from '@ionic-native/file/ngx';
import { FileTransfer, FileTransferObject } from '@ionic-native/file-transfer/ngx';
import { FilePath } from '@ionic-native/file-path/ngx';
import { Camera } from '@ionic-native/camera/ngx';
import { ServiceproviderPage } from '../serviceprovider/serviceprovider.page';

declare var cordova: any;
@Component({
  selector: 'app-addprovider',
  templateUrl: './addprovider.page.html',
  styleUrls: ['./addprovider.page.scss'],
  standalone: true,
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ],
  imports: [IonicModule, CommonModule, FormsModule,],
  providers: [Camera, FileTransfer, File, FilePath]
})

export class AddProviderPage implements OnInit {
 userData :{staffid : any,name : any, img : any, cat_id : Array<{}>, dob : any, identity_mark : any, working_since : any, education : any, married: any, cur_add : any, cont_no: any, alt_cont_no : any, per_add : any, per_cont_no : any, per_alt_cont_no : any, ref_name : any, ref_add : any, ref_cont_no : any, ref_alt_cont_no : any, f_name : any, f_occu : any, m_name : any, m_occu : any, hus_wife_name : any, hus_wife_occu : any, son_dot_name : any, son_dot_occu : any, other_name : any, other_occu : any, unit_id : Array <{}>, document_id : Array<{}>,Doc_img : Array<{}>};
 ServiceproviderPage: any ='serviceprovider';
 cat_list : Array <any>;
 doc_list : Array <any>;
 Unit_list : Array <any>;
 todayDate : any;
 service_prd_id : any;
 addContact:number;
 refDetail : number;
 addFamily : number;
 addDocument : number;
 addUnits : number;
 addmore: number;
 addmoredoc : number;
 addmorecat : number;
 message : string;
 staffID : any;
 options:any;
 base64Image:any;
 profileImage: string = null;
 myImagePath: string = null;
 //loading: Loading;
 unitindex:number=0;
 docindex:number=0;
 catindex:number=0;
 documentImage : Array <any>;
 Block_unit=0;
 Block_desc="";
	
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams, private camera: Camera,
    private transfer: FileTransfer,
    private file: File,
    private filePath: FilePath,
    private actionSheetCtrl: ActionSheetController,
    private toastCtrl: ToastController,
    private loadingCtrl: LoadingController) { 
      this.todayDate = new Date().toISOString();
      this.userData = {staffid : "" ,name : "", img : "", cat_id : [], dob : "", identity_mark : "", working_since : "", education : "", married:"", cur_add : "", cont_no : "", alt_cont_no : "", per_add : "", per_cont_no : "", per_alt_cont_no : "", ref_name : "", ref_add : "" , ref_cont_no : "", ref_alt_cont_no : "", f_name : "", f_occu : "", m_name : "", m_occu : "", hus_wife_name : "", hus_wife_occu : "", son_dot_name : "", son_dot_occu : "", other_name : "", other_occu : "", unit_id : [],document_id : [],Doc_img : []};
      this.cat_list = [];
      this.doc_list = [];
      this.Unit_list = [];
      this.documentImage = [];
      this.service_prd_id = 0;
      this.addContact = 0;
      this.refDetail = 0;
      this.addFamily = 0;
      this.addDocument = 0;
      this.addUnits = 0;
      this.addmore = 0;
      this.addmoredoc = 0;
      this.addmorecat = 0;
      this.staffID = "";
      this.userData.unit_id[this.unitindex] = 0;
      this.userData.document_id[this.docindex] = 0;
      this.userData.cat_id[this.catindex] = 0;
	}

  ngOnInit() {
    this.Block_unit=this.globalVars.MAP_UNIT_BLOCK;
    this.Block_desc=this.globalVars.MAP_BLOCK_DESC;
    this.fetchCategories();
  }
  @HostListener('document:ionBackButton', ['$event'])
  overrideHardwareBackAction(event: any) {
    event.detail.register(100, async () => {
      event.stopImmediatePropagation();
      event.stopPropagation();
      event.preventDefault();
    });
  }
  getStaffID()
  {
    var objData = [];
    this.connectServer.getData("ServiceProvider/GetStaffIdStatus", objData).then(
      resolve => {
        if(resolve['success'] == 1)
        {
          var staffdata = resolve['response']['StaffID'];
          console.log('test ' + staffdata[0]);
          console.log('test ' + staffdata[0]['StaffIDL']);
          this.staffID = staffdata[0]['StaffIDL'] ; 
        }
      }
    );
  }
  fetchCategories()
  {
    this.loaderView.showLoader('Loading ...');
    var objData = [];
    this.connectServer.getData("ServiceProvider/fetchCategory", objData).then(
      resolve => {
        this.fetchDocumentsList();
        this.loaderView.dismissLoader();
        if(resolve['success'] == 1)
        {
          console.log(resolve);
          var categoryList = resolve['response']['providerCategory'];
          for(var i = 0; i < categoryList.length; i++)
          {
            this.cat_list.push(categoryList[i]);
          }
        }
      }
    );
  }
  
  fetchDocumentsList()
  {
    var objData = [];
    this.connectServer.getData("ServiceProvider/fetchDocuments", objData).then(
        resolve => {
        	this.fetchUnitList();
          // this.loaderView.dismissLoader();
          if(resolve['success'] == 1)
          {
            console.log(resolve);
            var DocumentList = resolve['response']['document'];
            for(var i = 0; i < DocumentList.length; i++)
            {
              this.doc_list.push(DocumentList[i]);
            }
          }
        }
      );
  }
  
  fetchUnitList()
  {

    var objData = [];
    this.getStaffID();
    this.connectServer.getData("ServiceProvider/fetchUnits", objData).then(
      resolve => {
        //  this.loaderView.dismissLoader();
        if(resolve['success'] == 1)
        {
          console.log(resolve);
          var UnitList = resolve['response']['member']['0'];
          for(var i = 0; i < UnitList.length; i++)
          {
            this.Unit_list.push(UnitList[i]);
          }
        }
      }
    );
}
AddContactDetail()
{
  this.addContact = 1;
}
AddRefDetail()
{
  this.refDetail = 1;
}
AddFamilyDetail()
{
    this.addFamily = 1;
}
AddDocumentDetails()
{
   this.addDocument = 1;
}
AddUnitDetails()
{
  this.addUnits = 1;
}

/*      -------------------  Multiple Doc --------------------*/
AddMoreDoc()
{
  this.docindex= this.docindex + 1;
  this.userData.document_id[this.docindex] = 0;
  this.userData.Doc_img[this.docindex] = '';
  this.docSelected();
}
showButton(index)
{
  if(this.userData.document_id[index] != 0)
  {
    return true;
  }
  return false;
}
docSelected()
{
  this.addmoredoc = 0;
  var selectedCount = -1;
  for (var i = 0; i < this.userData.document_id.length; i++)
  {
    if(this.userData.document_id[i] != 0)
    {
      selectedCount++;
    }
  }

  if(selectedCount == this.docindex)
  {
    this.addmoredoc = 1;
  }
}
/*      -------------------  Multiple Unit --------------------*/
AddMoreUnit()
{
  this.unitindex= this.unitindex + 1;
  this.userData.unit_id[this.unitindex] = 0;
  this.unitSelected();
}

unitSelected()
{
  this.addmore = 0;
  var selectedCount = -1;
  for (var i = 0; i < this.userData.unit_id.length; i++)
  {
    if(this.userData.unit_id[i] != 0)
    {
      selectedCount++;
    }
  }
  if(selectedCount == this.unitindex)
  {
    this.addmore = 1;
  }
}
/*      -------------------  Multiple Category --------------------*/

AddMoreCat()
{
  this.catindex= this.catindex + 1;
  this.userData.cat_id[this.catindex] = 0;
  this.catSelected();
}

catSelected()
{
  this.addmorecat = 0;
  var selectedCount = -1;
  for (var i = 0; i < this.userData.cat_id.length; i++)
  {
    if(this.userData.cat_id[i] != 0)
    {
      selectedCount++;
    }
  }
  if(selectedCount == this.catindex)
  {
    this.addmorecat = 1;
  }
}
create()
{

	this.message = "";
	if(this.userData.staffid.length == 0)
	{
		this.message = "Please Enter Society's Staff Id";
	}
  else if(this.userData.name.length == 0)
  {
    this.message = "Please enter Provider Name";
  }
  else if(this.userData.cat_id.length == 0)
  {
    this.message = "Please Select Category";
  }
  else if(this.userData.dob.length == 0)
  {
    this.message = "Please Select Service Provider Date of Birth";
  }
  else if(this.userData.working_since.length == 0)
  {
    this.message = "Please select Working on society date";
  }
  else if(this.userData.cur_add.length == 0)
  {
    this.message = "Please enter Current address";
  }
  else if(this.userData.cont_no.length == 0)
  {
    this.message = "Please enter provider contact no.";
  }
  else
  {
		this.loaderView.showLoader('Please Wait ...');
		console.log(this.userData);
		this.connectServer.getData("ServiceProvider/addServiceProvider1", this.userData).then(
      resolve => {
        this.loaderView.dismissLoader();
        console.log('Response : ' + resolve);
        if(resolve['success'] == 1)
        {
          this.service_prd_id = resolve['response']['ServiceProID'];
          if(this.profileImage === null)
          {
            var p=[];
            p['tab']='0';
            p['dash']= "society";
            //this.navCtrl.setRoot(ServiceproviderPage,{details : p});
            let navigationExtras: NavigationExtras = {
              queryParams: 
              {
                details :p,
              }
            };
           
            this.navCtrl.navigateForward(this.ServiceproviderPage, { state: { details: p } });
          }
          else
          {
            // alert("call image function");
            this.uploadImage();
            for(var iImage = 0; iImage < this.documentImage.length; iImage++) {
              this.uploadDocument(this.documentImage[iImage]['documentId'], this.documentImage[iImage]['documentImage']);
              }
          }
        }
        else
        {
           alert("We have received your request. Please wait for approval.");
           var p=[];
           p['dash']="admin";
           let navigationExtras: NavigationExtras = {
            queryParams: 
            {
              details :p,
            }
          };
         
          this.navCtrl.navigateForward(this.ServiceproviderPage, { state: { details: p } });
         // this.navCtrl.setRoot(ServiceproviderPage,{details : p});
          this.message = resolve['response']['message'];
        }
      }
    );
  }
  }

  keyUpChecker(ev) {
    let elementChecker: string;
    let format = /^[a-zA-Z ]*$/i;
    elementChecker = ev.target.value;
    console.log(ev.target.value);
    if(!format.test(elementChecker)){
      this.userData.name = elementChecker.slice(0, -1);
    }
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
        this.myImagePath = imagePath;
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
 
    private createFileName()
		{
		var d = new Date(),
		n = d.getTime(),
		newFileName =  n + ".jpg";
		return newFileName;
		}
    
    private copyFileToLocalDir(namePath, currentName, newFileName, documentType, documentIndex)
		{
		    this.file.copyFile(namePath, currentName, cordova.file.dataDirectory, newFileName).then(
				    success => {
							if(documentType == 'profile') {
								this.profileImage = newFileName;
							}
							else if(documentType == 'document') {
							var objFileDetail = [];
							objFileDetail['documentId'] = this.userData['document_id'][documentIndex];
							this.userData.Doc_img[this.docindex] = newFileName;
							objFileDetail['documentImage'] = newFileName;
							this.documentImage.push(objFileDetail);
							}
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
    public pathForImage(img) {
      console.log({ "selected img": img });
      if (img === null) {
        return '';
      }
      else {
        let win: any = window;
      console.log({ "pathForImage3": win.Ionic.WebView.convertFileSrc(this.myImagePath) });
      return win.Ionic.WebView.convertFileSrc(this.myImagePath);
        //let win: any = window;
        // return win.Ionic.WebView.convertFileSrc(img)
       // return this.file.dataDirectory + img;
        // console.log({"hg": win.Ionic.WebView.convertFileSrc("file:///data/user/0/io.ionic.starter/cache/" + img)});
        // console.log({ "hgshjsj": win.Ionic.WebView.convertFileSrc(img) });
        // return win.Ionic.WebView.convertFileSrc(img);
      }
    }
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
   
	/*-------------------------------- fetch Doc image --------------------------*/
  
    public pathForDoc(Doc_img) {
      console.log({ "selected img": Doc_img });
      if (Doc_img === null) {
        return '';
      }
      else {
        let win: any = window;
      console.log({ "pathForImage3": win.Ionic.WebView.convertFileSrc(this.myImagePath) });
      return win.Ionic.WebView.convertFileSrc(this.myImagePath);
        //let win: any = window;
        // return win.Ionic.WebView.convertFileSrc(img)
       //return this.file.dataDirectory + Doc_img;
        // console.log({"hg": win.Ionic.WebView.convertFileSrc("file:///data/user/0/io.ionic.starter/cache/" + img)});
        // console.log({ "hgshjsj": win.Ionic.WebView.convertFileSrc(img) });
        // return win.Ionic.WebView.convertFileSrc(img);
      }
    }
   /* public pathForDoc1(Doc_img) {
      if (Doc_img === null) {
        return '';
      }
      else {
        let win: any = window;
        // return win.Ionic.WebView.convertFileSrc(img)
        // return cordova.file.dataDirectory + img;
        console.log({ "hg": win.Ionic.WebView.convertFileSrc("file:///data/user/0/io.ionic.starter/cache/" + Doc_img) });
        // console.log({"hgshjsj": win.Ionic.WebView.convertFileSrc( img)});
        // return win.Ionic.WebView.convertFileSrc("file:///data/user/0/io.ionic.starter/cache/" + img);
        return "file:///data/user/0/io.ionic.starter/cache/" + Doc_img;
  
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
			params : {'fileName': filename, 'serviceProvider_Id' : this.service_prd_id, 'feature' : 5, 'token' : this.globalVars.USER_TOKEN, 'tkey' : this.globalVars.MAP_TKEY, 'documentId' : documentId}
		};

		//const fileTransfer: TransferObject = this.transfer.create();
    const fileTransfer: FileTransferObject = this.transfer.create();
		
 				// Use the FileTransfer to upload the image
		fileTransfer.upload(targetPath, encodeURI(url), options).then
			(data =>{
						//this.loading.dismissAll()
						this.presentToast('Image successful uploaded.');
					//	alert("We have received your request. Please wait for approval.");
						 var p=[];
            p['tab']='0';
            p['dash']= "society";
            this.navCtrl.navigateForward("ServiceproviderPage", { state: p });
            //this.navCtrl.setRoot(ServiceproviderPage,{details : p});
					},
			 err => {
						//this.loading.dismissAll()
						this.presentToast('Error while uploading file.');
						var p=[];
                p['tab']='0';
                p['dash']= "society";
                this.navCtrl.navigateForward("ServiceproviderPage", { state: p });
                //this.navCtrl.setRoot(ServiceproviderPage,{details : p});
					}
			);
		}
    
	public uploadImage()
  {
     // Destination URL
     var url = "https://way2society.com/upload_image_from_mobile_new1.php";
      // File for Upload
     var targetPath = this.pathForImage(this.profileImage);
    // File name only
    var filename = this.profileImage;
    //alert(filename);
    var options = {
      fileKey: "file",
      fileName: filename,
      chunkedMode: false,
      mimeType: "multipart/form-data",
      params : {'fileName': filename, 'serviceProvider_Id' : this.service_prd_id, 'feature' : 4,  'token' : this.globalVars.USER_TOKEN, 'tkey' : this.globalVars.MAP_TKEY}
    };

    const fileTransfer: FileTransferObject = this.transfer.create();
    //this.loading = this.loadingCtrl.create({content: 'Please wait... \nUploading profile image will take some time.',});
    //this.loading.present();

   //alert(targetPath);

   //this.presentToast(targetPath);

    // Use the FileTransfer to upload the image
    fileTransfer.upload(targetPath, encodeURI(url), options).then
      (data =>{
          //this.loading.dismissAll()
          this.presentToast('Image successful uploaded.');
          alert("We have received your request. Please wait for approval.");
          var p=[];
              p['tab']='0';
              p['dash']= "society";
              //this.navCtrl.setRoot(ServiceproviderPage,{details : p});
              this.navCtrl.navigateForward("ServiceproviderPage", { state: p });
        },
     err => {
          //this.loading.dismissAll()
          this.presentToast('Error while uploading file.');
          var p=[];
          p['tab']='0';
          p['dash']= "society";
          //this.navCtrl.setRoot(ServiceproviderPage,{details : p});
          this.navCtrl.navigateForward("ServiceproviderPage", { state: p });
        }
    );
  }
}

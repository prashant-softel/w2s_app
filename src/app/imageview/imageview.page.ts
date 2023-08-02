import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras ,Router} from '@angular/router';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-imageview',
  templateUrl: './imageview.page.html',
  styleUrls: ['./imageview.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ImageviewPage implements OnInit {
  img_array: Array<any>;
  ViewImagePage : any ='viewimage';
  img_src = "";
  folder;
  photos: any;
  albumID: any;
  displayData : any;

  image: string = null;
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute,
    private router: Router) {

      this.img_array = [];
      // this.img_src = "";
      this.img_src = "https://way2society.com/uploads";
      // this.img_src = "http://localhost/master4/uploads";
      this.folder = "";
      this.albumID = 0;
      this.image = "";
      this.displayData= [];
      let details: any;
      details = this.router.getCurrentNavigation()?.extras?.state;
      console.log(details);
     // this.displayData(details);
     this.FetchImageInAlbumID(details);
     }

  ngOnInit() {
    //this.FetchImageInAlbumID();
  }
  FetchImageInAlbumID(details)
  {
    this.displayData = details.details;
    //this.displayData = details;//this.navParams.get("details");
    this.loaderView.showLoader('Loading ...');
    var obj = [];
    obj["fetch"] = "photoByAlbumID";
    obj["AlbumID"] = this.displayData['album_id'];
    console.log("ALbumID", this.displayData['album_id']);
    console.log("folder Nmae",this.displayData['folder']);
    this.connectServer.getData("Photoalbum", obj).then(
        resolve => { 
            this.loaderView.dismissLoader();
            if(resolve['success'] == 1)
            {
              var AllImageData = resolve['response']['PhotoDetails'];
              for(var iCnt = 0; iCnt <Object.keys(AllImageData).length;iCnt++)
              {
                this.img_array.push(AllImageData[iCnt]);
              }
                    
            }
        }
     );
  }

  
  public viewImage(image) {
    var obj = {
       folder: this.folder,
       img: image
     };
     let navigationExtras: NavigationExtras = {
      queryParams:
      {
        details:obj,
      }
    };
    this.navCtrl.navigateForward(this.ViewImagePage, { state: { details: obj} });
    //alert(image);
     // this.navCtrl.push(this.ViewImagePage, { details: image,details1 : this.img_array});
    // this.navCtrl.push(ViewImagePage, { details: image });
   }
}

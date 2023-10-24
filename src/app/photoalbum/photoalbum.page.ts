import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { ImageviewPage } from "../imageview/imageview.page";
//import * as _ from "lodash";

@Component({
  selector: 'app-photoalbum',
  templateUrl: './photoalbum.page.html',
  styleUrls: ['./photoalbum.page.scss'],
  standalone: true,
 
  imports: [IonicModule, CommonModule, FormsModule]
})
export class PhotoalbumPage implements OnInit {
  ImageviewPage :any ='imageview';
  coverImgs: Array<any>;
  photoalbum_array: Array<any>;
  temp_photoalbum_array: Array<any>;
  photoalbum_array_loaded: number;
  image_array: Array<any>;
  imgSource: string;
  folder_array: Array<any>;
  albumId: any;
  countOfAlbum: any;
  photos: Array<any>;
  namesOfFolder: Array<any>;
  imageId: Array<any>;
  folder: string;
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) {

    this.coverImgs = [];
    this.photoalbum_array = [];
    this.temp_photoalbum_array = [];
    this.photoalbum_array_loaded = 0;
    this.image_array = [{}];
    this.folder_array = [];
    this.imgSource = "https://way2society.com/uploads";
    this.albumId = 0;
    this.countOfAlbum = 0;
    this.photos = [];
    this.namesOfFolder = [];
    this.imageId = [];
    this.folder = "";
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
    this.fetchData();
  }
  fetchData() 
  {
    this.loaderView.showLoader('Loading ...');
    var obj = [];
    obj["fetch"] = "Allphotoalbum";
    this.connectServer.getData("Photoalbum", obj).then(
      resolve => { 
        this.loaderView.dismissLoader();
        if(resolve['success'] == 1)
        {
          var AlbumData = resolve['response']['PhotoAlbumDetails'];
          for(var iCnt = 0; iCnt < Object.keys(AlbumData).length;iCnt++)
          {
            this.folder_array.push(AlbumData[iCnt]);
          }
        }
      }
    );
  }

  selectItems(p) {

    let navigationExtras: NavigationExtras = {
      queryParams: 
      {
        details :p,
      }
    };
    this.navCtrl.navigateRoot(this.ImageviewPage,navigationExtras);
    /*let navigationExtras: NavigationExtras = {
      queryParams:
      {
        details: p,
      }
    };
    console.log("pass",details);
    this.navCtrl.navigateForward(this.ImageviewPage, { state: { details: p } });*/
    //alert("album ID "+p);
   // this.navCtrl.navigateForward("imageview", { state: p });
    // this.navCtrl.push(ImageviewPage, { details: p});
   }
 
  getImagesUrls(selectedFolder) {
    var urls = [];
    for (let i = 0; i < this.photos.length; i++) {
      if (this.photos[i]["folder"] == selectedFolder) {
        urls.push(this.photos[i]["url"]);
      }
    }
  return urls;
}
 
getImagesIDs(selectedFolder) {
  var ids = [];
  for (let i = 0; i < this.photos.length; i++) {
    if (this.photos[i]["folder"] == selectedFolder) {
      ids.push(this.photos[i]["id"]);
    }
  }
  return ids;
}
 
getCoverImage(selectedFolder) {
  console.log(this.photos);
  for (let i = 0; i < this.photos.length; i++) {
    if(this.photos[i]["folder"] == selectedFolder && this.photos[i]["cover"] == "1") {
        this.coverImgs.push({
          folder: selectedFolder,
          url: this.photos[i]["url"],
          name: this.getFolderNameFromFolder(selectedFolder)
        });
      }
    }
  }
 
  getFolderNameFromFolder(selectedFolder) {
    var name = "";
    for (let i = 0; i < this.photoalbum_array.length; i++) {
       if (this.photoalbum_array[i]["folder"] == selectedFolder) {
         name = this.photoalbum_array[i]["name"];
       }
     }
     return name;
   }
}

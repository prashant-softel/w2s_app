import { Component, OnInit,CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras ,Router} from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { PhotoViewer } from "@ionic-native/photo-viewer/ngx";

const slideOpts = {
  initialSlide: 2
};

@Component({
  selector: 'app-viewimage',
  templateUrl: './viewimage.page.html',
  styleUrls: ['./viewimage.page.scss'],
  standalone: true,
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ],
  imports: [IonicModule, CommonModule, FormsModule],
  providers: [PhotoViewer]
})
export class ViewimagePage implements OnInit {
  image: string;
  url: string;
  folder: string;
  id: any;
  details: any;
  imagearray : Array<any>; 
  imagearray1 : Array<any>; 

  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute,
    private router: Router,
    public photoViewer: PhotoViewer) { 
      this.imagearray = [];
      this.imagearray1 = [];
     
      let data_details: any;
      data_details = this.router.getCurrentNavigation()?.extras?.state;
      //console.log(data_details);
      this.details= data_details.details;
     // this.details = this.navParams.get("details");
      this.id = this.details["photo_id"];
      this.folder = this.details["folder"];
      this.url = "https://way2society.com/uploads";
      this.image = this.details["url"];
     
      let data_details1: any;
      data_details1 = this.router.getCurrentNavigation()?.extras?.state;
     // console.log(data_details1);
      this.imagearray= data_details1.details;//['img'];
     // this.imagearray = this.navParams.get("details1");
        console.log(this.imagearray);
        
        var k = 0;
        for(var i = 0;i < Object.keys(this.imagearray).length ; i++)
        {
          console.log(this.imagearray[i]['photo_id']);
          if(this.id <= this.imagearray[i].photo_id)//['photo_id'])
          {
            this.imagearray1[k] = this.imagearray[i];
            k++;
          }
        }
        var counter = Object.keys(this.imagearray1).length; 
        for(var i = 0;i < Object.keys(this.imagearray).length ; i++)
        {
           var m = 0;
           for(var j = 0;j < Object.keys(this.imagearray1).length ; j++)
          {
            if(this.imagearray[i]['photo_id'] == this.imagearray1[j]['photo_id'])
            {
              m++;
            }
          }
          if(m == 0)
          {
            this.imagearray1[counter] = this.imagearray[i];
            counter++;
          }
    }
  
   for(var i = 0;i < Object.keys(this.imagearray1).length ; i++)
  {
    console.log('Test : ' + i +  this.imagearray1[i]['url']);
  }
}

  ngOnInit() {
  }
  public viewImage() {
    var url = this.url + "/" + this.folder + "/" + this.image;
    this.photoViewer.show(url, this.image);
  }

}

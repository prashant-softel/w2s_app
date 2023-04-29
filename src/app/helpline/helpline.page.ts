import { Component , HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-helpline',
  templateUrl: './helpline.page.html',
  styleUrls: ['./helpline.page.scss'],
  standalone: true,
 
  imports: [IonicModule, CommonModule, FormsModule]
})
export class HelplinePage implements OnInit {
  Helpline_array :Array<any>;
  Category_array : Array<any>;
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) { 
    this.Helpline_array=[];
    this.Category_array=[];
  }

  ngOnInit() {
    this.fetchHelpLine();
  }
  // Backbutton code 
  @HostListener('document:ionBackButton', ['$event'])
  overrideHardwareBackAction(event: any) {
    event.detail.register(100, async () => {
      event.stopImmediatePropagation();
      event.stopPropagation();
      event.preventDefault();
    });
  }
  fetchHelpLine()
  {
     this.loaderView.showLoader('Loading');
     var objData =[];
      objData['fetch'] = 4;
       this.connectServer.getData("Directory", objData).then(
          resolve => { 
                      this.loaderView.dismissLoader();
                      if(resolve['success'] == 1)
                      {
                          var HelpLine =resolve['response']['directory'];   
                          for(var i=0;i < HelpLine.length; i++)
                          {
                              this.Helpline_array.push(HelpLine[i]);
                          }   
                          console.log(this.Helpline_array);             
                      }
                      else
                      {
                        
                      }
            }
       );

  }
}

import { Injectable } from '@angular/core';
import { LoadingController } from '@ionic/angular';

Injectable()
export class LoaderView {
  
  objLoader : any;
  constructor( private loadingCtrl : LoadingController) { }
 
  showLoader(content: any) {
    this.objLoader = this.loadingCtrl.create({
      spinner: 'bubbles',
      //content: content
    });

    this.objLoader.present();

   
}

dismissLoader() {

    this.objLoader.dismiss();
}
}
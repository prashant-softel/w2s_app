import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams } from '@ionic/angular';

/**
 * Generated class for the ContectPage page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */

@Component({

  templateUrl: 'contect.html',
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]

})
export class ContectPage {

  constructor(public navCtrl: NavController, public navParams: NavParams) {
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad ContectPage');
  }

}

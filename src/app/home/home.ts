import { Component } from '@angular/core';
import { IonicModule, NavController, NavParams } from '@ionic/angular';

import { Feedback } from '../feedback/feedback';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

/**
 * Generated class for the Home page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@Component({
	templateUrl: 'home.html',

	standalone: true,
	imports: [IonicModule, CommonModule, FormsModule]
})
export class Home {

	userData: { Email: string, Password: any, Code: any };

	constructor(public navCtrl: NavController, public navParams: NavParams) {

		this.userData = { Email: "", Password: "", Code: "" };
	}

	ionViewDidLoad() {
		console.log('ionViewDidLoad Home');
	}

	fetchDetails() {
		// need to be this.navCtrl.push(Feedback, { aims_code: this.userData.Code });
	}

}

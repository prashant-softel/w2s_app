
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
//import { DashboardPage } from '../dashboard/dashboard.page';
//import { LoginPage } from '../login/login.page';






@Component({
	selector: 'app-society',
	templateUrl: './society.page.html',
	styleUrls: ['./society.page.scss'],
	standalone: true,
	imports: [IonicModule, CommonModule, FormsModule],

})
export class SocietyPage implements OnInit {
	maps: Array<{ tkey: string; society: string; role: string; map: string; checked: any }>;
	selectedMapID: any;
	societyMap: any;
	LoginPage: any = 'login';
	DashboardPage: any = 'dashboard';
	constructor(private navCtrl: NavController,
		private globalVars: GlobalVars,
		private connectServer: ConnectServer,
		private platform: Platform,
		private loaderView: LoaderView,
		private params: NavParams) {
		this.maps = [];
		this.selectedMapID = 0;

	}
	ngOnInit_() {

	}
	ngOnInit() {
		this.loaderView.showLoader('Loading ...');
		this.globalVars.getMapIDArray().then(
			value => {
				var mapIDArray = value;
				//console.log("Map Arr" +mapIDArray);
				if (mapIDArray.length > 0) {
					console.log(mapIDArray);
					for (var i = 0; i < mapIDArray.length; i++) {
						var bChecked = "";
						mapIDArray[i]['checked'] = 0;
						if (this.globalVars.MAP_ID == mapIDArray[i]['map']) {
							bChecked = mapIDArray[i]['map'];
							mapIDArray[i]['checked'] = 1;
						}

						this.maps.push(mapIDArray[i]);
					}
				}
				console.log("maps:" + this.maps);
				//console.log("length:" +this.maps.length);
				this.loaderView.dismissLoader();
				if (this.maps.length == 1) {

					this.globalVars.setMapDetails(mapIDArray[0]['map'], mapIDArray[0]['society'], mapIDArray[0]['role'], mapIDArray[0]['tkey'], mapIDArray[0]['societyid'], mapIDArray[0]['unit_id'], mapIDArray[0]['unit_no'], mapIDArray[0]['isblock'], mapIDArray[0]['block_reason']);
					//console.log(this.globalVars.setMapDetails);
					this.navCtrl.navigateRoot(this.DashboardPage);
				}
			}
		);
	}
	societySelected(m) {
		this.globalVars.setMapDetails(m.map, m.society, m.role, m.tkey, m.societyid, m.unit_id, m.uni_no, m.isblock, m.block_reason);
		//	this.globalVars.setAppMenu('Member');
		this.navCtrl.navigateRoot(this.DashboardPage);

	}

	isMember(role) {
		if (role == "Member" || role == "Admin Member" || role == "Super Admin" || role == "Admin") {
			return true;
		}
		else {
			return false;
		}
	}

	logout() {
		this.globalVars.clearStorage();
		this.navCtrl.navigateRoot(this.LoginPage);
	}
}

import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { NavigationExtras } from '@angular/router';
import { ConnectServer } from 'src/service/connectserver';
import { LoaderView } from 'src/service/loaderview';
import { GlobalVars } from 'src/service/globalvars';

@Component({
	selector: 'app-directory',
	templateUrl: './directory.page.html',
	styleUrls: ['./directory.page.scss'],
	standalone: true,
	imports: [IonicModule, CommonModule, FormsModule]
})
export class DirectoryPage implements OnInit {
	tab: string = "member";
	role: string;
	member_directory_array: Array<any>;
	member_blood_group_array: Array<any>;
	member_professional_array: Array<any>;
	temp_member_directory_array: Array<any>;
	temp_member_blood_group_array: Array<any>;
	temp_member_professional_array: Array<any>;
	member_directory_array_loaded: number;
	member_blood_group_array_loaded: number;
	member_professional_array_loaded: number;
	commitee_array_loaded: number;

	commitee_array: Array<any>;
	otherArray: Array<any>;
	memberArray: Array<any>;
	temp_otherArray: Array<any>;
	temp_memberArray: Array<any>;
	constructor(private navCtrl: NavController,
		private globalVars: GlobalVars,
		private connectServer: ConnectServer,
		private platform: Platform,
		private loaderView: LoaderView,
		private params: NavParams) {

		this.member_directory_array = [];
		this.member_blood_group_array = [];
		this.member_professional_array = [];
		this.temp_member_directory_array = [];
		this.temp_member_blood_group_array = [];
		this.temp_member_professional_array = [];
		this.member_directory_array_loaded = 0;
		this.member_blood_group_array_loaded = 0;
		this.member_professional_array_loaded = 0;
		this.commitee_array_loaded = 0;
		this.temp_otherArray = [];
		this.temp_memberArray = [];
		this.commitee_array = [];
		this.otherArray = [];
		this.memberArray = [];
		this.role = "";
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
		this.role = this.globalVars.MAP_USER_ROLE;
		this.fetchData(0);
	}
	fetchData(tab) {
		//alert(tab);
		if (tab == 0) {
			if (!this.member_directory_array_loaded) {
				console.log('ionViewDidLoad Directory');
				var obj = [];
				obj['fetch'] = 0;
				this.connectServer.getData("Directory", obj).then(
					resolve => {
						this.loaderView.dismissLoader();
						if (resolve['success'] == 1) {
							this.member_directory_array = resolve['response']['directory'];
							this.temp_member_directory_array = this.member_directory_array;
							console.log(this.member_directory_array);
							this.member_directory_array_loaded = 1;
						}
					}
				);
			}
		}
		else if (tab == 1) {
			if (!this.member_blood_group_array_loaded) {
				console.log('ionViewDidLoad Directory');
				var obj = [];
				obj['fetch'] = 2;
				this.connectServer.getData("Directory", obj).then(
					resolve => {
						this.loaderView.dismissLoader();
						if (resolve['success'] == 1) {
							this.member_blood_group_array = resolve['response']['directory'];
							this.temp_member_blood_group_array = this.member_blood_group_array;
							console.log(this.member_blood_group_array);
							this.member_blood_group_array_loaded = 1;
						}
					}
				);
			}
		}
		else if (tab == 2) {
			if (!this.member_professional_array_loaded) {
				console.log('ionViewDidLoad Directory');
				var obj = [];
				obj['fetch'] = 1;
				this.connectServer.getData("Directory", obj).then(
					resolve => {
						this.loaderView.dismissLoader();
						if (resolve['success'] == 1) {
							this.member_professional_array = resolve['response']['directory'];
							this.temp_member_professional_array = this.member_professional_array;
							console.log(this.member_professional_array);
							this.member_professional_array_loaded = 1;
						}
					}
				);
			}
		}
		else if (tab == 3) {
			if (!this.commitee_array_loaded) {
				this.loaderView.showLoader('Loading ...');
				var obj = [];
				obj['fetch'] = "";
				this.connectServer.getData("Commitee", obj).then(
					resolve => {
						this.loaderView.dismissLoader();
						if (resolve['success'] == 1) {
							var commiteedetails = resolve['response']['commiteedetails'];

							for (var iMemCnt = 0; iMemCnt < Object.keys(commiteedetails).length; iMemCnt++) {
								this.commitee_array[iMemCnt] = commiteedetails[iMemCnt];
							}
							var i = 0;
							var j = 0;
							for (var iCnt = 0; iCnt < Object.keys(this.commitee_array).length; iCnt++) {
								if (this.commitee_array[iCnt]['position'] == "Commitee Member") {
									this.memberArray[i] = this.commitee_array[iCnt];
									i++;
								}
								else {
									this.otherArray[j] = this.commitee_array[iCnt];
									j++;
								}
							}
							console.log((this.commitee_array).length + ", " + Object.keys(this.otherArray).length + ", " + Object.keys(this.memberArray).length);
							this.temp_memberArray = this.memberArray;
							this.temp_otherArray = this.otherArray;
							// alert(this.temp_otherArray);
							this.commitee_array_loaded = 1;
						}
					}
				);
			}
		}
	}
	// Search bar functions
	getItems1(ev: any) {
		this.temp_member_directory_array = this.member_directory_array;
		let val = ev.target.value;
		if (val && val.trim() != '') {

			this.temp_member_directory_array = this.temp_member_directory_array.filter((p) => {
				let name: any = p;
				if (name.flat_name.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name.flat_name.toLowerCase().indexOf(val.toLowerCase()) > -1);
				else if (name.unit_no.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name.unit_no.toLowerCase().indexOf(val.toLowerCase()) > -1);
				else if (name.intercom_no.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name.intercom_no.toLowerCase().indexOf(val.toLowerCase()) > -1);
				else if (name.mobile_o.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name.mobile_o.toLowerCase().indexOf(val.toLowerCase()) > -1);
				else if (name.other_email.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name.other_email.toLowerCase().indexOf(val.toLowerCase()) > -1);
				return null;
			});

		}

	}
	getItems2(ev: any) {
		this.temp_member_blood_group_array = this.member_blood_group_array;
		let val = ev.target.value;
		if (val && val.trim() != '') {
			this.temp_member_blood_group_array = this.temp_member_blood_group_array.filter((p) => {
				let name: any = p;
				if (name.flat_name.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name.flat_name.toLowerCase().indexOf(val.toLowerCase()) > -1);
				else if (name.unit_no.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name.unit_no.toLowerCase().indexOf(val.toLowerCase()) > -1);
				else if (name.intercom_no.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name.intercom_no.toLowerCase().indexOf(val.toLowerCase()) > -1);
				else if (name.mobile_o.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name.mobile_o.toLowerCase().indexOf(val.toLowerCase()) > -1);
				else if (name.other_email.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name.other_email.toLowerCase().indexOf(val.toLowerCase()) > -1);
				else if (name.booldGroup.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name.booldGroup.toLowerCase().indexOf(val.toLowerCase()) > -1);

				return null;
			});
		}
	}
	getItems3(ev: any) {
		this.temp_member_professional_array = this.member_professional_array;
		let val = ev.target.value;
		if (val && val.trim() != '') {
			this.temp_member_professional_array = this.temp_member_professional_array.filter((p) => {
				let name: any = p;
				if (name.other_name.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name.other_name.toLowerCase().indexOf(val.toLowerCase()) > -1);
				else if (name.unit_no.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name.unit_no.toLowerCase().indexOf(val.toLowerCase()) > -1);
				else if (name.intercom_no.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name.intercom_no.toLowerCase().indexOf(val.toLowerCase()) > -1);
				else if (name.mobile_o.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name.mobile_o.toLowerCase().indexOf(val.toLowerCase()) > -1);
				else if (name.other_profile.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name.other_profile.toLowerCase().indexOf(val.toLowerCase()) > -1);

				return null;
			});
		}
	}
	getItems4(ev: any) {
		this.temp_memberArray = this.memberArray;
		this.temp_otherArray = this.otherArray;
		let val = ev.target.value;
		if (val && val.trim() != '') {
			this.temp_memberArray = this.temp_memberArray.filter((p) => {
				let name: any = p;
				if (name.name.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name.name.toLowerCase().indexOf(val.toLowerCase()) > -1);
				else if (name.position.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name.position.toLowerCase().indexOf(val.toLowerCase()) > -1);
				else if (name.mobile.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name.mobile.toLowerCase().indexOf(val.toLowerCase()) > -1);
				else if (name.unit_no.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name.unit_no.toLowerCase().indexOf(val.toLowerCase()) > -1);
				else if (name.unit_no.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name.category.toLowerCase().indexOf(val.toLowerCase()) > -1);
				return null;
			});
			this.temp_otherArray = this.temp_otherArray.filter((p) => {
				let name1: any = p;
				if (name1.name.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name1.name.toLowerCase().indexOf(val.toLowerCase()) > -1);
				else if (name1.position.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name1.position.toLowerCase().indexOf(val.toLowerCase()) > -1);
				else if (name1.mobile.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name1.mobile.toLowerCase().indexOf(val.toLowerCase()) > -1);
				else if (name1.unit_no.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name1.unit_no.toLowerCase().indexOf(val.toLowerCase()) > -1);
				else if (name1.unit_no.toLowerCase().indexOf(val.toLowerCase()) > -1)
					return (name1.category.toLowerCase().indexOf(val.toLowerCase()) > -1);
				return null;
			});
		}
	}
}

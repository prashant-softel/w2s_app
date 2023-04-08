import { Component, OnInit,CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule,NavController } from '@ionic/angular';
import { GlobalVars } from '../../service/globalvars';
import { LoaderView } from '../../service/loaderview';
//import { DashboardPage } from '../dashboard/dashboard.page';
//import { LoginPage } from '../login/login.page';


@Component({
  selector: 'app-society',
  templateUrl: './society.page.html',
  styleUrls: ['./society.page.scss'],
  standalone: true,
  schemas: [ CUSTOM_ELEMENTS_SCHEMA ],
  imports: [IonicModule, CommonModule, FormsModule]
})
export class SocietyPage implements OnInit {
  maps : Array<{ tkey: string; society: string; role: string; map: string; }> ;
	selectedMapID : any;
	societyMap : any;
  constructor(private globalVars: GlobalVars,
    private loaderView : LoaderView,
    private navCtrl: NavController) {
    this.maps = [];
    this.selectedMapID = 0;
    
   }

  ngOnInit() {
    //this.maps.push("","Demo Society","Admin","") ;
  }
 /* isMember(role: string)
	  {
	  		if(role == "Member" || role == "Admin Member" || role == "Super Admin" || role == "Admin")
	  		{
	  			return true;
	  		}
	  		else 
	  		{
	  			return false;
	  		}
	  }*/
    //societySelected(m: { map: number; society: string; role: string; tkey: string; societyid: number; unit_id: number; uni_no: number; isblock: number; block_reason: string; }) {
	  	//this.globalVars.setMapDetails(m.map, m.society, m.role, m.tkey, m.societyid, m.unit_id, m.uni_no, m.isblock, m.block_reason);
	    //this.globalVars.setAppMenu('Member');
	  	//this.navCtrl.setRoot(DashboardPage);
	  ////}

  //logout()
	 // {
	  	//this.globalVars.clearStorage();
      	//this.navCtrl.setRoot(LoginPage);
	  //}

}

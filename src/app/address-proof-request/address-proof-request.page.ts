import { Component, OnInit,HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform,} from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { HttpClient} from '@angular/common/http';
@Component({
  selector: 'app-address-proof-request',
  templateUrl: './address-proof-request.page.html',
  styleUrls: ['./address-proof-request.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class AddressProofRequestPage implements OnInit {
  AddressProofRequestDetails:any='address-proof-request-details';
  tab : string = "pending";
	message : string = "";
	AddressProof_pending : Array<any>;
  AddressProof_verified :  Array<any>;
  AddressProof_approved : Array<any>;
	temp_AddressProof_pending_array : Array<any>;
  temp_AddressProof_verified_array : Array<any>;
  temp_AddressProof_approved_array : Array<any>;
  AddressProof :  Array<any>;
	Block_unit : any = 0;
	Block_desc : String = "";
	getParam : {}; 
  data:any = {};
  details:any;
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute,
    public http: HttpClient) {
    this.temp_AddressProof_pending_array =[] ;
    this.temp_AddressProof_verified_array =[] ;
    this.temp_AddressProof_approved_array =[] ;
  	
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
    let details:any;
    this.route.queryParams.subscribe(params => {
      details = params["details"];
        
      });
    this.getParam =details;//this.navParams.get("details");
    this.Block_unit=this.globalVars.MAP_UNIT_BLOCK;
    this.Block_desc=this.globalVars.MAP_BLOCK_DESC;
    console.log("Global Vars : ",this.globalVars);
    this.tab = "pending";
    this.getCategoryId();
    if(this.getParam != "")
    {
      this.tab = details;//this.navParams.get("details");
      console.log("details : ",this.tab);
    }
    this.fetchAddressProofRequest();
  }
  fetchAddressProofRequest()
  {
    var link = this.globalVars.HOST_NAME+'api.php';
    console.log("Link : "+link);
    var myData = JSON.stringify({method:"getAddressProofRequest",type:this.tab,societyId:this.globalVars.MAP_SOCIETY_ID,unitId:this.globalVars.MAP_UNIT_ID,role:this.globalVars.MAP_USER_ROLE,loginId:this.globalVars.MAP_LOGIN_ID});
    this.http.post(link, myData)
      .subscribe(data => 
      {
        this.data.response = data["_body"]; //https://stackoverflow.com/questions/39574305/property-body-does-not-exist-on-type-response
        // console.log("fetchRenovationRequest :",this.data.response);
        var parsedData = JSON.parse(this.data.response);
        console.log("parsedData : ",parsedData['response']['AddressProofRequest']);
        this.AddressProof = parsedData['response']['AddressProofRequest'];
        var addressProofLength = parsedData['response']['length'];
        console.log("addressProofLength : ",addressProofLength);
        this.temp_AddressProof_pending_array = [];
        this.temp_AddressProof_verified_array = [];
        this.temp_AddressProof_approved_array = [];
        //console.log("Renovation length: ",this.Renovation.length);
        if(addressProofLength > 0)
        {
          for (var i = 0; i < this.AddressProof.length; i++)
          {
            if(this.tab == "pending")
            {
              this.temp_AddressProof_pending_array.push(this.AddressProof[i]);
            }
            else if(this.tab == "verified")
            {
              this.temp_AddressProof_verified_array.push(this.AddressProof[i]);
            }
            else
            {
              this.temp_AddressProof_approved_array.push(this.AddressProof[i]);
            }
          }
        }      
      }
    );  	
    console.log("temp_AddressProof_approved_array : ",this.temp_AddressProof_approved_array);
  }

  selectPendingAddressProofRequest(p)
  {
    let navigationExtras: NavigationExtras = {
      queryParams: 
      {
        details :p,
      }
    };
    this.navCtrl.navigateRoot(this.AddressProofRequestDetails,navigationExtras);
    //this.navCtrl.push(AddressProofRequestDetails,{details : p});
  }
  getCategoryId()
  {
    var data = [];
    var link = this.globalVars.HOST_NAME+'api.php';
    var myData = JSON.stringify({method:"getRequestId",societyId:this.globalVars.MAP_SOCIETY_ID,unitId:this.globalVars.MAP_UNIT_ID,role:this.globalVars.MAP_USER_ROLE,login_id:this.globalVars.LoginID});
    this.http.post(link, myData)
    .subscribe(data => 
    {
      this.data.response = data["_body"]; //https://stackoverflow.com/questions/39574305/property-body-does-not-exist-on-type-response
      var parsedData = JSON.parse(this.data.response);
      console.log(parsedData);
      var details = parsedData['response']['details'];
      console.log(details);
      this.globalVars.ADDRESS_PROOF_REQUEST_ID = details['AddressProofId'];
      this.globalVars.TENANT_REQUEST_ID = details['TenantRequestId'];
      this.globalVars.RENOVATION_REQUEST_ID = details['RenovationRequestId'];
    }, error => {
      console.log("Oooops!");
    });  
   }
}

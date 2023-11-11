import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { HTTP } from '@ionic-native/http/ngx';
import { HttpClient, HttpClientModule, HttpHeaders} from '@angular/common/http';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-renovation-request',
  templateUrl: './renovation-request.html',
  styleUrls: ['./renovation-request.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule, HttpClientModule],
  providers: [HTTP]
})
export class RenovationRequestPage implements OnInit {
  
  tab : string = "pending";
	message : string = "";
	Renovation_pending : Array <any>;
  Renovation_verified :  Array <any>;
  Renovation_approved : Array <any>;
	temp_Renovation_pending_array : Array <any>;
  temp_Renovation_verified_array : Array <any>;
  temp_Renovation_approved_array : Array <any>;
  httpHeader = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };
	Renovation :  Array <any>;
	Block_unit : any = 0;
	Block_desc : String = "";
	getParam : {}; 
  data:any = {};
  details:any;
  AddRenovationRequest: any = 'addrenovationrequest';
  RenovationRequestDetailsPage: any = 'RenavationRequestDetails';
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    public navParams: NavParams,
    private route: ActivatedRoute,
    public http: HttpClient) {

      this.temp_Renovation_pending_array =[] ;
      this.temp_Renovation_verified_array =[] ;
      this.temp_Renovation_approved_array =[] ;
      // this.getParam = this.navParams.get("details");
      this.getParam = [];
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
    console.log('ionViewDidLoad RenovationRequest');
    this.Block_unit=this.globalVars.MAP_UNIT_BLOCK;
    this.Block_desc=this.globalVars.MAP_BLOCK_DESC;
    console.log("Global Vars : ",this.globalVars);
    this.tab = "pending";
    
    let details: any;
    this.route.queryParams.subscribe(params => {
      console.log("Query Parameters:", params);
      details = params["details"];

    });
    this.getParam = details;
    console.log("details",details);
    this.getCategoryId();
    if(this.navParams.get("details") != "")
    {
      // this.tab = this.navParams.get("details");
      let details: any;
    this.route.queryParams.subscribe(params => {
      details = params["details"];

    });
     this.tab = details; 
      console.log(" tab details : ",this.tab);
    }
    this.fetchRenovationRequest();
  }
  fetchRenovationRequest()
  {
  	 // this.loaderView.showLoader('Loading');
	    var link = this.globalVars.HOST_NAME+'api.php';
      console.log(link);
      console.log("Link : "+link+"<br>loginId : "+this.globalVars.MAP_LOGIN_ID);
      var myData = JSON.stringify({method:"getRenovationDetails",type:this.tab,societyId:this.globalVars.MAP_SOCIETY_ID,unitId:this.globalVars.MAP_UNIT_ID,role:this.globalVars.MAP_USER_ROLE,loginId:this.globalVars.MAP_LOGIN_ID});
     
      this.http.post(link, myData, this.httpHeader)
      .pipe(
        map((response: any) => response)
      )
      .subscribe((data) => 
      {
        console.log("HTTP Response:", data);
          //this.loaderView.dismissLoader();
          
          this.data.response = data; //https://stackoverflow.com/questions/39574305/property-body-does-not-exist-on-type-response
         // console.log("fetchRenovationRequest :",this.data.response);
          var parsedData = this.data.response;
          console.log("parsedData : ",parsedData['response']['RenovationRequest']);
          this.Renovation = parsedData['response']['RenovationRequest'];
          var renovationLength = parsedData['response']['length'];
          console.log("Renovation : ",this.Renovation);
          //console.log("Renovation length: ",this.Renovation.length);
          this.temp_Renovation_pending_array = [];
           this.temp_Renovation_verified_array = [];
           this.temp_Renovation_approved_array = [];
          if(renovationLength > 0)
          {
            for (var i = 0; i < this.Renovation.length; i++)
  	        {
              if(this.tab == "pending")
              {
                console.log("Renovation : ",this.Renovation[i]);
                //this.Renovation_pending.push(this.Renovation[i]);
                this.temp_Renovation_pending_array.push(this.Renovation[i]);
              }
              else if(this.tab == "verified")
              {
               // this.Renovation_verified.push(this.Renovation[i]);
                this.temp_Renovation_verified_array.push(this.Renovation[i]);
              }
              else
              {
                //this.Renovation_approved.push(this.Renovation[i]);
                this.temp_Renovation_approved_array.push(this.Renovation[i]);
              }
            }
          }      
	      }
	    );  	
  }

  selectPendingRenovationRequest(p)
  {
    let navigationExtras: NavigationExtras = {
      queryParams:
      {
        details: p,

      }
    };
    this.navCtrl.navigateRoot(this.RenovationRequestDetailsPage,navigationExtras);
  }
   getCategoryId()
   {
     var data = [];
        var link = this.globalVars.HOST_NAME+'api.php';
        var myData = JSON.stringify({method:"getRequestId",societyId:this.globalVars.MAP_SOCIETY_ID,unitId:this.globalVars.MAP_UNIT_ID,role:this.globalVars.MAP_USER_ROLE,login_id:this.globalVars.MAP_LOGIN_ID});
        console.log("mydata",myData);
        this.http.post(link, myData, this.httpHeader)
        .pipe(
          map((response: any) => response)
        )
        .subscribe(
        (data) => {
          console.log("data of getcategory", data);
        if(data){
          this.data.response = data; //https://stackoverflow.com/questions/39574305/property-body-does-not-exist-on-type-response
          //this.data.response = data["_body"]; //https://stackoverflow.com/questions/39574305/property-body-does-not-exist-on-type-response
          console.log(this.data.response);
        try{
          var parsedData = this.data.response;
          // console.log("data parsing",parsedData);
          var details = parsedData['response']['details'];
          console.log(details);
          this.globalVars.ADDRESS_PROOF_REQUEST_ID = details['AddressProofId'];
          this.globalVars.TENANT_REQUEST_ID = details['TenantRequestId'];
          this.globalVars.RENOVATION_REQUEST_ID = details['RenovationRequestId'];
        }catch(e){
          console.log("Invalid JSON:", e);
        }
      }
        else{
          console.log("Empty response or undefined data");
        } 
      },  
        error => {
      console.log("Oooops!");
    }
    );  
  //   var link = this.globalVars.HOST_NAME + 'api.php';
  // var myData = JSON.stringify({
  //   method: "getRequestId",
  //   societyId: this.globalVars.MAP_SOCIETY_ID,
  //   unitId: this.globalVars.MAP_UNIT_ID,
  //   role: this.globalVars.MAP_USER_ROLE,
  //   login_id: this.globalVars.MAP_LOGIN_ID
  // });

  // this.http.post(link, myData, this.httpHeader)
  //   .pipe(
  //     map((response: any) => response["_body"]) // Use map operator to extract the body
  //   )
  //   .subscribe((responseBody) => {
  //     this.data.response = responseBody;
  //     var parsedData = JSON.parse(this.data.response);
  //     console.log(parsedData);
  //     var details = parsedData['response']['details'];
  //     console.log(details);
  //     this.globalVars.ADDRESS_PROOF_REQUEST_ID = details['AddressProofId'];
  //     this.globalVars.TENANT_REQUEST_ID = details['TenantRequestId'];
  //     this.globalVars.RENOVATION_REQUEST_ID = details['RenovationRequestId'];
  //   }, error => {
  //     console.log("Oooops!");
  //   });  
 }
 addRenovationRequest(){
  this.navCtrl.navigateRoot(this.AddRenovationRequest);
 }
  
}

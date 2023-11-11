import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AlertController, IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { HTTP } from '@ionic-native/http/ngx';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-RenovationRequestDetails',
  templateUrl: './RenovationRequestDetails.html',
  styleUrls: ['./RenovationRequestDetails.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule, HttpClientModule],
  providers: [HTTP]
})
export class RenovationRequestDetailsPage implements OnInit {
  RenovationDetails: Array<any>;
  RenovationRequestPage: any = 'renovation-request';
  unit_no: any;
  primary_owner_name: any;
  summery: any;
  application_date: any;
  start_date: any;
  end_date: any;
  location: any;
  type_of_work: any;
  work_details: any;
  priority: any;
  status: any;
  contractor_name: any;
  ContractorContactNo: any;
  contractor_address: any;
  max_labourer: any;
  verifiedStatus: any;
  verifiedByName: any;
  verifiedByDesignation: any;
  firstLevelApprovalStatus: any;
  firstApprovedByName: any;
  firstApprovalByDesignation: any;
  secondLevelApprovalStatus: any;
  secondApprovedByName: any;
  secondApprovalByDesignation: any;
  verificationAccess: any;
  approvalAccess: any;
  RenovationId: Array<any>;
  labourer_name: Array<any>;
  data: any = {};

  httpHeader = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public alertCtrl: AlertController,
    public connectServer: ConnectServer,
    private loaderView: LoaderView,
    public http: HttpClient,
    private route: ActivatedRoute,
    public globalVars: GlobalVars
  ) {

    this.RenovationDetails = [];
    this.RenovationDetails = this.navParams.get("details");
    this.unit_no = Number;
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
    console.log('ionViewDidLoad renovationRequestDetails');
    let details: any;
    this.route.queryParams.subscribe(params => {
      console.log("Query Parameters:", params);
      details = params["details"];

    });
    this.RenovationDetails = details;
    console.log("details", details);
    console.log(this.RenovationDetails);

    this.unit_no = this.RenovationDetails['unit_no'];
    this.primary_owner_name = this.RenovationDetails['primary_owner_name'];
    this.summery = this.RenovationDetails['summery'];
    this.application_date = this.RenovationDetails['application_date'];
    this.start_date = this.RenovationDetails['start_date'];
    this.end_date = this.RenovationDetails['end_date'];
    this.location = this.RenovationDetails['location'];
    this.type_of_work = this.RenovationDetails['type_of_work'];
    this.work_details = this.RenovationDetails['work_details'];
    this.priority = this.RenovationDetails['priority'];
    this.status = this.RenovationDetails['status'];
    this.contractor_name = this.RenovationDetails['contractor_name'];
    this.ContractorContactNo = this.RenovationDetails['ContractorContactNo'];
    this.contractor_address = this.RenovationDetails['contractor_address'];
    this.max_labourer = this.RenovationDetails['max_labourer'];
    this.verifiedStatus = this.RenovationDetails['verifiedStatus'];
    this.verifiedByName = this.RenovationDetails['verifiedByName'];
    this.verifiedByDesignation = this.RenovationDetails['verifiedByDesignation'];
    this.firstLevelApprovalStatus = this.RenovationDetails['firstLevelApprovalStatus'];
    this.firstApprovedByName = this.RenovationDetails['firstApprovedByName'];
    this.firstApprovalByDesignation = this.RenovationDetails['firstApprovalByDesignation'];
    this.secondLevelApprovalStatus = this.RenovationDetails['secondLevelApprovalStatus'];
    this.secondApprovedByName = this.RenovationDetails['secondApprovedByName'];
    this.secondApprovalByDesignation = this.RenovationDetails['secondApprovalByDesignation'];
    this.verificationAccess = this.RenovationDetails['verificationAccess'];
    this.approvalAccess = this.RenovationDetails['approvalAccess'];
    this.labourer_name = this.RenovationDetails['labourer_name'];
    this.RenovationId = this.RenovationDetails['RenovationId'];
    console.log(this.unit_no);
    // console.log("details of renovation :",this.navParams.get("details"));

  }
  updateRequestStatus(RenovationId, action) {
    console.log("renovation Id : " + RenovationId + " action : " + action);
    var link = this.globalVars.HOST_NAME + 'api.php';
    console.log("Link : " + link);
    var myData = JSON.stringify({ method: "approveRequest", action: action, requestType: this.globalVars.RENOVATION_REQUEST_ID, requestId: RenovationId, serviceRequestId: 0, societyId: this.globalVars.MAP_SOCIETY_ID, unitId: this.globalVars.MAP_UNIT_ID, role: this.globalVars.MAP_USER_ROLE, loginId: this.globalVars.MAP_LOGIN_ID });
    console.log("my data", myData);
    this.http.post(link, myData, this.httpHeader)
      .pipe(
        map((response: any) => response)
      )
      .subscribe((data) => {
        console.log("incoming data", data);
        this.data.response = data;

      }
      );
    var p = "";
    if (action == "verify") {
      p = "pending";

    }
    else {
      p = "verified";
    }
    let navigationExtras: NavigationExtras = {
      queryParams:
      {
        details: p,
      }
    };
    this.navCtrl.navigateRoot(this.RenovationRequestPage, navigationExtras);
  }

}

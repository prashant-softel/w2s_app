import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {IonicModule, NavController} from "@ionic/angular";
import {NgIf} from "@angular/common";
import { GlobalVars } from 'src/service/globalvars';
import {HttpClient} from "@angular/common/http"; // Import AlertController
import { LoaderView } from 'src/service/loaderview';

@Component({
  selector: 'app-paymentresult',
  templateUrl: './paymentresult.page.html',
  styleUrls: ['./paymentresult.page.scss'],
  imports: [
    IonicModule,
    NgIf
  ],
  standalone: true
})
export class PaymentresultPage implements OnInit {

  PaymentgatewayPage: any = 'paymentgateway';

  constructor(
    private navCtrl: NavController,
    private route: ActivatedRoute,
    private globalVars: GlobalVars,
    public http: HttpClient,
    private loaderView: LoaderView,
    private router: Router
  ) { } // Inject Router

  // Initialize as null
  paymentDetails:  {
    status: string;
    statusMessage: string;
    txnid: string;
    amount: number;
    ownerName: string;
    societyName: string;
    unitNo: string;
    unitPresentation: string
  } = null;

  // paymentStatus = 'success'
  // paymentStatus = 'failure'

  ngOnInit() {
    this.route.queryParams.subscribe( params => {
      this.paymentDetails = {
        status: params['status'],
        statusMessage: params['status_message'],
        txnid: params['txnid'],
        amount: params['amount'],
        ownerName: params['ownerName'], // Access correct parameter
        societyName: params['societyName'],
        unitNo: params['unitNo'],
        unitPresentation: params['unitPresentation']
      };
    });
  }

  resetGlobalData() {
    // console.log({ "reset": "global" });
    this.globalVars.getUserDetails().then(
      value => {
        // console.log({ "getUserDetails": value });
        if (value != null && value.hasOwnProperty('USER_TOKEN') && value.hasOwnProperty('USER_NAME')) {
          this.globalVars.setUserDetails(value.USER_TOKEN, value.USER_NAME);
        }
      }
    );
    this.globalVars.getMapDetails().then(
      value => {
        this.globalVars.setMapDetails(value.MAP_ID, value.MAP_SOCIETY_NAME, value.MAP_USER_ROLE, value.MAP_TKEY, value.MAP_SOCIETY_ID, value.mapUnit_id ?? value.MAP_UNIT_ID, value.mapUnit_no ?? value.MAP_UNIT_NO, value.mapUnit_Block ?? value.MAP_UNIT_BLOCK, value.MAP_BLOCK_DESC);
      }
    );
  }

  async getPaymentDues() {
    //alert("Comming Soon!");
    await this.resetGlobalData();

    var link = this.globalVars.HOST_NAME + 'api.php';
    var myData = {
      method: "getPaymentDues",
      societyId: this.globalVars.MAP_SOCIETY_ID,
      unitId: this.globalVars.MAP_UNIT_ID,
      role: this.globalVars.MAP_USER_ROLE,
      loginId: this.globalVars.MAP_LOGIN_ID
    };
    this.http.post<any>(link, myData)
      .subscribe(data => {
        // console.log("data received:", data); // Log the received json

        try {
          this.navCtrl.navigateRoot(this.PaymentgatewayPage, {
            state: {paymentData: data} // Pass data using NavigationExtras
          });
        } catch (error) {
          console.error("Error processing form:", error);
          // Display an error message to the user if something goes wrong
          // this.presentAlert('Error', 'Could not process the payment form. Please try again later.');

        } finally {  // Ensure the loader is dismissed even if there's an error
          this.loaderView.dismissLoader();
        }
      }, error => {
        // console.error("HTTP Error:", error);  // Log the full error object
        // this.presentAlert('Error', 'Could not connect to the payment gateway. Please check your internet connection.');
        this.loaderView.dismissLoader();
      });
  }

  printPage() {
    window.print();
  }

  goToDashboard() {
    this.router.navigate(['/dashboard']);
  }
}

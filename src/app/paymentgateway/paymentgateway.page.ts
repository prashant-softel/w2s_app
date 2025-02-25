import { Component, OnInit } from '@angular/core';
import {IonicModule, NavController, NavParams, Platform} from '@ionic/angular';
import { FormsModule } from '@angular/forms';
import {DecimalPipe, NgForOf} from "@angular/common";
import {GlobalVars} from "../../service/globalvars";
import {ConnectServer} from "../../service/connectserver";
import {LoaderView} from "../../service/loaderview";
import { AlertController } from '@ionic/angular';
import {HttpClient} from "@angular/common/http"; // Import AlertController
import { Router } from '@angular/router'; // Import Router

@Component({
  selector: 'app-paymentgateway',
  templateUrl: './paymentgateway.page.html',
  styleUrls: ['./paymentgateway.page.scss'],
  standalone: true,
  imports: [IonicModule, FormsModule, NgForOf, DecimalPipe], // Only FormsModule needed
})
export class PaymentgatewayPage implements OnInit {
  // ownerName = 'John Doe';
  // unitNo = 'A-101';
  // totalAmount = 0;
  // bills = [
  //   { type: 'Maintenance', amount: 1500, payAmount: 1, comments: '' },
  //   { type: 'Supplementary', amount: 500, payAmount: 0, comments: '' },
  //   { type: 'Invoice', amount: 1000, payAmount: 0, comments: '' }
  // ];
  PaymentresultPage: any = 'paymentresult';
  paymentData: { ownerName: string; unitNo: string; unitPresentation: string; totalAmount: number; bills: [{ type: string, amount: number, payAmount: number, comments: string }] } = null; // Initialize as null
  // totalAmount: number = 0;
  // paymentData: any;

  constructor(private navCtrl: NavController,
              private globalVars: GlobalVars,
              private connectServer: ConnectServer,
              private platform: Platform,
              private loaderView: LoaderView,
              private router: Router,
              public alertController: AlertController, // Inject AlertController
              public http: HttpClient,
              private params: NavParams) {
    // this.PaymentLink = "";
    // this.AllowPayment = "";
    // this.EnablePaytm = "0";
    // this.Enable_NEFT = "0";
    // this.paymentData = [];
  }



  ngOnInit() {
    if (this.router.getCurrentNavigation()?.extras.state)
    {
      this.paymentData = this.router.getCurrentNavigation().extras.state['paymentData'];
      // console.log('Payment Data received:', this.paymentData);
    }
    this.calculateTotal();
  }

  calculateTotal() {
    // console.log(this.paymentData);
    this.paymentData.totalAmount = this.paymentData.bills.reduce((sum, bill) => sum + bill.payAmount, 0);
  }

  async submitForm() {
    // Handle form submission, e.g., send data to server
    // console.log('Form submitted:', this.paymentData.bills);
    // this.navCtrl.navigateRoot(this.PaymentresultPage);

    if (this.paymentData.totalAmount <= 0) {
      // alert("Please enter Pay amount!");

      const alert = await this.alertController.create({ // Use async/await
        header: 'Error',
        message: 'Please enter Pay amount!',
        buttons: ['OK']
      });
      await alert.present(); // Present the alert
      return; // Stop further execution
    }
    else {
      this.loaderView.showLoader('Loading ...');
      this.ccavenueRequest();
    }
  }


  ccavenueRequest() {
    var link = this.globalVars.HOST_NAME + 'api.php';
    var myData = {
      method: "ccavenueRequest",
      societyId: this.globalVars.MAP_SOCIETY_ID,
      unitId: this.globalVars.MAP_UNIT_ID,
      role: this.globalVars.MAP_USER_ROLE,
      mapId: this.globalVars.MAP_ID,
      unitPresentation: this.paymentData.unitPresentation,
      amount: this.paymentData.totalAmount,
      bills: this.paymentData.bills
    };
    this.http.post(link, myData, { responseType: 'text' })
      .subscribe((data: string) => {
        console.log("HTML Form received:", data); // Log the received HTML

        try {  // Use a try...catch block for better error handling
          const container = document.createElement('div');
          container.innerHTML = data;

          setTimeout(() => {
            const form = document.getElementById('ccavenue_form') as HTMLFormElement;  // Type assertion for safety
            if (form) {
              form.submit();
              // console.log("Form submitted via setTimeout");
            } else {
              // console.error("Form not found inside setTimeout"); // This should not happen now
            }
          }, 0);


          document.body.appendChild(container); // Add to DOM to trigger redirect

          // Debug: Check if the form is being submitted
          // console.log("Form appended to DOM. Check for redirection.");


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


}

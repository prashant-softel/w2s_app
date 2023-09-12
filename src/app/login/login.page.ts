import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform, ActionSheetController } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
// import { ViewbillPage } from '../viewbill/viewbill.page';
// import { ViewnoticePage } from '../viewnotice/viewnotice.page';
// import { VieweventPage } from '../viewevent/viewevent.page';
// import { ViewimposefinePage } from '../viewimposefine/viewimposefine.page';
// import { TakepollPage } from '../takepoll/takepoll.page';
// import { ClassifiedsdetailsPage } from '../classifiedsdetails/classifiedsdetails.page';
import { ConnectServer } from 'src/service/connectserver';
// import { VisitorInPage } from '../visitor-in/visitor-in.page';
// import { ViewreceiptPage } from '../viewreceipt/viewreceipt.page';
import { NavigationExtras } from '@angular/router';
//import { InAppBrowser } from '@ionic-native/in-app-browser/ngx';
//import { WebIntent } from '@ionic-native/web-intent/ngx';


@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
  standalone: true,
  //providers:[InAppBrowser],
  imports: [IonicModule, CommonModule, FormsModule]
})
export class LoginPage implements OnInit {
  userData: { Email: string, Password: any };
  showLogin: any;
  message: any;
  films: any | [];
  notificationDetails: any;
  bHasNotification: any;
  SocietyPage: any = 'society';
  DashboardPage: any = 'dashboard';
  NewuserPage: any = 'newuser';
  payFlag: boolean;
  loginID: number;


  constructor(
    private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private loaderView: LoaderView,
    private params: NavParams,
    public actionSheet: ActionSheetController,
    private platform: Platform,
    // private inAppBrowser: InAppBrowser

  ) {

    this.userData = { Email: "", Password: "" };
    this.showLogin = false;
    this.message = "";

    this.notificationDetails = [];

    this.bHasNotification = false;
    this.loginID = 0;

  }
  ngOnInit(): void {

    this.reinitializeData();
  }
  launch() {
    let uri = 'https://way2society.com/forgotpassword.php';//`upi://pay?pa=${UPI_ID}&pn=${UPI_NAME}&tid=${tid}&am=${totalPrice}&cu=INR&tn=${UPI_TXN_NOTE}&tr=${orderId}`;
    alert("call 1");

    window.open("https://way2society.com/forgotpassword.php", '_system', 'location=yes');
    // cordova.exec("InAppBrowser", "open", ['http://apache.org', '_blank', 'location=yes']);
    // alert("call");
    /*this.platform.ready().then(() => {
      this.payFlag = true;
      var browserRef = window.open('https://way2society.com',"_blank","hidden=no,location=no,clearsessioncache=yes,clearcache=yes,hardwareback=no");
      browserRef.addEventListener('loadstop', function(e: InAppBrowserEvent) {
      var loc = e.url;
      alert("call"+loc);
      if(loc == "https://way2society.com")
      {
            setTimeout(function () {
              browserRef.close();
            }, 5000);  
      }
      });
      });*/
    //this.platform.ready().then(() => {
    //cordova.InAppBrowser.open('https://way2society.com/', "_system", "location=true");
    // });

  }

  ionViewDidLoad() {
    // if (this.params.get("notification_details") != null) {
    //   this.bHasNotification = true;
    //   this.notificationDetails = this.params.get("notification_details");
    //   alert(this.notificationDetails);
    // }
    // if (this.params.get("activation_details") != null) {
    //   this.showLogin = false;
    //   this.userData = this.params.get("activation_details");
    //   this.signin();
    // }
    // else {
    //   this.verifyToken();
    // }
  }

  processNotification() {

    this.loaderView.showLoader('Initializing App ...');

    var pages = [];
    // pages['ViewbillPage'] = ViewbillPage;
    // pages['ViewnoticePage'] = ViewnoticePage;
    // pages['VieweventPage'] = VieweventPage;
    // pages['ViewimposefinePage'] = ViewimposefinePage;
    // pages['TakepollPage'] = TakepollPage;
    // pages['ClassifiedsdetailsPage'] = ClassifiedsdetailsPage;
    // //pages['ProviderDetailsPage'] = ProviderDetailsPage;
    // //  pages['ViewServiceRequestPage'] = ViewServiceRequestPage;
    // pages['VisitorInPage'] = VisitorInPage;
    // pages['ViewreceiptPage'] = ViewreceiptPage;

    // alert("calling");



    try {
      console.log(this.notificationDetails);
      alert(this.notificationDetails['map_id'] + "|" + this.notificationDetails['page_ref'] + "|" + this.notificationDetails['details'] + "|" + this.notificationDetails['pagename']);
      this.connectServer.getData("NotificationDetails", this.notificationDetails).then(
        resolve => {
          //alert(resolve['success']);

          //  alert(JSON.stringify(resolve['response']));
          // alert(pages[this.notificationDetails.pagename]);

          this.loaderView.dismissLoader();
          if (resolve['success'] == 1) {
            const navigationExtras: NavigationExtras = {
              queryParams: {
                details: resolve['response']
              }
            };

            this.navCtrl.navigateRoot(pages[this.notificationDetails.pagename], navigationExtras);

          }
          else {
            //alert("Success=0 ");
            this.navCtrl.navigateRoot(this.SocietyPage);
          }
        }
      );
    }
    catch (err) {
      this.loaderView.dismissLoader();
      this.navCtrl.navigateRoot(this.SocietyPage);
    }
  }


  reinitializeData() {

    this.loaderView.showLoader('Initializing App');

    this.globalVars.getUserDetails().then(
      value => {
        this.loaderView.dismissLoader();
        if (value != null && value.hasOwnProperty('USER_TOKEN') && value.hasOwnProperty('USER_NAME')) {
          this.globalVars.setUserDetails(value.USER_TOKEN, value.USER_NAME);
          console.log("inlogin user role : " + this.globalVars.USER_ROLE)
          if (this.globalVars.USER_ROLE == "1") {
            // this.navCtrl.navigateRoot();
          }
          else {

            this.goToSurvey();
          }
          this.showLogin = false;
        }
        else {
          this.showLogin = true;
        }
      }
    );
  }
  goToSurvey() {
    //this.loaderView.showLoader('Verifying Details');

    var obj = [];
    obj['isActive'] = 0;
    obj['surveyID'] = 1;
    obj['bool_questions'] = true;
    obj['bool_ratings'] = false;
    obj['bool_doctors'] = false;
    var surveyDetails = {};

    this.navCtrl.navigateRoot("Survey", { state: { details: { loginID: this.loginID } } });
  }

  signin() {

    this.message = "";
    if (this.userData.Email.length == 0 || this.userData.Password.length == 0) {
      this.message = "Please enter Username and Password";
      //this.presentToast();
    }
    else {
      this.loaderView.showLoader('Verifying Details');
      //var serverResponse = [];

      //this.message = "Verifying Details. Please Wait ...";  
      this.connectServer.login(this.userData).then(
        resolve => {
          this.message = "";
          this.loaderView.dismissLoader();

          if (resolve != null && resolve.hasOwnProperty('success')) {
            if (resolve['success'] == 0) {
              this.message = "Enter valid User ID and Password";

            }
            else if (resolve['success'] == 1) {
              this.globalVars.setUserDetails(resolve['response']['token'], resolve['response']['name']);
              this.loginID = resolve['response']['LoginID'];

              setTimeout(() => {
                this.loaderView.dismissLoader();
                this.reinitializeData();
              }, 2000);

            }


          }
        }
      );
    }
  }
  /*login() {
      facebookConnectPlugin.login(['email'], function(response) {
          alert('Logged in');
          alert(JSON.stringify(response.authResponse));
      }, function(error){
          alert(error);
      })
  }

  getdetails() {
      facebookConnectPlugin.getLoginStatus((response) => {
          if(response.status == "connected") {
              facebookConnectPlugin.api('/' + response.authResponse.userID + '?fields=id,name,gender',[],
              function onSuccess(result) {
                  alert(JSON.stringify(result));
              },
              function onError(error) {
                  alert(error);
              }
              );
          }
          else {
              alert('Not logged in');
          }
      })
  }

  logout() {
      facebookConnectPlugin.logout((response) => {
          alert(JSON.stringify(response));
      })
  }*/

  openForgotPasswordLink() {
    window.open("https://way2society.com/forgotpassword.php", '_blank', 'location=no');
  }
  openActivationLink() {

    //this.navCtrl.push(NewuserPage);
    this.navCtrl.navigateForward(this.NewuserPage);
    //window.open("https://way2society.com/newuseractivation.php", '_blank', 'location=no');
    //window.open("http://localhost/beta_aws_11/newuseractivation.php", '_blank', 'location=no');


  }

  /*GetData()
  {
    this.connectServer.getFilms().subscribe({
      next: (response: any) => {
        this.films = response.results;
        console.log(this.films);
      
      },
      error: (err) => {
        alert('There was an error in retrieving data from the server');
      }
    });
  }*/

}

//http://way2society.com:8080/Mobile/login.php?token=V6n6EzFZjLIsjMZy6r4uJmElE4SCfawIDOxId72i-4FXQRPgasKCZ_ioETwUlcNE83j7p6aKDa-6eSbdLZ9FsQ&grant_type=refresh_token&test=test


import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ViewbillPage } from '../viewbill/viewbill.page';
import { ViewnoticePage } from '../viewnotice/viewnotice.page';
import { VieweventPage } from '../viewevent/viewevent.page';
import { ViewimposefinePage } from '../viewimposefine/viewimposefine.page';
import { TakepollPage } from '../takepoll/takepoll.page';
import { ClassifiedsdetailsPage } from '../classifiedsdetails/classifiedsdetails.page';
import { ConnectServer } from 'src/service/connectserver';
import { VisitorInPage } from '../visitor-in/visitor-in.page';
import { ViewreceiptPage } from '../viewreceipt/viewreceipt.page';
import { NavigationExtras } from '@angular/router';



@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
  standalone: true,
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
  constructor(
    private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams) {

    this.userData = { Email: "", Password: "" };
    this.showLogin = false;
    this.message = "";

    this.notificationDetails = [];

    this.bHasNotification = false;
  }
  ngOnInit(): void {
    
    this.reinitializeData();
  }

  launch(url) {
    //this.platform.ready().then(() => {
    //  let browser = new InAppBrowser('https://ionic.io', '_self', 'location=no');
    //});
  }

  ionViewDidLoad() {

    ////For Testing notification
    /* this.notificationDetails['map_id'] = 3719;
        this.notificationDetails['page_ref'] = "5";
         this.notificationDetails['details'] = "169/241";
        this.notificationDetails['pagename'] = "TakepollPage";
   
       this.bHasNotification = true;
   */

    if (this.params.get("notification_details") != null) {
      this.bHasNotification = true;
      this.notificationDetails = this.params.get("notification_details");
      alert(this.notificationDetails);
    }
    if (this.params.get("activation_details") != null) {
      this.showLogin = false;
      this.userData = this.params.get("activation_details");
      this.signin();
    }
    else {
      this.verifyToken();
    }
  }

  processNotification() {

    this.loaderView.showLoader('Initializing App ...');

    var pages = [];
    pages['ViewbillPage'] = ViewbillPage;
    pages['ViewnoticePage'] = ViewnoticePage;
    pages['VieweventPage'] = VieweventPage;
    pages['ViewimposefinePage'] = ViewimposefinePage;
    pages['TakepollPage'] = TakepollPage;
    pages['ClassifiedsdetailsPage'] = ClassifiedsdetailsPage;
    //pages['ProviderDetailsPage'] = ProviderDetailsPage;
    //  pages['ViewServiceRequestPage'] = ViewServiceRequestPage;
    pages['VisitorInPage'] = VisitorInPage;
    pages['ViewreceiptPage'] = ViewreceiptPage;

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

  verifyToken() {

    this.loaderView.showLoader('Initializing App ...');

    /*this.loaderView.dismissLoader();
                        this.showLogin = true;

                        return;
*/
    this.globalVars.getUserDetails().then(
      value => {
        if (value != null && value.hasOwnProperty('USER_TOKEN') && value.hasOwnProperty('USER_NAME')) {
          //console.log(value);
          this.globalVars.setUserDetails(value.USER_TOKEN, value.USER_NAME);

          var objdataRefresh = [];
          objdataRefresh['token'] = value.USER_TOKEN;
          objdataRefresh['grant_type'] = "refresh_token";
          objdataRefresh['deviceID'] = this.globalVars.DEVICE_ID;

          this.connectServer.login(objdataRefresh).then(
            resolve => {
              this.message = "";
              this.loaderView.dismissLoader();

              if (resolve != null && resolve.hasOwnProperty('success')) {
                this.globalVars.LATEST_APP_VERSION = resolve['version'];
                this.globalVars.APP_DOWNLOAD_LINK = resolve['app_link'];

                if (resolve['success'] == 0) {
                  this.message = resolve['message'];
                }
                else if (resolve['success'] == 1) {
                  this.globalVars.getMapDetails().then(
                    value => {
                      //console.log('Map Details' + value);
                      this.globalVars.setUserDetails(resolve['response']['token'], resolve['response']['name']);

                      this.globalVars.setMapIDArray(resolve['response']['map']);

                      var bMapFound = false;
                      var iMapID = 0;

                      var mapIDArray = resolve['response']['map'];
                      if (value != null || this.bHasNotification == true) {
                        //iMapID = value.MAP_ID;
                        //alert(this.bHasNotification);
                        if (this.bHasNotification == true) {
                          iMapID = this.notificationDetails.map_id;
                          //  alert(iMapID);
                        }
                        else {
                          iMapID = value.MAP_ID;
                        }

                        if (mapIDArray.length > 0) {
                          for (var i = 0; i < mapIDArray.length; i++) {

                            if (iMapID == mapIDArray[i]['map']) {
                              bMapFound = true;

                              this.globalVars.setMapDetails(mapIDArray[i]['map'], mapIDArray[i]['society'], mapIDArray[i]['role'], mapIDArray[i]['tkey'], mapIDArray[i]['society_id'], mapIDArray[i]['unit_id'], mapIDArray[i]['unit_no'], mapIDArray[i]['isblock'], mapIDArray[i]['block_reason']);
                            }
                          }
                        }

                        if (iMapID > 0 && bMapFound == true) {
                          if (this.bHasNotification == true) {
                            this.processNotification();
                          }
                          else {
                            this.navCtrl.navigateRoot(this.DashboardPage);
                          }
                        }
                        else {
                          this.navCtrl.navigateRoot(this.SocietyPage);
                        }
                      }
                      else {
                        this.navCtrl.navigateRoot(this.SocietyPage);
                      }
                    }
                  );


                }

              }
            }
          );
        }
        else {
          this.loaderView.dismissLoader();
          this.showLogin = true;
        }
      }
    );

  }

  reinitializeData() {

    this.loaderView.showLoader('Initializing App ...');

    this.globalVars.getUserDetails().then(
      value => {
        if (value != null && value.hasOwnProperty('USER_TOKEN') && value.hasOwnProperty('USER_NAME')) {
          this.globalVars.setUserDetails(value.USER_TOKEN, value.USER_NAME);

          this.globalVars.getMapDetails().then(
            value => {
              this.loaderView.dismissLoader();

              if (value != null && value.hasOwnProperty('MAP_ID') && value.hasOwnProperty('MAP_SOCIETY_NAME') && value.hasOwnProperty('MAP_USER_ROLE') && value.hasOwnProperty('MAP_SOCIETY_ID')) {
                this.globalVars.setMapDetails(value.MAP_ID, value.MAP_SOCIETY_NAME, value.MAP_USER_ROLE, value.MAP_TKEY, value.MAP_SOCIETY_ID, value.MAP_UNIT_ID, value.MAP_UNIT_NO, value.UNIT_BLOCK, value.BLOCK_DESC);
                this.navCtrl.navigateRoot('dashboard');
              }
              else {
                //let navigationExtras: NavigationExtras = {
                  //queryParams: {
                      //userName:'TESTTSTSTS' ,
                     
                 // }
              //};
                //this.navCtrl.navigateRoot(this.SocietyPage,navigationExtras);
                this.navCtrl.navigateRoot(this.SocietyPage);
              }

              this.showLogin = false;
            }
          );
        }
        else {
          this.loaderView.dismissLoader();
          this.showLogin = true;
        }
      }
    );
  }

  signin() {

    //debugger;
    this.message = "";
    if (this.userData.Email.length == 0 || this.userData.Password.length == 0) {
      this.message = "Please enter Username and Password";
    }
    else {
      this.loaderView.showLoader('Verifying Details ...');
      //var serverResponse = [];
      this.userData['deviceID'] = this.globalVars.DEVICE_ID;
      this.message = "Verifying Details. Please Wait ...";
      this.connectServer.login(this.userData).then(
        resolve => {
          this.message = "";
          this.loaderView.dismissLoader();

          if (resolve != null && resolve.hasOwnProperty('success')) {
            this.globalVars.LATEST_APP_VERSION = resolve['version'];
            this.globalVars.APP_DOWNLOAD_LINK = resolve['app_link'];

            if (resolve['success'] == 0) {
              this.message = resolve['response']['message'];
            }
            else if (resolve['success'] == 1) {
              this.globalVars.setUserDetails(resolve['response']['token'], resolve['response']['name']);

              this.globalVars.setMapIDArray(resolve['response']['map']);

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
    this.navCtrl.navigateForward(this.DashboardPage);
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


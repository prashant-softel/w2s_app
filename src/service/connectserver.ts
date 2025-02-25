import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { GlobalVars } from './globalvars';
import { Observable, catchError, map, of, throwError } from 'rxjs';
//http://way2society.com:8080/W2S/
//http://way2society.com:8080/W2S_Beta/
var headers = new Headers();
headers.append('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
headers.append('Accept', 'application/json');
headers.append('content-type', 'application/json');
@Injectable()
export class ConnectServer {
  serverURL = 'http://way2society.com:8080/W2S1/';
  // serverURL='http://localhost:8080/Unichem_web/';
  //serverURL='https://way2society.com:8443/W2S/';
  API_URL = '';
  constructor(public http: HttpClient, private globalVars: GlobalVars) { }

  getFilms() {
    return this.http.get('https://swapi.dev/api/films');
  }

  login(objData) {

    return new Promise(resolve => {

      objData['device'] = this.globalVars.DEVICE_ID;
      var sURL = this.serverURL + "Login" + this.generateQueryString(objData);
      //alert(sURL);
      this.http.get(sURL)

        // .map(res => res.json())
        .subscribe(data => {
          resolve(data);
        }, error => {
          console.log({ "error": error });

          alert("We are unable to connect to our servers. Please try after some time.");
          var objResponse = [];
          objResponse['success'] = -1;
          resolve(objResponse);
          //console.log(JSON.stringify(error.json()));
        });
    });
  }
  NewUser(objData) {
    return new Promise(resolve => {

      //objData['device'] = this.globalVars.DEVICE_ID;
      objData['MobileNo'] = '0';
      objData['FBCode'] = '0';
      var sURL = this.serverURL + "NewUser" + this.generateQueryString(objData);
      this.http.get(sURL)

        .subscribe(data => {
          resolve(data);
        }, error => {
          alert("We are unable to connect to our servers. Please try after some time.");
          var objResponse = [];
          objResponse['success'] = -1;
          resolve(objResponse);
          //console.log(JSON.stringify(error.json()));
        });
    });
  }

  getData(fileURL, objData) {
    return new Promise(resolve => {

      if (objData == null) {
        objData = [];
      }

      //  objData['token'] = "wvSn5ujhqo2IgEmOgxIb2ZAGGHR-HbsaeKZZKxciGQKI9_C3V40SOX52-2mjVxBfVC2QXhQ5St6S3CI0a3Vxks1ufdVEVu3vQX8oMcWP3YsmdtCCPWgcTB8i60jj_M4e";
      //  objData['tkey'] = "uQm4W95yfSIue7LTJWPf77-96QUgGcr1jFzlZ9eGx4FFrTNFFZDp888Vz_KxsWYEJAFdO0xZjdpFi80KKbIs6mUCUbrqH2LX3TTGqV1rtzSMNuPamGjpwEnk-0GC6qRigwJ2zebEUXWtJC0ejTbJPg";
      //  objData['map'] = "3737";

      objData['token'] = this.globalVars.USER_TOKEN;
      objData['tkey'] = this.globalVars.MAP_TKEY;
      objData['map'] = this.globalVars.MAP_ID;


      var sURL = this.serverURL + fileURL + this.generateQueryString(objData);
      //alert(sURL);

      this.http.get(sURL)
        // .map(res => res.json())
        .subscribe(data => {
          //alert('Received : ' + data['success']);
          console.log(data);
          if (data['success'] == 498) {
            alert(data['response']['message']);
            if (data['response']['message'] == 'Expired') {
              //this.refreshToken(fileURL, objData);
            }
          }
          else {
            resolve(data);
          }
        }, error => {
          alert("We are unable to connect to our servers. Please try after some time.");
          var objResponse = [];
          objResponse['success'] = -1;
          resolve(objResponse);
          //console.log(JSON.stringify(error.json()));
        });
    });
  }
  postData() {
    this.http.post("https://httpbin.org/post", "firstname=Nic")
      .subscribe(data => {

      }, error => {
        console.log(JSON.stringify(error.json()));
      });
  }

  sendImage(albumId, image) {
    return new Promise((resolve, reject) => {
      var obj = {
        album_id: albumId,
        image: image
      };

      this.http.post(this.API_URL, JSON.stringify(obj)).
        subscribe(res => {
          resolve(res);
        }, (err) => {
          reject(err);
        });
    });
  }

  generateQueryString(objData) {

    var sQueryString = "";
    if (Object.keys(objData).length > 0) {
      for (var i = 0; i < Object.keys(objData).length; i++) {
        var sData = (Object.keys(objData)[i] + '=' + objData[Object.keys(objData)[i]]);
        sQueryString += sData + '&';
      }
      sQueryString += "v=" + this.globalVars.APP_VERSION;

      if (sQueryString.length > 0) {
        sQueryString = "?" + sQueryString;
      }
    }

    return sQueryString;
  }
  checkUrlValidity(url: string) {
    fetch('https://way2society.com/maintenance_bills/RHG_TEST/October-December%202022/bill-RHG_TEST-202-October-December%202022-0.pdf')
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.blob();
      })
      .then(blob => {
        // At this point, 'blob' contains the PDF file data
        console.log('Received PDF file data:', blob);

        // You can use this blob data to perform further operations, such as displaying it in an <iframe> or saving it to a file
      })
      .catch(error => {
        console.error('There was a problem with the fetch operation:', error);
      });
    return false;
    // return this.http.get(url + "fdfg", { responseType: 'blob', observe: 'response' })
    //   .pipe(
    //     catchError((error: HttpErrorResponse) => {
    //       console.log({ "errrrror": error });

    //       if (error.error instanceof Blob && error.headers.has('Content-Type')) {
    //         const contentType = error.headers.get('Content-Type');
    //         return of(contentType === 'application/pdf');
    //       }
    //       return throwError(false);
    //     })
    //   );
    // try {

    //   await this.http.get(url).toPromise();
    //   return true;
    // } catch (error) {
    //   return false;
    // }
  }

}
import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { GlobalVars } from './globalvars';
//http://way2society.com:8080/W2S/
//http://way2society.com:8080/W2S_Beta/
var headers = new Headers();
headers.append('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
  headers.append('Accept','application/json');
  headers.append('content-type','application/json');
@Injectable()
export class ConnectServer {
  //serverURL='http://way2society.com:8080/W2S/';
  serverURL='https://way2society.com:8443/W2S/';
  API_URL='';
  constructor(public http: HttpClient, private globalVars:GlobalVars ) { }

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

      /*objData['token'] = "wvSn5ujhqo2IgEmOgxIb2ZAGGHR-HbsaeKZZKxciGQItalHa_9fwwsmXiI__fmk_5iOzMD_fWRKsjVhOojZcco5LriHVHBm83PhQ9NN9zLIaDz9vBVEdl6rqX4QNjCqt";
      objData['tkey'] = "vuC_qblCEWnTc0IyiLWa5puajsl4pkMSMt0jvyMc74rwn28pGFjQp7yCEI1azwQ9S7i4HYZIpKsh0khpcIJlrtNOqqgL1h33SKpcKkFa51AF1P5zTnsQud77hO1_klyFwaKs4HxVqqsCC5-HdiZGmg";
      objData['map'] = "3737";*/
      
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

}
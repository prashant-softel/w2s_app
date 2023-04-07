import { Injectable } from '@angular/core';

import { Events } from '@ionic/angular';
//import { Observable } from 'rxjs';
import { Storage } from '@ionic/storage';
//import { IonicStorageModule } from '@ionic/storage-angular';

import { DashboardPage } from '../app/dashboard/dashboard.page';
import { EventsPage } from '../app/events/events.page';
 
@Injectable()
export class GlobalVars {
  HAS_LOGGED_IN = 'hasLoggedIn';
  USER_TOKEN = "";
  USER_NAME = "";

  MAP_ID = 0;
  MAP_SOCIETY_NAME = "";
  MAP_USER_ROLE = "";
  MAP_TKEY = "";
  MAP_SOCIETY_ID = 0;
  MAP_UNIT_ID = 0;
  MAP_UNIT_NO = 0;
  DEVICE_ID = "";
  MAP_UNIT_BLOCK = 0;
  MAP_BLOCK_DESC = "";
  APP_VERSION = "3.0.20230401";
  LATEST_APP_VERSION = "";
  APP_DOWNLOAD_LINK = "";
  /* Profile Flages    Admin  and Admin Member*/     
  APPROVALS_LEASE = 0;
  APPROVALS_CLASSIFIED =0;
  PROFILE_CREATE_ALBUM= 0;
  PROFILE_CREATE_POLL= 0;
  PROFILE_EDIT_MEMBER= 0;
  PROFILE_MANAGE_LIEN= 0;
  PROFILE_PHOTO_APPROVAL= 0;
  PROFILE_SEND_EVENT= 0;
  PROFILE_SEND_NOTICE= 0;
  PROFILE_SEND_NOTIFICATION= 0;
  PROFILE_SERVICE_PROVIDER= 0;
  PROFILE_USER_MANAGEMENT= 0;
  APP_MENU = [];
  constructor(public storage: Storage) { }
 
  login(username) {
    this.storage.set(this.HAS_LOGGED_IN, true);
   
  }
  logout() {
    this.storage.remove(this.HAS_LOGGED_IN);
    this.storage.remove('username');
    this.events.publish('user:logout');
  }
  setUserDetails(userToken, userName) {
    var obj = {"USER_TOKEN" : userToken, "USER_NAME" : userName};
    this.storage.set('userDetails', obj);

    this.USER_TOKEN = userToken;
    this.USER_NAME = userName;
  }
  getUserDetails() {
    return this.storage.get('userDetails').then((value) => {
      return value;
    });
  }
  setMapDetails(mapID, mapSocietyName, mapUserRole, mapTkey, mapSociety_id, mapUnit_id, mapUnit_no,mapUnit_Block,mapBlock_desc) {
    var obj = {"MAP_ID" : mapID, "MAP_SOCIETY_NAME" : mapSocietyName, "MAP_USER_ROLE" : mapUserRole, "MAP_TKEY" : mapTkey, "MAP_SOCIETY_ID" : mapSociety_id , mapUnit_id : "MAP_UNIT_ID", mapUnit_no : "MAP_UNIT_NO", mapUnit_Block :"MAP_UNIT_BLOCK", mapBlock_desc :"MAP_BLOCK_DESC" };
    this.storage.set('mapDetails', obj);

    this.MAP_ID = mapID;
    this.MAP_SOCIETY_NAME = mapSocietyName;
    this.MAP_USER_ROLE = mapUserRole;
    this.MAP_TKEY = mapTkey;
    this.MAP_SOCIETY_ID = mapSociety_id;
    this.MAP_UNIT_ID =mapUnit_id;
    this.MAP_UNIT_NO =mapUnit_no;
    this.MAP_UNIT_BLOCK =mapUnit_Block;
    this.MAP_BLOCK_DESC =mapBlock_desc;
  }
  setUserProfileDetails(Approve_lease, Approve_classified,Create_album,Create_POll,Edit_MemberProfile,Manage_lien,Approve_photo,send_even,send_notice,Approve_provider,send_notification,user_managment) {
    
     this.APPROVALS_LEASE = Approve_lease;
     this.APPROVALS_CLASSIFIED = Approve_classified;
     this.PROFILE_CREATE_ALBUM = Create_album;
     this.PROFILE_CREATE_POLL = Create_POll;
     this.PROFILE_EDIT_MEMBER = Edit_MemberProfile;
     this.PROFILE_MANAGE_LIEN =Manage_lien;
     this.PROFILE_PHOTO_APPROVAL =Approve_photo;
     this.PROFILE_SEND_EVENT =send_even;
     this.PROFILE_SEND_NOTICE =send_notice;
     this.PROFILE_SERVICE_PROVIDER =Approve_provider;
     this.PROFILE_SEND_NOTIFICATION =send_notification;
     this.PROFILE_USER_MANAGEMENT =user_managment;
  
   }
   getProfileDetails() {
    return this.storage.get('profileDetails').then((value) => {
      return value;
    });
  }
  getMapDetails() {
    return this.storage.get('mapDetails').then((value) => {
      return value;
    });
  }

  setMapIDArray(mapArray) {
    this.storage.set('mapArray', mapArray);
  }
  getMapIDArray() {
    return this.storage.get('mapArray').then((value) => {
      return value;
    });
  }
  clearStorage() {
    this.storage.clear();
    this.HAS_LOGGED_IN = 'hasLoggedIn';
    this.USER_TOKEN = "";
    this.USER_NAME = "";

    this.MAP_ID = 0;
    this.MAP_SOCIETY_NAME = "";
    this.MAP_USER_ROLE = "";
    this.MAP_TKEY = ""
    this.MAP_SOCIETY_ID = 0;
    this.MAP_UNIT_ID = 0;
    this.MAP_UNIT_NO = 0;
    this.MAP_UNIT_BLOCK =0;
    this.MAP_BLOCK_DESC ="";
}
hasLoggedIn() {
  return this.storage.get(this.HAS_LOGGED_IN).then((value) => {
    return value === true;
  });
}
}
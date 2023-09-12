import { Injectable } from '@angular/core';

import { StorageService } from './StorageService';

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
  WAR_VERSION = "";
  //APP_VERSION = "1.0.20180117";
  LATEST_APP_VERSION = "2.1.20230803";
  APP_DOWNLOAD_LINK = "";
  /* Profile Flages    Admin  and Admin Member*/
  APPROVALS_LEASE = 0;
  APPROVALS_CLASSIFIED = 0;
  PROFILE_CREATE_ALBUM = 0;
  PROFILE_CREATE_POLL = 0;
  PROFILE_EDIT_MEMBER = 0;
  PROFILE_MANAGE_LIEN = 0;
  PROFILE_PHOTO_APPROVAL = 0;
  PROFILE_SEND_EVENT = 0;
  PROFILE_SEND_NOTICE = 0;
  PROFILE_SEND_NOTIFICATION = 0;
  PROFILE_SERVICE_PROVIDER = 0;
  PROFILE_USER_MANAGEMENT = 0;
  MEMBER_DUES_AMOUNT = 0;
  LoginID = 0;
  HOST_NAME = "https://way2society.com/"; //Server
  MAP_LOGIN_ID = 0;
  RENOVATION_REQUEST_ID = 0;
  TENANT_REQUEST_ID = 0;
  ADDRESS_PROOF_REQUEST_ID = 0;
  MEMBER_UNIT_ID = 0;

  APP_MENU = [];

  USER_ROLE = "2";    // 1 is for Managers & 2 is for MRs
  MENU_ARR = [];

  APP_VERSION = "0.9.20170921";

  constructor(public storage: StorageService) {

    /*this.PROFILE_APPROVALS_LEASE = 0;
     this.PROFILE_CLASSIFIED= 0;
     this.PROFILE_CREATE_ALBUM= 0;
     this.PROFILE_CREATE_POLL= 0;
     this.PROFILE_EDIT_MEMBER= 0;
     this.PROFILE_MANAGE_LIEN= 0;
     this.PROFILE_PHOTO_APPROVAL= 0;
     this.PROFILE_SEND_EVENT= 0;
     this.PROFILE_SEND_NOTICE= 0;
     this.PROFILE_SEND_NOTIFICATION= 0;
     this.PROFILE_SERVICE_PROVIDER= 0;
     this.PROFILE_USER_MANAGEMENT= 0;*/
    this.MAP_LOGIN_ID = 0;
  }

  /*setAppMenu(role) {
  alert(role);
      if(role == 'Member') {
        this.APP_MENU = [
          { title: 'Dashboard', component: DashboardPage }

        ];
      }
      else if(role == 'AdminMember') {
      this.APP_MENU = [
          { title: 'Dashboard1', component: DashboardPage }

        ];
      }
  }*/

  login(username) {
    this.storage.set(this.HAS_LOGGED_IN, true);
    //this.setUserName(username);
    //this.events.publish('user:login');
  }

  logout() {
    this.storage.remove(this.HAS_LOGGED_IN);
    this.storage.remove('username');
    //this.events.publish('user:logout');
  }

  setUserDetails(userToken, userName) {
    var obj = { "USER_TOKEN": userToken, "USER_NAME": userName };
    this.storage.set('userDetails', obj);

    this.USER_TOKEN = userToken;
    this.USER_NAME = userName;
  }

  getUserDetails(): Promise<any> {
    // return resolve(this.storage.get('userDetails'));
    var data = this.storage.get('userDetails');
    return new Promise((resolve, reject) => {
      resolve(JSON.parse(data));
    });
  }

  setMapDetails(mapID, mapSocietyName, mapUserRole, mapTkey, mapSociety_id, mapUnit_id, mapUnit_no, mapUnit_Block, mapBlock_desc) {
    var obj = { "MAP_ID": mapID, "MAP_SOCIETY_NAME": mapSocietyName, "MAP_USER_ROLE": mapUserRole, "MAP_TKEY": mapTkey, "MAP_SOCIETY_ID": mapSociety_id, mapUnit_id: "MAP_UNIT_ID", mapUnit_no: "MAP_UNIT_NO", mapUnit_Block: "MAP_UNIT_BLOCK", mapBlock_desc: "MAP_BLOCK_DESC" };
    this.storage.set('mapDetails', obj);

    this.MAP_ID = mapID;
    this.MAP_SOCIETY_NAME = mapSocietyName;
    // alert( this.MAP_SOCIETY_NAME);
    this.MAP_USER_ROLE = mapUserRole;
    this.MAP_TKEY = mapTkey;
    this.MAP_SOCIETY_ID = mapSociety_id;
    this.MAP_UNIT_ID = mapUnit_id;
    this.MAP_UNIT_NO = mapUnit_no;
    this.MAP_UNIT_BLOCK = mapUnit_Block;
    this.MAP_BLOCK_DESC = mapBlock_desc;
    //  alert(  this.MAP_UNIT_NO);

  }
  setUserProfileDetails(Approve_lease, Approve_classified, Create_album, Create_POll, Edit_MemberProfile, Manage_lien, Approve_photo, send_even, send_notice, Approve_provider, send_notification, user_managment) {
    // var obj = {"APPROVALS_LEASE" : Approve_lease, "APPROVALS_CLASSIFIED" : Approve_classified};
    // this.storage.set('profileDetails', obj);

    this.APPROVALS_LEASE = Approve_lease;
    this.APPROVALS_CLASSIFIED = Approve_classified;
    this.PROFILE_CREATE_ALBUM = Create_album;
    this.PROFILE_CREATE_POLL = Create_POll;
    this.PROFILE_EDIT_MEMBER = Edit_MemberProfile;
    this.PROFILE_MANAGE_LIEN = Manage_lien;
    this.PROFILE_PHOTO_APPROVAL = Approve_photo;
    this.PROFILE_SEND_EVENT = send_even;
    this.PROFILE_SEND_NOTICE = send_notice;
    this.PROFILE_SERVICE_PROVIDER = Approve_provider;
    this.PROFILE_SEND_NOTIFICATION = send_notification;
    this.PROFILE_USER_MANAGEMENT = user_managment;
    //alert(this.APPROVALS_LEASE);
    // alert(this.APPROVALS_CLASSIFIED);
  }
  getProfileDetails(): Promise<any> {
    return new Promise((resolve, reject) => {
      resolve(JSON.parse(this.storage.get('profileDetails')));
    });

  }
  getMapDetails(): Promise<any> {
    //return this.storage.get('mapDetails');
    var data = this.storage.get('mapDetails');
    return new Promise((resolve, reject) => {
      resolve(JSON.parse(data));
    });


  }

  setMapIDArray(mapArray) {

    this.storage.set('mapArray', mapArray);
  }

  getMapIDArray(): Promise<any> {
    return new Promise((resolve, reject) => {
      resolve(JSON.parse(this.storage.get('mapArray')));
    });

  }
  setUserUnit(member_unit) {
    console.log("mem", member_unit);
    this.MEMBER_UNIT_ID = member_unit;
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
    this.MAP_UNIT_BLOCK = 0;
    this.MAP_BLOCK_DESC = "";
  }

  /****************************** Delete Unused Functions From Below ****************************/

  /*setUserToken(userToken) {
    this.storage.set('userToken', userToken);
    this.USER_TOKEN = userToken;
  }

  getUserToken() {
    return this.storage.get('userToken').then((value) => {
      return value;
    });
  }

  setUserName(userName) {
    this.storage.set('userName', userName);
    this.USER_NAME = userName;
  }

  getUserName() {
    return this.storage.get('userName').then((value) => {
      return value;
    });
  }*/

  /*setUserRole(userRole) {
    this.storage.set('userRole', userRole);
  }

  getUserRole() {
    return this.storage.get('userRole').then((value) => {
      return value;
    });
  }*/

  /*setMapID(mapId) {
    this.storage.set('mapId', mapId);
    this.MAP_ID = mapId;
  }

  getMapID() {
    return this.storage.get('mapId').then((value) => {
      return value;
    });
  }

  setMapSociety(mapSociety) {
    this.storage.set('mapSociety', mapSociety);
    this.SOCIETY_NAME = mapSociety;
  }

  getMapSociety() {
    return this.storage.get('mapSociety').then((value) => {
      return value;
    });
  }*/

  // return a promise
  hasLoggedIn() {
    return this.storage.get(this.HAS_LOGGED_IN);
  }
}

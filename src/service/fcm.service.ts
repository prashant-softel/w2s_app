import { Injectable } from '@angular/core';
import { Capacitor } from '@capacitor/core';
import { PushNotifications } from '@capacitor/push-notifications';
import { GlobalVars } from './globalvars';

@Injectable({
  providedIn: 'root'
})
export class FcmService {

  constructor(
    public globalVars: GlobalVars,
  ) {

  }
  initPush() {
    if (Capacitor.getPlatform() !== 'web') {
      console.log("initPushToken");
      this.registerPush();
    }
  }
  private async registerPush() {
    try {
      await this.addListener();
      let permStatus = await PushNotifications.checkPermissions();

      if (permStatus.receive === 'prompt') {
        permStatus = await PushNotifications.requestPermissions();
      }

      if (permStatus.receive !== 'granted') {
        throw new Error('User denied permissions!');
      }

      await PushNotifications.register();
    } catch (e) {
      console.log({ "eeeeee": e });
    }
  }
  async getDeliveredNotifications() {
    const notificationList = await PushNotifications.getDeliveredNotifications();
    console.log('delivered notifications', notificationList);
  }
  async addListener() {
    await PushNotifications.addListener('registration', token => {
      this.globalVars.DEVICE_ID = token.value;
      console.info('Registration token: ', token.value);
    });

    await PushNotifications.addListener('registrationError', err => {
      console.error('Registration error: ', err.error);
    });

    await PushNotifications.addListener('pushNotificationReceived', notification => {
      console.log('Push notification received: ', notification);
    });

    await PushNotifications.addListener('pushNotificationActionPerformed', notification => {
      console.log('Push notification action performed', notification.actionId, notification.inputValue);
    });
  }
}

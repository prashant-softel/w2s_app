import { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'com.ionicframework.way2society869487',
  appName: 'Way2Society',
  webDir: 'www',
  server: {
    androidScheme: 'http'
  },
  "plugins": {
    "PushNotifications": {
      "presentationOptions": ["badge", "sound", "alert"]
    },
    PhotoViewer: {
      iosImageLocation: 'Library/Images',
      androidImageLocation: 'Files/Images',
    }
  }
};

export default config;

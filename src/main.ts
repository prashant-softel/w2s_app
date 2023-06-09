import { enableProdMode, importProvidersFrom } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { RouteReuseStrategy, provideRouter } from '@angular/router';
import { IonicModule, IonicRouteStrategy } from '@ionic/angular';
import { IonicStorageModule } from '@ionic/storage-angular';
import { routes } from './app/app.routes';
import { AppComponent } from './app/app.component';
import { environment } from './environments/environment';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

if (environment.production) {
  enableProdMode();
}
platformBrowserDynamic().bootstrapModule(AppComponent);

bootstrapApplication(AppComponent, {

  providers: [
    {
      provide: RouteReuseStrategy,

      useClass: IonicRouteStrategy
    },
    importProvidersFrom(
      IonicStorageModule.forRoot({}),
      IonicModule.forRoot({})),
    provideRouter(routes),
  ],
});

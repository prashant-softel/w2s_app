import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { IonicModule, NavParams } from '@ionic/angular';
import { ConnectServer } from 'src/service/connectserver';


import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss'],
  standalone: true,
  providers: [ConnectServer
    , GlobalVars,
    LoaderView,
    NavParams 

  ],

  imports: [
    IonicModule,
    HttpClientModule,
    CommonModule,
  ],
})
export class AppComponent {
  constructor() { }
}

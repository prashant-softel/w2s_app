import { HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { ApiProvider } from 'src/service/api';
import { IonicStorageModule } from '@ionic/storage-angular';
@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss'],
  standalone: true,
  providers:[ApiProvider],
  imports: [IonicModule,
    HttpClientModule,
    
  ],
})
export class AppComponent {
  constructor() {}
}

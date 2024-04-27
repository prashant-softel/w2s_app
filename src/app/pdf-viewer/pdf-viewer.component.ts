import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-pdf-viewer',
  templateUrl: './pdf-viewer.component.html',
  styleUrls: ['./pdf-viewer.component.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class PdfViewerComponent implements OnInit {

  constructor(
    private http: HttpClient,

  ) {
    this.loadPdf();

  }

  ngOnInit() { }
  pdfUrl: string;



  // async loadPdf() {
  //   const response = await Http.request({
  //     method: 'GET',
  //     url: 'YOUR_PDF_URL_HERE',
  //     responseType: 'blob',
  //   });

  //   const blob = new Blob([response.data], { type: 'application/pdf' });
  //   this.pdfUrl = URL.createObjectURL(blob);
  // }
  loadPdf(): any {
    // this.pdfUrl = URL.createObjectURL("https://way2society.com/maintenance_bills/RHG_TEST/October-December%202022/bill-RHG_TEST-202-October-December%202022-0.pdf");
    return this.http
      .get("https://way2society.com/maintenance_bills/RHG_TEST/October-December%202022/bill-RHG_TEST-202-October-December%202022-0.pdf", {
        responseType: 'blob',
      })
      .toPromise()
      .then((res) => {

        return new Blob([res], { type: 'application/pdf' });
      })
      .catch((error) => {

        console.log({ 'Download error': error });
      });
  }

}

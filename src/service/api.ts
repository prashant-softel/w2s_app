import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
 
@Injectable()
export class ApiProvider {
 
  constructor(public http: HttpClient) { }
 
  getFilms() {
    return this.http.get('https://swapi.dev/api/films');
  }
}
import { Injectable } from '@angular/core';



@Injectable({
    providedIn: 'root'
})
export class StorageService {
  

    constructor() {
       
    }

   

    // Create and expose methods that users of this service can
    // call, for example:
    public set(key: string, value: any) {
        localStorage.setItem(key,JSON.stringify(value));
        //this._storage?.set(key, value);
    }
    public get(key: string) {
        console.log(key);
        return  localStorage.getItem(key);
    }
    public clear() {
       
    }
    public remove(key) {
       
    }
}
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  constructor(private hc: HttpClient) {}

  public makeRequest<T = any>(path: string, method: string, body?: any, options?: any): Observable<T> {

    return this.hc.request(
      method,
      `${environment.API_BASE_URL}${path}`,
      { body, ...options, observe: 'body', responseType: 'json' }
    ) as Observable<T>;
  }

}

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
 
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
 
import { Config } from './config';
import { MessageService } from '../message/message.service';

const httpOptions = {
  headers: new HttpHeaders({ 'Accept' : 'application/json'})
};
 
@Injectable({ providedIn: 'root' })
export class ConfigService {
 
  private configUrl = 'api/config';  // URL to web api
 
  constructor(
    private http: HttpClient,
    private messageService: MessageService) { }
 
  
  /** GET config. Will 404 if id not found */
  getConfig(): Observable<Config> {
    return this.http.get<Config>(this.configUrl, httpOptions).pipe(
      tap(_ => this.messageService.success('fetched config', 5000)),
      catchError(this.handleError<any>('getConfig'))
    );

  }
 
  /** PUT: update the config on the server */
  updateConfig (config: Config): Observable<any> {
      const options = {
          headers: httpOptions.headers.set('Content-Type', 'application/json')
      };
    return this.http.put(this.configUrl, config, options).pipe(
      tap(_ => this.messageService.success('updated config', 5000)),
      catchError(this.handleError<any>('updateConfig'))
    );
  }
 
  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T> (operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
 
      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead
 
      // TODO: better job of transforming error for user consumption
      this.messageService.error(`${operation} failed: ${error.message}`, 5000);
 
      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
 
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
 
import { Episode } from './episode/episode';
import { MessageService } from './message/message.service';
 
const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};
 
const httpOptions = {
  headers: new HttpHeaders({ 'Accept' : 'application/json'})
};

@Injectable({ providedIn: 'root' })
export class EpisodeService {
 
  private episodeUrl = 'api/episode';  // URL to web api
 
  constructor(
    private http: HttpClient,
    private messageService: MessageService) { }
 
  /** GET heroes from the server */
  getEpisodes (): Observable<Episode[]> {
    return this.http.get<Episode[]>(this.episodeUrl, httpOptions)
      .pipe(
        tap(heroes => this.messageService.success('fetched episodes', 5000)),
        catchError(this.handleError('getEpisodes', []))
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
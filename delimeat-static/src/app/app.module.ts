import { NgModule } from '@angular/core';
import { BrowserModule }  from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { HttpClientModule }    from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
 
import { AppComponent } from './app.component';
import { ConfigComponent } from './config/config.component';
import { EpisodesComponent } from './episode/episodes.component';
import { GuideComponent } from './guide/guide.component';
import { MessagesComponent } from './message/messages.component';
import { NavbarComponent } from './navbar/navbar.component';
import { ShowsComponent } from './shows/shows.component';

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  declarations: [
    AppComponent,
    ConfigComponent,
    EpisodesComponent,
    GuideComponent,
    MessagesComponent,
    NavbarComponent,
    ShowsComponent
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { EpisodesComponent } from './episode/episodes.component';
import { GuideComponent } from './guide/guide.component';
import { ConfigComponent } from './config/config.component';
import { ShowsComponent } from './shows/shows.component';

const routes: Routes = [
  { path: '', redirectTo: '/episodes', pathMatch: 'full' },
  { path: 'episodes', component: EpisodesComponent },
  { path: 'guide', component: GuideComponent },
  { path: 'settings', component: ConfigComponent },
  { path: 'shows', component: ShowsComponent }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes, { useHash: true }) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
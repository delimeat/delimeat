import { Component, OnInit } from '@angular/core';
 
import { Episode } from './episode';
import { EpisodeService } from '../episode.service';
 
@Component({
  selector: 'app-episodes',
  template: require('./episodes.component.html')
})
  
export class EpisodesComponent implements OnInit {
  episodes: Episode[];
 
  constructor(private episodeService: EpisodeService) { }
 
  ngOnInit() {
    this.getEpisodes();
  }
 
  getEpisodes(): void {
    this.episodeService.getEpisodes().subscribe(episodes => this.episodes = episodes);
  }
 
}
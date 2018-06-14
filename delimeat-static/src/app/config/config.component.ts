import {Component, OnInit} from '@angular/core';

import {Config} from "./config";
import {ConfigService} from "./config.service";

@Component({
  selector: 'app-config',
  template: require('./config.component.html')
})

export class ConfigComponent implements OnInit {

  model: any = {};
  
  constructor(private configService: ConfigService) {}

  ngOnInit(): void {
    this.getConfig();
  }

  getConfig(): void {
    this.configService.getConfig().subscribe((config: Config) => this.model = config);
  }
  
  onSubmit() {
    this.configService.updateConfig(this.model).subscribe((config: Config) => this.model = config);
  }

}
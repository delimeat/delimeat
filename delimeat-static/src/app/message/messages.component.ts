import { Component, OnInit } from '@angular/core';
import { MessageService } from './message.service';
 
@Component({
  selector: 'app-messages',
  template: require('./messages.component.html')
})
export class MessagesComponent implements OnInit {
 
  constructor(public messageService: MessageService) {}
 
  ngOnInit() {
  }
 
}
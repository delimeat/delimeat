import { Injectable } from '@angular/core';
 
import { Message } from './message';

@Injectable({
  providedIn: 'root',
})
export class MessageService {
  messages: Message[] = [];
 
  add(type: string, text: string, timeout: number) {
    this.messages.push({type, text, timeout});
  }
  
  error(text: string, timeout: number){
    this.add('danger', text, timeout);
  }
  
  success(text: string, timeout: number){
    this.add('success', text, timeout);
  }
  
  warning(text: string, timeout: number){
    this.add('warning', text, timeout);
  }
  
  info(text: string, timeout: number){
    this.add('info', text, timeout);
  }
 
  clear() {
    this.messages = [];
  }
}
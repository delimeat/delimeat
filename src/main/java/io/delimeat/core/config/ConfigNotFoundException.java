package io.delimeat.core.config;

public class ConfigNotFoundException extends ConfigException{
  
  static final long serialVersionUID = -1;
  
  public ConfigNotFoundException(String message){
    super(message);
  }
  
  public ConfigNotFoundException(Throwable cause){
    super(cause);
  }
  
  public ConfigNotFoundException(String message,Throwable cause){
    super(message,cause);
  }
  
}

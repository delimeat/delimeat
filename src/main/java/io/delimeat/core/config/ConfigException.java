package io.delimeat.core.config;

public class ConfigException extends Exception{
  
  static final long serialVersionUID = -1;
  
  public ConfigException(String message){
    super(message);
  }
  
  public ConfigException(Throwable cause){
    super(cause);
  }
  
  public ConfigException(String message,Throwable cause){
    super(message,cause);
  }
}

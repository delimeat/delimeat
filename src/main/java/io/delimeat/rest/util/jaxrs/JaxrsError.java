package io.delimeat.rest.util.jaxrs;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JaxrsError {
  
  private String error;
  private int status;  
  
  public JaxrsError(){}
  
  public JaxrsError(int status,String error){
    this.status = status;
    this.error = error;
  }
  
  public void setError(String error){
    this.error = error;
  }
  
  public String getError(){
    return error;
  }
  
  public void setStatus(int status){
    this.status = status;
  }
  
  public int getStatus(){
    return status;
  }
  
  @Override
  public String toString() {
    return "Status: " + status + " Error: " + error;
  }
  
}

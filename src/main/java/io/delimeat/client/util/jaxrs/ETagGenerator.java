package io.delimeat.client.util.jaxrs;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

import javax.ws.rs.NameBinding;

@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ETagGenerator {
 	String[] value(); 
}
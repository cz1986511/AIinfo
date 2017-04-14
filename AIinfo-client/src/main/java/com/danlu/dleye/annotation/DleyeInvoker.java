package com.danlu.dleye.annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.danlu.dleye.constants.InvokerType;

@Target({java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DleyeInvoker {
    
    public abstract String description();
    
    public abstract String paraDesc();
    
    public abstract InvokerType type();

}

package com.danlu.dleye.manager;

import java.io.PrintStream;
import java.util.Map;

import javax.servlet.ServletContext;

import com.danlu.dleye.constants.ManagerType;

public abstract interface DleyeManager {
    
    public abstract String service(Map<String, String> paramMap);
    
    public abstract void init(ServletContext paramServletContext, PrintStream paramPrintStream, Map<String, Object> paramMap);
    
    public abstract ManagerType getType();

}

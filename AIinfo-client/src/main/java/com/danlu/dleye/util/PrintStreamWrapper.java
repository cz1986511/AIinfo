package com.danlu.dleye.util;

import java.io.PrintStream;

public class PrintStreamWrapper extends PrintStream{
    
    public final DleyeOutputStream ao;

    public PrintStreamWrapper(DleyeOutputStream out, boolean autoFlush)
    {
      super(out, autoFlush);
      this.ao = out;
    }

}

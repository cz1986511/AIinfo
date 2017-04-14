package com.danlu.dleye.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.danlu.dleye.constants.ManagerType;

public class ManagerLoggerUtil {
    
    public static final int _MEM_LOG_LIMIT = 1048576;
    public static final Map<ManagerType, DleyeOutputStream> streamMap = new HashMap<ManagerType, DleyeOutputStream>();

    private static DleyeOutputStream bootstrapStream = null;

    public static final Map<ManagerType, String> initLogs = new HashMap<ManagerType, String>();

    public static String bootstrapLog = null;

    public static PrintStream getManagerLogger(ManagerType managerType)
    {
      DleyeOutputStream DleyeOutputStream = new DleyeOutputStream(new ByteArrayOutputStream(), 1048576);
      streamMap.put(managerType, DleyeOutputStream);
      return new PrintStream(DleyeOutputStream, true);
    }

    public static synchronized PrintStream getBootstrapLogger()
    {
      if (bootstrapStream == null)
      {
        bootstrapStream = new DleyeOutputStream(new ByteArrayOutputStream(), 1048576);
      }
      return new PrintStream(bootstrapStream, true);
    }

    public static void invalidBootstrapStream()
    {
      if (bootstrapStream != null)
      {
        bootstrapStream.invalid();
        bootstrapLog = new String(bootstrapStream.getTarget().toByteArray()).replaceAll("\n", "<br/>");

        bootstrapStream.target = null;
        bootstrapStream = null;
      }
    }

    public static void invalidInitLogger(ManagerType managerType)
    {
      DleyeOutputStream DleyeOutputStream = (DleyeOutputStream)streamMap.remove(managerType);
      if (DleyeOutputStream != null)
      {
        DleyeOutputStream.invalid();
        initLogs.put(managerType, new String(DleyeOutputStream.getTarget().toByteArray()).replaceAll("\n", "<br/>"));

        DleyeOutputStream.target = null;
      }
    }

    public static String convertThrowableToString(Throwable t)
    {
      ByteArrayOutputStream bo = new ByteArrayOutputStream();
      PrintWriter out = new PrintWriter(bo);
      t.printStackTrace(out);
      out.close();
      return new String(bo.toByteArray()).replaceAll("\n", "<br/>");
    }

}

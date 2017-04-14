package com.danlu.dleye.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class DleyePrintStreamFactory {

    public static int _ATEYE_OUT_LIMIT = 1048576;

    public static final ThreadLocal<PrintStreamWrapper> printStreamLocal = new ThreadLocal<PrintStreamWrapper>();

    public static PrintStream getPrintStream() {
        return (PrintStream) printStreamLocal.get();
    }

    public static void put() {
        DleyeOutputStream ao = new DleyeOutputStream(new ByteArrayOutputStream(), _ATEYE_OUT_LIMIT);
        PrintStreamWrapper psw = new PrintStreamWrapper(ao, true);
        printStreamLocal.set(psw);
    }

    public static String getDleyeOut() {
        String outMessage = "";
        PrintStreamWrapper psw = (PrintStreamWrapper) printStreamLocal.get();
        if (psw != null) {
            psw.flush();
            DleyeOutputStream ao = psw.ao;
            outMessage = new String(ao.getTarget().toByteArray()).replaceAll("\n", "<br/>");
            printStreamLocal.remove();
        }
        return outMessage;
    }

}

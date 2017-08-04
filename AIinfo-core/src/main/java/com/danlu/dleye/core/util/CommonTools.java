package com.danlu.dleye.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonTools {

    public static String getDateString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date());
    }

}

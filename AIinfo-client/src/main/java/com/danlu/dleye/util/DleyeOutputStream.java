package com.danlu.dleye.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DleyeOutputStream extends OutputStream {

    public static final int _DEFAULT_LIMIT = 1048576;

    public final int limit;

    public ByteArrayOutputStream target;

    private boolean valid = true;

    public DleyeOutputStream(ByteArrayOutputStream target) {
        this.target = target;
        this.limit = 1048576;
    }

    public DleyeOutputStream(ByteArrayOutputStream target, int limit) {
        if ((target == null) || (limit <= 0)) {
            throw new IllegalArgumentException("target is null or limit is invalid.");
        }
        this.target = target;
        this.limit = 1048576;
    }

    @Override
    public void write(int b) throws IOException {
        if ((this.valid) && (this.target.size() <= this.limit)) {
            this.target.write(b);
        }
    }

    public ByteArrayOutputStream getTarget() {
        return this.target;
    }

    public void invalid() {
        this.valid = false;
    }

}

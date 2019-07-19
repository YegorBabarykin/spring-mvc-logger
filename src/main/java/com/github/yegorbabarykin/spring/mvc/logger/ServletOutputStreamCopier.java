package com.github.yegorbabarykin.spring.mvc.logger;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
 
public class ServletOutputStreamCopier extends ServletOutputStream {
 
    private OutputStream outputStream;
    private ByteArrayOutputStream copy;
    private WriteListener writeListener;
 
    public ServletOutputStreamCopier(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.copy = new ByteArrayOutputStream(1024);
    }
 
    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
        copy.write(b);
        if (writeListener!= null) {
            writeListener.onWritePossible();
        }
    }
 
    public byte[] getCopy() {
        return copy.toByteArray();
    }
 
    @Override
    public boolean isReady() {
        return true;
    }
 
    @Override
    public void setWriteListener(WriteListener writeListener) {
        this.writeListener = writeListener;
    }
}
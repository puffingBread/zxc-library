package org.humor.zxc.library.starter.email;

import javax.activation.DataSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Date: 2021/12/4
 * Time: 5:04 PM
 *
 * @author xuzz
 */
public class ByteArrayDataSource implements DataSource {
    private final String contentType;
    private final byte[] buf;
    private final int len;

    public ByteArrayDataSource(byte[] buf, String contentType) {
        this(buf, buf.length, contentType);
    }

    public ByteArrayDataSource(byte[] buf, int length, String contentType) {
        this.buf = buf;
        this.len = length;
        this.contentType = contentType;
    }

    @Override
    public String getContentType() {
        if (contentType == null) {
            return "application/octet-stream";
        }
        return contentType;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(buf, 0, len);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public OutputStream getOutputStream() {
        throw new UnsupportedOperationException();
    }

}

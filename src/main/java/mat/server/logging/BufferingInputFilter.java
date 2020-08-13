package mat.server.logging;


import org.apache.commons.logging.Log;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BufferingInputFilter implements Filter {
    private static final Log log = LogFactory.getLog(BufferingInputFilter.class);

    public static class NonBufferingRequestWrapper extends HttpServletRequestWrapper {
        private final String body;

        public NonBufferingRequestWrapper(HttpServletRequest request) {
            super(request);

            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = null;

            try {
                InputStream inputStream = request.getInputStream();

                if (inputStream != null) {
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    char[] charBuffer = new char[80000];
                    int bytesRead;

                    while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                        stringBuilder.append(charBuffer, 0, bytesRead);
                    }
                }
            } catch (IOException ioe) {
                log.error("Error buffering input stream.", ioe);
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException ioe) {
                        log.error("Error closing reader.", ioe);
                    }
                }
            }
            body = stringBuilder.toString();
        }

        @Override
        public ServletInputStream getInputStream() {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
            return new ServletInputStream() {
                public boolean isFinished() {
                    return false;
                }

                public boolean isReady() {
                    return true;
                }

                public void setReadListener(ReadListener readListener) {
                    throw new UnsupportedOperationException();
                }

                public int read() {
                    return byteArrayInputStream.read();
                }


            };
        }

        @Override
        public BufferedReader getReader() {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = new NonBufferingRequestWrapper((HttpServletRequest) request);
        HttpServletResponse res = (HttpServletResponse) response;
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
    }
}


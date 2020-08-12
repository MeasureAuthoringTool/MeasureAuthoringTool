package mat.server.logging;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;

public class LogFactory {
    public static Log getLog(Class<?> clazz) {
        return new LogDecorator(org.apache.commons.logging.LogFactory.getLog(clazz));
    }

    public static Log getLog(String name) {
        return new LogDecorator(org.apache.commons.logging.LogFactory.getLog(name));
    }

    public static class LogDecorator implements Log {
        private final Log log;

        public LogDecorator(Log log) {
            this.log = log;
        }

        @Override
        public boolean isFatalEnabled() {
            return log.isFatalEnabled();
        }

        @Override
        public boolean isErrorEnabled() {
            return log.isErrorEnabled();
        }

        @Override
        public boolean isWarnEnabled() {
            return log.isWarnEnabled();
        }

        @Override
        public boolean isInfoEnabled() {
            return log.isInfoEnabled();
        }

        @Override
        public boolean isDebugEnabled() {
            return log.isDebugEnabled();
        }

        @Override
        public boolean isTraceEnabled() {
            return log.isTraceEnabled();
        }

        public String getLogHeaders() {
            return StringUtils.defaultString(RequestResponseLoggingFilter.logHeaders.get());
        }

        @Override
        public void fatal(Object o) {
            log.fatal(getLogHeaders() + o);
        }

        @Override
        public void fatal(Object o, Throwable throwable) {
            log.fatal(getLogHeaders() + o, throwable);
        }

        @Override
        public void error(Object o) {
            log.error(getLogHeaders() + o);
        }

        @Override
        public void error(Object o, Throwable throwable) {
            log.error(getLogHeaders() + o, throwable);
        }

        @Override
        public void warn(Object o) {
            log.warn(getLogHeaders() + o);
        }

        @Override
        public void warn(Object o, Throwable throwable) {
            log.warn(getLogHeaders() + o, throwable);
        }

        @Override
        public void info(Object o) {
            log.info(getLogHeaders() + o);
        }

        @Override
        public void info(Object o, Throwable throwable) {
            log.info(getLogHeaders() + o, throwable);

        }

        @Override
        public void debug(Object o) {
            log.debug(getLogHeaders() + o);

        }

        @Override
        public void debug(Object o, Throwable throwable) {
            log.debug(getLogHeaders() + o, throwable);

        }

        @Override
        public void trace(Object o) {
            log.trace(getLogHeaders() + o);

        }

        @Override
        public void trace(Object o, Throwable throwable) {
            log.trace(getLogHeaders() + o, throwable);
        }
    }
}

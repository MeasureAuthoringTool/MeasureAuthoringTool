package mat.server;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SpringRemoteServiceServlet extends RemoteServiceServlet {

    private static final long serialVersionUID = 8359364426336388916L;

    protected ApplicationContext context;

    @Override
    public void init(ServletConfig config) throws ServletException {
        log.info(getClass().getSimpleName() + " init()");
        super.init(config);
        if (this.context == null) {
            this.context = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
            this.context.getAutowireCapableBeanFactory().autowireBean(this);
        }
    }

    @Override
    protected SerializationPolicy doGetSerializationPolicy(HttpServletRequest request, String moduleBaseURL, String strongName) {
        try {
            return super.doGetSerializationPolicy(request, moduleBaseURL, strongName);
        } catch (Exception exc) {
            log("Exception in doGetSerializationPolicy", exc);
            String uri = request.getRequestURI();
            String base = request.getScheme() + "://" + request.getServerName() + uri;
            base = base.substring(0, base.lastIndexOf("/") + 1);
            return super.doGetSerializationPolicy(request, base, strongName);
        }
    }

    public ApplicationContext getContext() {
        return context;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void log(String msg) {
        log.info(msg);
        super.log(msg);
    }

    @Override
    public void log(String message, Throwable t) {
        log.error(message, t);
        super.log(message, t);
    }

    @Override
    protected void doUnexpectedFailure(Throwable e) {
        log.error("Exception while dispatching incoming RPC call", e);
        super.doUnexpectedFailure(e);
    }

}

package mat.server.auth.model;

import com.google.gwt.user.client.rpc.SerializationException;
import mat.server.SpringRemoteServiceServlet;
import mat.server.util.ServerConstants;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@WebServlet("/harpCheck")
public class HarpVerificationServlet extends SpringRemoteServiceServlet {

    private WebClient webClient;

    @Override
    public String processCall(String payload) throws SerializationException{
        TokenIntrospect introspect = validateToken(payload);
        getThreadLocalRequest().setAttribute("tokenValidation", introspect.toString());
        getThreadLocalRequest().setAttribute("token", payload);
        getThreadLocalRequest().setAttribute("harpUrl", ServerConstants.getHarpUrl());
        getThreadLocalRequest().setAttribute("loginUrl", ServerConstants.getEnvURL());

        try {
            getThreadLocalRequest().getRequestDispatcher("/HarpVerification.jsp").forward(getThreadLocalRequest(), getThreadLocalResponse());
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
        return introspect.toString();
    }

    @Override
    protected String readContent(HttpServletRequest request) throws ServletException, IOException {
        return request.getReader().lines().collect(Collectors.joining(System.lineSeparator())).substring("loginPost=".length());
    }

    private TokenIntrospect validateToken(String token) {
        return getClient()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/introspect")
                    .queryParam("token", token)
                    .queryParam("client_id", ServerConstants.getHarpClientId())
                    .queryParam("token_type_hint", "id_token")
                    .build())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .bodyToMono(TokenIntrospect.class).block();
    }

    private WebClient getClient() {
        if(isNull(webClient)) {
            this.webClient = WebClient.create(ServerConstants.getHarpUrl());
        }
        return webClient;
    }
}

package mat.server.auth.model;

import mat.server.SpringRemoteServiceServlet;
import mat.server.util.ServerConstants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/harpLogin")
public class HarpLoginServlet extends SpringRemoteServiceServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("clientId", ServerConstants.getHarpClientId());
        req.setAttribute("harpBaseUrl", ServerConstants.getHarpBaseUrl());

        req.getRequestDispatcher("/HarpLogin.jsp").forward(req, resp);
    }
}

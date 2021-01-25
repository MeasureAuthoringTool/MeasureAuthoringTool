package mat.vsac;

public interface RefreshTokenManager {
    String getRefreshedToken();

    void setRefreshedToken(String newTicketGrantingTicket);
}

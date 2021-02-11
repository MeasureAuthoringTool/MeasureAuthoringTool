package mat.vsac;

public class RefreshTokenManagerImpl implements RefreshTokenManager {
    private static RefreshTokenManager singletonInstance;

    private static final ThreadLocal<String> refreshedToken = new ThreadLocal<>();

    @Override
    public  String getRefreshedToken() {
        try {
            return refreshedToken.get();
        } finally {
            refreshedToken.remove();
        }
    }

    @Override
    public  void setRefreshedToken(  String newTicketGrantingTicket ) {
        refreshedToken.set(newTicketGrantingTicket);
    }

    public static RefreshTokenManager getInstance(){
        if (singletonInstance == null){
            singletonInstance = new RefreshTokenManagerImpl();
        }

        return singletonInstance;
    }
}

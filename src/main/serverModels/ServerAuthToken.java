package serverModels;

/**
 * Authentication token used for validating user actions (create game, join game, list games, etc)
 */
public class ServerAuthToken {

    /**
     * The username this token is associated with
     */
    private String username;

    /**
     * The authentication code for this token
     */
    private String authToken;

    /**
     * Creates a new AuthTokenDAO object from a username and authentication code
     * @param username The username the token is associated with
     * @param authToken The authentication code for this token
     */
    public ServerAuthToken(String username, String authToken){
        this.authToken = authToken;
        this.username = username;
    }
    public String getUsername(){
        return username;
    }
    public String getAuthToken(){
        return authToken;
    }
}

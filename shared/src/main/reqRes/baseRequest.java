package reqRes;

/**
 * Basic request object used to parse HTTP requests.
 */
public class baseRequest {
    /**
     * The username associated with this request. Used for creating an account or logging in.
     */
    public String username;
    /**
     * The authtoken associated with this request. Used for listing games, joining games, creating games, or logging out.
     */
    public String authToken;
    /**
     * The password associated with this request. Used for creating an account or logging in.
     */
    public String password;
    /**
     * The email associated with this request. Used for creating an account.
     */
    public String email;
    /**
     * The game name associated with this request. Used for creating a new game.
     */
    public String gameName;
    /**
     * The player color associated with this request. Used for joining games.
     */
    public String playerColor;
    /**
     * The game ID associated with this request. Used for joining games.
     */
    public int gameID;

    /**
     * Creates base class. Fields must be populated by accessing the attributes.
     */
    public baseRequest(){};
}

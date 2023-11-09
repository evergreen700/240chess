package serverModels;

/**
 * Server model object for users within the chess game
 */
public class ServerUser {
    /**
     * User's unique name
     */
    private String username;
    /**
     * User password
     */
    private String password;
    /**
     * User's unique email
     */
    private String email;

    /**
     * Creates new ServerUser object.
     * @param username: Username for the user
     * @param password: Password for the user
     * @param email: Email for the user
     */
    public ServerUser(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}

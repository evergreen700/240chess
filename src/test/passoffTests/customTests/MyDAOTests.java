package passoffTests.customTests;

import chess.*;
import dataAccess.*;
import org.junit.jupiter.api.*;
import passoffTests.testClasses.TestModels;
import reqRes.baseRequest;
import serverModels.ServerAuthToken;
import serverModels.ServerGame;
import serverModels.ServerUser;
import services.ClearApplicationService;

@SuppressWarnings("unused")

public class MyDAOTests {

    private static TestModels.TestUser existingUser;
    private static TestModels.TestUser newUser;
    private String nephiToken;
    private String badToken = "aaaaaaaaaaaaaaaaaaaaaaaaaaa";
    String existingUserToken = "498a9340-20fb-4e01-af4c-9f6d143f10bd";
    String newUserToken = "778a4200-269b-4e01-af4c-9f6d143f10bd";
    int existingGameID = 0;
    private static ServerGame firstGame;
    @BeforeAll
    public static void init() throws InvalidMoveException {
        existingUser = new TestModels.TestUser();
        existingUser.username="Nephi";
        existingUser.password="iwillgoanddo";
        existingUser.email="nephi@promisedland.net";

        newUser = new TestModels.TestUser();
        newUser.username = "Laman";
        newUser.password = "wannagohome";
        newUser.email = "laman@promisedland.net";

        firstGame = new ServerGame();
        firstGame.getGame().makeMove(new Move(new Position(1, 2), new Position(3, 1), null));

    }

    @BeforeEach
    public void setup(){
        new ClearApplicationService().clearAll(new baseRequest());

        ServerUser nephi = new ServerUser(existingUser.username,existingUser.password, existingUser.email);
        ServerAuthToken nephiToken = new ServerAuthToken(existingUser.username,existingUserToken);
        try{
            new UserDAO().insert(nephi);
            new AuthTokenDAO().insert(nephiToken);
            existingGameID = new GameDAO().insert(firstGame);
        } catch(DataAccessException e){}
    }
    @Test
    @DisplayName("Normal user insertion")
    public void successInsertion(){
        ServerUser laman = new ServerUser(newUser.username, newUser.password, newUser.email);

        Assertions.assertDoesNotThrow(()-> new UserDAO().insert(laman), "Method threw an exception");
    }


    @Test
    @DisplayName("Duplicate user insertion")
    public void failureUserInsertion(){
        ServerUser secondNephi = new ServerUser(existingUser.username, existingUser.password, existingUser.email);

        Assertions.assertThrows(DataAccessException.class, ()-> new UserDAO().insert(secondNephi), "Method did not throw expected exception");
    };

    @Test
    @DisplayName("Normal user find")
    public void successUserFind(){
        ServerUser retrievedNephi = null;
        try{retrievedNephi = new UserDAO().find(existingUser.username);}
        catch(DataAccessException ex){
            System.out.println(ex);
            Assertions.fail();
        }

        Assertions.assertEquals(existingUser.username,retrievedNephi.getUsername(),
                "Returned user object did not contain the same username");
        Assertions.assertEquals(existingUser.password,retrievedNephi.getPassword(),
                "Returned user object did not contain the same password");
        Assertions.assertEquals(existingUser.email,retrievedNephi.getEmail(),
                "Returned user object did not contain the same email");
    }

    @Test
    @DisplayName("Find nonexistant user")
    public void badUsernameFind(){
        ServerUser retrievedSam = null;
        try{retrievedSam = new UserDAO().find("Sam");}
        catch(DataAccessException ex){
            System.out.println(ex);
            Assertions.fail();
        }
        Assertions.assertNull(retrievedSam,
                "Response contained a user object when it shouldn't have");
    }

    @Test
    @DisplayName("Normal delete all users")
    public void clearAllUsers(){
        ServerUser retrievedNephi = null;
        try{new UserDAO().deleteAll();
            retrievedNephi = new UserDAO().find(existingUser.username);
        }
        catch(DataAccessException ex){
            System.out.println(ex);
            Assertions.fail();
        }
        Assertions.assertNull(retrievedNephi,
                "Response contained a user object when it shouldn't have");
    }

    @Test
    @DisplayName("Normal Insert AuthToken")
    public void successInsertAuth(){
        ServerAuthToken lamanToken = new ServerAuthToken(newUser.username,newUserToken);

        Assertions.assertDoesNotThrow(()-> new AuthTokenDAO().insert(lamanToken),
                "Method threw an exception");
    }

    @Test
    @DisplayName("Insert Duplicate AuthToken")
    public void duplicateInsertAuth(){
        ServerAuthToken secondNephiToken = new ServerAuthToken(existingUser.username, existingUserToken);

        Assertions.assertThrows(DataAccessException.class, ()-> new AuthTokenDAO().insert(secondNephiToken), "Method did not throw expected exception");
    }

    @Test
    @DisplayName("Normal Find AuthToken")
    public void successFindAuth(){
        ServerAuthToken retrievedToken = null;
        try{
            retrievedToken = new AuthTokenDAO().find(existingUserToken);
        }
        catch(DataAccessException ex){
            System.out.println(ex);
            Assertions.fail();
        }
        Assertions.assertEquals(existingUser.username,retrievedToken.getUsername(),
                "Returned authToken object did not contain the same username");
        Assertions.assertEquals(existingUserToken, retrievedToken.getAuthToken(),
                "Returned authToken object did not contain the same auth token string");
    }

    @Test
    @DisplayName("Find Invalid AuthToken")
    public void findInvalidAuth(){
        ServerAuthToken retrievedToken = null;
        try{
            retrievedToken = new AuthTokenDAO().find(badToken);
        }
        catch(DataAccessException ex){
            System.out.println(ex);
            Assertions.fail();
        }
        Assertions.assertNull(retrievedToken,
                "Returned authToken object not null");
    }

    @Test
    @DisplayName("Normal delete AuthToken")
    public void successAuthDelete(){
        Assertions.assertDoesNotThrow(()-> new AuthTokenDAO().delete(existingUserToken),
                "Method threw an exception");
    }

    @Test
    @DisplayName("Delete Invalid AuthToken")
    public void deleteInvalidAuthToken(){
        Assertions.assertThrows(DataAccessException.class, ()-> new AuthTokenDAO().delete(badToken),
                "Method did not throw expected exception");
    }

    @Test
    @DisplayName("Delete All AuthToken")
    public void deleteAllAuth(){
        ServerAuthToken retrievedToken = null;
        try{new AuthTokenDAO().deleteAll();
            retrievedToken = new AuthTokenDAO().find(existingUserToken);
        }
        catch(DataAccessException ex){
            System.out.println(ex);
            Assertions.fail();
        }
        Assertions.assertNull(retrievedToken,
                "Response contained an authToken object when it shouldn't have");
    }


    @Test
    @DisplayName("Normal Insert Game")
    public void successInsertGame() throws DataAccessException{
        ServerGame newGame = new ServerGame();
        int gameIndex = new GameDAO().insert(newGame);

        Assertions.assertNotNull(new GameDAO().find(gameIndex),
                "Inserted game not found");
    }
    @Test
    @DisplayName("Insert Null Game")
    public void insertNullGame(){
        Assertions.assertThrows(DataAccessException.class, ()-> new GameDAO().insert(null),
                "Method did not throw expected exception");
    }


    @Test
    @DisplayName("Normal Update Game")
    public void successUpdateGame(){
        ServerGame newGame = new ServerGame();
        try{
            newGame.setGameID(new GameDAO().insert(newGame));
        }catch(DataAccessException ex){
            System.out.println(ex);
            Assertions.fail();
        }

        newGame.setWhiteUsername(existingUser.username);
        newGame.setBlackUsername(newUser.username);

        Assertions.assertDoesNotThrow(()->new GameDAO().update(newGame),
                "Method threw an exception when it shouldn't have");
    }

    @Test
    @DisplayName("Update Game Bad GameID")
    public void updateBadGameID(){
        ServerGame newGame = new ServerGame();
        try{
            new GameDAO().insert(newGame);
        }catch(DataAccessException ex){
            System.out.println(ex);
            Assertions.fail();
        }
        newGame.setGameID(-1);
        newGame.setWhiteUsername(existingUser.username);
        newGame.setBlackUsername(newUser.username);

        Assertions.assertThrows(DataAccessException.class, ()->new GameDAO().update(newGame),
                "Method didn't throw the right exception");
    }

    @Test
    @DisplayName("Normal Find Game")
    public void successFindGame(){
        ServerGame retrivedGame = null;
        try{
            retrivedGame = new GameDAO().find(existingGameID);
        }catch(DataAccessException ex){
            System.out.println(ex);
            Assertions.fail();
        }
        Assertions.assertEquals(Knight.class, retrivedGame.getGame().gameBoard.getPiece(new Position(3,1)).getClass(),
                "Piece on the board did not have the correct type");
    }

    @Test
    @DisplayName("Find Game Bad GameID")
    public void badIDFindGame(){
        ServerGame retrivedGame = null;
        try{
            retrivedGame = new GameDAO().find(-1);
        }catch(DataAccessException ex){
            System.out.println(ex);
            Assertions.fail();
        }
        Assertions.assertNull(retrivedGame,
                "Did not return null game");
    }

    @Test
    @DisplayName("Normal List Games")
    public void successListGames(){
        ServerGame newGame = new ServerGame();
        ServerGame[] allGames = new ServerGame[0];
        try{
            new GameDAO().deleteAll();
            new GameDAO().insert(newGame);
            new GameDAO().insert(newGame);
            new GameDAO().insert(newGame);
            new GameDAO().insert(newGame);
            allGames = new GameDAO().findAll();
        }catch(DataAccessException ex){
            System.out.println(ex);
            Assertions.fail();
        }

        Assertions.assertEquals(4, allGames.length,
                "Did not return the proper number of games");
    }

    @Test
    @DisplayName("List Games With No Games Stored")
    public void noGamesListGames(){
        ServerGame newGame = new ServerGame();
        ServerGame[] allGames = new ServerGame[0];
        try{
            new GameDAO().deleteAll();
            allGames = new GameDAO().findAll();
        }catch(DataAccessException ex){
            System.out.println(ex);
            Assertions.fail();
        }

        Assertions.assertEquals(0, allGames.length,
                "Did not return the proper number of games");
    }

    @Test
    @DisplayName("Normal Clear All Games")
    public void clearAllGames(){
        ServerGame retrievedGame = null;
        try{
            new GameDAO().deleteAll();
            retrievedGame = new GameDAO().find(existingGameID);
        }catch(DataAccessException ex){
            System.out.println(ex);
            Assertions.fail();
        }

        Assertions.assertNull(retrievedGame,
                "Found a game when it should have been deleted.");
    }
}


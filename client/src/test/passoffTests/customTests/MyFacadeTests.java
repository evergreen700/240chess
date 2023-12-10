package passoffTests.customTests;

import org.junit.jupiter.api.*;
import serverModels.*;
import reqRes.baseRequest;
import reqRes.baseResponse;
import reqRes.gameResponse;
import serverFacade.*;
@SuppressWarnings("unused")

public class MyFacadeTests {

    private static ServerUser existingUser;
    private static ServerUser newUser;
    private String nephiToken;
    private String badToken = "aaaaaaaaaaaaaaaaaaaaaaaaaaa";
    private static ServerFacade chessServer;

    @BeforeAll
    public static void init(){
        existingUser = new ServerUser(
                "Nephi",
                "iwillgoanddo",
                "nephi@promisedland.net");

        newUser = new ServerUser(
                "Laman",
                "wannagohome",
                "laman@promisedland.net");

        chessServer = new ServerFacade("localhost", "8080");
    }

    @BeforeEach
    public void setup(){
        chessServer.clearAll();

        nephiToken = chessServer.register(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());
    }
    @Test
    @DisplayName("Normal User Registration")
    public void successRegister(){
        String lamanToken = chessServer.register(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());

        Assertions.assertNotNull(lamanToken,
                "Response did not return authentication String");
    }


    @Test
    @DisplayName("Duplicate User Registration")
    public void failureRegister(){

        String secondNephiToken = chessServer.register(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());

        Assertions.assertNull(secondNephiToken,
                "Response contained an authToken String when it should not have");
    };

    @Test
    @DisplayName("Normal User Login")
    public void successLogin(){
        String nephiLoginToken = chessServer.login(existingUser.getUsername(), existingUser.getPassword());

        Assertions.assertNotNull(nephiLoginToken,
                "Response did not return authentication String");
    }


    @Test
    @DisplayName("Login Wrong Password")
    public void wrongPasswordLogin(){
        String nephiLoginToken = chessServer.login(existingUser.getUsername(), "joinWithUs");

        Assertions.assertNull(nephiLoginToken,
                "Response contained an authToken when it shouldn't have");
    }


    @Test
    @DisplayName("Normal Logout")
    public void successLogout(){
        boolean logoutStatus = chessServer.logout(nephiToken);

        Assertions.assertTrue(logoutStatus,
                "Logout not successful");
    }

    @Test
    @DisplayName("Logout No AuthToken")
    public void badAuthTokenLogout(){
        boolean logoutStatus = chessServer.logout(badToken);

        Assertions.assertFalse(logoutStatus,
                "Logout gave successful response when it shouldn't have");
    }


    @Test
    @DisplayName("Normal Create Game")
    public void successCreateGame(){

        boolean gameStatus = chessServer.createGame(nephiToken, "TestName");
        baseResponse.gameInfo[] gameList = chessServer.listGames(nephiToken);

        Assertions.assertTrue(gameStatus, "Creation did not give a success response");
        Assertions.assertEquals("TestName", gameList[0].gameName,
                "GameName not properly stored or retrieved");
    }

    @Test
    @DisplayName("Create Game Bad AuthToken")
    public void badAuthTokenCreateGame(){
        boolean gameStatus = chessServer.createGame(badToken, "TestName");

        Assertions.assertFalse(gameStatus, "Creation gave success response when it shouldn't have");
    }


    @Test
    @DisplayName("Normal Join Game")
    public void successJoinGame(){
        chessServer.createGame(nephiToken, "TestName");
        baseResponse.gameInfo[] gameList = chessServer.listGames(nephiToken);
        boolean joinStatus = chessServer.joinGame(nephiToken, gameList[0].gameID, "BLACK");
        gameList = chessServer.listGames(nephiToken);

        Assertions.assertTrue(joinStatus, "Joining game did not give success response");
        Assertions.assertEquals(existingUser.getUsername(), gameList[0].blackUsername, "Username not properly recorded or retrieved");
    }

    @Test
    @DisplayName("Join Game Bad GameID")
    public void badGameIDJoin(){
        chessServer.createGame(nephiToken, "TestName");
        baseResponse.gameInfo[] gameList = chessServer.listGames(nephiToken);
        boolean joinStatus = chessServer.joinGame(nephiToken, 0, "BLACK");

        Assertions.assertFalse(joinStatus, "Joining game success response when it should not have");
    }


    @Test
    @DisplayName("Normal List Games")
    public void successListGames(){
        chessServer.createGame(nephiToken, "TestName");
        chessServer.createGame(nephiToken, "TestName2");
        chessServer.createGame(nephiToken, "TestName3");

        baseResponse.gameInfo[] listGamesResponse = chessServer.listGames(nephiToken);

        Assertions.assertNotNull(listGamesResponse,
                "Response did not contain a game list");
        Assertions.assertEquals(3, listGamesResponse.length,
                "Response contained the wrong number of games");
    }

    @Test
    @DisplayName("List Games No AuthToken")
    public void noAuthTokenListGames(){
        chessServer.createGame(nephiToken, "TestName");
        chessServer.createGame(nephiToken, "TestName2");
        chessServer.createGame(nephiToken, "TestName3");

        baseResponse.gameInfo[] listGamesResponse = chessServer.listGames(badToken);

        Assertions.assertNull(listGamesResponse,
                "Returned a non-null value when it should not have");
    }



    @Test
    @DisplayName("Clear All Data")
    public void clearAllData(){
        chessServer.clearAll();

        String nephiLoginToken = chessServer.login(existingUser.getUsername(), existingUser.getPassword());

        Assertions.assertNull(nephiLoginToken,
                "Response contained an authToken when it shouldn't have");
    }

    @Test
    @DisplayName("Clear All Data, bad connection")
    public void clearAllDataBadConnection(){
        boolean clearResult = new ServerFacade("localhost", "9090").clearAll();

        Assertions.assertFalse(clearResult,
                "Gave success response when it should not have");
    }
}


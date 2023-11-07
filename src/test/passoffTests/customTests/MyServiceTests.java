package passoffTests.customTests;

import org.junit.jupiter.api.*;
import passoffTests.testClasses.TestModels;
import reqRes.baseRequest;
import reqRes.baseResponse;
import reqRes.gameResponse;
import services.ClearApplicationService;
import services.GameService;
import services.UserAuthService;

@SuppressWarnings("unused")

public class MyServiceTests {

    private static TestModels.TestUser existingUser;
    private static TestModels.TestUser newUser;
    private String nephiToken;
    private String badToken = "aaaaaaaaaaaaaaaaaaaaaaaaaaa";

    @BeforeAll
    public static void init(){
        existingUser = new TestModels.TestUser();
        existingUser.username="Nephi";
        existingUser.password="iwillgoanddo";
        existingUser.email="nephi@promisedland.net";

        newUser = new TestModels.TestUser();
        newUser.username = "Laman";
        newUser.password = "wannagohome";
        newUser.email = "laman@promisedland.net";

    }

    @BeforeEach
    public void setup(){
        new ClearApplicationService().clearAll(new baseRequest());

        baseRequest createNephi = new baseRequest();
        createNephi.username = existingUser.username;
        createNephi.password = existingUser.password;
        createNephi.email = existingUser.password;

        nephiToken = new UserAuthService().registerUser(createNephi).authToken;
    }
    @Test
    @DisplayName("Normal User Registration")
    public void successRegister(){
        baseRequest createLaman = new baseRequest();
        createLaman.username = newUser.username;
        createLaman.password = newUser.password;
        createLaman.email = newUser.password;

        baseResponse lamanResponse = new UserAuthService().registerUser(createLaman);

        Assertions.assertEquals(newUser.username, lamanResponse.username,
                "Response did not give the same username as user");
        Assertions.assertNotNull(lamanResponse.authToken,
                "Response did not return authentication String");
        Assertions.assertNull(lamanResponse.message,
                "Response included an error message: "+lamanResponse.message);
    }


    @Test
    @DisplayName("Duplicate User Registration")
    public void failureRegister(){
        baseRequest createSecondNephi = new baseRequest();
        createSecondNephi.username = existingUser.username;
        createSecondNephi.password = existingUser.password;
        createSecondNephi.email = existingUser.password;

        baseResponse secondNephiResponse = new UserAuthService().registerUser(createSecondNephi);

        Assertions.assertNull(secondNephiResponse.username,
                "Response contained a username");
        Assertions.assertNull(secondNephiResponse.authToken,
                "Response contained an authToken");
        Assertions.assertEquals("Error: already taken", secondNephiResponse.message,
                "Response did not have the right error message");
    };

    @Test
    @DisplayName("Normal User Login")
    public void successLogin(){
        baseRequest loginNephi = new baseRequest();
        loginNephi.username = existingUser.username;
        loginNephi.password = existingUser.password;

        baseResponse nephiResponse = new UserAuthService().loginUser(loginNephi);

        Assertions.assertEquals(existingUser.username, nephiResponse.username,
                "Response did not give the same username as user");
        Assertions.assertNotNull(nephiResponse.authToken,
                "Response did not return authentication String");
        Assertions.assertNull(nephiResponse.message,
                "Response included an error message: "+nephiResponse.message);
    }


    @Test
    @DisplayName("Login Wrong Password")
    public void wrongPasswordLogin(){
        baseRequest loginNephi = new baseRequest();
        loginNephi.username = existingUser.username;
        loginNephi.password = "large1nstatur3";

        baseResponse nephiResponse = new UserAuthService().loginUser(loginNephi);

        Assertions.assertNull(nephiResponse.username,
                "Response contained a username when it shouldn't have");
        Assertions.assertNull(nephiResponse.authToken,
                "Response contained an authToken when it shouldn't have");
        Assertions.assertEquals("Error: unauthorized", nephiResponse.message,
                "Response did not contain expected error message");
    }


    @Test
    @DisplayName("Normal Logout")
    public void successLogout(){
        baseRequest logoutNephi = new baseRequest();
        logoutNephi.authToken = nephiToken;

        baseResponse logoutResponse = new UserAuthService().logoutUser(logoutNephi);

        Assertions.assertNull(logoutResponse.message,
                "Response contained an error message when it shouldn't have");
    }

    @Test
    @DisplayName("Logout No AuthToken")
    public void badAuthTokenLogout(){
        baseRequest logoutFail = new baseRequest();

        baseResponse logoutResponse = new UserAuthService().logoutUser(logoutFail);

        Assertions.assertEquals("Error: unauthorized", logoutResponse.message,
                "Response did not contain expected error message");
    }


    @Test
    @DisplayName("Normal Create Game")
    public void successCreateGame(){
        baseRequest newGameRequest = new baseRequest();
        newGameRequest.authToken = nephiToken;
        newGameRequest.gameName = "testName";

        gameResponse newGameResponse = (gameResponse) new GameService().createGame(newGameRequest);

        Assertions.assertNotNull(newGameResponse.gameID, "Response did not contain a gameID");
        Assertions.assertTrue(newGameResponse.gameID > 0, "Response did not contain a valid gameID");
        Assertions.assertNull(newGameResponse.message,
                "Response contained an error message when it shouldn't have");
    }

    @Test
    @DisplayName("Create Game Bad AuthToken")
    public void badAuthTokenCreateGame(){
        baseRequest newGameRequest = new baseRequest();
        newGameRequest.authToken = badToken;
        newGameRequest.gameName = "test1";

        baseResponse newGameResponse = new GameService().createGame(newGameRequest);

        Assertions.assertEquals(baseResponse.class, newGameResponse.getClass(), "Response was of the wrong type and likely contained a gameID");
        Assertions.assertEquals("Error: unauthorized", newGameResponse.message,
                "Response did not contain expected error message");
    }


    @Test
    @DisplayName("Normal Join Game")
    public void successJoinGame(){
        baseRequest newGameRequest = new baseRequest();
        newGameRequest.authToken = nephiToken;
        newGameRequest.gameName = "testName";
        gameResponse newGameResponse = (gameResponse) new GameService().createGame(newGameRequest);

        baseRequest joinGameRequest = new baseRequest();
        joinGameRequest.gameID = newGameResponse.gameID;
        joinGameRequest.playerColor = "BLACK";
        joinGameRequest.authToken = nephiToken;

        baseResponse joinGameResponse = new GameService().joinGame(joinGameRequest);

        Assertions.assertNull(newGameResponse.message,
                "Response contained an error message when it shouldn't have");
    }

    @Test
    @DisplayName("Join Game Bad GameID")
    public void badGameIDJoin(){
        baseRequest newGameRequest = new baseRequest();
        newGameRequest.authToken = nephiToken;
        newGameRequest.gameName = "testName";
        gameResponse newGameResponse = (gameResponse) new GameService().createGame(newGameRequest);

        baseRequest joinGameRequest = new baseRequest();
        joinGameRequest.gameID = 100;
        joinGameRequest.playerColor = "BLACK";
        joinGameRequest.authToken = nephiToken;

        baseResponse joinGameResponse = new GameService().joinGame(joinGameRequest);

        Assertions.assertEquals("Error: bad request", joinGameResponse.message,
                "Response didn't contain the expected error message");
    }

    @Test
    @DisplayName("Normal List Games")
    public void successListGames(){
        baseRequest newGameRequest = new baseRequest();
        newGameRequest.authToken = nephiToken;
        newGameRequest.gameName = "testName";

        new GameService().createGame(newGameRequest);
        new GameService().createGame(newGameRequest);
        new GameService().createGame(newGameRequest);

        baseResponse listGamesResponse = new GameService().listGames(newGameRequest);

        Assertions.assertEquals(3, listGamesResponse.games.length,
                "Response contained the wrong number of games");
        Assertions.assertNull(listGamesResponse.message,
                "Response contained an error message when it shouldn't have");
    }

    @Test
    @DisplayName("List Games No AuthToken")
    public void noAuthTokenListGames(){
        baseRequest newGameRequest = new baseRequest();
        newGameRequest.authToken = nephiToken;
        newGameRequest.gameName = "testName";

        new GameService().createGame(newGameRequest);
        new GameService().createGame(newGameRequest);
        new GameService().createGame(newGameRequest);

        baseRequest emptyRequest = new baseRequest();

        baseResponse listGamesResponse = new GameService().listGames(emptyRequest);

        Assertions.assertNull(listGamesResponse.games,
                "Response contained games when it shouldn't have");
        Assertions.assertEquals("Error: unauthorized", listGamesResponse.message,
                "Response contained the wrong error message");
    }



    @Test
    @DisplayName("Clear All Data")
    public void clearAllData(){
        baseRequest clearGameRequest = new baseRequest();
        baseResponse clearGameResponse = new ClearApplicationService().clearAll(clearGameRequest);

        Assertions.assertNull(clearGameResponse.message,
                "Response contained an error message when it shouldn't have");
    }
}


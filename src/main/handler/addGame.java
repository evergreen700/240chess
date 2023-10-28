package handler;
import com.google.gson.Gson;
import reqRes.*;
import server.ChessServer;
import services.GameService;
import spark.Request;
import spark.Response;

/**
 * Handler object used to add games to the database
 */
public class addGame {
    /**
     * Handler function used to add a game to the database
     * @param req JavaSpark request object
     * @param res JavaSpark response object
     * @return Body JSON string of response object
     */
    public String addGame(Request req, Response res){
        baseRequest myRequest = new Gson().fromJson(req.body(), baseRequest.class);
        myRequest.authToken = req.headers("Authorization");
        baseResponse myResponse = new GameService().createGame(myRequest);
        res.body(new Gson().toJson(myResponse));
        res.status(ChessServer.getStatusCode(myResponse.message));
        return res.body();
    }
}

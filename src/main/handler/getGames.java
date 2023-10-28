package handler;

import com.google.gson.Gson;
import reqRes.baseRequest;
import reqRes.baseResponse;
import server.ChessServer;
import services.GameService;
import spark.Request;
import spark.Response;

/**
 * Handler object used to retrieve the list of games from the database
 */
public class getGames {
    /**
     * Handler function used to get the full list of games in the database
     * @param req JavaSpark request object
     * @param res JavaSpark response object
     * @return Body JSON string of response object
     */
    public String getGames(Request req, Response res){
        baseRequest myRequest = new baseRequest();
        myRequest.authToken = req.headers("Authorization");
        baseResponse myResponse = new GameService().listGames(myRequest);
        res.body(new Gson().toJson(myResponse));
        res.status(ChessServer.getStatusCode(myResponse.message));
        return res.body();
    }
}

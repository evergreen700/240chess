package handler;

import com.google.gson.Gson;
import reqRes.baseRequest;
import reqRes.baseResponse;
import server.ChessServer;
import services.GameService;
import spark.Request;
import spark.Response;

/**
 * Handler object used to join games already in the database
 */
public class joinGame {
    /**
     * Handler function used to join a game already in the database
     * @param req JavaSpark request object
     * @param res JavaSpark response object
     * @return Body JSON string of response object
     */
    public String joinGame(Request req, Response res){
        baseRequest myRequest = new Gson().fromJson(req.body(), baseRequest.class);
        myRequest.authToken = req.headers("Authorization");
        baseResponse myResponse = new GameService().joinGame(myRequest);
        res.body(new Gson().toJson(myResponse));
        res.status(ChessServer.getStatusCode(myResponse.message));
        return res.body();
    }
}

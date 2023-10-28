package handler;
import com.google.gson.Gson;
import reqRes.*;
import services.UserAuthService;
import spark.Request;
import spark.Response;
import server.ChessServer;

/**
 * Handler object used to add users to the database
 */
public class addUser {
    /**
     * Handler function used to add a user to the database
     * @param req JavaSpark request object
     * @param res JavaSpark response object
     * @return Body JSON string of response object
     */
    public String addUser(Request req, Response res){
        baseRequest myRequest = new Gson().fromJson(req.body(), baseRequest.class);
        baseResponse myResponse = new UserAuthService().registerUser(myRequest);
        res.body(new Gson().toJson(myResponse));
        res.status(ChessServer.getStatusCode(myResponse.message));
        return res.body();
    }
}

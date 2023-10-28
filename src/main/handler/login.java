package handler;

import com.google.gson.Gson;
import reqRes.baseRequest;
import reqRes.baseResponse;
import server.ChessServer;
import services.UserAuthService;
import spark.Request;
import spark.Response;

/**
 * Handler object used to authenticate login attempts and stores an authToken in the database
 */
public class login {
    /**
     * Handler function used to authenticate a user and store an authentication token in the database
     * @param req JavaSpark request object
     * @param res JavaSpark response object
     * @return Body JSON string of response object
     */
    public String login(Request req, Response res){
        baseRequest myRequest = new Gson().fromJson(req.body(), baseRequest.class);
        baseResponse myResponse = new UserAuthService().loginUser(myRequest);
        res.body(new Gson().toJson(myResponse));
        res.status(ChessServer.getStatusCode(myResponse.message));
        System.out.println(res.body());
        return res.body();
    }
}

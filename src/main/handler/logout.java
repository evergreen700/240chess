package handler;

import com.google.gson.Gson;
import reqRes.baseRequest;
import reqRes.baseResponse;
import server.ChessServer;
import services.UserAuthService;
import spark.Request;
import spark.Response;

/**
 * Handler object used to authenticate logout attempts and removes authTokens from the database
 */
public class logout {
    /**
     * Handler function used to log out a user and remove the corresponding authToken from the database
     * @param req JavaSpark request object
     * @param res JavaSpark response object
     * @return Body JSON string of response object
     */
    public String logout(Request req, Response res){
        baseRequest myRequest = new baseRequest();
        myRequest.authToken = req.headers("Authorization");
        baseResponse myResponse = new UserAuthService().logoutUser(myRequest);
        res.body(new Gson().toJson(myResponse));
        res.status(ChessServer.getStatusCode(myResponse.message));
        System.out.println(res.body());
        return res.body();
    }
}

package handler;
import reqRes.*;
import services.ClearApplicationService;
import spark.Request;
import spark.Response;

/**
 * Handler object used to clear the database
 */
public class clearDB {
    /**
     * Handler function used to clear the database
     * @param req JavaSpark request object
     * @param res JavaSpark response object
     * @return Body JSON string of response object
     */
    public String clearDB(Request req, Response res){
        new ClearApplicationService().clearAll(new baseRequest());
        res.status(200);
        return "{}";
    }
}

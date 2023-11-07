package services;

import dataAccess.*;
import reqRes.*;

/**
 * Contains the clearAll service
 */

public class ClearApplicationService {
    /**
     * Clears all records from the database
     * @param req Request object. No fields needed
     * @return Response object.
     */
    public baseResponse clearAll(baseRequest req){
        try{
            new AuthTokenDAO().deleteAll();
            new GameDAO().deleteAll();
            new UserDAO().deleteAll();
            return new baseResponse();
        }catch(DataAccessException e){
            baseResponse errorResponse = new baseResponse();
            errorResponse.message = "Error: description";
            return errorResponse;
        }
    }
}

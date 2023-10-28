package services;
import reqRes.*;
import dataAccess.*;
import serverModels.*;

import java.util.UUID;

/**
 * Contains the services for
 */
public class UserAuthService {
    /**
     * Creates a new user record and provides an authentication token.
     * @param req Request object. Must contain unique username, password, and unique email.
     * @return Response object. Contains username and authToken string.
     */
    public baseResponse registerUser(baseRequest req){
        baseResponse res = new baseResponse();
        if (req.username == null || req.password == null || req.email == null){
            res.message = "Error: bad request";
            return res;
        }else{
            try{
                new UserDAO().insert(new ServerUser(req.username, req.password, req.email));
                ServerAuthToken userToken = new ServerAuthToken(req.username, UUID.randomUUID().toString());
                while(new AuthTokenDAO().find(userToken.getAuthToken())!=null){
                    userToken = new ServerAuthToken(req.username, UUID.randomUUID().toString());
                }
                try{
                    new AuthTokenDAO().insert(userToken);
                    res.username = userToken.getUsername();
                    res.authToken = userToken.getAuthToken();
                    return res;
                }catch(DataAccessException exception){
                    res.message = "Error: description";
                    return res;
                }
            }catch(DataAccessException exception){
                res.message = "Error: already taken";
                return res;
            }
        }
    }

    /**
     * Checks login information and provides an authentication token.
     * @param req Request object. Must contain a username and password.
     * @return Response object. Contains username and authToken string.
     */
    public baseResponse loginUser(baseRequest req){
        baseResponse res = new baseResponse();
        if (req.username == null || req.password == null){
            res.message = "Error: bad request";
            return res;
        }else{
            ServerUser userInfo = null;
            try{
                userInfo = new UserDAO().find(req.username);
            }catch(DataAccessException e){
                res.message = "Error: Description";
            }
            if (userInfo == null) {
                res.message = "Error: unauthorized";
                return res;
            }else{
                if(userInfo.getPassword().equals(req.password)){
                    ServerAuthToken userToken = new ServerAuthToken(req.username, UUID.randomUUID().toString());
                    try{
                        new AuthTokenDAO().insert(userToken);
                        res.username = userToken.getUsername();
                        res.authToken = userToken.getAuthToken();
                        return res;
                    }catch(DataAccessException exception){
                        res.message = "Error: description";
                        return res;
                    }
                }else{
                    res.message = "Error: unauthorized";
                    return res;
                }
            }
        }
    }

    /**
     * Logs a user out by deleting a corresponding authToken
     * @param req Request object. Contains a valid authToken.
     * @return Response object.
     */
    public baseResponse logoutUser(baseRequest req){
        baseResponse res = new baseResponse();
        try{
            new AuthTokenDAO().delete(req.authToken);
            return res;
        }catch (DataAccessException exception) {
            res.message = "Error: unauthorized";
            return res;
        }
    }
}

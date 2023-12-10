package serverFacade;

import com.google.gson.Gson;
import reqRes.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerFacade {

    private String serverAddress;
    private String serverPort;

    public ServerFacade(String serverAddress, String serverPort){
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    //Doesn't require authToken
    public String login(String username, String password){
        baseRequest req = new baseRequest();
        req.username = username;
        req.password = password;
        try{
            gameResponse res = sendRequest(req, "session", "POST");
            if(res==null){
                return null;
            }
            return res.authToken;
        }catch(IOException e){
            System.out.println("http exception: "+ e.getMessage());
            return null;
        }
    }

    public String register(String username, String password, String email){
        baseRequest req = new baseRequest();
        req.username = username;
        req.password = password;
        req.email = password;
        try{
            gameResponse res = sendRequest(req,"user","POST");
            if(res==null){
                return null;
            }
            return res.authToken;
        }catch(IOException e){
            System.out.println("http exception: "+ e.getMessage());
            return null;
        }
    }

    //Requires authToken
    public boolean logout(String authToken){
        baseRequest req = new baseRequest();
        req.authToken = authToken;
        try{
            gameResponse res = sendRequest(req, "session", "DELETE");
            if(res==null){
                return false;
            }
            return true;
        }catch(IOException e){
            System.out.println("http exception: "+ e.getMessage());
            return false;
        }
    }

    public boolean createGame(String authToken, String gameName){
        baseRequest req = new baseRequest();
        req.authToken = authToken;
        req.gameName = gameName;
        try{
            gameResponse res = sendRequest(req, "game", "POST");
            if(res==null){
                return false;
            }
            return true;
        }catch(IOException e){
            System.out.println("http exception: "+ e.getMessage());
            return false;
        }
    }

    public baseResponse.gameInfo[] listGames(String authToken){
        baseRequest req = new baseRequest();
        req.authToken = authToken;
        req.gameName="Better not";
        try{
            gameResponse res = sendRequest(req, "game", "GET");
            if(res==null){
                return null;
            }
            return res.games;
        }catch(IOException e){
            System.out.println("http exception: "+ e.getMessage());
            return null;
        }
    }

    public boolean joinGame(String authToken, int gameID, String playerColor){
        baseRequest req = new baseRequest();
        req.authToken = authToken;
        req.gameID = gameID;
        req.playerColor = playerColor;
        try{
            gameResponse res = sendRequest(req, "game", "PUT");
            if(res==null){
                return false;
            }
            return true;
        }catch(IOException e){
            System.out.println("http exception: "+ e.getMessage());
            return false;
        }
    }

    public boolean clearAll(){
        baseRequest req = new baseRequest();
        try{
            gameResponse res = sendRequest(req, "db", "DELETE");
            if(res==null){
                return false;
            }
            return true;
        }catch(IOException e){
            System.out.println("http exception: "+ e.getMessage());
            return false;
        }
    }

    private gameResponse sendRequest(baseRequest req, String resource, String verb) throws IOException{
        HttpURLConnection http;
        http = (HttpURLConnection) (new URL("http://"+serverAddress+":"+serverPort+"/"+resource)).openConnection();
        http.setRequestMethod(verb);
        http.setDoOutput(true);
        http.addRequestProperty("Accept", "application/json");
        http.addRequestProperty("Authorization", req.authToken);
        http.connect();
        req.authToken = null;
        String body = new Gson().toJson(req);
        if (verb != "GET") {
            OutputStream httpStream = http.getOutputStream();
            OutputStreamWriter httpStreamWriter = new OutputStreamWriter(httpStream);
            httpStreamWriter.write(body);
            httpStreamWriter.flush();
            httpStream.close();
        }
        int statusCode = http.getResponseCode();
        InputStream httpInputStream;
        gameResponse res;
        if (statusCode == 200){
            httpInputStream = http.getInputStream();
            InputStreamReader inputStreamHTTP = new InputStreamReader(httpInputStream);
            res = gameResponse.serializer().fromJson(inputStreamHTTP,gameResponse.class);
            return res;
        } else{
            return null;
        }
    }

}

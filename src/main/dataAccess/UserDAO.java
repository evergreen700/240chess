package dataAccess;
import serverModels.*;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Data Access Object used to manipulate and access user records within the database.
 */
public class UserDAO {
    /*private static HashMap<String, ServerUser> userStorage = new HashMap<>();
    private static HashSet<String> emailList = new HashSet<>();
*/
    private ServerUser getQuery(String selectStatement, Database db) throws DataAccessException{
      var conn = db.getConnection();
      try (var preparedStatement = conn.prepareStatement(selectStatement);
        var results = preparedStatement.executeQuery()) {
          if (results.next()){
              String uname = results.getString(1);
              String upword = results.getString(2);
              String uemail = results.getString(3);
              return new ServerUser(uname, upword, uemail);
          }else{
              return null;
          }
      } catch (java.sql.SQLException ex) {
          throw new DataAccessException(ex.toString());
      } finally {
          db.returnConnection(conn);
      }
    }

    private boolean putObject(String insertStatement, Database db) throws DataAccessException{
      var conn = db.getConnection();
      try (var preparedStatement = conn.prepareStatement(insertStatement)) {
          return preparedStatement.execute();
      } catch (java.sql.SQLException ex) {
          throw new DataAccessException(ex.toString());
      } finally {
          db.returnConnection(conn);
      }
    }
    /**
     * Adds new user to the database.
     * @param newUser The ServerUser object to store in the database
     * @throws DataAccessException The username or email are already in use on a different account.
     */
    public void insert(ServerUser newUser) throws DataAccessException {
        ServerUser existingUser = find(newUser.getUsername());
        if(existingUser!=null){
            throw new DataAccessException("Username already in use");
        }/*else if(emailList.contains(newUser.getEmail())){
            throw new DataAccessException("Email already in use");
        }*/else {
            putObject("INSERT INTO chess.users(Username, Password, Email) VALUES('"+newUser.getUsername()+"','"+newUser.getPassword()+"','"+newUser.getEmail()+"');", new Database());
        }
    }

    /**
     * Finds the user with the corresponding username in the database. If the name is not currently in use returns null.
     * @param username The username associated with the user account
     * @return Returns a new ServerUser object
     */
    public ServerUser find(String username) throws DataAccessException{
        var conn = new Database().getConnection();
        try (var preparedStatement = conn.prepareStatement("SELECT * FROM chess.users WHERE Username = ?;")){
            preparedStatement.setString(1,username);
             var results = preparedStatement.executeQuery();
            if (results.next()){
                String upword = results.getString(2);
                String uemail = results.getString(3);
                new Database().returnConnection(conn);
                return new ServerUser(username, upword, uemail);
            }else{
                new Database().returnConnection(conn);
                return null;
            }
        } catch (java.sql.SQLException ex) {
            new Database().returnConnection(conn);
            throw new DataAccessException(ex.toString());
        } finally {
            new Database().returnConnection(conn);
        }
    }

    /**
     * Removes all user entries from the database
     */
    public void deleteAll() throws DataAccessException{
        putObject("DELETE FROM chess.users", new Database());
    }
}

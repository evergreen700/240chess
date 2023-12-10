package websocket;

public class ResponseException extends Exception{

    public ResponseException() {}

    public ResponseException(String message) {
        super(message);
    }
}

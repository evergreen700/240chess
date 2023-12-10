package webSocketMessages.serverMessages;

public class NotifyMessage extends ServerMessage{
    String message;
    public NotifyMessage(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }
}

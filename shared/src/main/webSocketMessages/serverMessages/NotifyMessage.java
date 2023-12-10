package webSocketMessages.serverMessages;

public class NotifyMessage extends ServerMessage{
    public String message;
    public NotifyMessage(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }
}

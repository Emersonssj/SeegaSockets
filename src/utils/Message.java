package utils;

import java.io.Serializable;

public class Message implements Serializable {
    public enum Type {
        INIT, MOVE, CHAT, FORFEIT, WINNER
    }

    public Type type;
    public Object content;

    public Message(Type type, Object content) {
        this.type = type;
        this.content = content;
    }
}

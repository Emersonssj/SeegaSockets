package client;

import utils.Message;
import javax.swing.*;
import java.awt.*;

public class ChatUI extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private SeegaClient client;

    public ChatUI(SeegaClient client) {
        this.client = client;

        setTitle("Chat Seega");
        setSize(400, 300);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        inputField = new JTextField();
        add(inputField, BorderLayout.SOUTH);

        inputField.addActionListener(e -> sendChat());

        setVisible(true);
    }

    private void sendChat() {
        String text = inputField.getText();
        if (!text.isEmpty()) {
            client.sendMessage(new Message(Message.Type.CHAT, text));
            inputField.setText("");
        }
    }

    public void receiveMessage(String msg) {
        chatArea.append(msg + "\n");
    }
}

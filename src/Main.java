import client.SeegaClient;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        String serverIP = JOptionPane.showInputDialog("Digite o IP do servidor:");
        new SeegaClient(serverIP);
    }
}
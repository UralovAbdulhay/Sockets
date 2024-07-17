import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Client extends Application implements TCPConnectionListener {

    private static final String IP_ADD = "35.158.159.254";
    private static final int PORT = 15086;

    private TCPConnection connection;

    public static void main(String[] args) {

        launch(args);

    }

    private final TextField name = new TextField("Abdulhay");
    private final TextArea textArea = new TextArea();
    private final TextField mesgField = new TextField();
    private final Button send = new Button("send");


    @Override
    public void start(Stage stage) {
        System.out.println("start1");

        textArea.setEditable(false);
        textArea.setWrapText(true);
        send.setDefaultButton(true);
        send.setOnAction(this::send);

        System.out.println("start2");

        HBox hBox = new HBox(10D, mesgField, send);
        hBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(mesgField, Priority.ALWAYS);

        System.out.println("start3");

        VBox vBox = new VBox(10D, name, textArea, hBox);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        VBox.setVgrow(textArea, Priority.ALWAYS);
        stage.setScene(new Scene(vBox));
        stage.setOnCloseRequest(event -> {
            System.exit(0);
        });
        stage.show();

        System.out.println("start4");

        try {
            connection = new TCPConnection(this, IP_ADD, PORT);
        } catch (IOException e) {
            printMessage("Connection exception: " + e);
        }

        System.out.println("start5");

    }


    private void send(ActionEvent event) {
        String msg = mesgField.getText().trim();
        if (msg.isEmpty()){
            mesgField.clear();
            return;
        }
        mesgField.clear();
        connection.sendString(name.getText().trim() + ": " + msg);
    }


    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMessage("Connection ready.... ");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        printMessage(value);

    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMessage("Connection close.");

    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        printMessage("Connection exception: " + e);
    }


    private synchronized void printMessage(String msg) {


        textArea.appendText(  msg + "\n" );
//        textArea.setScrollTop(0);

    }


}

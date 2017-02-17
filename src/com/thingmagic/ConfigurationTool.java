
package com.thingmagic;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ConfigurationTool extends Application 
{
    
    private static MainController _instance;
    
    public static MainController Instance() {
        System.out.println(ConfigurationTool._instance);
        return ConfigurationTool._instance;
    }
    
    @Override
    public void start(Stage stage) throws Exception 
    {
        Parent root = FXMLLoader.load(this.getClass().getResource("/fxml/Main.fxml"));        
        Scene scene = new Scene(root);
        stage.setTitle("PARAM: Pixel Academy RFID Administration and Manager");
        stage.getIcons().add(new Image(("/images/thingmagic-icon.png")));         
        MainController main = new MainController();
        
        stage.setScene(scene);
        main.setStage(stage);
        stage.setMaximized(true);
        stage.show();
        
        

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                  System.exit(0);
            }
        });
        
        ConfigurationTool._instance = main;
    }

    public static void main(String[] args)
    {        
        launch(args);
    }
}
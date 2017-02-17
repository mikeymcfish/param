/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thingmagic;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javax.swing.JOptionPane;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author Pixel Member
 */
public class MemberResult {
    private String memberName;
    private int memberID;
    private String memberAssign;
    private String memberEPC;
    
    public MemberResult(String n, int i, String epc){
        this.memberName = n;
        this.memberID = i;
        this.memberAssign = MainController.API_ROOT + MainController.API_ASSIGN_URL + this.memberID;
        this.memberEPC = epc;
    }
    
    
    public String getMemberEPC(){ return this.memberEPC; }
    public String getMemberName(){ return this.memberName; }
    public int getMemberID() { return this.memberID; }
    public Button getMemberAssign() { 
        String url = this.memberAssign; 
        String name = this.memberName;
        int id = this.memberID;
        String epc = this.memberEPC;
        
        Button b = new Button();
        //System.out.println(epc.length());
        if (epc.length() > 0){
            b.setText("Clear " + name + "'s EPC");
        }
        else {
            b.setText("Assign " + name + "'s Bracelet");
        }
        
                b.setOnAction(new EventHandler<ActionEvent>() 
                {
                    @Override
                    public void handle(ActionEvent event) {
                        Platform.runLater(new Runnable(){
                            public void run(){
                                if (epc.length() > 0){
                                    //System.out.println(epc.length());
                                    //System.out.println("Already has an EPC");
                                    int option = JOptionPane.showConfirmDialog(null, "Are you sure you wish to clear this members EPC? This cannot be undone.", "Confirmation", JOptionPane.YES_NO_OPTION);
                                    if (option == JOptionPane.NO_OPTION)
                                    {
                                       return;
                                    }
                                    String url = MainController.API_ROOT + MainController.API_CLEAR_EPC_URL + id;
                                    System.out.println("Sending clear request to URL: " + url);
                                    showMessage("success", "Clearing");

                                    //if (true) return;

                                    try {
                                        HttpClient httpclient = HttpClients.createDefault();
                                        HttpPost httppost = new HttpPost(url);
                                        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
                                        //params.add(new BasicNameValuePair("epc", epc));
                                        params.add(new BasicNameValuePair("secret_ley", MainController.API_SECRET_KEY));
                                        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

                                        HttpResponse response = httpclient.execute(httppost);
                                        HttpEntity entity = response.getEntity();

                                        if (entity != null) {
                                            InputStream instream = entity.getContent();
                                            try {
                                                String stream_content = MainController.getStringFromInputStream(instream);
                                                //System.out.println(stream_content);
                                                //showWarningErrorMessage("error", stream_content);
                                                if (stream_content.length() < 1){

                                                }
                                                else {
                                                    System.out.println(stream_content);
                                                   showMessage("success", stream_content);
                                                }

                                            } finally {
                                                instream.close();
                                                MainController.resetAssignMode();
                                            }
                                        }
                                        else {
                                            //System.out.println("Entity was Null");
                                            showMessage("error", "Error reading API call. Data was malformed.");
                                        }
                                    } catch (UnsupportedEncodingException ex) {
                                        java.util.logging.Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (IOException ex) {
                                        java.util.logging.Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (UnsupportedOperationException ex) {
                                        java.util.logging.Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                else {
                                    if (MainController.assignMode){
                                        if (MainController.assignMemberID == id){
                                            System.out.println("Cancelling Assign Mode");
                                            MainController.resetAssignMode();
                                            showMessage("waring", "Assign Mode has been cancelled");
                                            b.setTextFill(Paint.valueOf("black"));
                                        }
                                        else {

                                            showMessage("warning", "You are already in assign mode for a different user. Cancel by clicking that users button again");
                                        }
                                    }
                                    else {
                                        System.out.println("Turning on Assign Mode for: " + name + " : " + id);

                                        MainController.setAssignMode(id);
                                       showMessage("success", "Assign Mode has been turned on for Member: " + name + ". Please select an EPC number to assign on the left, or click the assign button to cancel");

                                        b.setTextFill(Paint.valueOf("green"));
                                    }
                                }
                            }//end run
                        });//end run later
                    };//end handle
                });//end b event function
            
        
        return b;
    }
    
    public void showMessage(String type, String msg){
        Platform.runLater(new Runnable(){
            public void run(){
                MainController.doMessage(type, msg);
            }
        });
    }
}

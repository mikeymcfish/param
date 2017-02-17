package com.thingmagic;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class TagResults 
{
    private String deviceId;
    private String epc;
    private String time;
    private String rssi;
    private String count;
    private String antenna;
    private String protocol;
    private String frequency;
    private String phase;
    private String data;

    public TagResults(String deviceId, String epc,String time,String rssi,String count,String antenna,String protocol,String frequency,String phase)
    {
        this.deviceId=deviceId;
        this.epc=epc;
        this.time= time;
        this.rssi= rssi;
        this.count= count;
        this.antenna = antenna;                
        this.protocol = protocol;
        this.frequency = frequency;
        this.phase = phase;
    }
    
    public TagResults(String deviceId, String epc,String data,String time,String rssi,String count,String antenna,String protocol,String frequency,String phase)
    {
        this.deviceId=deviceId;
        this.epc=epc;
        this.data=data;
        this.time= time;
        this.rssi= rssi;
        this.count= count;
        this.antenna = antenna;
        this.protocol = protocol;
        this.frequency = frequency;
        this.phase = phase;
    }
    
    public String getDeviceId()
    {
        return deviceId;
    }
    public void setDeviceId(String deviceId)
    {
        this.deviceId=deviceId;
    }
    
    public String getEpc()
    {
        return epc;
    }
    public void setEpc(String epc)
    {
        this.epc=epc;
    }
    
    public Button getEpcButton() { 
        String epc = this.epc;
        
        Button b = new Button();
        
        b.setText(epc);
        b.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) {
                //System.out.println("Button Event for epc button: " + epc);
                if (MainController.assignMode){
                    if (MainController.assignMemberID <= 0){
                        //do nothing
                        System.out.println("Member ID not assigned. Doing nothing");
                        MainController.resetAssignMode();
                        return;
                    }
                    
                    MainController.assignEPC = epc;
                    String url = MainController.API_ROOT + MainController.API_ASSIGN_URL + MainController.assignMemberID;
                    System.out.println("Sending Assign request to URL: " + url + " with parameters: epc="+epc);
                    
                    
                    //if (true) return;
                    
                    try {
                        HttpClient httpclient = HttpClients.createDefault();
                        HttpPost httppost = new HttpPost(url);
                        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
                        params.add(new BasicNameValuePair("epc", epc));
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
                                    MainController.Instance().showWarningErrorMessage("success", stream_content);
                                }

                            } finally {
                                instream.close();
                                MainController.resetAssignMode();
                            }
                        }
                        else {
                            //System.out.println("Entity was Null");
                            MainController.Instance().showWarningErrorMessage("error", "Error reading API call. Data was malformed.");
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
                    //do nothing
                    System.out.println("Assign Mode not on");
                    return;
                }
               
            }
        });//end event function
        
        return b;
    }
    
    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }
    
    public String getTime()
    {
        return time;
    }
    public void setTime(String time)
    {
        this.time=time;
    }
    
    public String getRssi()
    {
        return rssi;
    }
    public void setRssi(String rssi)
    {
        this.rssi = rssi;
    }
    
    public String getCount()
    {
        return count;
    }
    public void setCount(String count)
    {
        this.count=count;
    }
    
    public String getAntenna()
    {
        return antenna;
    }                
    public void setAntenna(String antenna)
    {
        this.antenna=antenna;
    }
    
    public String getProtocol()
    {
        return protocol;
    }                
    public void setProtocol(String protocol)
    {
        this.protocol=protocol;
    }
    
    public String getFrequency()
    {
        return frequency;
    }                
    public void setFrequency(String frequency)
    {
        this.frequency=frequency;
    }
    
    public String getPhase()
    {
        return phase;
    }                
    public void setPhase(String phase)
    {
        this.phase=phase;
    }
    
}

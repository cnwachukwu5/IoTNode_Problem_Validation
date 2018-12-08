import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.io.*;
import java.net.Socket;

public class IoTNode {

    public static void main(String[] args) {
        try(Socket socketAtNode = new Socket("localhost", 56123);
            PrintWriter sendToServer = new PrintWriter(socketAtNode.getOutputStream(), true);
            BufferedReader readFromServer = new BufferedReader(new InputStreamReader(socketAtNode.getInputStream()));){

            System.out.println("Connected to the Server!");
            List<String> strings = randomString(100);
            strings.add("EODATA");
            int counter = 0;
            ArrayList<String> lostData = new ArrayList<>();

            while(counter < strings.size()){
                String serve_Status = readFromServer.readLine();
                System.out.println("Server Status: " + serve_Status);

                String value = strings.get(counter);

                if(!serve_Status.equals("CONGESTED")){
                    sendToServer.println(value);
                    System.out.println("Send");
                }else{
                    lostData.add(value);
                    sendToServer.println("ODE");
                }
                if(counter == strings.size()-1){
                    sendToServer.println("EODATA");
                }
                ++counter;
                Thread.sleep(50);
            }
            System.out.println("Total data lost: " + lostData.size());
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private static String anyString(){
        String name = UUID.randomUUID().toString();
        name = name.replace("-","");
        return name;
    }

    private static List<String> randomString(int lengthOfList){
        ArrayList<String> listOfString = new ArrayList<>();
        for(int x = 0; x < lengthOfList; x++){
            listOfString.add(anyString());
        }

        return listOfString;
    }
}

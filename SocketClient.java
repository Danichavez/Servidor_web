/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package t1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SocketClient implements Runnable { //interfaz que instancia y ejecuta hilos

    private Socket client; //socket del navegador

    public SocketClient(Socket client) {// Constructor que inicializa al socket
        this.client = client;
    }

    @Override
    public void run() {
        try {
            System.out.println("Hilo inicializado con el nombre: " + Thread.currentThread().getName());

            readResponse();

            return;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void readResponse() throws IOException, InterruptedException {

        try {
            //mensaje del navegador al servidor
            BufferedReader request = new BufferedReader(new InputStreamReader(client.getInputStream()));
            //Salida del servidor al navegador
            BufferedWriter response = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            
            String requestHeader = ""; //string cabecera
            String temp = "."; //almacena mensaje del navegador

            StringBuilder sb = new StringBuilder(); //clase para concatenar strings de la cabecera
            String nav = ""; //string navegador
            
            while (!temp.equals("")) { //mientras temp sea distinto de vacio
                temp = request.readLine(); //lee mensaje del navegador
                System.out.println(temp); //imprime entrada del navegador

                String[] palabras = temp.split(" "); //vector que guarda las palabras de la cabecera

                for (String i : palabras) {
                    if (i.equals("Chrome/67.0.3396.99")) { //si valor de palabras es chrome
                        nav= "CHROME";//si vector de palabras es chrome
                        System.out.println("\nEL NAVEGADOR ES -  CHROME\n"); //imprime nombre navegador
                        
                    }
                    if (i.equals("Firefox/33.0")) { //si es firefox
                        System.out.println("\nEL NAVEGADOR ES -  FIREFOX\n");
                         nav= "FIREFOX"; 
                       
                    }
                }
                requestHeader += temp + "\n";
            }
            System.out.println("----------------------------------------");

            

            if (requestHeader.split("\n")[0].contains("GET")&& checkURL(requestHeader.split("\n")[0].split(" ")[1])) { //si en la cabecera se envia una peticion GET y existe el archivo html
                //si envia la peticion correcta
                responseHeader(200, sb); //200 ok
                response.write(sb.toString()); //envía confirmación de acceso

                response.write(getData(nav, false)); //extrae flujo del html
                //sb.setLength(0);
                response.flush();//almacena archivo
            } else {
                //si envia una peticion no valida
                // 404 page not found
                responseHeader(404, sb); //valida 
                response.write(getData("404", true));
                response.write(sb.toString()); //informa error 404
                
                //sb.setLength(0);
                response.flush();
            }

            request.close();
            response.close();

            client.close(); //termina socket del navegador
            return;
        } catch (Exception e) {
            
        }

    }

   

    private static void responseHeader(int responseCode, StringBuilder sb) { //recibe de parametro el codigo y string de cabecera

        if (responseCode == 200) {

            sb.append("HTTP/1.1 200 OK\r\n");
            sb.append("Date:" + getTimeStamp() + "\r\n");
            sb.append("Server:localhost\r\n");
            sb.append("Content-Type: text/html\r\n");
            sb.append("Connection: Closed\r\n\r\n");

            System.out.println("HTTP/1.1 200 OK\r\n");
            System.out.println("Date:" + getTimeStamp() + "\r\n");
            System.out.println("Server:localhost\r\n");
            System.out.println("Content-Type: text/html\r\n");
            System.out.println("Connection: Closed\r\n\r\n");

        } else if (responseCode == 404) {

            sb.append("HTTP/1.1 404 Not Found\r\n");
            sb.append("Date:" + getTimeStamp() + "\r\n");
            sb.append("Server:localhost\r\n");
            sb.append("\r\n");
            

            System.out.println("HTTP/1.1 404 Not Found\r\n");
            System.out.println("Date:" + getTimeStamp() + "\r\n");
            System.out.println("Server:localhost\r\n");
            System.out.println("\r\n");

        } 
    }
    
     private static boolean checkURL(String nav) { //retorna confirmacion de que el archivo existe
         String correct="/";
        File myFile = new File(nav);
        if(nav.equals(correct)){
            return true;
        }
        return myFile.exists() && !myFile.isDirectory();
    }

    private static String getData(String navegador, boolean aux) { //metodo que lee el contenido del archivo html
        File myFile = null; //creación de archivo
        if(navegador.equals("CHROME") && aux != true){
            myFile=new File ("C:\\Users\\Daniela\\Documents\\NetBeansProjects\\T1\\src\\t1//indexC.html");
        }else if(navegador.equals("FIREFOX") && aux != true){
            myFile=new File("C:\\Users\\Daniela\\Documents\\NetBeansProjects\\T1\\src\\t1//indexF.html");
        }
        else if (navegador.equals("404") && aux == true) {
            myFile=new File("C:\\Users\\Daniela\\Documents\\NetBeansProjects\\T1\\src\\t1//404notfound.html");
        }
        
               
        
        String responseToClient = ""; //devolver ruta de acceso del archivo
        BufferedReader reader; 

         
        // System.out.println(myFile.getAbsolutePath()); 
        try {
            reader = new BufferedReader(new FileReader(myFile)); //se inicializa el lector del archivo
           
            String line = null;

            while (!(line = reader.readLine()).contains("</html>")) { //mientras el string no contenga el nombre del archivo
               System.out.println(line);
                responseToClient += line; //envía null
            }
            responseToClient += line; 
//			System.out.println(responseToClient);
            reader.close();

        } catch (Exception e) {

        }
        return responseToClient;
    }

    // TimeStamp
    private static String getTimeStamp() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

}

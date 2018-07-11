/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package t1;

/**
 *
 * @author Daniela
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadedServer {

	private ServerSocket serverSocket; //socket del servidor
	private int port; //puerto donde se comunicaran

	public MultiThreadedServer(int port) {  //constructor inicializa el puerto
		this.port = port;
	}

	public void start() throws IOException, InterruptedException { //método para iniciar el servidor

		serverSocket = new ServerSocket(port);
		
		System.out.println("Iniciando el servidor en el puerto: " + port); 

		Socket client = null; 

		while (true) {
			System.out.println("Esperando petición...");
			
			client = serverSocket.accept(); //espera a un cliente que requiera conectarse
			
			System.out.println("El siguiente cliente se ha conectado: " + client.getInetAddress());
			
			System.out.println();
			
			//Genera nuevo hilo para el cliente
			Thread thread = new Thread(new SocketClient(client));
			
			thread.start();
		}
	}

}

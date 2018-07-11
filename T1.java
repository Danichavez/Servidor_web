/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package t1;

import java.io.IOException;

/**
 *
 * @author Daniela
 */
public class T1 {

   public static void main(String[] args) throws InterruptedException {
//		int portNumber = Integer.parseInt(args[0]);

		int portNumber = 3753; //Puerto para la conexión
		
		try {
			// Inicializando el Socket Server multi hilos
			MultiThreadedServer socketServer = new MultiThreadedServer(portNumber);
			
			socketServer.start();

		} catch (IOException e) {//si se produce una excepcion se imprime el registro
			e.printStackTrace();
		}
	}
    
}

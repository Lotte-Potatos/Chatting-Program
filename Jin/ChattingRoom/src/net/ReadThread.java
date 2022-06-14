package net;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.Set;
import java.util.TreeSet;
import java.io.InputStreamReader;
import view.ClientFrame;

public class ReadThread extends Thread {

	Socket socket;
	ClientFrame cf;

	public static Set<String> clientList = new TreeSet<String>();

	public ReadThread(Socket socket, ClientFrame cf) {
		this.socket = socket;
		this.cf = cf;
	}

	@Override
	public void run() {
		super.run();
		try {
			while (true) {
				BufferedReader br;

				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				String str = br.readLine();
				if (str == null) {
					System.out.println("접속끊김");
				}

//				cf.textA.append(str + "\n");
				
				if (str.contains("[")) {
					clientList.add(str.substring(str.indexOf('[') + 1, str.indexOf(']')));
				}
				
				Thread.sleep(300);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

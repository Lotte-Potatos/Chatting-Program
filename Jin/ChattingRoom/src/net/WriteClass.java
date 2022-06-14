package net;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Set;

import view.ClientRoomFrame;
import view.IdFrame;

public class WriteClass {
	Socket socket;
	ClientRoomFrame crf;
	
	public static Set<String> clientList = ReadThread.clientList;

	public WriteClass(Socket socket, ClientRoomFrame crf) {
		this.socket = socket;
		this.crf = crf;
	}

	public void sendMessage(String roomName) {
		try {
			PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

			String msg = "";
			String id = IdFrame.tf.getText();

			// 첫번째 전송
			if (!clientList.contains(id)) {
				InetAddress iaddr = socket.getLocalAddress();
				String ip = iaddr.getHostAddress(); // xxx.xxx.xxx.xxx

				msg = "[" + id + "]님 로그인(" + ip + ")";
				
				clientList.add(id);

			}
			// 그 외 전송
			else {
				msg = "[" + id + "]" + crf.textInput.getText();
			}

			// server로 전송
			pw.println(msg);
			pw.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

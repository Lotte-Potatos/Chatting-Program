package threadex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class ServerThread extends Thread {

	Socket socket;
	List<Socket> list;

	public ServerThread(Socket socket, List<Socket> list) {
		this.socket = socket;
		this.list = list;
	}

	@Override
	public void run() {
		super.run();

		try {

			while (true) {

				// 수신(recieve)
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				String str = reader.readLine();

				System.out.println("client로부터 받은 메시지:" + str);

				// 송신(send)

				for (Socket s : list) {
					if (socket != s) {
						PrintWriter writer = new PrintWriter(s.getOutputStream());
						writer.println(str);
						writer.flush();

					}
				}

				Thread.sleep(300);

			}
		} catch (SocketException e) {
			// SocketException
			System.out.println("연결이 끊긴 IP:" + socket.getInetAddress());
			list.remove(socket);

			// 접속되어 있는 남아 있는 클라이언트 출력
			for (Socket s : list) {
				System.out.println(
						"접속되어 있는 IP: " + s.getInetAddress() + " Post: " + s.getPort());
			}
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}

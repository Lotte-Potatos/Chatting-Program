package threadex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;

import dto.ChatRoom;

public class ServerThread extends Thread {

//	public static final int INIT = 5050;
	public static final int LOGIN = 1010;
	public static final int MSG = 2020;
	public static final int CHATROOM = 3030;

	Socket socket;
	List<Socket> list;
	List<String> userIdList;
	Vector<ChatRoom> chatList;
	int protocol = -1;

	public ServerThread(Socket socket, List<Socket> list, List<String> userIdList,
			Vector<ChatRoom> chatList) {
		this.socket = socket;
		this.list = list;
		this.userIdList = userIdList;
		this.chatList = chatList;
	}

	public String readRecieve(String str) {
		// 수신(recieve) 문자열 분석 함수
		String result = str;

		StringTokenizer st = new StringTokenizer(str, "|");

		protocol = Integer.parseInt(st.nextToken());

		String roomName = st.nextToken();
		String id = st.nextToken();

//		if(protocol==INIT) {
//			String initStr = "";
//			return initStr;
//		}

		switch (protocol) {
		case LOGIN:
			userIdList.add(id);
			break;
		case CHATROOM:
			int code = 1;
			int index = -1;
			List<String> temp = new ArrayList<String>();
			for (ChatRoom cr : chatList) {
				if (cr.getRoomName().equals(roomName)) {
					temp = cr.getUserlist();
					index = chatList.indexOf(cr);
				}
			}
			if (index == -1) {
				temp.add(id);
				chatList.add(new ChatRoom(roomName, temp));
			} else {
				if (!chatList.get(index).getUserlist().contains(id)) {
					temp.add(id);
					chatList.get(index).setUserlist(temp);
					code = 2;
				}
			}
			result = CHATROOM + "|" + roomName + "|" + id + "|" + code + "|";
			while (st.hasMoreTokens()) {
				result += st.nextToken();
			}
			break;
		}

		return result;
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

				String sendStr = readRecieve(str);

				// 송신(send)

				for (Socket s : list) {
					if (socket != s || protocol == CHATROOM) {
						PrintWriter writer = new PrintWriter(s.getOutputStream());
						writer.println(sendStr);
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

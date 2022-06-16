package net;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import dto.ChatRoom;

import java.io.InputStreamReader;
import java.io.PrintWriter;

import view.ClientFrame;
import view.ClientRoomFrame;
import view.IdFrame;

public class ReadThread extends Thread {

	Socket socket;
	ClientFrame cf;

	public static List<String> clientList = new ArrayList<String>();
	public static Vector<ChatRoom> chatList = new Vector<ChatRoom>();

	public ReadThread(Socket socket, ClientFrame cf) {
		this.socket = socket;
		this.cf = cf;
//		init();
	}

	public void init() {
		try {
			PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

			int protocol = Code.INIT;
			String result = protocol + "|";

			// server로 전송
			pw.println(result);
			pw.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
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

				// 채팅방 목록 테이블에 추가
				cf.model1.setRowCount(0);
				if (!chatList.isEmpty()) {
					for (ChatRoom cr : chatList) {

						cf.model1.addRow(new Object[] { cr.getRoomName(), cr.getUserlistToString() });

					}
				}

				// 접속중인 id 테이블에 추가
				cf.model2.setRowCount(0);
				if (!clientList.isEmpty()) {
					for (String s : clientList) {
						cf.model2.addRow(new Object[] { s });
					}
				}

				readRecieve(str);

				Thread.sleep(300);

				// 채팅방 목록 변경된 사항 포함해서 테이블에 추가
				cf.model1.setRowCount(0);

				if (!chatList.isEmpty()) {
					for (ChatRoom cr : chatList) {

						cf.model1.addRow(new Object[] { cr.getRoomName(), cr.getUserlistToString() });

					}
				}

				// 접속중인 id 테이블에 추가
				cf.model2.setRowCount(0);
				if (!clientList.isEmpty()) {
					for (String s : clientList) {
						cf.model2.addRow(new Object[] { s });
						System.out.println(s);
					}
				}

				Thread.sleep(300);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void readRecieve(String str) {
		String myId = IdFrame.textIdInput.getText();
		// 수신(recieve) 문자열 분석 함수
		String result = "";
		StringTokenizer st = new StringTokenizer(str, "|");

		int protocol = Integer.parseInt(st.nextToken());
		String roomName = "";
		String id = "";

		if (protocol == Code.INIT) {
		
//			if (st.hasMoreTokens()) {
//				StringTokenizer stk = new StringTokenizer(st.nextToken(), ",");
//
//				while (stk.hasMoreTokens()) {
//					clientList.add(st.nextToken());
//				}
//			}
			return;
		}

		switch (protocol) {
		case Code.LOGIN:
			id = st.nextToken();
			clientList.add(id);
			while (st.hasMoreTokens()) {
				result += st.nextToken();
			}
			if (!clientList.contains(id)) {

				clientList.add(id);
			}
			break;
		case Code.MSG:
			roomName = st.nextToken();
			id = st.nextToken();
			while (st.hasMoreTokens()) {
				result += st.nextToken();
			}
			if (roomName.equals(ClientRoomFrame.roomName)) {
				cf.crf.textChatArea.append(result + "\n");
			}
			break;
		case Code.CHATROOM:
			roomName = st.nextToken();
			id = st.nextToken();
			String code = st.nextToken();
			List<String> temp = new ArrayList<String>();

			while (st.hasMoreTokens()) {
				result += st.nextToken();
			}

			if (!clientList.contains(id)) {

				clientList.add(id);
			}

			int index = -1;
			for (ChatRoom cr : chatList) {
				if (cr.getRoomName().equals(roomName)) {
					temp = cr.getUserlist();
					index = chatList.indexOf(cr);
					break;
				}
			}

			if (index == -1) {
				temp.add(id);
				chatList.add(new ChatRoom(roomName, temp));
			} else {

				if (!chatList.get(index).getUserlist().contains(id)) {
					temp.add(id);
					chatList.get(index).setUserlist(temp);
				}

			}

			if (roomName.equals(ClientRoomFrame.roomName)) {
				cf.crf.textChatArea.append(result + "\n");
			}
			break;
		}

	}

}

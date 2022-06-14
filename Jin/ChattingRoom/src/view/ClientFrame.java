package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.Socket;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.WriteClass;

// 채팅 form
public class ClientFrame extends JFrame implements WindowListener, ActionListener {

	public Socket socket;
	WriteClass wc;
	ClientRoomFrame crf;

//	public JTextField roomTitleInput = new JTextField(20);
//	public JTextArea textA = new JTextArea();
//
//	JButton btnTransfer = new JButton("send");
//	JButton btnExit = new JButton("exit");
//
//	JPanel panel = new JPanel();

	JTable table1, table2;
	DefaultTableModel model1, model2;
	JTextField textRoomNameInput;
	JTextArea textArea;
	JButton btnCreateRoom, btnExit;
	JScrollBar bar;

	Set<String> clientList = WriteClass.clientList;

//	public boolean isFirst = true; // 첫번째 전송

	public ClientFrame(Socket socket) {
		super("로비"); // 타이틀 , = setTitle();

		this.socket = socket;

		new IdFrame(this, crf);

		String[] col1 = { "방이름", "ID" };
		String[][] row1 = new String[0][2];

		model1 = new DefaultTableModel(row1, col1);
		table1 = new JTable(model1);
		JScrollPane roomsPane = new JScrollPane(table1);

		textRoomNameInput = new JTextField();

		btnCreateRoom = new JButton("방 만들기");
		btnCreateRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				crf = new ClientRoomFrame(socket, textRoomNameInput.getText());
				crf.setVisible(true);
			}
		});
		
		btnExit = new JButton("나가기");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		btnCreateRoom.setBackground(Color.gray);
		btnExit.setBackground(Color.gray);

		// 배치
		setLayout(null);
		roomsPane.setBounds(10, 15, 300, 400);
		add(roomsPane);

		textRoomNameInput.setBounds(10, 420, 300, 30);
		add(textRoomNameInput);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2, 20, 20));

		buttonPanel.add(btnCreateRoom);
		buttonPanel.add(btnExit);
		buttonPanel.setBounds(10, 460, 300, 50);
		add(buttonPanel);

		setBounds(200, 200, 340, 570);

		setVisible(false);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		System.exit(0);

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}

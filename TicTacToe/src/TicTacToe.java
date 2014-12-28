import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TicTacToe extends JFrame {

	private static final long serialVersionUID = 1L;
	private MyButton[][] board;
	private String myMark;
	private String opponentMark;
	private volatile boolean onTurn;
	private Point myLastMove;

	public TicTacToe(String myMark, String opponentMark) {
		super("TicTacToe");
		this.board = new MyButton[3][3];
		this.myMark = myMark;
		this.opponentMark = opponentMark;
		this.onTurn = "X".equals(myMark) ? true : false;
		this.myLastMove = null;
		initializeGame();
	}

	private void initializeGame() {
		setSize(400, 400);
		setResizable(false);
		setLayout(new GridLayout(3, 3));
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board[i][j] = new MyButton();
				add(board[i][j]);

			}
		}
		setVisible(true);

	}

	public String getMyMark() {
		return this.myMark;
	}

	public String getOpponentMark() {
		return this.opponentMark;
	}

	public void opponentMove(int x, int y) {
		board[x][y].setText(opponentMark);
		this.onTurn = true;
	}

	public Point getMyLastMove() {
		return this.myLastMove;
	}

	public boolean hasWinner(String playerMark) {
		for (int i = 0; i < 3; i++) {
			if (playerMark.equals(board[0][i].getText())
					&& board[1][i].getText().equals(board[2][i].getText())
					&& playerMark.equals(board[1][i].getText())) {
				return true;
			} else if (playerMark.equals(board[i][0].getText())
					&& board[i][1].getText().equals(board[i][2].getText())
					&& playerMark.equals(board[i][1].getText())) {
				return true;
			}
		}
		if (playerMark.equals(board[0][0].getText())
				&& board[1][1].getText().equals(board[2][2].getText())
				&& playerMark.equals(board[1][1].getText())) {
			return true;
		}
		if (playerMark.equals(board[0][2].getText())
				&& board[1][1].getText().equals(board[2][0].getText())
				&& playerMark.equals(board[1][1].getText())) {
			return true;
		}

		return false;
	}

	public boolean isOnTurn() {
		return this.onTurn;
	}

	public void setOnTurn(boolean onTurn) {
		this.onTurn = onTurn;
	}

	public void displayWinMessage() {
		JOptionPane.showMessageDialog(this, "YOU WON!");
	}

	public void displayLoseMessage() {
		JOptionPane.showMessageDialog(this, "YOU LOST!");
	}

	public void displayOutOfGameMessage() {
		JOptionPane.showMessageDialog(this, "Your opponent has left the game!");
	}

	public Point getIndexes(MyButton onEventButton) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == onEventButton) {
					return new Point(i, j);
				}
			}
		}
		return null;
	}

	private class MyButton extends JButton implements ActionListener {

		private static final long serialVersionUID = 1L;

		public MyButton() {
			this.setFont(new Font("Ariel", Font.BOLD, 40));
			this.setIcon(new ImageIcon());
			addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (onTurn && "".equals(this.getText())) {
				myLastMove = getIndexes(this);
				this.setText(myMark);
				onTurn = false;
			}
		}

	}

}

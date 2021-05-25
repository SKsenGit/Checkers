
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

//Отмечать фишки с обязательными ходами
//сделать дамке мультиход
//Сообщения о победе, подсчет очков игроков, ввод игроков
//Выбор хода компьютером
public class Board extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Piece[][] gameField;
	Piece markedPiece;
	int markedX;
	int markedY;
	GameColors nowStepColor; 
	Player playerWhite;
	Player playerBlack;
	boolean blackIsDown;
	boolean nowMultyStep;
	boolean GameOver;
	boolean oneGamer;
	
	public Board(boolean oneGamer) {
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		this.oneGamer = oneGamer;
		markedPiece = null;
		markedX = -1;
		markedY = -1;
		nowStepColor = GameColors.WHITE;
		gameField = new Piece[8][8];
		blackIsDown = false;
		nowMultyStep = false;
		GameOver = false;
		playerWhite = new Player("White player",GameColors.WHITE,false);
		playerBlack = new Player("Black player",GameColors.BLACK,oneGamer);
		for(int i = 0;i<3;i++) {
			for(int j=0;j<8;j++) {
				if(i%2==0 && j%2!=0 || i%2!=0 && j%2==0) {
					gameField[i][j] = new Piece(blackIsDown?playerWhite:playerBlack);
					
					//break;
				}
			}
		}
		for(int i = 5;i<8;i++) {
			for(int j=0;j<8;j++) {
				if(i%2==0 && j%2!=0 || i%2!=0 && j%2==0) {
					gameField[i][j] = new Piece(blackIsDown?playerBlack:playerWhite);					
				}
			}
		}
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//paint field
		g.fillRect(200, 100, 400, 400);
		
		for(int i = 200; i<=500; i+=100) {
			for(int j = 100; j<=400; j+=100) {
				g.clearRect(i, j, 50, 50);
			}
		}
		
		for(int i = 250; i<=550; i+=100) {
			for(int j = 150; j<=450; j+=100) {
				g.clearRect(i, j, 50, 50);
			}
		}
		//paint pieces
		for(int i = 0;i<gameField.length;i++) {
			for(int j = 0;j<gameField[0].length;j++) {
				if(gameField[i][j] != null) {
					int x = 193 + j*50;
					int y = 93	+ i*50;
					g.drawImage(gameField[i][j].getImage(), x, y, this);
				}
			}
		}
		
		//paint player info, scores
		g.drawString(playerWhite.getName(), 80, 80);
		g.drawString(""+playerWhite.getScores(), 80, 100);
		g.drawString(playerBlack.getName(), 680, 80);
		g.drawString(""+playerBlack.getScores(), 680, 100);
		
		
		if(GameOver) {
			if(playerWhite.isWinner) {
				g.drawString("White player is the winner!!!", 350, 50);
			}
			else if(playerBlack.isWinner){
				g.drawString("Black player is the winner!!!", 350, 50);
			}
			
			g.drawImage(Toolkit.getDefaultToolkit().getImage("GameOver.png"), 250, 150, this);
		}
		
		
	}
	@Override
	protected void processMouseEvent(MouseEvent mouseEvent) {
		super.processMouseEvent(mouseEvent);
		
		if(mouseEvent.getButton() == MouseEvent.BUTTON1 && mouseEvent.getID()== MouseEvent.MOUSE_CLICKED && !GameOver) {
			
			int x = mouseEvent.getX()-200;
			int y = mouseEvent.getY()-100;
			int j = (int)((float) x/400*8);
			int i = (int)((float) y/400*8);
			//moveStep(i,j);
			
			if(moveStep(i,j)) {
				this.repaint();
				
				if(oneGamer && nowStepColor == GameColors.BLACK) {
					notHumanPieceMark();
					
					notHumanMoveStep();
				}
			}
		}
	}

	protected boolean moveStep(int i, int j) {
		boolean blackCell = (i%2==0 && j%2!=0 || i%2!=0&&j%2==0);
		boolean sameCell = (i==markedY && j==markedX);
		boolean cellEmpty = (gameField[i][j] == null);
		boolean stepPossible = false;
		
		
		
		if(!blackCell || cellEmpty && markedPiece==null) {
			return false;
		}
		
				
		if(sameCell && gameField[i][j].getWasStep()) {
			gameField[i][j].setIsMarked(false);
			gameField[i][j].setWasStep(false);
			markedPiece=null;
			markedX = -1;
			markedY = -1;
			
			changeStepColor();
			nowMultyStep = false;
			return true;
		}
		
		if(!cellEmpty) {
						
			if(markedPiece==null && gameField[i][j].getGameColor() == nowStepColor && havePossibleStep(i,j,gameField[i][j])) {
				markedPiece = gameField[i][j];
				markedPiece.setIsMarked(true); 
				markedX = j;
				markedY = i;
			}
			
			
		}
		
		if(cellEmpty && markedPiece!=null) {
			boolean isKing = markedPiece.getIsKing();
			if(isKing) {
				
				int amountCellY = markedY>i?markedY-i:i-markedY;
				int amountCellX = markedX>j?markedX-j:j-markedX;
								
				if(amountCellY!=amountCellX) {//it isn't a diagonal
					return false;
				}
				
				int startX = markedY; 
				int startY = markedX;
				
				int kX = startX>i?-1:1;
				int kY = startY>j?-1:1;
				int m = 0;
				int n = 0;
				ArrayList<Integer> capturedPieces = new ArrayList<>();
				
				for(int k = 1;k <amountCellY;k++ ) {
					m = startX+(k*kX);
					n = startY+(k*kY);
					
						// is same color piece in the way
						if(gameField[m][n]!=null && gameField[m][n].getGameColor()==markedPiece.getGameColor()) {
							return false;
						}
						// there are two pieces near each other in the way
						if(gameField[m][n]!=null && gameField[m][n].getGameColor()!=markedPiece.getGameColor()) {
							if(gameField[m+kX][n+kY]!=null && gameField[m+kX][n+kY]!=markedPiece 
									|| gameField[m-kX][n-kY]!=null && gameField[m-kX][n-kY]!=markedPiece ) {
								System.out.println("2");
								return false;
							}
							capturedPieces.add((m)*10+n);
						}
					
				}
				
				//remove pieces
				int scores = 0;
				for(int cell:capturedPieces) {
					
					gameField[cell/10][cell-(cell/10*10)]= null;
					//add scores
					scores++;
				}
				
				stepPossible = true;
				addScores(scores);
				
				
			}
			else {//not king
				
				int amountCell = markedY>i?markedY-i:i-markedY;
				//simple step
				if(moveDirectionPosible(markedPiece,markedY,i) && amountCell==1 && !nowMultyStep) {
					stepPossible = true;					
					
				}
				//capture step
				if(amountCell==2 ) {
					int capturedX = markedX>j?markedX-1:markedX+1;
					int capturedY = markedY>i?markedY-1:markedY+1;
										
					if(gameField[capturedY][capturedX]!=null && gameField[capturedY][capturedX].getGameColor()!= markedPiece.getGameColor()) {
						
						
						nowMultyStep = haveRequiredStep(i,j,isKing,gameField[capturedY][capturedX].getGameColor());
						markedPiece.setWasStep(nowMultyStep);
						
						gameField[capturedY][capturedX] = null;
						addScores(1);
						stepPossible = true;
						
					}
				}
				//check possible been King
				if(moveDirectionPosible(markedPiece,markedY,i)&& (i==0 || i==7)){
					markedPiece.setIsKing();
				}
				
			}
			
		}
		else {//choice pieces current player (with color)
			boolean stepColorPossible = (gameField[i][j].getGameColor() == nowStepColor);
			if(!stepColorPossible) {
				return false;
			}
			
			
		}
		
		
			if(stepPossible) {
				gameField[i][j] = markedPiece;
				gameField[markedY][markedX] = null;
				//addScores(1);
				markedX = j;
				markedY = i;
				if(!nowMultyStep) {
					markedPiece.setIsMarked(false);
					markedPiece = null;
					markedX = -1;
					markedY = -1;
					changeStepColor();
					checkTheEnd();
				}
								
				
				
				
			}
			
		return true;
	}
	protected void addScores(int scores) {
		if(nowStepColor == GameColors.BLACK) {
			playerBlack.addScores(scores);
		}
		else {
			playerWhite.addScores(scores);
		}
	}
	protected void changeStepColor() {
		if(nowStepColor == GameColors.WHITE) {
			nowStepColor = GameColors.BLACK;
		}
		else {
			nowStepColor = GameColors.WHITE;
		}
	}
	protected boolean havePossibleStep(int i,int j,Piece piece) {
		boolean pieceIsKing = piece.getIsKing();
		GameColors removedPieceColor = piece.getGameColor()==GameColors.BLACK?GameColors.WHITE:GameColors.BLACK;
		
			if(i+1<8 && j+1<8 && gameField[i+1][j+1] == null && moveDirectionPosible(piece, i, i+1)) {
				//System.out.println("1");
					return true;
			}
			if(i+1<8 && j-1>-1 && gameField[i+1][j-1] == null&& moveDirectionPosible(piece, i, i+1)) {
				//System.out.println("2");
				return true;
			}
			if(i-1>-1 && j+1<8 && gameField[i-1][j+1] == null && moveDirectionPosible(piece, i, i-1)) {
				//System.out.println("3");
				return true;
			}
			if(i-1>-1 && j-1>-1 && gameField[i-1][j-1] == null && moveDirectionPosible(piece, i, i-1)) {
				//System.out.println("4");
				return true;
			}
		
		return haveRequiredStep(i,j,pieceIsKing, removedPieceColor);
	}
	protected boolean haveRequiredStep(int i,int j,boolean pieceIsKing, GameColors removedPieceColor) {
		//System.out.println(i+" "+j+" "+pieceIsKing+" "+removedPieceColor);
		
			if(i+2<8 && j+2<8 && gameField[i+1][j+1] != null && gameField[i+1][j+1].getGameColor() == removedPieceColor &&
					gameField[i+2][j+2] == null) {
				//System.out.println("5");
					return true;
			}
			else if(i+2<8 && j-2>-1 && gameField[i+1][j-1] != null && gameField[i+1][j-1].getGameColor() == removedPieceColor &&
					gameField[i+2][j-2] == null) {
				//System.out.println("6");
					return true;
			}
			else if(i-2>-1 && j+2<8 && gameField[i-1][j+1] != null && gameField[i-1][j+1].getGameColor() == removedPieceColor &&
					gameField[i-2][j+2] == null) {
				//System.out.println("7");
					return true;
			}
			else if(i-2>-1 && j-2>-1 && gameField[i-1][j-1] != null && gameField[i-1][j-1].getGameColor() == removedPieceColor &&
					gameField[i-2][j-2] == null) {
				//System.out.println("8");
					return true;
			} 
			
			
				
		
		return false;
	}
	
	protected boolean moveDirectionPosible(Piece piece, int startY, int endY) {
		boolean moveDown = (endY-startY>0);
		return (piece.getIsKing() || moveDown && blackIsDown && piece.getGameColor()==GameColors.WHITE
				|| moveDown && !blackIsDown && piece.getGameColor()==GameColors.BLACK 
				|| !moveDown && blackIsDown && piece.getGameColor()==GameColors.BLACK 
				|| !moveDown && !blackIsDown && piece.getGameColor()==GameColors.WHITE);
	}
	
	protected void checkTheEnd() {
		int piecesForStep = (int) Arrays.stream(gameField).flatMap(x->Arrays.stream(x)).filter(x->x!=null).filter(x->x.getGameColor()==nowStepColor).count();
		
		boolean haveStepWhite = false;
		boolean haveStepBlack = false;
		for(int i = 0;i<gameField.length;i++) {
			for(int j = 0;j<gameField[i].length;j++) {
				if(gameField[i][j]!=null && gameField[i][j].getGameColor()==GameColors.WHITE && havePossibleStep(i,j,gameField[i][j])) {
					haveStepWhite = true;
					
					
				}
				if(gameField[i][j]!=null && gameField[i][j].getGameColor()==GameColors.BLACK && havePossibleStep(i,j,gameField[i][j])) {
					haveStepBlack = true;
					
					
				}
				
			}
			if(haveStepWhite&&haveStepBlack) {
				break;
			}
		}
		
			
		
		//System.out.println("have steps black"+haveStepBlack);
		//System.out.println("have steps white"+haveStepWhite);
		GameOver = piecesForStep==0||!haveStepWhite||!haveStepBlack?true:false;	
		if(!haveStepBlack||piecesForStep==0 && nowStepColor == GameColors.BLACK) {
			playerWhite.setWinner();
		}
		if(!haveStepWhite||piecesForStep==0 && nowStepColor == GameColors.WHITE) {
			playerBlack.setWinner();
		}
		
	}
	
	protected void notHumanPieceMark() {
		//System.out.println("notHuman Step now!");
		for(int i = gameField.length-1;i>-1;i--) {
			for(int j = 0;j<gameField[0].length;j++) {
				if(gameField[i][j] != null && gameField[i][j].getGameColor() == nowStepColor && havePossibleStep(i,j,gameField[i][j])) {
					markedPiece = gameField[i][j];
					markedPiece.setIsMarked(true); 
					markedX = j;
					markedY = i;
					return;
				}
			}
		}
			
	}
	
	protected void notHumanMoveStep() {
		/*if(markedPiece.getIsKing()) {
			
		}
		else {
			
		}*/
		int i = markedY;
		int j = markedX;
		GameColors removedPieceColor = playerWhite.getGameColor();
		if(i+2<8 && j+2<8 && gameField[i+1][j+1] != null && gameField[i+1][j+1].getGameColor() == removedPieceColor &&
				gameField[i+2][j+2] == null) {
			moveStep(i+2,j+2);
			
		}
		else if(i+2<8 && j-2>-1 && gameField[i+1][j-1] != null && gameField[i+1][j-1].getGameColor() == removedPieceColor &&
				gameField[i+2][j-2] == null) {
			moveStep(i+2,j-2);
		}
		else if(i-2>-1 && j+2<8 && gameField[i-1][j+1] != null && gameField[i-1][j+1].getGameColor() == removedPieceColor &&
				gameField[i-2][j+2] == null) {
			moveStep(i-2,j+2);
		}
		else if(i-2>-1 && j-2>-1 && gameField[i-1][j-1] != null && gameField[i-1][j-1].getGameColor() == removedPieceColor &&
				gameField[i-2][j-2] == null) {
			moveStep(i-2,j-2);
		} 
		else if(i+1<8 && j+1<8 && gameField[i+1][j+1] == null) {
			moveStep(i+1,j+1);
		}
		else if(i+1<8 && j-1>-1 && gameField[i+1][j-1] == null) {
			moveStep(i+1,j-1);
		}
		
	}
			
	
	
}

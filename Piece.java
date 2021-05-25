import java.awt.*;


public class Piece {
	private boolean isMarked;
	private GameColors pieceColor;
	private Image pieceImg;
	private Image pieceImgMarked;
	private boolean isKing;
	private Player player;
	private boolean wasStep;
	private boolean isCaptured;
	
	public Piece(Player player) {
		isMarked = false;
		isKing = false;
		wasStep = false;
		isCaptured = false;
		this.pieceColor = player.getGameColor();
		this.player = player;
		if(pieceColor == GameColors.BLACK) {
			pieceImg = Toolkit.getDefaultToolkit().getImage("piece_dark.png");
			pieceImgMarked = Toolkit.getDefaultToolkit().getImage("piece_dark_select.png");
		}
		else {
			pieceImg = Toolkit.getDefaultToolkit().getImage("piece_light.png");
			pieceImgMarked = Toolkit.getDefaultToolkit().getImage("piece_light_select.png");			
		}
	}
	
	
	public boolean getIsMarked() {
		return isMarked;
	}
	
	public void setIsMarked(boolean isMarked) {
		this.isMarked = isMarked;
	}
	
	public GameColors getGameColor(){
			return pieceColor;
	}
	
	public Image getImage() {
		if(isMarked) {
			return pieceImgMarked;
		}
		return pieceImg;
	}
	public boolean getIsKing() {
		return isKing;
	}
	
	public void setIsKing() {
		isKing = true;
		if(pieceColor == GameColors.BLACK) {
			pieceImg = Toolkit.getDefaultToolkit().getImage("piece_dark_king.png");
			pieceImgMarked = Toolkit.getDefaultToolkit().getImage("piece_dark_king_select.png");
		}
		else {
			pieceImg = Toolkit.getDefaultToolkit().getImage("piece_light_king.png");
			pieceImgMarked = Toolkit.getDefaultToolkit().getImage("piece_light_king_select.png");			
		}
	}
	
	public void setWasStep(boolean wasStep) {
		this.wasStep = wasStep;
	}
	
	public boolean getWasStep() {
		return wasStep;
	}
	public void setIsCaptured(boolean isCaptured) {
		this.isCaptured = isCaptured;
	}
	
	public boolean getIsCaptured() {
		return isCaptured;
	}
	

}

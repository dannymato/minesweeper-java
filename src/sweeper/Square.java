package sweeper;

/**
 * Created by danny on 2/14/17.
 */
public class Square {

	private boolean hasBomb;

	private int numBombs;

	private boolean hasFlag;

	private boolean isDisabled;

	public Square(){

	}

	public int getNumBombs(){return numBombs;}

	public void setNumBombs(int bombs){numBombs = bombs;}

	public boolean getHasBomb(){return hasBomb;}

	public void setHasBomb(boolean bomb){hasBomb = bomb;}

	public void addFlag(){hasFlag = true;}

	public void removeFlag(){hasFlag = false;}

	public boolean getHasFlag(){return hasFlag;}

	public boolean getDisabled(){return isDisabled;}

	public void setDisabled(boolean disabled){isDisabled = disabled;}

	public boolean isWhitespace(){
		return !hasBomb && numBombs==0 && !isDisabled;
	}

	@Override
	public String toString() {
		if(hasFlag)
			return "F";
		else if(hasBomb)
			return "B";
		else if(numBombs == 0)
			return "  ";
		else
			return String.valueOf(numBombs);
	}
}

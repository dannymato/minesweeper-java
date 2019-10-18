package sweeper;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    private final int width = 15;
    private final int height = 15;

    private int numBombs;
    private int flagsLeft;

    private int currTime = 0;

    private static Square[][] squares;

    private Animation animation;

    private boolean isFirst = true;

    private Color[] colors = {Color.GRAY, Color.BLUE, Color.GREEN, Color.RED, Color.INDIGO, Color.BROWN, Color.PINK, Color.YELLOW, Color.CADETBLUE};

    private Button[][] button;

	private GridPane gridPane = new GridPane();

    private void setupGrid(int x1 , int y1){

    	numBombs = width*height/5;
    	flagsLeft = numBombs;

    	squares = null;

    	squares = new Square[width][height];

    	for(int i = 0; i < width;i++){

    		for(int j = 0; j < height; j++){
    			squares[i][j] = new Square();
			}

		}

    	for(int i = numBombs;i > 0;i--){

    		int x = (int)(Math.random()*width);
    		int y = (int)(Math.random()*height);

    		if(squares[x][y].getHasBomb() || ((x == x1) && (y == y1))){
    			i++;
			}
			else{
    			squares[x][y].setHasBomb(true);
			}

		}

		for(int i = 0; i < width;i++){

			for(int j = 0; j < height; j++){

				int x = 0;

				if(i > 0 && j > 0){
					if(squares[i-1][j-1].getHasBomb()){
						x++;
					}
				}

				if(i > 0){
					if(squares[i-1][j].getHasBomb()){
						x++;
					}
				}

				if(i > 0 && j < height-1){
					if(squares[i-1][j+1].getHasBomb()){
						x++;
					}
				}
				if(j > 0){
					if(squares[i][j-1].getHasBomb()){
						x++;
					}
				}
				if(j < height-1){
					if(squares[i][j+1].getHasBomb()){
						x++;
					}
				}
				if(j > 0 && i < width-1){
					if(squares[i+1][j-1].getHasBomb()){
						x++;
					}
				}
				if(i < width-1){
					if(squares[i+1][j].getHasBomb()){
						x++;
					}
				}
				if(j < height-1 && i < width-1){
					if(squares[i+1][j+1].getHasBomb()){
						x++;
					}
				}

				squares[i][j].setNumBombs(x);

			}

		}


	}

    @Override
    public void start(Stage primaryStage) throws Exception{

		Label numFlags = new Label(String.valueOf(flagsLeft));
		gridPane.add(numFlags,width-1,height+1);

		setupButtons(numFlags);

		Label label = new Label("000");
		gridPane.add(label,0,height+1);
		animation = new Transition() {
			{
				setInterpolator(Interpolator.LINEAR);
				setCycleCount(999);
				setCycleDuration(Duration.millis(1000));

			}
			@Override
			protected void interpolate(double v) {
				if(v == 0) {
					if(++currTime < 10){
						label.setText("00" + String.valueOf(currTime));
					}
					else if(currTime >= 10 && currTime < 100){
						label.setText("0" + String.valueOf(currTime));
					}
					else {
						label.setText(String.valueOf(currTime));
					}
					System.out.println(currTime);
				}
			}
		};

		Button restart = new Button("\u263A");
		restart.setOnAction((ActionEvent actionEvent) -> {
				removeAllFromGrid();
				printGrid();
				currTime = 0;
				setupButtons(numFlags);
				label.setText("000");
				isFirst = true;
				animation.stop();
		});

		gridPane.add(restart,width/2,height+1);

		gridPane.setHgap(0);
		gridPane.setVgap(0);


        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(gridPane, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);

    }

    private void showAll(){
		for (int i = 0; i < width; i++){
			for (int j = 0; j < height; j++){
				showValue(i,j);
				if(squares[i][j].getHasBomb() && !squares[i][j].getHasFlag()){
					button[i][j].setText("B");
					button[i][j].setTextFill(Color.BLACK);
					button[i][j].setStyle("-fx-background-color: #FF0000");
				}
				else if(squares[i][j].getHasFlag() && !squares[i][j].getHasBomb()){
					button[i][j].setText("X");
					button[i][j].setTextFill(Color.BLACK);
				}
			}
		}
	}

	private boolean hasWon(){
		if(flagsLeft > 0)
			return false;
		else {
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					if (squares[i][j].getHasBomb()) {
						System.out.println(i + " " + j);
						System.out.println(!(squares[i][j].getHasBomb() && squares[i][j].getHasFlag()));
						if (!(squares[i][j].getHasBomb() && squares[i][j].getHasFlag())) {
							return false;
						}
					}
				}
			}

			return true;
		}
	}

	private void findWhitespace(int x, int y){

		showValue(x,y);
		if(x < width -1 && y < height - 1) {
			if (squares[x + 1][y + 1].isWhitespace()) {
				System.out.println("Found whitespace");
				findWhitespace(x + 1,y + 1);
			}
			showValue(x + 1, y + 1);
		}
		if(y < height - 1) {
			if (squares[x][y + 1].isWhitespace()) {
				System.out.println("Found whitespace");
				findWhitespace(x, y + 1);
			}
			showValue(x, y + 1);
		}
		if(x > 0 && y < height - 1) {
			if (squares[x - 1][y + 1].isWhitespace()) {
				System.out.println("Found whitespace");
				findWhitespace(x - 1, y + 1);
			}
			showValue(x - 1, y + 1);
		}
		if(x < width - 1) {
			if (squares[x + 1][y].isWhitespace()) {
				System.out.println("Found whitespace");
				findWhitespace(x + 1, y);
			}
			showValue(x + 1, y);
		}
		if(x < width - 1 && y > 0) {
			if (squares[x + 1][y - 1].isWhitespace()) {
				System.out.println("Found whitespace");
				findWhitespace(x + 1, y - 1);
			}
			showValue(x + 1, y - 1);
		}
		if(x > 0) {
			if (squares[x - 1][y].isWhitespace()) {
				System.out.println("Found whitespace");
				findWhitespace(x - 1, y);
			}
			showValue(x - 1, y);
		}
		if(x > 0 && y > 0) {
			if (squares[x - 1][y - 1].isWhitespace()) {
				System.out.println("Found whitespace");
				findWhitespace(x - 1, y - 1);
			}
			showValue(x - 1, y - 1);
		}
		if(y > 0) {
			if (squares[x][y - 1].isWhitespace()) {
				System.out.println("Found whitespace");
				findWhitespace(x, y - 1);
			}
			showValue(x, y - 1);
		}
	}

	private void setupButtons(Label numFlags){
		button = null;
		button = new Button[width][height];
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				button[i][j] = new Button("  ");
				button[i][j].setFont(Font.font("Verdana", FontWeight.BLACK,20));
				button[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mouseEvent) {
						int x = gridPane.getRowIndex((Button)(mouseEvent.getSource()));
						int y = gridPane.getColumnIndex((Button)(mouseEvent.getSource()));

						//Flags
						if(mouseEvent.getButton() == MouseButton.SECONDARY){
							if(isFirst){
								isFirst = false;
								animation.play();
								setupGrid(x,y);
								System.out.println("Started animation");
							}
							if(squares[x][y].getHasFlag()){
								button[x][y].setText("  ");
								squares[x][y].removeFlag();
								flagsLeft++;
								System.out.println(flagsLeft);
							}
							else {
								if(flagsLeft > 0) {
									button[x][y].setText("F");
									button[x][y].setTextFill(Color.RED);
									squares[x][y].addFlag();
									flagsLeft--;
									System.out.println(flagsLeft);
								}
							}
							numFlags.setText(String.valueOf(flagsLeft));

						}
						else if(mouseEvent.getButton() == MouseButton.PRIMARY) {
							if(isFirst){
								setupGrid(x,y);
							}
							if (!squares[x][y].getHasFlag()) {
								if (squares[x][y].getHasBomb()) {
									showAll();
									animation.stop();
								} else {
									button[x][y].setDisable(true);
									if (squares[x][y].getNumBombs() == 0 && !squares[x][y].getHasBomb())
										findWhitespace(x, y);
									else
										showValue(x, y);
									if (isFirst) {
										setupGrid(x,y);
										isFirst = false;
										animation.play();
										System.out.println("Started animation");
									}
								}
							}
						}

						if(hasWon()){
							System.out.println("You won!");
							showAll();
							animation.stop();
						}
					}
				});

				gridPane.add(button[i][j],j,i);
			}
		}
	}

	private void showValue(int x, int y){
		if(!squares[x][y].getHasBomb() && !squares[x][y].getHasFlag()) {
			button[x][y].setText(squares[x][y].toString());
			button[x][y].setTextFill(colors[squares[x][y].getNumBombs()]);
			button[x][y].setDisable(true);
			squares[x][y].setDisabled(true);
		}
	}

	private void printGrid(){
		for(int i = 0; i < width; i++){
			System.out.println();
			for(int j = 0; j < height; j++){
				System.out.print(squares[i][j]);
			}
		}

	}

	private void removeAllFromGrid(){
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				gridPane.getChildren().remove(button[i][j]);
			}
		}
	}
}

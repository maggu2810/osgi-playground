
package de.maggu2810.playground.osgiplayground.tetris;

import java.util.HashMap;
import java.util.Map;

import de.maggu2810.playground.osgiplayground.tetris.data.BlockUnmodifiable;
import de.maggu2810.playground.osgiplayground.tetris.data.Board;
import de.maggu2810.playground.osgiplayground.tetris.data.ResourceProvider;
import de.maggu2810.playground.osgiplayground.tetris.data.ResourceProviderImpl;
import de.maggu2810.playground.osgiplayground.tetris.data.Tetrominoes;
import de.maggu2810.playground.osgiplayground.tetris.data.TetrominoesI;
import de.maggu2810.playground.osgiplayground.tetris.data.TetrominoesJ;
import de.maggu2810.playground.osgiplayground.tetris.data.TetrominoesL;
import de.maggu2810.playground.osgiplayground.tetris.data.TetrominoesO;
import de.maggu2810.playground.osgiplayground.tetris.data.TetrominoesS;
import de.maggu2810.playground.osgiplayground.tetris.data.TetrominoesT;
import de.maggu2810.playground.osgiplayground.tetris.data.TetrominoesZ;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 22;

    private final static int PIXEL = 30;
    private final static int MUSIC_FADE_IN_MILLIS = 400;
    private final static int MUSIC_FADE_OUT_MILLIS = 200;

    private final ResourceProvider rsc = new ResourceProviderImpl();

    private Board board;

    // Flag if the key pressed event should be handled.
    private boolean handleKeyPressedEvent;

    private boolean running, gameOver;
    private BorderPane gameOverCenter;
    private CheckBox checkGravity, checkSound, checkColor;
    private GridPane tetrisGrid;

    private int colorChoice, shapeSpeed;
    private Label subScore, subLevel, subLine, score, line, level, spacePause;
    private Map<Integer, Map<Class<? extends Tetrominoes<?>>, Color>> colors;
    private MediaPlayer mainThemePlayer, soundEffectPlayer;
    private Rectangle boardShade;
    private SequentialTransition shapeTransition;
    private StackPane stackPane;

    /**
     * Paints the stage to reflect the data in the Board class.
     */
    public void paint() {
        if (!board.getGameOver()) {

            // In case the user presses the spacebar too quickly
            if (checkSound.isSelected() && mainThemePlayer.getStatus() == MediaPlayer.Status.PAUSED) {
                playMusic();
            }

            // Updates the speed of the falling block if the speed changed
            if (shapeSpeed != board.getTimePerBlock()) {
                shapeTransition.stop();
                createTransition();
            }

            // Updates the stats about the game
            score.setText(String.valueOf(board.getScore()));
            line.setText(String.valueOf(board.getNumClearedLines()));
            level.setText(String.valueOf(board.getLevel()));

            // Gets points of fallen shapes and remove everything from the tetrisGrid
            final Map<BlockUnmodifiable, Tetrominoes<?>> points = board.getPoints();
            tetrisGrid.getChildren().clear();

            // Iterates through each cell in the tetrisGrid
            for (int i = 0; i < BOARD_HEIGHT; i++) {
                for (int j = 0; j < BOARD_WIDTH; j++) {
                    final BlockUnmodifiable currentPoint = new BlockUnmodifiable(j, i);
                    final Group cellGroup = new Group();

                    // Shapes used for each cell
                    final Rectangle square = new Rectangle(PIXEL, PIXEL);
                    final Polygon topShade = new Polygon();
                    final Polygon bottomShade = new Polygon();

                    // Shade's thickness
                    final double shadeThick = PIXEL / 7.5;

                    // If the current point was or is a part of a shape, color the square
                    if (points.containsKey(currentPoint)) {
                        // The top and left part of the shade
                        topShade.setOpacity(0.5);
                        topShade.setFill(Color.WHITE);
                        topShade.getPoints()
                                .addAll(new Double[] { 0.0, 0.0, (double) PIXEL, 0.0, PIXEL - shadeThick, shadeThick,
                                        shadeThick, shadeThick, shadeThick, PIXEL - shadeThick, 0.0, (double) PIXEL });

                        // The bottom and right part of the shade
                        bottomShade.setOpacity(0.5);
                        bottomShade.setFill(Color.BLACK);
                        bottomShade.getPoints()
                                .addAll(new Double[] { 0.0, (double) PIXEL, (double) PIXEL, (double) PIXEL,
                                        (double) PIXEL, 0.0, PIXEL - shadeThick, shadeThick, PIXEL - shadeThick,
                                        PIXEL - shadeThick, shadeThick, PIXEL - shadeThick });

                        // Sets the color of the square according to its point type
                        square.setFill(colors.get(colorChoice).get(points.get(currentPoint).getType()));

                        cellGroup.getChildren().addAll(square, topShade, bottomShade);
                    } else {
                        // The top and left part of the shade
                        topShade.setOpacity(0.1);
                        topShade.setFill(Color.BLACK);
                        topShade.getPoints()
                                .addAll(new Double[] { 0.0, 0.0, (double) PIXEL, 0.0, PIXEL - shadeThick, shadeThick,
                                        shadeThick, shadeThick, shadeThick, PIXEL - shadeThick, 0.0, (double) PIXEL });

                        // The bottom and right part of the shade
                        bottomShade.setOpacity(0.25);
                        bottomShade.setFill(Color.BLACK);
                        bottomShade.getPoints()
                                .addAll(new Double[] { 0.0, (double) PIXEL, (double) PIXEL, (double) PIXEL,
                                        (double) PIXEL, 0.0, PIXEL - shadeThick, shadeThick, PIXEL - shadeThick,
                                        PIXEL - shadeThick, shadeThick, PIXEL - shadeThick });

                        // Used to create the glossy effect
                        final Rectangle topRec = new Rectangle(PIXEL, PIXEL / 2.65);
                        topRec.setOpacity(0.05);
                        topRec.setFill(Color.WHITE);

                        final Arc halfCircle = new Arc(PIXEL / 2.0, PIXEL / 2.0, PIXEL / 2.0, PIXEL / 8.0, 0.0f,
                                180.0f);
                        halfCircle.setOpacity(0.05);
                        halfCircle.setFill(Color.WHITE);
                        halfCircle.setType(ArcType.ROUND);
                        halfCircle.setRotate(180.0);

                        // Colors the square in a sort of gradient
                        square.setFill(Color.GRAY);
                        square.setOpacity(55.0 / 62.0 - (i + 30.0) / ((double) BOARD_HEIGHT + 50));

                        cellGroup.getChildren().addAll(square, topShade, bottomShade, halfCircle, topRec);
                    }
                    tetrisGrid.add(cellGroup, j, i);
                }
            }
        } else {
            // Stops the game, the moving shape, and the music
            gameOver = true;
            running = false;
            setSceneDisable(true);
            shapeTransition.stop();
            stopMusic();

            // Play sound effect
            if (checkSound.isSelected()) {
                soundEffectPlayer.play();
            }

            // Elements of the game over screen
            final ImageView gameOverImg = new ImageView(new Image(rsc.imageGameOver().toString()));

            final Label gameOverTitle = new Label("GAME OVER");
            gameOverTitle.setTextFill(Color.WHITE);
            gameOverTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15.0));
            gameOverTitle.setTextAlignment(TextAlignment.CENTER);

            final Label gameOverSub = new Label(
                    "You made " + board.getScore() + " points\nand reached level " + board.getLevel() + "!");
            gameOverSub.setTextFill(Color.WHITE);
            gameOverSub.setFont(Font.font("Segoe UI Semilight", 13.0));
            gameOverSub.setTextAlignment(TextAlignment.CENTER);

            final VBox gameOverVbox = new VBox();
            gameOverVbox.getChildren().addAll(gameOverImg, gameOverTitle, gameOverSub);
            gameOverVbox.setAlignment(Pos.CENTER);

            gameOverCenter = new BorderPane();
            gameOverCenter.setCenter(gameOverVbox);

            stackPane.getChildren().addAll(boardShade, gameOverCenter);
        }
    }

    @Override
    public void start(final Stage primaryStage) {
        // The main layout of the game - create
        final HBox root = new HBox();

        // Sets the colors
        colorChoice = 0;
        colors = new HashMap<>();

        final Map<Class<? extends Tetrominoes<?>>, Color> color1 = new HashMap<>();
        color1.put(TetrominoesL.class, Color.ORANGE);
        color1.put(TetrominoesI.class, Color.CYAN);
        color1.put(TetrominoesT.class, Color.PURPLE);
        color1.put(TetrominoesS.class, Color.GREEN);
        color1.put(TetrominoesZ.class, Color.RED);
        color1.put(TetrominoesJ.class, Color.BLUE);
        color1.put(TetrominoesO.class, Color.YELLOW);
        colors.put(0, color1);

        final Map<Class<? extends Tetrominoes<?>>, Color> color2 = new HashMap<>();
        color2.put(TetrominoesL.class, Color.rgb(248, 121, 41));
        color2.put(TetrominoesI.class, Color.rgb(11, 165, 223));
        color2.put(TetrominoesT.class, Color.rgb(192, 58, 180));
        color2.put(TetrominoesS.class, Color.rgb(135, 212, 47));
        color2.put(TetrominoesZ.class, Color.rgb(215, 23, 53));
        color2.put(TetrominoesJ.class, Color.rgb(44, 87, 220));
        color2.put(TetrominoesO.class, Color.rgb(251, 187, 49));
        colors.put(1, color2);

        // Background music setup
        mainThemePlayer = new MediaPlayer(new Media(rsc.mediaTetris().toString()));
        mainThemePlayer.setOnEndOfMedia(() -> {
            mainThemePlayer.seek(Duration.ZERO);
        });

        // Sound effect setup
        soundEffectPlayer = new MediaPlayer(new Media(rsc.mediaGameOver().toString()));

        // Application icon
        primaryStage.getIcons().add(new Image(rsc.imageTetris().toString()));

        // Grid setup
        tetrisGrid = new GridPane();
        tetrisGrid.getStyleClass().add("grid");
        tetrisGrid.getStyleClass().add("background");

        for (int i = 0; i < BOARD_WIDTH; i++) {
            tetrisGrid.getColumnConstraints().add(new ColumnConstraints(PIXEL));
        }

        for (int i = 0; i < BOARD_HEIGHT; i++) {
            tetrisGrid.getRowConstraints().add(new RowConstraints(PIXEL));
        }

        // Bop left side of the game
        final VBox vboxBottom = new VBox();
        vboxBottom.getStyleClass().add("background");

        score = new Label();
        level = new Label();
        line = new Label();

        score.getStyleClass().add("score");
        level.getStyleClass().add("score");
        line.getStyleClass().add("score");

        subScore = new Label("score");
        subLevel = new Label("level");
        subLine = new Label("lines cleared");

        subScore.getStyleClass().add("subScore");
        subLevel.getStyleClass().add("subScore");
        subLine.getStyleClass().add("subScore");

        final VBox vboxTop = new VBox();
        vboxTop.getStyleClass().add("background");
        vboxTop.getChildren().addAll(score, subScore, level, subLevel, line, subLine);

        // Bottom left side of the game
        checkGravity = new CheckBox("gravity");
        checkGravity.setSelected(true);
        checkGravity.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                root.requestFocus();
            }
        });
        checkGravity.selectedProperty().addListener((observable, oldValue, newValue) -> {
            board.setGravity(newValue);
            checkGravity.setSelected(newValue);
        });

        checkSound = new CheckBox("music");
        checkSound.setSelected(true);
        checkSound.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                root.requestFocus();
            }
        });
        checkSound.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                playMusic();
            } else {
                stopMusic();
            }
        });

        checkColor = new CheckBox("original colors");
        checkColor.setSelected(true);
        checkColor.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                root.requestFocus();
            }
        });
        checkColor.selectedProperty().addListener((observable, oldValue, newValue) -> {
            colorChoice = (colorChoice + 1) % 2;
            paint();
        });

        spacePause = new Label("press space to pause");
        spacePause.getStyleClass().add("pause");

        vboxBottom.getChildren().addAll(checkColor, checkSound, checkGravity, spacePause);

        // Seperate layouts from the top to the bottom
        final BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("background");
        borderPane.setTop(vboxTop);
        borderPane.setBottom(vboxBottom);

        // Allows to overlays layouts for pausing the game
        stackPane = new StackPane();
        stackPane.getChildren().add(tetrisGrid);

        // The main layout of the game - setup
        root.getStyleClass().add("background");
        root.setPadding(new Insets(5.0));
        root.setSpacing(25.0);
        root.getChildren().addAll(borderPane, stackPane);

        // Elements of the pause screen
        final ImageView pauseImg = new ImageView(new Image(rsc.imagePause().toString()));

        final BorderPane pauseCenter = new BorderPane();
        pauseCenter.setCenter(pauseImg);

        boardShade = new Rectangle(BOARD_WIDTH * PIXEL + 2 * (BOARD_WIDTH - 1),
                BOARD_HEIGHT * PIXEL + 2 * (BOARD_HEIGHT - 1));
        boardShade.setOpacity(0.5);
        boardShade.setFill(Color.BLACK);
        boardShade.toFront();

        // Creates the scene
        final Scene scene = new Scene(root);
        scene.getStylesheets().add(rsc.stylesheetApplication().toString());
        scene.setOnKeyReleased(ke -> {
            handleKeyPressedEvent = true;
        });
        scene.setOnKeyPressed((ke) -> {
            if (running) {
                if (ke.getCode().equals(KeyCode.LEFT) || ke.getCode().equals(KeyCode.A)) {
                    board.moveLeft();
                    paint();
                } else if (ke.getCode().equals(KeyCode.DOWN) || ke.getCode().equals(KeyCode.S)) {
                    board.moveDown();
                    paint();
                } else if (ke.getCode().equals(KeyCode.RIGHT) || ke.getCode().equals(KeyCode.D)) {
                    board.moveRight();
                    paint();
                } else if ((ke.getCode().equals(KeyCode.UP) || ke.getCode().equals(KeyCode.W))
                        && handleKeyPressedEvent) {
                    handleKeyPressedEvent = false;
                    board.rotate();
                    paint();
                } else if (ke.getCode().equals(KeyCode.ENTER) && handleKeyPressedEvent) {
                    handleKeyPressedEvent = false;
                    while (board.moveDown()) {
                    }
                    paint();
                }
            }
            if (ke.getCode().equals(KeyCode.SPACE)) {
                if (gameOver) {
                    shapeTransition.stop();
                    startNewGame();
                    board.setGravity(checkGravity.isSelected());
                    stackPane.getChildren().removeAll(boardShade, gameOverCenter);
                } else {
                    if (running) {
                        running = false;

                        stopMusic();
                        shapeTransition.stop();
                        stackPane.getChildren().addAll(boardShade, pauseCenter);
                    } else {
                        running = true;

                        playMusic();
                        shapeTransition.play();
                        stackPane.getChildren().removeAll(boardShade, pauseCenter);
                    }
                }
                setSceneDisable(!running);
            }
        });

        startNewGame();

        // Shows the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tetris");
        primaryStage.show();
    }

    /**
     * Creates/Resets variables needed to start a new game and calls .
     */
    public void startNewGame() {
        board = new Board(BOARD_WIDTH, BOARD_HEIGHT);
        running = true;
        gameOver = false;
        handleKeyPressedEvent = true;
        shapeSpeed = board.getTimePerBlock();

        paint();
        createTransition();
        playMusic();

        soundEffectPlayer.stop();
        soundEffectPlayer.seek(Duration.ZERO);
        mainThemePlayer.seek(Duration.ZERO);
    }

    /**
     * Creates a new transition for the moving shape.
     * Mostly called to changed the speed of the moving shape.
     */
    public void createTransition() {
        shapeSpeed = board.getTimePerBlock();

        shapeTransition = new SequentialTransition();
        final PauseTransition pauseTransition = new PauseTransition(Duration.millis(shapeSpeed));
        pauseTransition.setOnFinished(evt -> {
            board.moveDown();
            paint();
        });
        shapeTransition.getChildren().add(pauseTransition);
        shapeTransition.setCycleCount(Timeline.INDEFINITE);
        shapeTransition.play();
    }

    /**
     * Plays the Tetris music with a fade in.
     */
    public void playMusic() {
        if (checkSound.isSelected()) {
            mainThemePlayer.play();
            mainThemePlayer.setVolume(0);

            final Timeline musicTimeline = new Timeline(new KeyFrame(Duration.millis(MUSIC_FADE_IN_MILLIS),
                    new KeyValue(mainThemePlayer.volumeProperty(), 1)));
            musicTimeline.play();
        }
    }

    /**
     * Stops the Tetris music with a fade out.
     */
    public void stopMusic() {
        final Timeline musicTimeline = new Timeline(new KeyFrame(Duration.millis(MUSIC_FADE_OUT_MILLIS),
                new KeyValue(mainThemePlayer.volumeProperty(), 0)));
        musicTimeline.setOnFinished(evt -> {
            mainThemePlayer.pause();
        });
        musicTimeline.play();
    }

    /**
     * Disables every element in the scene.
     *
     * <p>
     * Used to pause the game and when the game is finished.
     *
     * @param value flag if the scene should be disabled ({@code true}) or enabled ({@code false})
     */
    public void setSceneDisable(final boolean value) {
        level.setDisable(value);
        score.setDisable(value);
        line.setDisable(value);
        subLevel.setDisable(value);
        subScore.setDisable(value);
        subLine.setDisable(value);

        checkColor.setDisable(value);
        checkGravity.setDisable(value);
        checkSound.setDisable(value);

        spacePause.setDisable(value);
    }

    /**
     * Main method.
     */
    public static void main(final String[] args) {
        launch(args);
    }

}
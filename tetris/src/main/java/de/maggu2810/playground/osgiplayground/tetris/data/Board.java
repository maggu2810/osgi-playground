
package de.maggu2810.playground.osgiplayground.tetris.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * The class <b>Board</b> represents the board/grid used in Tetris.
 *
 * <p>
 * This class contains the moving tetrominoes,
 * a list of blocks of fallen tetrominoes and
 * stats about the game.
 * <p>
 * It contains the methods used for collision detection,
 * manipulating the falling tetrominoes and
 * clearing full lines
 * all while keeping up with
 * the score, level, number of cleared lines and the speed of the falling shape.
 *
 * @author Benoît Jeaurond
 */
public class Board {

    private final TetrominoesRegistryImpl tetrominoesReg = new TetrominoesRegistryImpl();

    /**
     * Constants specifying the board size
     */
    private final int width, height;

    /**
     * Keeps the state of the game
     */
    private boolean gameOver;

    /**
     * True if the gravity feature is on (gravity on blocks above cleared lines with space underneath the cleared line)
     */
    private boolean gravity;

    /**
     * Stats about the current game
     */
    private int numClearedLines, level, score, timePerBlock;

    /**
     * A list of points of the fallen shapes
     */
    private Map<BlockUnmodifiable, Tetrominoes<?>> blocks;

    /**
     * The shape that is currently moving
     */
    private Tetrominoes<?> currentShape;

    /**
     * Constructor.
     *
     * @param width the boards width
     * @param height the boards height
     */
    public Board(final int width, final int height) {
        this.width = width;
        this.height = height;

        this.blocks = new HashMap<>();
        this.gameOver = false;
        this.numClearedLines = 0;
        this.level = 0;
        this.score = 0;
        this.gravity = true;
        this.timePerBlock = 800;

        createCurrentShape();
    }

    /**
     * Creates a new shape randomly and sets it as the current shape.
     */
    public void createCurrentShape() {
        final TetrominoesCreator<?> creator = tetrominoesReg.randomCreator();

        if (currentShape != null) {
            currentShape.getBlocks().forEach(block -> blocks.put(block, currentShape));
        }

        currentShape = creator.create();
    }

    private boolean pointsContains(final BlockUnmodifiable block) {
        return blocks.containsKey(block);
    }

    /**
     * Helper method for collision detection
     *
     * @return true if there are point(s) down the current shape
     */
    private boolean hasPointsDown() {
        for (final BlockUnmodifiable i : currentShape.getBlocks()) {
            if (pointsContains(new BlockUnmodifiable(i.getX(), i.getY() + 1))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method for collision detection
     *
     * @return true if there are point(s) right of the current shape
     */
    private boolean hasPointsRight() {
        for (final BlockUnmodifiable i : currentShape.getBlocks()) {
            if (pointsContains(new BlockUnmodifiable(i.getX() + 1, i.getY()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method for collision detection
     *
     * @return true if there are point(s) left of the current shape
     */
    private boolean hasPointsLeft() {
        for (final BlockUnmodifiable i : currentShape.getBlocks()) {
            if (pointsContains(new BlockUnmodifiable(i.getX() - 1, i.getY()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method for ending the game
     *
     * @return true if the current shape is close to the top
     */
    private boolean closeToTopBorder() {
        for (final BlockUnmodifiable i : currentShape.getBlocks()) {
            if (i.getY() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method for moving the shape left
     *
     * @return true if the current shape is close to the left
     */
    private boolean closeToLeftBorder() {
        for (final BlockUnmodifiable i : currentShape.getBlocks()) {
            if (i.getX() == 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * Helper method for moving the shape right
     *
     * @return true if the current shape is close to the right
     */
    private boolean closeToRightBorder() {
        for (final BlockUnmodifiable i : currentShape.getBlocks()) {
            if (i.getX() == width - 1) {
                return true;
            }
        }

        return false;
    }

    /**
     * Helper method for moving the shape down
     *
     * @return true if the current shape is close to the bottom
     */
    private boolean closeToBottomBorder() {
        for (final BlockUnmodifiable i : currentShape.getBlocks()) {
            if (i.getY() == height - 1) {
                return true;
            }
        }

        return false;
    }

    /**
     * Helper method for rotating the current shape
     *
     * @return true if the next rotation is within the board and does not collide
     * with other shapes
     */
    private boolean canRotate() {
        final List<BlockUnmodifiable> rotated = currentShape.getRotatedBlocks();
        for (final BlockUnmodifiable i : rotated) {
            if (i.getX() >= width || i.getY() >= height || i.getX() < 0 || i.getY() < 0 || pointsContains(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Rotates the current shape
     */
    public void rotate() {
        if (canRotate()) {
            currentShape.rotate();
        }
    }

    /**
     * Moves the current shape left
     */
    public void moveLeft() {
        if (!hasPointsLeft() && !closeToLeftBorder()) {
            currentShape.moveLeft();
        }
    }

    /**
     * Moves the current shape right
     */
    public void moveRight() {
        if (!hasPointsRight() && !closeToRightBorder()) {
            currentShape.moveRight();
        }
    }

    /**
     * Moves the current shape down, checks if the game is finished and creates
     * another shape if it can't move down
     *
     * @return flag if the current shape has been moved down
     */
    public boolean moveDown() {
        if (!hasPointsDown() && !closeToBottomBorder()) {
            currentShape.moveDown();
            return true;
        } else {
            if (closeToTopBorder()) {
                gameOver = true;
            } else {
                createCurrentShape();
                removeLines();
            }
            return false;
        }
    }

    /**
     * Removes full lines (if present), updates score and level.
     */
    private void removeLines() {
        boolean gravityTriggerd;

        do {
            gravityTriggerd = false;
            final List<Integer> fullLines = new ArrayList<>(height);

            Set<BlockUnmodifiable> allPoints = getPoints().keySet();

            if (!allPoints.isEmpty()) {
                for (int i = 0; i < height; i++) {
                    boolean full = true;
                    row: for (int j = 0; j < width; j++) {
                        if (!allPoints.contains(new BlockUnmodifiable(j, i))) {
                            full = false;
                            break row;
                        }
                    }

                    if (full) {
                        fullLines.add(i);
                    }
                }
            }

            if (fullLines.size() != 0) {
                numClearedLines += fullLines.size();
                score += calculateCurrentScore(fullLines.size());

                int mostBottomLine = 0;

                for (final int i : fullLines) {
                    if (i > mostBottomLine) {
                        mostBottomLine = i;
                    }

                    final Predicate<BlockUnmodifiable> pointsPredicate = p -> p.getY() == i;
                    blocks.keySet().removeIf(pointsPredicate);

                    final Map<BlockUnmodifiable, Tetrominoes<?>> pointsNew = new HashMap<>();
                    blocks.forEach((block, tetrominoes) -> {
                        if (block.getY() < i) {
                            pointsNew.put(block.getModY(1), tetrominoes);
                        } else {
                            pointsNew.put(block, tetrominoes);
                        }
                    });
                    blocks = pointsNew;
                }

                if (mostBottomLine != height - 1 && gravity) {

                    allPoints = getPoints().keySet();

                    for (int i = 0; i < width; i++) {
                        int numOfEmpty = 0;

                        for (int j = mostBottomLine + 1; j < height; j++) {
                            if (!allPoints.contains(new BlockUnmodifiable(i, j))) {
                                numOfEmpty++;
                            } else {
                                break;
                            }
                        }

                        if (numOfEmpty != 0) {
                            gravityTriggerd = false;
                            final Map<BlockUnmodifiable, Tetrominoes<?>> pointsNew = new HashMap<>();
                            for (final Map.Entry<BlockUnmodifiable, Tetrominoes<?>> entry : blocks.entrySet()) {
                                final BlockUnmodifiable block = entry.getKey();
                                if (block.getX() == i && block.getY() <= mostBottomLine) {
                                    pointsNew.put(block.getModY(numOfEmpty), entry.getValue());
                                    gravityTriggerd = true;
                                } else {
                                    pointsNew.put(block, entry.getValue());
                                }
                            }
                            ;
                            blocks = pointsNew;
                        }
                    }
                }
            }
        } while (gravityTriggerd);

        // Based on this website
        // https://en.wikipedia.org/wiki/Tetris#Variations
        level = numClearedLines / 10;

        updateSpeed();
    }

    /**
     * Calculates the score of the current cleared lines Based on
     * http://tetris.wikia.com/wiki/Scoring
     *
     * @param num number of cleared lines at once
     * @return the score for the that number of lines
     */
    private int calculateCurrentScore(final int num) {
        int baseNum = 40;

        if (num == 2) {
            baseNum = 100;
        } else if (num == 3) {
            baseNum = 300;
        } else if (num == 4) {
            baseNum = 1000;
        }

        return baseNum * (level + 1);
    }

    /**
     * Updates the time (milliseconds) per block on screen according to the level
     * and this source
     * https://gaming.stackexchange.com/questions/13057/tetris-difficulty
     */
    private void updateSpeed() {
        final double baseFrame = 48.0;

        if (-1 < level && level < 9) {
            timePerBlock = (int) ((baseFrame - level * 5.0) / 60.0 * 1000.0);
        } else if (level == 9) {
            timePerBlock = (int) (6.0 / 60.0 * 1000.0);
        } else if (9 < level && level < 19) {
            timePerBlock = (int) ((8.0 - (13.0 - 1.0) / 3.0) / 60.0 * 1000.0);
        } else if (18 < level && level < 29) {
            timePerBlock = (int) (2.0 / 60.0 * 1000.0);
        } else {
            timePerBlock = (int) (1.0 / 60.0 * 1000.0);
        }
    }

    /**
     * Returns a list of points
     *
     * @return list containing all the points on the board (including the current shape)
     */
    public Map<BlockUnmodifiable, Tetrominoes<?>> getPoints() {
        final Map<BlockUnmodifiable, Tetrominoes<?>> points = new HashMap<>();
        points.putAll(this.blocks);
        currentShape.getBlocks().forEach(block -> points.put(block, currentShape));
        return points;
    }

    /**
     * Print the board and the value of some methods, used for testing this class
     */
    public void getStatus() {
        final StringBuffer sb = new StringBuffer();

        sb.append(toString());
        sb.append("--- Border ---\n");
        sb.append("Left " + closeToLeftBorder() + "\n");
        sb.append("Right " + closeToRightBorder() + "\n");
        sb.append("Bottom " + closeToBottomBorder() + "\n");
        sb.append("--- Points ---\n");
        sb.append("Left " + hasPointsLeft() + "\n");
        sb.append("Right " + hasPointsRight() + "\n");
        sb.append("Bottom " + hasPointsDown() + "\n");
        sb.append("--- Rotate ---\n");
        sb.append(canRotate());

        System.out.println(sb.toString());
    }

    /**
     * Getter of numClearedLines
     *
     * @return the total number of cleared lines
     */
    public int getNumClearedLines() {
        return numClearedLines;
    }

    /**
     * Getter of gravity
     *
     * @return true if gravity is on, false otherwise
     */
    public boolean getGravity() {
        return gravity;
    }

    /**
     * Setter for gravity
     *
     * @param gravity the new value of gravity
     */
    public void setGravity(final boolean gravity) {
        this.gravity = gravity;
    }

    /**
     * Getter of gameOver
     *
     * @return true if the game is finished
     */
    public boolean getGameOver() {
        return gameOver;
    }

    /**
     * Getter of level
     *
     * @return the current level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Getter of timePerBlock
     *
     * @return the time for each block to be on the screen
     */
    public int getTimePerBlock() {
        return timePerBlock;
    }

    /**
     * Getter of score
     *
     * @return the score of the current game
     */
    public int getScore() {
        return score;
    }

}
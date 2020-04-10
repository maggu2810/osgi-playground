
package de.maggu2810.playground.osgiplayground.tetris.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Base class for the specific tetrominoes implementations.
 * <p>
 * {@link "http://tetris.wikia.com/wiki/SRS"}
 *
 * @param <T> the specific tetrominoes type
 */
public class TetrominoesBase<T extends Tetrominoes<T>> implements Tetrominoes<T> {

    public interface Rotator {
        void rotate(int rotation, int lowX, int lowY, List<BlockModifiable> blocks);
    }

    private final Rotator rotator;

    private final Class<T> type;

    /**
     * Conatins the current rotation orientation (an integer from 0 to 3)
     */
    private int rotation;

    /**
     * List containing the points of the shape
     */
    private final List<BlockModifiable> blocks;

    /**
     * Constructor.
     *
     * @param type the specific tetrominoes type
     */
    public TetrominoesBase(final Class<T> type, final Rotator rotator, final List<? extends @NonNull Block> blocks) {
        this(type, rotator, 0, blocks);
    }

    private TetrominoesBase(final Class<T> type, final Rotator rotator, final int rotation,
            final List<? extends @NonNull Block> blocks) {
        this.type = type;
        this.rotator = rotator;
        this.rotation = rotation;
        this.blocks = new ArrayList<>(blocks.size());
        for (final Block block : blocks) {
            this.blocks.add(new BlockModifiable(block.getX(), block.getY()));
        }
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    private TetrominoesBase<T> duplicate() {
        return new TetrominoesBase<>(type, rotator, rotation, blocks);
    }

    // ----

    /**
     * Moves the shape down one spot
     */
    @Override
    public void moveDown() {
        blocks.forEach(block -> block.modY(1));
    }

    /**
     * Moves the shape left one spot
     */
    @Override
    public void moveLeft() {
        blocks.forEach(block -> block.modX(-1));
    }

    /**
     * Moves the shape right one spot
     */
    @Override
    public void moveRight() {
        blocks.forEach(block -> block.modX(1));
    }

    /**
     * Rotates the shape to the right
     *
     * The algorithm used is based on http://tetris.wikia.com/wiki/SRS
     */
    @Override
    public void rotate() {
        int lowX = 100;
        int lowY = 100;

        for (final Block i : blocks) {
            if (i.getX() < lowX) {
                lowX = i.getX();
            }

            if (i.getY() < lowY) {
                lowY = i.getY();
            }
        }

        rotator.rotate(rotation, lowX, lowY, blocks);
        rotation = (rotation + 1) % 4;
    }

    /**
     * Getter for the points of the next rotation of the shape
     *
     * @return list of rotated points
     */
    @Override
    public List<BlockUnmodifiable> getRotatedBlocks() {
        final TetrominoesBase<T> rotated = duplicate();
        rotated.rotate();
        return rotated.blocks.stream().map(BlockUnmodifiable::new).collect(Collectors.toList());
    }

    /**
     * Getter for points
     *
     * @return list of points
     */
    @Override
    public List<BlockUnmodifiable> getBlocks() {
        return blocks.stream().map(BlockUnmodifiable::new).collect(Collectors.toList());
    }

}

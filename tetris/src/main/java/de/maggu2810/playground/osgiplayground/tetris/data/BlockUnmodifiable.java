
package de.maggu2810.playground.osgiplayground.tetris.data;

/**
 * A square block that state / position is not modifiable.
 */
public class BlockUnmodifiable implements Block {

    /**
     * Coordinates of the block
     */
    private final int x, y;

    /**
     * Constructor.
     *
     * @param block the block that acts as source
     */
    public BlockUnmodifiable(final Block block) {
        this(block.getX(), block.getY());
    }

    /**
     * Constructor.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public BlockUnmodifiable(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    /**
     * Creates a block that y coordinate is modified.
     *
     * @param mod the value to be added to the y coordinate
     * @return a new unmodifiable block that contains the current x and the modified y value
     */
    public BlockUnmodifiable getModY(final int mod) {
        return new BlockUnmodifiable(x, y + mod);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BlockUnmodifiable other = (BlockUnmodifiable) obj;
        if (x != other.x) {
            return false;
        }
        if (y != other.y) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BlockUnmodifiable [x=" + x + ", y=" + y + "]";
    }

}

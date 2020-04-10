
package de.maggu2810.playground.osgiplayground.tetris.data;

/**
 * A square block that state / position is modifiable.
 */
public class BlockModifiable implements Block {

    /**
     * Coordinates of the block
     */
    private int x, y;

    /**
     * Constructor.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public BlockModifiable(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the location.
     *
     * @param x the new x coordinate
     * @param y the new y coordinate
     */
    public void setLocation(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the x coordinate.
     *
     * @param x the new x coordinate
     */
    public void setX(final int x) {
        this.x = x;
    }

    @Override
    public int getX() {
        return x;
    }

    /**
     * Modify the value of the x coordinate
     *
     * @param mod the value to be added to the x coordinate
     */
    public void modX(final int mod) {
        x += mod;
    }

    /**
     * Sets the y coordinate.
     *
     * @param y the new y coordinate
     */
    public void setY(final int y) {
        this.y = y;
    }

    @Override
    public int getY() {
        return y;
    }

    /**
     * Modify the value of the y coordinate
     *
     * @param mod the value to be added to the y coordinate
     */
    public void modY(final int mod) {
        y += mod;
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
        final BlockModifiable other = (BlockModifiable) obj;
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
        return "BlockModifiable [x=" + x + ", y=" + y + "]";
    }

}

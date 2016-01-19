package com.walrusone.skywars.utilities;

public class EmptyChest {
    private final int x;
    private final int z;
    private final int y;
    private boolean dble;

    public EmptyChest(int x, int y, int z, boolean dble) {
        this.x = x;
        this.z = z;
        this.y = y;
        this.dble = dble;
    }

    public EmptyChest(int x, int y, int z) {
        this(x, y, z, false);
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public int getY() {
        return y;
    }

    public boolean isDble() {
        return dble;
    }

    public void setDble(boolean dble) {
        this.dble = dble;
    }

}

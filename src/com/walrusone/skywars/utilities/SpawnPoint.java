package com.walrusone.skywars.utilities;

class SpawnPoint {
    private final double x;
    private final double z;
    private final double y;

    public SpawnPoint(double x2, double y2, double z2) {
        this.x = x2;
        this.z = z2;
        this.y = y2;
    }

    public Double getX() {
        return x;
    }

    public Double getZ() {
        return z;
    }

    public Double getY() {
        return y;
    }

}

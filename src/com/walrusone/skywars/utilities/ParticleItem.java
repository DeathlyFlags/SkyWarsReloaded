package com.walrusone.skywars.utilities;


public class ParticleItem {

    private final int cost;
    private final String effect;
    private final String name;

    public ParticleItem(String effect, String name, int cost) {
        this.cost = cost;
        this.effect = effect;
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public String getEffect() {
        return effect;
    }

    public String getName() {
        return name;
    }
}

package com.mygdx.game;

import java.io.Serializable;
import java.util.List;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Birds> birds;
    private List<Pig> pigs;
    private List<Block> blocks;
    private List<Birds> remainingBirds;
    private int level;
    private float slingshotX;
    private float slingshotY;

    // Constructor
    public GameState(List<Birds> birds, List<Pig> pigs, List<Block> blocks, List<Birds> remainingBirds, int level, float slingshotX, float slingshotY) {
        this.birds = birds;
        this.pigs = pigs;
        this.blocks = blocks;
        this.remainingBirds = remainingBirds;
        this.level = level;
        this.slingshotX = slingshotX;
        this.slingshotY = slingshotY;
    }

    // Getters
    public List<Birds> getBirds() {
        return birds;
    }

    public List<Pig> getPigs() {
        return pigs;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public List<Birds> getRemainingBirds() {
        return remainingBirds;
    }

    public int getLevel() {
        return level;
    }

    public float getSlingshotX() {
        return slingshotX;
    }

    public float getSlingshotY() {
        return slingshotY;
    }

    // Capture the current game state
    public void captureGameState(List<Birds> birds, List<Pig> pigs, List<Block> blocks, List<Birds> remainingBirds, int level, float slingshotX, float slingshotY) {
        this.birds = birds;
        this.pigs = pigs;
        this.blocks = blocks;
        this.remainingBirds = remainingBirds;
        this.level = level;
        this.slingshotX = slingshotX;
        this.slingshotY = slingshotY;
    }
}

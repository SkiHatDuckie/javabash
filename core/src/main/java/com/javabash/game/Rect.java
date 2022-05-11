package com.javabash.game;

import com.badlogic.gdx.graphics.Color;

public class Rect {
    float x;
    float y;
    float width;
    float height;
    Color color;

    public Rect(float x, float y, float width, float height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }
}

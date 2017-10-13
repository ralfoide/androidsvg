package com.caverock.androidsvg;

import android.graphics.RectF;

class Box implements Cloneable {
    float minX, minY, width, height;

    Box(float minX, float minY, float width, float height) {
        this.minX = minX;
        this.minY = minY;
        this.width = width;
        this.height = height;
    }

    Box(Box copy) {
        this.minX = copy.minX;
        this.minY = copy.minY;
        this.width = copy.width;
        this.height = copy.height;
    }

    static Box fromLimits(float minX, float minY, float maxX, float maxY) {
        return new Box(minX, minY, maxX - minX, maxY - minY);
    }

    RectF toRectF() {
        return new RectF(minX, minY, maxX(), maxY());
    }

    float maxX() {
        return minX + width;
    }

    float maxY() {
        return minY + height;
    }

    void union(Box other) {
        if (other.minX < minX) minX = other.minX;
        if (other.minY < minY) minY = other.minY;
        if (other.maxX() > maxX()) width = other.maxX() - minX;
        if (other.maxY() > maxY()) height = other.maxY() - minY;
    }

    public String toString() {
        return "[" + minX + " " + minY + " " + width + " " + height + "]";
    }
}

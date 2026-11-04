package com.spiremod.spec;

/** Box faces, as seen from OUTSIDE the box. FRONT = +z (entity forward), LEFT = +x (entity's left). */
public enum Face {
    FRONT, BACK, RIGHT, LEFT, TOP, BOTTOM;
    /** face-local pixel width/height for a box of size dx,dy,dz */
    public int w(int dx, int dy, int dz) { return this == FRONT || this == BACK || this == TOP || this == BOTTOM ? dx : dz; }
    public int h(int dx, int dy, int dz) { return this == TOP || this == BOTTOM ? dz : dy; }
    /** texture-region origin (u,v) relative to the box texOffs */
    public int u(int dx, int dy, int dz) {
        switch (this) {
            case RIGHT: return 0;
            case FRONT: case TOP: return dz;
            case LEFT: case BOTTOM: return dz + dx;
            default: return 2 * dz + dx; // BACK
        }
    }
    public int v(int dx, int dy, int dz) { return this == TOP || this == BOTTOM ? 0 : dz; }
}

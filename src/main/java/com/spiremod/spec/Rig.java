package com.spiremod.spec;

/** Handed to ModelSpec.proc(...) so code can add procedural motion on top of clips. All adds are offsets from rest. */
public interface Rig {
    /** add rotation (deg) to part */
    void rot(String part, float rx, float ry, float rz);
    /** add position offset (pixels) */
    void pos(String part, float x, float y, float z);
    /** multiply scale */
    void scl(String part, float sx, float sy, float sz);
    /** show/hide a part (and its children) */
    void show(String part, boolean visible);
    boolean has(String part);
}

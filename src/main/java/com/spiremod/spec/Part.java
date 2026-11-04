package com.spiremod.spec;

import java.util.ArrayList;
import java.util.List;

/** A rigid-body node: pivot + boxes + child parts. All values in DSL pixels / degrees (see Box doc). */
public final class Part {
    public final String name;
    public final Part parent;
    final ModelSpec spec;
    public float x, y, z;       // pivot, relative to parent pivot (for top-level parts: relative to ground centre)
    public float rx, ry, rz;    // rest rotation in degrees (right-handed in the DSL frame)
    public float sx = 1, sy = 1, sz = 1;
    public final List<Box> boxes = new ArrayList<>();
    public final List<Part> kids = new ArrayList<>();
    public int index;

    Part(ModelSpec spec, Part parent, String name) { this.spec = spec; this.parent = parent; this.name = name; }

    /** add a child part */
    public Part add(String childName) { return spec.newPart(this, childName); }
    /** pivot position relative to parent */
    public Part at(float x, float y, float z) { this.x = x; this.y = y; this.z = z; return this; }
    /** rest rotation (deg). rx+: top tips forward / hanging limb swings back; ry+: turn left; rz+: roll right */
    public Part rot(float rx, float ry, float rz) { this.rx = rx; this.ry = ry; this.rz = rz; return this; }
    public Part scale(float s) { sx = sy = sz = s; return this; }
    public Part scale(float sx, float sy, float sz) { this.sx = sx; this.sy = sy; this.sz = sz; return this; }
    /** add a cuboid; min corner (x0,y0,z0) relative to pivot, integer size. */
    public Box box(float x0, float y0, float z0, int dx, int dy, int dz) {
        Box b = new Box(x0, y0, z0, dx, dy, dz); boxes.add(b); return b;
    }
    /** centred horizontally on pivot: box spans x -dx/2..dx/2, z -dz/2..dz/2, y from y0 up */
    public Box cbox(float y0, int dx, int dy, int dz) { return box(-dx / 2f, y0, -dz / 2f, dx, dy, dz); }
}

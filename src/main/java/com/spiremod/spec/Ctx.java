package com.spiremod.spec;

/** Per-frame animation inputs (filled from the entity in game, or by Preview). All times in ticks. */
public final class Ctx {
    public float age;            // ageInTicks incl. partial
    public float swing, swingAmt;// walk limbSwing / limbSwingAmount (0..1)
    public float headYaw, headPitch; // degrees, relative to body
    public String base = "idle"; // looping base clip (entity "stance": idle/sleep/curl...)
    public String move;          // active move clip or null
    public float moveT;          // ticks since the move started
    public String fade;          // clip that just finished (blends back to idle)
    public float fadeT;
    public float hurtT = -1;     // ticks since last hurt, <0 = none
    public float deathT = -1;    // ticks since death began, <0 alive
    public float spawnT = -1;    // ticks since spawn, <0 none
    public float hpFrac = 1;
    public int phase;            // boss phase / form
    public int var;              // free entity variable (stacks, counters)
    public float partial;
}

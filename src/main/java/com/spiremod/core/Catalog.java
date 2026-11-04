package com.spiremod.core;

import com.spiremod.mobs.AllMobs;

import java.util.*;

/** All creature definitions, in registration order. */
public final class Catalog {
    private Catalog() {}
    public static final List<MobDef> ALL = new ArrayList<>();
    private static final Map<String, MobDef> BY_ID = new LinkedHashMap<>();
    private static boolean loaded;

    public static synchronized void init() {
        if (loaded) return; loaded = true;
        AllMobs.collect(d -> { if (BY_ID.putIfAbsent(d.id, d) != null) throw new IllegalStateException("duplicate mob id " + d.id); ALL.add(d); });
    }
    public static MobDef get(String id) { return BY_ID.get(id); }
}

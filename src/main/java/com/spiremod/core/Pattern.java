package com.spiremod.core;

import java.util.ArrayList;
import java.util.List;

/** Chooses the id of the next move. Use the builders or a lambda: s -> "chomp". */
@FunctionalInterface
public interface Pattern {
    String next(SpireMob self);

    /** fixed repeating cycle */
    static Pattern cycle(String... ids) { return s -> ids[s.getTurn() % ids.length]; }
    /** play ids once in order, then repeat the last `loopFrom..end` section */
    static Pattern seq(int loopFrom, String... ids) {
        return s -> { int t = s.getTurn(); return ids[t < ids.length ? t : loopFrom + (t - ids.length) % (ids.length - loopFrom)]; };
    }
    /** first move fixed, then another pattern */
    static Pattern first(String id, Pattern then) { return s -> s.getTurn() == 0 ? id : then.next(s); }
    static Random random() { return new Random(); }

    /** StS-style weighted random with consecutive-use limits and first-turn exclusions */
    final class Random implements Pattern {
        private final List<String> ids = new ArrayList<>(); private final List<Integer> w = new ArrayList<>();
        private final java.util.Map<String, Integer> maxRepeat = new java.util.HashMap<>();
        private final java.util.Set<String> notFirst = new java.util.HashSet<>();
        private final java.util.Map<String, String> notAfter = new java.util.HashMap<>();
        public Random add(String id, int weight) { ids.add(id); w.add(weight); return this; }
        /** id may be used at most n times in a row */
        public Random maxRepeat(String id, int n) { maxRepeat.put(id, n); return this; }
        public Random notFirst(String id) { notFirst.add(id); return this; }
        /** id may not directly follow `prev` */
        public Random notAfter(String id, String prev) { notAfter.put(id, prev); return this; }
        public String next(SpireMob s) {
            int total = 0; int[] eff = new int[ids.size()];
            for (int i = 0; i < ids.size(); i++) {
                String id = ids.get(i); int wt = w.get(i);
                if (maxRepeat.containsKey(id) && s.repeatCount(id) >= maxRepeat.get(id)) wt = 0;
                if (s.getTurn() == 0 && notFirst.contains(id)) wt = 0;
                if (notAfter.containsKey(id) && notAfter.get(id).equals(s.lastMove())) wt = 0;
                eff[i] = wt; total += wt;
            }
            if (total <= 0) return ids.get(0);
            int r = s.rand().nextInt(total);
            for (int i = 0; i < eff.length; i++) { r -= eff[i]; if (r < 0) return ids.get(i); }
            return ids.get(0);
        }
    }
}

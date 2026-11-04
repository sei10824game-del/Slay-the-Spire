package com.spiremod.core;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

/** Runs the StS turn loop: plan next move (intent), approach, play the move script. */
public class SpireMoveGoal extends Goal {
    private final SpireMob m;
    public SpireMoveGoal(SpireMob m) { this.m = m; setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK)); }

    @Override public boolean canUse() { LivingEntity t = m.getTarget(); return t != null && t.isAlive(); }
    @Override public boolean canContinueToUse() { return canUse(); }
    @Override public boolean isInterruptable() { return false; }

    @Override public void stop() {
        m.getNavigation().stop();
        if (m.current != null) m.cancelMove();
    }

    private void plan() {
        MobDef d = m.def;
        String id = d.pattern != null ? d.pattern.next(m) : d.moves.keySet().iterator().next();
        Move mv = d.moves.get(id);
        if (mv == null) mv = d.moves.values().iterator().next();
        m.planned = mv; m.refreshIntent();
    }

    private void face(LivingEntity t, float step) {
        double dx = t.getX() - m.getX(), dz = t.getZ() - m.getZ();
        float yaw = (float) (Mth.atan2(dz, dx) * 57.29578) - 90f;
        m.setYRot(Mth.rotLerp(0.5f, m.getYRot(), Mth.approachDegrees(m.getYRot(), yaw, step)));
        m.yBodyRot = m.getYRot(); m.setYHeadRot(m.getYRot());
        m.getLookControl().setLookAt(t, 30f, 30f);
    }

    @Override public void tick() {
        LivingEntity t = m.getTarget();
        if (t == null) return;
        if (m.isStunned()) { m.getNavigation().stop(); return; }
        if (m.current == null) {
            if (m.planned == null) plan();
            Move mv = m.planned;
            double dist = Math.sqrt(m.distanceToSqr(t.getX(), t.getY(), t.getZ()));
            double horiz = Math.sqrt((t.getX() - m.getX()) * (t.getX() - m.getX()) + (t.getZ() - m.getZ()) * (t.getZ() - m.getZ()));
            double useDist = mv.reach > 6 ? dist : Math.max(horiz, dist * 0.8);
            boolean inReach = !mv.needsTarget || (useDist <= mv.reach + t.getBbWidth() * 0.5 + m.getBbWidth() * 0.5 && (!mv.needsLos || m.hasLineOfSight(t)));
            if (mv.needsTarget && !inReach) {
                if (m.def.flying) m.getMoveControl().setWantedPosition(t.getX(), t.getY() + 1.0, t.getZ(), 1.0);
                else m.getNavigation().moveTo(t, 1.0);
                m.getLookControl().setLookAt(t, 30f, 30f);
            } else if (mv.needsTarget) {
                m.getNavigation().stop(); face(t, 40f);
            }
            if (m.cooldown > 0) { m.cooldown--; return; }
            if (inReach) start(mv, t);
            return;
        }
        Move mv = m.current;
        if (mv.stationary) m.getNavigation().stop();
        if (mv.faceTarget && (mv.faceUntil < 0 || m.moveTick <= mv.faceUntil)) face(t, 22f);
        for (Move.Timed a : mv.actions) if (a.tick == m.moveTick) a.act.run(m, t);
        m.moveTick++;
        if (m.moveTick >= mv.length) finish();
    }

    private void start(Move mv, LivingEntity t) {
        m.current = mv; m.moveTick = 0; m.planned = null;
        if (!m.def.keepBlock) m.resetBlock();
        m.clearIntent();
        m.setAnimClip(mv.anim);
        if (mv.onStart != null) mv.onStart.run(m, t);
    }

    private void finish() {
        Move mv = m.current;
        m.recordMove(mv.id);
        m.current = null; m.setAnimClip("");
        m.cooldown = Cfg.MOVE_COOLDOWN;
        if (m.def.onMoveEnd != null) m.def.onMoveEnd.accept(m);
        if (m.planned == null) plan();
    }
}

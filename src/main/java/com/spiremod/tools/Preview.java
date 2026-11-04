package com.spiremod.tools;

import com.spiremod.spec.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * Software preview of a ModelSpec (no Minecraft needed).
 *   java com.spiremod.tools.Preview <fq.ClassName> out.png [clip=idle] [times=0,10,20] [views=f34,side,back] [w=0.6 h=1.8] [swing=0 amt=0] [age=0]
 * The class must expose `public static ModelSpec model()`.
 * clip: base clip name for "idle"-like loops, or a move clip (then times = ticks since move start).
 * Special clip names: death, hurt, spawn. A grey box shows the hitbox (w,h in blocks); ground grid squares = 1 block.
 */
public final class Preview {
    static int S = 280; // frame size

    public static void main(String[] a) throws Exception {
        Class<?> cls = Class.forName(a[0]);
        ModelSpec spec = (ModelSpec) cls.getMethod("model").invoke(null);
        render(spec, a[1], Arrays.copyOfRange(a, 2, a.length));
    }

    public static void render(ModelSpec spec, String out, String[] opt) throws Exception {
        Map<String, String> o = new HashMap<>();
        for (String s : opt) { int i = s.indexOf('='); if (i > 0) o.put(s.substring(0, i), s.substring(i + 1)); }
        String clip = o.getOrDefault("clip", "idle");
        String[] ts = o.getOrDefault("times", "0,10,20").split(",");
        String[] views = o.getOrDefault("views", "f34").split(",");
        float hw = Float.parseFloat(o.getOrDefault("w", "0.6")), hh = Float.parseFloat(o.getOrDefault("h", "1.8"));
        if (o.containsKey("size")) S = Integer.parseInt(o.get("size"));
        TexBuilder tex = TexBuilder.build(spec);
        Posing po = new Posing(spec);
        BufferedImage sheet = new BufferedImage(S * ts.length, S * views.length, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = sheet.createGraphics();
        g.setColor(new Color(0x2b2e36)); g.fillRect(0, 0, sheet.getWidth(), sheet.getHeight());
        float maxExtent = Math.max(hh * 16f * spec.scale, 28f) * 1.15f;
        for (int vi = 0; vi < views.length; vi++) for (int ti = 0; ti < ts.length; ti++) {
            float t = Float.parseFloat(ts[ti]);
            Ctx c = new Ctx(); c.age = Float.parseFloat(o.getOrDefault("age", "0")) + (clip.equals("idle") || spec.clips.containsKey(clip) && spec.clips.get(clip).loop ? t : 0);
            c.swing = Float.parseFloat(o.getOrDefault("swing", "0")) + (o.containsKey("walk") ? t * 0.8f : 0);
            c.swingAmt = Float.parseFloat(o.getOrDefault("amt", o.containsKey("walk") ? "1" : "0"));
            if (clip.equals("death")) c.deathT = t; else if (clip.equals("hurt")) c.hurtT = t; else if (clip.equals("spawn")) c.spawnT = t;
            else if (!clip.equals("idle")) {
                Clip k = spec.clips.get(clip);
                if (k == null) throw new IllegalArgumentException("no clip " + clip + "; have " + spec.clips.keySet());
                if (k.loop) c.base = clip; else { c.move = clip; c.moveT = t; }
            }
            c.hpFrac = Float.parseFloat(o.getOrDefault("hp", "1")); c.phase = Integer.parseInt(o.getOrDefault("phase", "0"));
            c.var = Integer.parseInt(o.getOrDefault("var", "0"));
            po.eval(c);
            BufferedImage fr = new BufferedImage(S, S, BufferedImage.TYPE_INT_ARGB);
            drawFrame(fr, spec, po, tex, views[vi], maxExtent, hw, hh);
            g.drawImage(fr, ti * S, vi * S, null);
            g.setColor(Color.WHITE); g.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g.drawString(clip + " t=" + (int) t + " " + views[vi], ti * S + 6, vi * S + 14);
        }
        g.dispose();
        File f = new File(out); if (f.getParentFile() != null) f.getParentFile().mkdirs();
        ImageIO.write(sheet, "png", f);
        System.out.println("wrote " + out + "  clips=" + spec.clips.keySet() + " tex=" + tex.w + "x" + tex.h);
    }

    // ---- tiny matrix math (MC model space: x left, y DOWN, z back; entity faces -z) ----
    static double[] ident() { return new double[]{1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,0,1}; }
    static double[] mul(double[] a, double[] b) {
        double[] r = new double[16];
        for (int i = 0; i < 4; i++) for (int j = 0; j < 4; j++) { double s = 0; for (int k = 0; k < 4; k++) s += a[i*4+k] * b[k*4+j]; r[i*4+j] = s; }
        return r;
    }
    static double[] trans(double x, double y, double z) { double[] m = ident(); m[3] = x; m[7] = y; m[11] = z; return m; }
    static double[] scl(double x, double y, double z) { double[] m = ident(); m[0] = x; m[5] = y; m[10] = z; return m; }
    static double[] rx(double a) { double c = Math.cos(a), s = Math.sin(a); return new double[]{1,0,0,0, 0,c,-s,0, 0,s,c,0, 0,0,0,1}; }
    static double[] ry(double a) { double c = Math.cos(a), s = Math.sin(a); return new double[]{c,0,s,0, 0,1,0,0, -s,0,c,0, 0,0,0,1}; }
    static double[] rz(double a) { double c = Math.cos(a), s = Math.sin(a); return new double[]{c,-s,0,0, s,c,0,0, 0,0,1,0, 0,0,0,1}; }
    static double[] ap(double[] m, double x, double y, double z) {
        return new double[]{m[0]*x+m[1]*y+m[2]*z+m[3], m[4]*x+m[5]*y+m[6]*z+m[7], m[8]*x+m[9]*y+m[10]*z+m[11]};
    }

    static void drawFrame(BufferedImage img, ModelSpec spec, Posing po, TexBuilder tex, String view, float maxExtent, float hw, float hh) {
        int W = img.getWidth(), H = img.getHeight();
        double yaw, pitch = 18;
        switch (view) { case "side": yaw = 90; pitch = 8; break; case "back": yaw = 180; break; case "top": yaw = 0; pitch = 80; break; case "front": yaw = 0; pitch = 8; break;
            case "b34": yaw = 150; break; default: yaw = -35; }
        double scale = (H * 0.82) / maxExtent;
        double[] world = ident();
        // entity space (MC): model origin at y=24 is ground; flip so y up in view
        float[] zbuf = new float[W * H]; Arrays.fill(zbuf, -1e9f);
        double cy = Math.cos(Math.toRadians(yaw)), sy = Math.sin(Math.toRadians(yaw)), cp = Math.cos(Math.toRadians(pitch)), sp = Math.sin(Math.toRadians(pitch));
        double[][] mats = new double[spec.parts.size()][];
        boolean[] shown = new boolean[spec.parts.size()];
        for (Part p : spec.parts) {
            double[] parent = p.parent == spec.root ? trans(0, 24, 0) : mats[p.parent.index];
            boolean pv = p.parent == spec.root || shown[p.parent.index];
            int i = p.index;
            shown[i] = pv && po.vis[i];
            double px = p.x + po.ox[i], py = p.y + po.oy[i], pz = p.z + po.oz[i];
            double[] m = mul(parent, trans(px, -py, -pz));
            double rxv = Math.toRadians(p.rx + po.orx[i]), ryv = -Math.toRadians(p.ry + po.ory[i]), rzv = -Math.toRadians(p.rz + po.orz[i]);
            m = mul(m, mul(rz(rzv), mul(ry(ryv), rx(rxv))));
            m = mul(m, scl(p.sx * po.osx[i], p.sy * po.osy[i], p.sz * po.osz[i]));
            mats[i] = m;
        }
        double gs = spec.scale;
        // ground grid + hitbox
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int k = -4; k <= 4; k++) { for (int axis = 0; axis < 2; axis++) {
            double[] a = axis == 0 ? new double[]{k*16, 0, -64} : new double[]{-64, 0, k*16};
            double[] b = axis == 0 ? new double[]{k*16, 0, 64} : new double[]{64, 0, k*16};
            int[] pa = proj(a, cy, sy, cp, sp, scale, W, H), pb = proj(b, cy, sy, cp, sp, scale, W, H);
            g.setColor(new Color(255,255,255,28)); g.drawLine(pa[0], pa[1], pb[0], pb[1]); } }
        g.setColor(new Color(120, 200, 255, 120));
        double bw = hw * 8, bh = hh * 16;
        double[][] bc = {{-bw,0,-bw},{bw,0,-bw},{bw,0,bw},{-bw,0,bw}};
        for (int k = 0; k < 4; k++) {
            double[] a = bc[k], b = bc[(k+1)%4];
            int[] pa = proj(a, cy, sy, cp, sp, scale, W, H), pb = proj(b, cy, sy, cp, sp, scale, W, H); g.drawLine(pa[0], pa[1], pb[0], pb[1]);
            int[] pa2 = proj(new double[]{a[0], -bh, a[2]}, cy, sy, cp, sp, scale, W, H), pb2 = proj(new double[]{b[0], -bh, b[2]}, cy, sy, cp, sp, scale, W, H);
            g.drawLine(pa2[0], pa2[1], pb2[0], pb2[1]); g.drawLine(pa[0], pa[1], pa2[0], pa2[1]);
        }
        g.dispose();
        double[] light = norm(new double[]{-0.4, -0.8, -0.5});
        for (Part p : spec.parts) {
            if (!shown[p.index]) continue;
            double[] m = mul(scl(gs, gs, gs), mats[p.index]);
            // (global scale applied about ground origin: matrices include trans(0,24,0); scale about y=24 )
            m = mul(trans(0, 24, 0), mul(scl(gs, gs, gs), mul(trans(0, -24, 0), mats[p.index])));
            for (Box b : p.boxes) for (Face f : Face.values()) {
                float ig = b.inflate;
                float[][] cs = new float[4][];
                float[][] uvs = {{0,0},{1,0},{1,1},{0,1}};
                double[][] wp = new double[4][];
                for (int k = 0; k < 4; k++) {
                    float[] c = b.corner(f, uvs[k][0], uvs[k][1]);
                    float[] n = Box.normal(f);
                    double lx = c[0] + n[0]*ig, ly = c[1] + n[1]*ig, lz = c[2] + n[2]*ig;
                    // DSL -> MC local
                    double[] w = ap(m, lx, -ly, -lz);
                    wp[k] = w;
                }
                float[] nn = Box.normal(f);
                double[] wn = ap(m, nn[0], -nn[1], -nn[2]); double[] w0 = ap(m, 0, 0, 0);
                double[] nrm = norm(new double[]{wn[0]-w0[0], wn[1]-w0[1], wn[2]-w0[2]});
                double lam = Math.max(0, -(nrm[0]*light[0] + nrm[1]*light[1] + nrm[2]*light[2]));
                double sh = 0.55 + 0.45 * lam;
                int fw = f.w(b.dx, b.dy, b.dz), fh = f.h(b.dx, b.dy, b.dz);
                int tu = b.u + f.u(b.dx, b.dy, b.dz), tv = b.v + f.v(b.dx, b.dy, b.dz);
                int[][] sp2 = new int[4][]; double[] zs = new double[4];
                for (int k = 0; k < 4; k++) { double[] w = wp[k]; sp2[k] = proj(new double[]{w[0], w[1] - 24, w[2]}, cy, sy, cp, sp, scale, W, H); zs[k] = depth(new double[]{w[0], w[1]-24, w[2]}, cy, sy, cp, sp); }
                tri(img, zbuf, sp2, zs, uvs, tex, tu, tv, fw, fh, sh, 0, 1, 2); tri(img, zbuf, sp2, zs, uvs, tex, tu, tv, fw, fh, sh, 0, 2, 3);
            }
        }
    }

    static double[] norm(double[] v) { double l = Math.sqrt(v[0]*v[0]+v[1]*v[1]+v[2]*v[2]); return new double[]{v[0]/l, v[1]/l, v[2]/l}; }
    // view space for yaw=0 (front view): X = MC x (entity left appears on viewer's right), Y up, Zf away from viewer
    static double[] view(double[] p, double cy, double sy) {
        double X = p[0], Y = -p[1], Zf = p[2];
        return new double[]{X * cy + Zf * sy, Y, -X * sy + Zf * cy};
    }
    static int[] proj(double[] p, double cy, double sy, double cp, double sp, double scale, int W, int H) {
        double[] v = view(p, cy, sy);
        double up = v[1] * cp + v[2] * sp;
        return new int[]{(int) Math.round(W / 2.0 + v[0] * scale), (int) Math.round(H * 0.88 - up * scale)};
    }
    static double depth(double[] p, double cy, double sy, double cp, double sp) {
        double[] v = view(p, cy, sy);
        return v[2] * cp - v[1] * sp; // smaller = nearer
    }

    static void tri(BufferedImage img, float[] zb, int[][] s, double[] z, float[][] uv, TexBuilder tex, int tu, int tv, int fw, int fh, double sh, int i0, int i1, int i2) {
        int W = img.getWidth(), H = img.getHeight();
        int minX = Math.max(0, Math.min(s[i0][0], Math.min(s[i1][0], s[i2][0]))), maxX = Math.min(W - 1, Math.max(s[i0][0], Math.max(s[i1][0], s[i2][0])));
        int minY = Math.max(0, Math.min(s[i0][1], Math.min(s[i1][1], s[i2][1]))), maxY = Math.min(H - 1, Math.max(s[i0][1], Math.max(s[i1][1], s[i2][1])));
        double x0 = s[i0][0], y0 = s[i0][1], x1 = s[i1][0], y1 = s[i1][1], x2 = s[i2][0], y2 = s[i2][1];
        double den = (y1 - y2) * (x0 - x2) + (x2 - x1) * (y0 - y2);
        if (Math.abs(den) < 1e-9) return;
        for (int y = minY; y <= maxY; y++) for (int x = minX; x <= maxX; x++) {
            double l0 = ((y1 - y2) * (x + .5 - x2) + (x2 - x1) * (y + .5 - y2)) / den;
            double l1 = ((y2 - y0) * (x + .5 - x2) + (x0 - x2) * (y + .5 - y2)) / den;
            double l2 = 1 - l0 - l1;
            if (l0 < -1e-4 || l1 < -1e-4 || l2 < -1e-4) continue;
            double d = -(z[i0] * l0 + z[i1] * l1 + z[i2] * l2);
            if (d <= zb[y * W + x]) continue;
            double u = uv[i0][0] * l0 + uv[i1][0] * l1 + uv[i2][0] * l2, v = uv[i0][1] * l0 + uv[i1][1] * l1 + uv[i2][1] * l2;
            int px = tu + Math.min(fw - 1, Math.max(0, (int) (u * fw))), py = tv + Math.min(fh - 1, Math.max(0, (int) (v * fh)));
            int c = tex.argb[py * tex.w + px]; int gl = tex.glow[py * tex.w + px];
            double k = gl != 0 ? 1.15 : sh;
            int r = (int) Math.min(255, ((c >> 16) & 255) * k), gg = (int) Math.min(255, ((c >> 8) & 255) * k), b = (int) Math.min(255, (c & 255) * k);
            zb[y * W + x] = (float) d;
            img.setRGB(x, y, 0xFF000000 | r << 16 | gg << 8 | b);
        }
    }
}

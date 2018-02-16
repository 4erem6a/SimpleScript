package com.evg.ss.modules.jcanvas;

import com.evg.ss.lib.Function;
import com.evg.ss.modules.SSExports;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.values.NullValue;
import com.evg.ss.values.UndefinedValue;
import com.evg.ss.values.Value;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

/**
 * Based on:
 * {https://github.com/aNNiMON/Own-Programming-Language-Tutorial/blob/latest/src/main/java/com/annimon/ownlang/modules/canvas/canvas.java}
 */

@SSExports("jcanvas")
public final class JCanvasModule {

    private static JFrame frame = null;
    private static CanvasPanel panel = null;
    private static Graphics2D graphics = null;
    private static BufferedImage img = null;

    private static void line(int x1, int y1, int x2, int y2) {
        if (graphics == null)
            return;
        graphics.drawLine(x1, y1, x2, y2);
    }

    private static void oval(int x, int y, int w, int h) {
        if (graphics == null)
            return;
        graphics.drawOval(x, y, w, h);
    }

    private static void foval(int x, int y, int w, int h) {
        if (graphics == null)
            return;
        graphics.fillOval(x, y, w, h);
    }

    private static void rect(int x, int y, int w, int h) {
        if (graphics == null)
            return;
        graphics.drawRect(x, y, w, h);
    }

    private static void frect(int x, int y, int w, int h) {
        if (graphics == null)
            return;
        graphics.fillRect(x, y, w, h);
    }

    private static void clip(int x, int y, int w, int h) {
        if (graphics == null)
            return;
        graphics.setClip(x, y, w, h);
    }

    private static Function intConsumer4Convert(IntConsumer4 consumer) {
        return args -> {
            Arguments.checkArgcOrDie(args, 4);
            consumer.accept(args[0].asNumber().intValue(),
                    args[1].asNumber().intValue(),
                    args[2].asNumber().intValue(),
                    args[3].asNumber().intValue());
            return new UndefinedValue();
        };
    }

    @SSExports("createWindow")
    public static Value createWindow(Value... args) {
        Arguments.checkArgcOrDie(args, 0, 1, 2, 3);
        String title = "";
        int width = 640;
        int height = 480;
        switch (args.length) {
            case 1:
                title = args[0].asString();
                break;
            case 2:
                width = args[0].asNumber().intValue();
                height = args[1].asNumber().intValue();
                break;
            case 3:
                title = args[0].asString();
                width = args[1].asNumber().intValue();
                height = args[2].asNumber().intValue();
                break;
        }
        if (frame != null)
            closeWindow();
        panel = new CanvasPanel(width, height);
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        return new UndefinedValue();
    }

    @SSExports("text")
    public static Value text(Value... args) {
        Arguments.checkArgcOrDie(args, 3);
        if (graphics == null)
            return new UndefinedValue();
        int x = args[0].asNumber().intValue();
        int y = args[1].asNumber().intValue();
        graphics.drawString(args[2].asString(), x, y);
        return new UndefinedValue();
    }

    @SSExports("input")
    public static Value input(Value... args) {
        Arguments.checkArgcOrDie(args, 0, 1);
        final String input = JOptionPane.showInputDialog(args.length == 0 ? "" : args[0].asString());
        return (input == null ? new NullValue() : Value.of(input));
    }

    @SSExports("repaint")
    public static Value repaint(Value... args) {
        Arguments.checkArgcOrDie(args, 0);
        if (panel == null)
            return new UndefinedValue();
        panel.invalidate();
        panel.repaint();
        return new UndefinedValue();
    }

    @SSExports("setColor")
    public static Value setColor(Value... args) {
        Arguments.checkArgcOrDie(args, 1);
        if (graphics == null)
            return new UndefinedValue();
        graphics.setColor(new Color(args[0].asNumber().intValue()));
        return new UndefinedValue();
    }

    @SSExports("rgb")
    public static Value rgb(Value... args) {
        Arguments.checkArgcOrDie(args, 3);
        return _rgba(args[0].asNumber().intValue(),
                args[1].asNumber().intValue(),
                args[2].asNumber().intValue(),
                255);
    }

    @SSExports("rgba")
    public static Value rgba(Value... args) {
        Arguments.checkArgcOrDie(args, 4);
        return _rgba(args[0].asNumber().intValue(),
                args[1].asNumber().intValue(),
                args[2].asNumber().intValue(),
                args[3].asNumber().intValue());
    }

    @SSExports("hsb")
    public static Value hsb(Value... args) {
        Arguments.checkArgcOrDie(args, 3);
        return Value.of(Color.getHSBColor(args[0].asNumber().intValue(),
                args[1].asNumber().intValue(),
                args[2].asNumber().intValue()).getRGB());
    }

    private static Value _rgba(int r, int g, int b, int a) {
        return Value.of(new Color(r, g, b, a).getRGB());
    }

    @SSExports("closeWindow")
    public static Value closeWindow(Value... args) {
        Arguments.checkArgcOrDie(args, 0);
        if (frame != null)
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        return new UndefinedValue();
    }

    @FunctionalInterface
    private interface IntConsumer4 {
        void accept(int i1, int i2, int i3, int i4);
    }

    private static class CanvasPanel extends JPanel {

        public CanvasPanel(int width, int height) {
            setPreferredSize(new Dimension(width, height));
            img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            graphics = img.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            setFocusable(true);
            requestFocus();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, null);
        }

    }
}
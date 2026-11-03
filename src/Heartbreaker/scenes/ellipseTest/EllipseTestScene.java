package Heartbreaker.scenes.ellipseTest;

import Heartbreaker.main.Heartbreaker;
import Heartbreaker.valentine.GameFrame;
import Heartbreaker.valentine.input.KeyInput;
import Heartbreaker.valentine.input.MouseInput;
import Heartbreaker.valentine.scenes.DefaultTestScene;
import Heartbreaker.valentine.ui.Fonts;
import Heartbreaker.valentine.ui.TextPanel;
import Heartbreaker.valentine.ui.WorldGrid;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Visual test bench for Ellipses.java. Feeds the inputs to Ellipses.main through stdin,
 * reads its printed bounding box, and draws it against the analytic bounding box.
 *
 * Controls:
 *  Right click drag - move the nearest focus (hold shift to snap to 0.5)
 *  W / S            - grow / shrink the major axis length (hold shift for fine steps)
 *  R                - random ellipse
 *  I                - type exact input "x1 y1 x2 y2 majorLength"
 *  C                - copy input and output to the clipboard
 *  F                - fit the view to the ellipse
 *  T                - cycle ternary search trace: both / x / y / off
 *  [ / ]            - step through individual search calls ([ past the first shows all), hold to repeat
 *  H                - hide / show help
 *  Left drag / arrows / scroll / - = - pan and zoom (DefaultTestScene)
 */
public class EllipseTestScene extends DefaultTestScene {

    //world pixels per algorithm unit
    private static final double UNIT = 40;

    private static final Font LABEL_FONT = Fonts.mono(Font.PLAIN, 12);

    private static final Color BACKGROUND = new Color(14, 16, 28);
    private static final Color TEXT = new Color(225, 228, 235);
    private static final Color DIM_TEXT = new Color(140, 145, 160);
    private static final Color FOCUS = new Color(255, 90, 200);
    private static final Color RESULT = new Color(255, 215, 60);
    private static final Color EXPECTED = new Color(70, 220, 255);
    private static final Color GOOD = new Color(90, 230, 120);
    private static final Color BAD = new Color(255, 95, 95);

    private double x1 = -5, y1 = 0, x2 = 5, y2 = 0;
    private double majorLength = 16;

    private boolean dirty = true;
    private int draggedFocus = -1;
    private volatile String pendingInput = null;
    private volatile boolean dialogOpen = false;
    private boolean showHelp = true;

    private String toast = null;
    private double toastTime = 0;

    //last result of the algorithm: minX minY maxX maxY
    private double[] result;
    private String rawOutput = "";
    private String error = null;
    private double runTimeMs = 0;

    //One recursive call of ternarySearch, recorded through Ellipses.traceHook
    private static class TraceStep {
        int axis;
        double start, end, midLeft, midRight;
        double[] startPt, endPt, midLeftPt, midRightPt;
        double[][] arc;
    }
    private final ArrayList<TraceStep> trace = new ArrayList<>();
    private static final String[] TRACE_MODES = {"both", "x search", "y search", "off"};
    private int traceMode = 0;
    //-1 shows every step, otherwise only that step is highlighted
    private int selectedStep = -1;

    @Override
    public boolean initialize(){
        super.initialize();
        //Grid counts algorithm units with y pointing up
        grid = new WorldGrid(UNIT, true);
        Heartbreaker.stopTrack();
        //The algorithm runs on the first update, not here: initialize can be on a loading thread and stdout is redirected while it runs
        fitView();
        return true;
    }

    @Override
    public void windowResized(){
        super.windowResized();
        fitView();
    }

    @Override
    public void updateScene(double delta){
        super.updateScene(delta);

        if(pendingInput != null){
            parseInput(pendingInput);
            pendingInput = null;
        }
        if(toastTime > 0){
            toastTime -= delta;
        }
        //Keys typed into the input dialog shouldn't trigger shortcuts
        if(dialogOpen){
            return;
        }

        //Focus dragging
        if(MouseInput.isButtonPressed(MouseEvent.BUTTON3)){
            Point2D.Double m = screenToUnits(MouseInput.getPosition());
            if(KeyInput.isKeyPressed(KeyEvent.VK_SHIFT)){
                m.setLocation(Math.round(m.x * 2) / 2.0, Math.round(m.y * 2) / 2.0);
            }
            if(draggedFocus == -1){
                draggedFocus = nearestFocus(MouseInput.getPosition());
            }
            if(draggedFocus == 1 && (m.x != x1 || m.y != y1)){
                x1 = m.x; y1 = m.y; dirty = true;
            } else if(draggedFocus == 2 && (m.x != x2 || m.y != y2)){
                x2 = m.x; y2 = m.y; dirty = true;
            }
        } else {
            draggedFocus = -1;
        }

        double lengthSpeed = KeyInput.isKeyPressed(KeyEvent.VK_SHIFT) ? 0.5 : 4;
        if(KeyInput.isKeyPressed(KeyEvent.VK_W)){
            majorLength += lengthSpeed * delta;
            dirty = true;
        }
        if(KeyInput.isKeyPressed(KeyEvent.VK_S)){
            majorLength = Math.max(0, majorLength - lengthSpeed * delta);
            dirty = true;
        }

        if(KeyInput.isKeyJustPressed(KeyEvent.VK_R)){
            randomize();
        }
        if(KeyInput.isKeyJustPressed(KeyEvent.VK_I)){
            promptInput();
        }
        if(KeyInput.isKeyJustPressed(KeyEvent.VK_C)){
            copyToClipboard();
        }
        if(KeyInput.isKeyJustPressed(KeyEvent.VK_F)){
            fitView();
        }
        if(KeyInput.isKeyJustPressed(KeyEvent.VK_H)){
            showHelp = !showHelp;
        }
        if(KeyInput.isKeyJustPressed(KeyEvent.VK_T)){
            traceMode = (traceMode + 1) % TRACE_MODES.length;
            selectedStep = -1;
        }
        if(KeyInput.isKeyRepeated(KeyEvent.VK_CLOSE_BRACKET)){
            selectedStep = Math.min(selectedStep + 1, visibleSteps().size() - 1);
        }
        if(KeyInput.isKeyRepeated(KeyEvent.VK_OPEN_BRACKET)){
            selectedStep = Math.max(selectedStep - 1, -1);
        }

        if(dirty){
            runAlgorithm();
            dirty = false;
        }
    }

    private void showToast(String message){
        toast = message;
        toastTime = 1.8;
    }

    private void randomize(){
        Random rand = new Random();
        x1 = Math.round((rand.nextDouble() * 20 - 10) * 10) / 10.0;
        y1 = Math.round((rand.nextDouble() * 20 - 10) * 10) / 10.0;
        x2 = Math.round((rand.nextDouble() * 20 - 10) * 10) / 10.0;
        y2 = Math.round((rand.nextDouble() * 20 - 10) * 10) / 10.0;
        double focalDist = Point2D.distance(x1,y1,x2,y2);
        majorLength = Math.round((focalDist + 0.5 + rand.nextDouble() * 10) * 10) / 10.0;
        dirty = true;
    }

    private String inputString(){
        return fmt(x1) + " " + fmt(y1) + " " + fmt(x2) + " " + fmt(y2) + " " + fmt(majorLength);
    }

    //Short form for inputs so dragged values don't show 15 digits
    private static String fmt(double v){
        String s = String.format("%.4f", v);
        s = s.replaceAll("0+$", "").replaceAll("\\.$", "");
        return s.equals("-0") ? "0" : s;
    }

    private void promptInput(){
        String current = inputString();
        dialogOpen = true;
        //Runs off the game loop so the scene keeps drawing while the dialog is open
        SwingUtilities.invokeLater(() -> {
            String s = JOptionPane.showInputDialog(null, "x1 y1 x2 y2 majorLength", current);
            if(s != null){
                pendingInput = s;
            }
            dialogOpen = false;
        });
    }

    private void parseInput(String s){
        try{
            String[] parts = s.trim().split("\\s+");
            double[] v = new double[5];
            for(int i = 0; i < 5; i++){
                v[i] = Double.parseDouble(parts[i]);
            }
            x1 = v[0]; y1 = v[1]; x2 = v[2]; y2 = v[3]; majorLength = v[4];
            dirty = true;
            fitView();
        } catch (Exception e){
            showToast("Bad input: " + s);
        }
    }

    private void copyToClipboard(){
        double[] expected = expectedBox();
        String text = "input:    " + inputString() + "\n"
                + "output:   " + rawOutput + "\n"
                + "expected: " + (expected == null ? "none" : expected[0] + " " + expected[1] + " " + expected[2] + " " + expected[3]);
        try{
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
            showToast("Copied input / output / expected");
        } catch (Exception e){
            showToast("Clipboard unavailable");
        }
    }

    /**
     * Centers the camera on the ellipse and zooms so it fills most of the window.
     */
    private void fitView(){
        double[] box = expectedBox();
        if(box == null){
            box = new double[]{Math.min(x1,x2) - 1, Math.min(y1,y2) - 1, Math.max(x1,x2) + 1, Math.max(y1,y2) + 1};
        }
        double w = Math.max((box[2] - box[0]) * UNIT, 1);
        double h = Math.max((box[3] - box[1]) * UNIT, 1);
        camera.setPosition(new Point2D.Double((box[0] + box[2]) / 2.0 * UNIT, -(box[1] + box[3]) / 2.0 * UNIT));
        camera.setZoom(Math.min(GameFrame.GAME_WIDTH / w, GameFrame.GAME_HEIGHT / h) * 0.6);
    }

    /**
     * Runs Ellipses.main exactly as the judge would: inputs on stdin, answer read from stdout.
     */
    private void runAlgorithm(){
        InputStream oldIn = System.in;
        PrintStream oldOut = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        error = null;
        result = null;
        trace.clear();
        Ellipses.traceHook = this::recordStep;
        try{
            String input = x1 + " " + y1 + " " + x2 + " " + y2 + " " + majorLength;
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            System.setOut(new PrintStream(captured, true));
            long start = System.nanoTime();
            Ellipses.main(new String[0]);
            runTimeMs = (System.nanoTime() - start) / 1_000_000.0;
        } catch (Throwable t){
            error = t.toString();
        } finally {
            Ellipses.traceHook = null;
            System.setIn(oldIn);
            System.setOut(oldOut);
        }
        selectedStep = Math.min(selectedStep, visibleSteps().size() - 1);

        rawOutput = captured.toString().trim();
        if(error == null){
            try{
                String[] parts = rawOutput.split("\\s+");
                result = new double[4];
                for(int i = 0; i < 4; i++){
                    result[i] = Double.parseDouble(parts[i]);
                }
            } catch (Exception e){
                result = null;
                error = "Could not parse output: \"" + rawOutput + "\"";
            }
        }
    }

    /**
     * Called by Ellipses.ternarySearch on every recursive call. Points are taken from
     * Ellipses.finalPoint right away so they use the same state the search saw.
     * @param data axis, start, end, midLeft, midRight
     */
    private void recordStep(double[] data){
        TraceStep step = new TraceStep();
        step.axis = (int) data[0];
        step.start = data[1];
        step.end = data[2];
        step.midLeft = data[3];
        step.midRight = data[4];
        step.startPt = Ellipses.finalPoint(step.start);
        step.endPt = Ellipses.finalPoint(step.end);
        step.midLeftPt = Ellipses.finalPoint(step.midLeft);
        step.midRightPt = Ellipses.finalPoint(step.midRight);
        int samples = 64;
        step.arc = new double[samples + 1][];
        for(int i = 0; i <= samples; i++){
            step.arc[i] = Ellipses.finalPoint(step.start + (step.end - step.start) * i / samples);
        }
        trace.add(step);
    }

    private ArrayList<TraceStep> visibleSteps(){
        ArrayList<TraceStep> steps = new ArrayList<>();
        for(TraceStep step : trace){
            if(traceMode == 0 || (traceMode == 1 && step.axis == 0) || (traceMode == 2 && step.axis == 1)){
                steps.add(step);
            }
        }
        return steps;
    }

    /**
     * Analytic axis aligned bounding box, used as the reference answer.
     * @return minX minY maxX maxY, or null if the ellipse doesn't exist
     */
    private double[] expectedBox(){
        double a = majorLength / 2.0;
        double c = Point2D.distance(x1,y1,x2,y2) / 2.0;
        if(a < c){
            return null;
        }
        double b = Math.sqrt(a * a - c * c);
        double phi = Math.atan2(y2 - y1, x2 - x1);
        double cx = (x1 + x2) / 2.0;
        double cy = (y1 + y2) / 2.0;
        double hw = Math.sqrt(Math.pow(a * Math.cos(phi), 2) + Math.pow(b * Math.sin(phi), 2));
        double hh = Math.sqrt(Math.pow(a * Math.sin(phi), 2) + Math.pow(b * Math.cos(phi), 2));
        return new double[]{cx - hw, cy - hh, cx + hw, cy + hh};
    }

    private Point2D.Double unitsToScreen(double x, double y){
        return grid.unitsToScreen(this, x, y);
    }

    private Point2D.Double screenToUnits(Point p){
        return grid.screenToUnits(this, p.x, p.y);
    }

    /**
     * @return 1 or 2 for the focus closest to the given screen point
     */
    private int nearestFocus(Point p){
        double d1 = unitsToScreen(x1, y1).distance(p);
        double d2 = unitsToScreen(x2, y2).distance(p);
        return d1 <= d2 ? 1 : 2;
    }

    @Override
    public void draw(Graphics2D g, double delta){
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        g.setColor(BACKGROUND);
        g.fillRect(0, 0, GameFrame.GAME_WIDTH, GameFrame.GAME_HEIGHT);
        drawInfinitePlane(g);
        drawObjects(g, delta);
        camera.update(delta);

        double[] expected = expectedBox();

        //Reference ellipse
        if(expected != null){
            double a = majorLength / 2.0;
            double c = Point2D.distance(x1,y1,x2,y2) / 2.0;
            double b = Math.sqrt(a * a - c * c);
            double phi = Math.atan2(y2 - y1, x2 - x1);
            double cx = (x1 + x2) / 2.0;
            double cy = (y1 + y2) / 2.0;
            Path2D.Double path = new Path2D.Double();
            int steps = 256;
            for(int i = 0; i <= steps; i++){
                double t = (Math.PI * 2 * i) / steps;
                double px = cx + a * Math.cos(t) * Math.cos(phi) - b * Math.sin(t) * Math.sin(phi);
                double py = cy + a * Math.cos(t) * Math.sin(phi) + b * Math.sin(t) * Math.cos(phi);
                Point2D.Double s = unitsToScreen(px, py);
                if(i == 0){
                    path.moveTo(s.x, s.y);
                } else {
                    path.lineTo(s.x, s.y);
                }
            }
            g.setColor(TEXT);
            g.setStroke(new BasicStroke(2));
            g.draw(path);

            //Reference box
            g.setColor(EXPECTED);
            g.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[]{8, 6}, 0));
            drawBox(g, expected);
        }

        drawTrace(g);

        //Algorithm's box
        if(result != null){
            g.setColor(RESULT);
            g.setStroke(new BasicStroke(2));
            drawBox(g, result);
            drawDot(g, result[0], result[1], RESULT, 9);
            drawDot(g, result[2], result[3], RESULT, 9);
        }

        //Foci and center, with a ring on the focus a right click would grab
        drawDot(g, x1, y1, FOCUS, 12);
        drawDot(g, x2, y2, FOCUS, 12);
        drawDot(g, (x1 + x2) / 2.0, (y1 + y2) / 2.0, DIM_TEXT, 6);
        int ringFocus = draggedFocus != -1 ? draggedFocus : nearestFocus(MouseInput.getPosition());
        Point2D.Double ring = ringFocus == 1 ? unitsToScreen(x1, y1) : unitsToScreen(x2, y2);
        if(draggedFocus != -1 || ring.distance(MouseInput.getPosition()) < 30){
            g.setColor(FOCUS);
            g.setStroke(new BasicStroke(2));
            g.draw(new Ellipse2D.Double(ring.x - 11, ring.y - 11, 22, 22));
        }

        drawHud(g, expected);
    }

    private void drawTrace(Graphics2D g){
        ArrayList<TraceStep> steps = visibleSteps();
        if(steps.isEmpty()){
            return;
        }
        Point2D.Double center = unitsToScreen((x1 + x2) / 2.0, (y1 + y2) / 2.0);
        Stroke dashed = new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[]{5, 4}, 0);

        if(selectedStep == -1){
            //Every step, later (deeper) steps drawn more opaque
            g.setStroke(new BasicStroke(1.25f));
            for(int i = 0; i < steps.size(); i++){
                TraceStep step = steps.get(i);
                int alpha = 40 + (int) (215.0 * (i + 1) / steps.size());
                g.setColor(withAlpha(axisColor(step.axis), alpha));
                drawRay(g, center, step.midLeftPt);
                drawRay(g, center, step.midRightPt);
            }
            return;
        }

        TraceStep step = steps.get(selectedStep);
        Color col = axisColor(step.axis);

        //Search interval along the ellipse
        Path2D.Double arc = new Path2D.Double();
        for(int i = 0; i < step.arc.length; i++){
            Point2D.Double s = unitsToScreen(step.arc[i][0], step.arc[i][1]);
            if(i == 0){
                arc.moveTo(s.x, s.y);
            } else {
                arc.lineTo(s.x, s.y);
            }
        }
        g.setColor(withAlpha(col, 140));
        g.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(arc);

        g.setColor(col);
        g.setStroke(dashed);
        drawRay(g, center, step.startPt);
        drawRay(g, center, step.endPt);
        g.setStroke(new BasicStroke(2));
        drawRay(g, center, step.midLeftPt);
        drawRay(g, center, step.midRightPt);
        drawDot(g, step.midLeftPt[0], step.midLeftPt[1], col, 9);
        drawDot(g, step.midRightPt[0], step.midRightPt[1], col, 9);

        drawTag(g, step.startPt, "start", col);
        drawTag(g, step.endPt, "end", col);
        drawTag(g, step.midLeftPt, "midLeft", col);
        drawTag(g, step.midRightPt, "midRight", col);
    }

    private void drawRay(Graphics2D g, Point2D.Double center, double[] pt){
        Point2D.Double s = unitsToScreen(pt[0], pt[1]);
        g.draw(new Line2D.Double(center, s));
    }

    private void drawTag(Graphics2D g, double[] pt, String text, Color color){
        Point2D.Double s = unitsToScreen(pt[0], pt[1]);
        TextPanel.label(g, text, s.x + 8, s.y - 8, color, LABEL_FONT);
    }

    private Color axisColor(int axis){
        return axis == 0 ? new Color(255, 160, 50) : new Color(130, 255, 130);
    }

    private Color withAlpha(Color c, int alpha){
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), Math.max(0, Math.min(255, alpha)));
    }

    private void drawBox(Graphics2D g, double[] box){
        Point2D.Double min = unitsToScreen(box[0], box[1]);
        Point2D.Double max = unitsToScreen(box[2], box[3]);
        double left = Math.min(min.x, max.x);
        double top = Math.min(min.y, max.y);
        g.draw(new Rectangle.Double(left, top, Math.abs(max.x - min.x), Math.abs(max.y - min.y)));
    }

    private void drawDot(Graphics2D g, double x, double y, Color color, int size){
        Point2D.Double s = unitsToScreen(x, y);
        g.setColor(color);
        g.fill(new Ellipse2D.Double(s.x - size / 2.0, s.y - size / 2.0, size, size));
    }

    private void drawHud(Graphics2D g, double[] expected){
        int w = GameFrame.GAME_WIDTH;
        int h = GameFrame.GAME_HEIGHT;

        //Results panel
        TextPanel info = new TextPanel();
        double a = majorLength / 2.0;
        double c = Point2D.distance(x1,y1,x2,y2) / 2.0;
        double phi = Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
        info.add("input     " + inputString(), TEXT, true);
        info.add(String.format("          a=%s  b=%s  c=%s  phi=%s°", fmt(a),
                a >= c ? fmt(Math.sqrt(a * a - c * c)) : "NaN", fmt(c), fmt(phi)), DIM_TEXT);
        info.add("output    " + (rawOutput.isEmpty() ? "(nothing printed)" : rawOutput), RESULT);
        if(expected != null){
            info.add(String.format("expected  %.6f %.6f %.6f %.6f", expected[0], expected[1], expected[2], expected[3]), EXPECTED);
        } else {
            info.add("expected  none (major length < focal distance)", EXPECTED);
        }
        if(expected != null && result != null){
            double maxErr = 0;
            for(int i = 0; i < 4; i++){
                maxErr = Math.max(maxErr, Math.abs(result[i] - expected[i]));
            }
            boolean pass = maxErr < 1e-4;
            info.add(String.format("diff      %+.6f %+.6f %+.6f %+.6f", result[0] - expected[0],
                    result[1] - expected[1], result[2] - expected[2], result[3] - expected[3]), pass ? GOOD : BAD);
            info.add(String.format("          %s  max %.2e", pass ? "PASS" : "FAIL", maxErr), pass ? GOOD : BAD, true);
        }
        if(error != null){
            info.add("error     " + error, BAD, true);
        }
        info.add(String.format("run time  %.3f ms", runTimeMs), DIM_TEXT);

        //Trace section
        ArrayList<TraceStep> steps = visibleSteps();
        int xCalls = 0;
        for(TraceStep step : trace){
            if(step.axis == 0) xCalls++;
        }
        info.add(String.format("trace     %s  (x calls %d, y calls %d)", TRACE_MODES[traceMode], xCalls, trace.size() - xCalls), DIM_TEXT);
        if(traceMode != 3 && trace.isEmpty() && error == null){
            info.add("          no trace recorded - is the TEST SCENE HOOK still in Ellipses.java?", BAD);
        }
        if(selectedStep >= 0 && selectedStep < steps.size()){
            TraceStep step = steps.get(selectedStep);
            Color col = axisColor(step.axis);
            info.add(String.format("step %d/%d  (%s axis)", selectedStep + 1, steps.size(), step.axis == 0 ? "x" : "y"), col, true);
            info.add(String.format("  start    θ=%+.8f  (%.4f, %.4f)", step.start, step.startPt[0], step.startPt[1]), col);
            info.add(String.format("  midLeft  θ=%+.8f  (%.4f, %.4f)", step.midLeft, step.midLeftPt[0], step.midLeftPt[1]), col);
            info.add(String.format("  midRight θ=%+.8f  (%.4f, %.4f)", step.midRight, step.midRightPt[0], step.midRightPt[1]), col);
            info.add(String.format("  end      θ=%+.8f  (%.4f, %.4f)", step.end, step.endPt[0], step.endPt[1]), col);
            info.add(String.format("  width    %.3e", step.end - step.start), col);
        }
        info.draw(g, 12, 12, TextPanel.Anchor.TOP_LEFT);

        //Cursor readout
        Point2D.Double cursor = screenToUnits(MouseInput.getPosition());
        new TextPanel().add(String.format("cursor (%.3f, %.3f)", cursor.x, cursor.y), DIM_TEXT)
                .draw(g, w - 12, h - 12, TextPanel.Anchor.BOTTOM_RIGHT);

        //Help / legend
        TextPanel help = new TextPanel();
        if(showHelp){
            help.add("right drag  move focus (shift snaps to 0.5)", DIM_TEXT)
                .add("W / S       major length (shift = fine)", DIM_TEXT)
                .add("R random   I type input   C copy   F fit view", DIM_TEXT)
                .add("T trace mode   [ / ] step search calls (hold)", DIM_TEXT)
                .add("left drag / arrows pan   scroll or - = zoom", DIM_TEXT)
                .add("H hide help   Esc menu", DIM_TEXT)
                .add("", DIM_TEXT)
                .add("■ algorithm box", RESULT)
                .add("■ expected box (dashed)", EXPECTED)
                .add("■ x search θ", axisColor(0))
                .add("■ y search θ", axisColor(1))
                .add("● focus", FOCUS);
        } else {
            help.add("H help", DIM_TEXT);
        }
        help.draw(g, 12, h - 12, TextPanel.Anchor.BOTTOM_LEFT);

        //Toast
        if(toast != null && toastTime > 0){
            int alpha = (int) (255 * Math.min(1, toastTime / 0.4));
            new TextPanel().add(toast, withAlpha(TEXT, alpha), true).draw(g, w / 2, 12, TextPanel.Anchor.TOP_CENTER);
        }
    }
}


/*************************************************************************
 *  Compilation:  javac Draw.java
 *
 *  Simple graphics library.
 *
 *  For documentation, see http://www.cs.princeton.edu/introcs/24inout 
 *
 *  Todo
 *  ------------------
 *     -  Add support for CubicCurve2D or QudarticCurve2D or Arc2D
 *     -  Add support for gradient fill, etc.
 *     -  keep XOR mode?
 *
 *  Remarks
 *  -------
 *     -  lines are much faster than spots?
 *     -  can't use AffineTransform since it inverts images and strings
 *     -  careful using setFont in inner loop within an animation -
 *        it can cause flicker
 *
 *************************************************************************/


import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.net.*;
import java.applet.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class Draw {

    private final BufferedImage offscreenImage; // double buffered image
    private final BufferedImage onscreenImage;  // double buffered image
    private final Graphics2D offscreen;
    private final Graphics2D onscreen;

    private final int width, height;            // size of drawing area in pixels

    protected JFrame frame;                     // the frame for drawing to the screen
    private Insets insets;                      // frame border

    private double xmin, xmax, ymin, ymax;      // boundary of (x, y) coordinates
    private double x = 0.0, y = 0.0;            // turtle is at coordinate (x, y)
    private double orientation = 0.0;           // facing this many degrees counterclockwise
    private Color background = Color.WHITE;     // background color
    private Color foreground = Color.BLACK;     // foreground color
    private boolean fill = true;                // fill in circles and rectangles?
    private String title = "";                  // title of the frame in the menubar

    private Font font = new Font("Serif", Font.PLAIN, 16);


    // create a new drawing region of given dimensions
    public Draw(int width, int height) {
        this.width  = width;
        this.height = height;
        if (width <= 0 || height <= 0) throw new RuntimeException("Illegal dimension");
        offscreenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        onscreenImage  = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        offscreen = (Graphics2D) offscreenImage.getGraphics();
        onscreen  = (Graphics2D) onscreenImage.getGraphics();
        setScale(0.0, 0.0, 1.0, 1.0);
        clear();
    }


    // accessor methods
    public double x()           { return x;           }
    public double y()           { return y;           }
    public double orientation() { return orientation; }
    public int width()          { return width;       }
    public int height()         { return height;      }
    public boolean isFillOn()   { return fill;        }

    // simple state changing methods
    public void setTitle(String s) { title = s; }

    public void fillOn()        { fill    = true;     }
    public void fillOff()       { fill    = false;    }

    public void xorOn()         { offscreen.setXORMode(background); }
    public void xorOff()        { offscreen.setPaintMode();         }

    // rotate counterclockwise in degrees
    public void rotate(double angle) { orientation += angle; }
    public void setOrientation(double orientation) {this.orientation=orientation; }


   /***********************************************************************************
    *  Affine transform
    ***********************************************************************************/

    // change the user coordinate system
    public void setScale(double xmin, double ymin, double xmax, double ymax) {

        // update (x, y) so that they stay at same screen position ???
        // update orientation so that it stays the same relative to screen coorindates ???

        // there may be some bugs when using scaling with images ???


        this.xmin = xmin;
        this.ymin = ymin;
        this.xmax = xmax;
        this.ymax = ymax;
    }

    // scale from user coordinates to screen coordinates
    public double scaleX(double x)  { return width  * (x - xmin) / (xmax - xmin); }
    public double scaleY(double y)  { return height * (ymax - y) / (ymax - ymin); }
    public double factorX(double w) { return w * width  / Math.abs(xmax - xmin);  }
    public double factorY(double h) { return h * height / Math.abs(ymax - ymin);  }


    // scale from screen coordinates to user coordinates
    public double toUserX(double x)  { return (xmax - xmin) * (x - insets.left) / width  + xmin; }
    public double toUserY(double y)  { return (ymax - ymin) * (height - y + insets.top) / height + ymin; }


   /***********************************************************************************
    *  Background and foreground colors
    ***********************************************************************************/

    // clear the background
    public void clear() {
        offscreen.setColor(background);
        offscreen.fillRect(0, 0, width, height);
        offscreen.setColor(foreground);
    }

    // clear the background with a new color
    public void clear(Color color) {
        background = color;
        clear();
    }
   
    // set the pen size
    public void setPenSize(double size) {
        BasicStroke stroke = new BasicStroke((float) size);
        offscreen.setStroke(stroke);
    }

    // set the foreground color
    public void setColor(Color color) {
        foreground = color;
        offscreen.setColor(foreground);
    }

    // set the foreground color using red-green-blue (inputs between 0 and 255)
    public void setColorRGB(int r, int g, int b) {
        setColor(new Color(r, g, b));
    }

    // set the foreground color using hue-saturation-brightness (inputs between 0 and 255)
    public void setColorHSB(int h, int s, int b) {
        setColor(Color.getHSBColor(1.0f * h / 255, 1.0f * s / 255, 1.0f * b / 255));
    }

    // set the foreground color using hue-saturation-brightness (inputs between 0.0 and 1.0)
    public void setColorHSB(double h, double s, double b) {
        setColor(Color.getHSBColor((float) h, (float) s, (float) b));
    }

    // set the foreground color to a random color
    public void setColorRandom() {
        setColorHSB((int) (Math.random() * 256), 255, 255);
    }


   /***********************************************************************************
    *  Move the turtle
    ***********************************************************************************/

    // go to (x, y) with pen up
    public void moveTo(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // go to (x, y) with the pen down
    public void lineTo(double x, double y) {
        offscreen.draw(new Line2D.Double(scaleX(this.x), scaleY(this.y), scaleX(x), scaleY(y)));
        this.x = x;
        this.y = y;
    }

    // walk forward with the pen down
    public void forward(double d) {
        double oldx = x;
        double oldy = y;
        x += d * Math.cos(Math.toRadians(orientation));
        y += d * Math.sin(Math.toRadians(orientation));
        offscreen.draw(new Line2D.Double(scaleX(x), scaleY(y), scaleX(oldx), scaleY(oldy)));
    }



   /***********************************************************************************
    *  Draw spots
    ***********************************************************************************/
    // draw pixel at current location
    public void spot() { spot(0.0); }

    // draw circle of diameter d, centered at current location; degenerate to pixel if small
    public void spot(double d) {
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(d);
        double hs = factorY(d);
        if (ws <= 1 && hs <= 1) offscreen.fillRect((int) Math.round(xs), (int) Math.round(ys), 1, 1);
        else if ( fill) offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        else if (!fill) offscreen.draw(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
    }


    // draw w-by-h rectangle, centered at current location; degenerate to single pixel if too small
    public void spot(double w, double h) {
        // screen coordinates
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(w);
        double hs = factorY(h);
        offscreen.rotate(Math.toRadians(orientation), xs, ys);
        if (ws <= 1 && hs <= 1) offscreen.fillRect((int) Math.round(xs), (int) Math.round(ys), 1, 1);
        else if ( fill) offscreen.fill(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        else if (!fill) offscreen.draw(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        offscreen.rotate(Math.toRadians(-orientation), xs, ys);
    }

  // get an image from the given filename
    private Image getImage(String filename) {

        // try to read from file
        ImageIcon icon = new ImageIcon(filename);

        // in case file is inside a .jar
        if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            URL url = Draw.class.getResource(filename);
            if (url == null) throw new RuntimeException("image " + filename + " not found");
            icon = new ImageIcon(url);
        }

        return icon.getImage();
    }

    public void drawPolygon(double[] x, double[] y) {
        int N = x.length;
        GeneralPath path = new GeneralPath();
        path.moveTo((float) scaleX(x[0]), (float) scaleY(y[0]));
        for (int i = 0; i < N; i++)
            path.lineTo((float) scaleX(x[i]), (float) scaleY(y[i]));
        path.closePath();
  
        ////offscreen.rotate(Math.toRadians(orientation), xs, ys);
        if (fill) offscreen.fill(path);
        else      offscreen.draw(path);
        ////offscreen.rotate(Math.toRadians(-orientation), xs, ys);
    }

    // draw spot using gif - fix to be centered at (x, y)
    public void spot(String s) {
        Image image = getImage(s);
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = image.getWidth(null);
        int hs = image.getHeight(null);

        // center of rotation is (xs, ys)
        offscreen.rotate(Math.toRadians(orientation), xs, ys);
        offscreen.drawImage(image, (int) (xs - ws/2.0), (int) (ys - hs/2.0), null);
        offscreen.rotate(Math.toRadians(-orientation), xs, ys);
    }

    // draw spot using gif, centered on (x, y), scaled of size w-by-h
    // center vs. !center
    public void spot(String s, double w, double h) {   
        Image image = getImage(s);
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(w);
        double hs = factorY(h);
        if (ws <= 1 && hs <= 1) offscreen.fillRect((int) Math.round(xs), (int) Math.round(ys), 1, 1);

        else {
            // center of rotation is (xs, ys)
            offscreen.rotate(Math.toRadians(orientation), xs, ys);
            offscreen.drawImage(image, (int) Math.round(xs - ws/2.0),
                                       (int) Math.round(ys - hs/2.0),
                                       (int) Math.round(ws),
                                       (int) Math.round(hs), null);
            offscreen.rotate(Math.toRadians(-orientation), xs, ys);
        }
    }


   /***********************************************************************************
    *  Writing text
    ***********************************************************************************/

    // write the given string in the current font
    public void setFont(Font font) {
        this.font = font;
    }

    // write the given string in the current font, center on the current location
    public void write(String s) {
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = metrics.stringWidth(s);
        int hs = metrics.getDescent();
        offscreen.rotate(Math.toRadians(orientation), xs, ys);
        offscreen.drawString(s, (float) (xs - ws/2.0), (float) (ys + hs));
        offscreen.rotate(Math.toRadians(-orientation), xs, ys);
    }


   /***********************************************************************************
    *  Display the image on screen or save to file
    ***********************************************************************************/
    // wait for a short while
    public void pause(int delay) {
        show();
        try { Thread.currentThread().sleep(delay); }
        catch (InterruptedException e) { }
    }

    // view on-screen, creating new frame if necessary
    public void show() {

        // create the GUI for viewing the image if needed
        if (frame == null) {
            frame = new JFrame();
            ImageIcon icon = new ImageIcon(onscreenImage);
            frame.setContentPane(new JLabel(icon));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);            // closes all windows
            // frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);      // closes only current window
            frame.setTitle(title);
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
            insets = frame.getInsets();    // must be after frame is rendered
        }

        // draw
        onscreen.drawImage(offscreenImage, 0, 0, null);
        frame.setTitle(title);
        frame.repaint();
    }


    // save to file - suffix can be png, jpg, or gif
    public void save(String filename) {
        File file = new File(filename);
        String suffix = filename.substring(filename.lastIndexOf('.') + 1);
        try { ImageIO.write(offscreenImage, suffix, file); }
        catch (IOException e) { e.printStackTrace(); }
    }

    // for emebedding into a JPanel
    public JLabel getJLabel() {
        if (offscreenImage == null) return null;         // no image available
        ImageIcon icon = new ImageIcon(offscreenImage);
        JLabel jlabel = new JLabel(icon);
        jlabel.setAlignmentX(0.5f);
        return jlabel;
    }


   /***********************************************************************************
    *  Sound
    ***********************************************************************************/

    // play a wav or midi sound
    public void play(String s) {
        URL url = null;
        try {
            File fil = new File( s );
            if(fil.canRead()) url = fil.toURL( );
        }
        catch (MalformedURLException e) { }
        // URL url = Draw.class.getResource(s); 
        if (url == null) throw new RuntimeException("audio " + s + " not found");
        AudioClip clip = Applet.newAudioClip(url);
        clip.play();
    }







    // test client
    public static void main(String args[]) {
        int size=500;
        int pen=10;
        Draw t = new Draw(size, size);
        t.setScale(0, 0, size, size);
        t.clear(Color.GRAY);
        t.setColor(Color.BLUE);
        t.moveTo(0,0);
        t.setPenSize(pen);
        for (int i=size; i>=0; i-=2*pen) {
            t.forward(i);
            t.rotate(90);
            t.pause(10);
        }

        t.setOrientation(0);
        t.moveTo(2*pen,2*pen);
        for (int i=size-4*pen; i>=0; i-=2*pen) {
            t.forward(i);
            t.rotate(90);
            t.pause(10);
        }
        
        
//        t.moveTo(100, 300);
//        t.setColor(Color.BLUE);
//        t.spot(30);
//        t.rotate(30);
//        t.setColor(Color.GREEN);
//        t.forward(200);
//        t.rotate(-30);
//        t.setColor(Color.RED);
//        t.spot(30, 30);
//        t.pause(1000);
//
//        t.moveTo(200, 100);
//      //  t.spot("joker.gif");
//        t.spot(10);
//        t.setColor(Color.MAGENTA);
//        t.lineTo(200, 175);
//        t.spot(10);
//        Font font = new Font("Arial", Font.BOLD, 30);
//        t.setFont(font);
//        t.write("Joker");
//        t.pause(1000);
//
//        t.moveTo(400, 100);
//        t.rotate(90);
//     //   t.spot("joker.gif");
//        t.spot(10);
//        t.moveTo(475, 100);
//        t.spot(10);
//        t.write("Kingpin");
        t.show();   // don't forget to repaint at end
    }

}


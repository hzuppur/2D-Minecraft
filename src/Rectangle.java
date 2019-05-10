public class Rectangle {
  public int x, y, w, h;
  private int[] pixels;
  
  public Rectangle(int x, int y, int w, int h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
  }
  
  public Rectangle() {
    this(0, 0, 0, 0);
  }
  
  public void generateGraphics(int colour) {
    pixels = new int[w * h];
    for (int i = 0; i < pixels.length; i++) {
      pixels[i] = colour;
    }
  }
  
  public boolean intersects(Rectangle otherRectangle) {
    if (x > otherRectangle.x + otherRectangle.w || otherRectangle.x > x + w)
      return false;
    if (y > otherRectangle.y + otherRectangle.h || otherRectangle.y > y + h)
      return false;
    
    return true;
  }
  
  public void generateGraphics(int bordewWidth, int colour) {
    pixels = new int[w * h];
    
    for (int i = 0; i < pixels.length; i++)
      pixels[i] = Game.alpha;
    
    for (int y = 0; y < bordewWidth; y++)
      for (int x = 0; x < w; x++)
        pixels[x + y * w] = colour;
    
    for (int y = 0; y < h; y++)
      for (int x = 0; x < bordewWidth; x++)
        pixels[x + y * w] = colour;
    
    for (int y = 0; y < h; y++)
      for (int x = w - bordewWidth; x < w; x++)
        pixels[x + y * w] = colour;
    
    for (int y = h - bordewWidth; y < h; y++)
      for (int x = 0; x < w; x++)
        pixels[x + y * w] = colour;
  }
  
  public int[] getPixels() {
    if (pixels != null) {
      return pixels;
    } else {
      System.out.println("Attemted to retrive pixels from a rectangle without generated graphics");
    }
    return null;
  }
  
  public String toString() {
    return "[" + x + "," + y + "," + w + "," + h + "]";
  }
}

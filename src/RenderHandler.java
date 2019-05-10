import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;


public class RenderHandler {
  private BufferedImage view;
  private Rectangle camera;
  private int[] pixels;
  
  public RenderHandler(int width, int height) {
    //Create a BufferedImage that will represent our view.
    view = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    camera = new Rectangle(0, 0, width, height);
    //Create an array for pixels
    pixels = ((DataBufferInt) view.getRaster().getDataBuffer()).getData();
  }
  
  //rendesrs pixel Array to screen
  public void render(Graphics graphics) {
        /*for (int index = 0; index < pixels.length; index++) {
            pixels[index] = (int) (Math.random() * 0xFFFFFF);
        }*/
    
    graphics.drawImage(view, 0, 0, view.getWidth(), view.getHeight(), null);
  }
  
  //Render image to our array of pixels
  public void renderImage(BufferedImage image, int xPos, int yPos, int xZoom, int yZoom, boolean fixed) {
    int[] imagePixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    renderArray(imagePixels, image.getWidth(), image.getHeight(), xPos, yPos, xZoom, yZoom, fixed);
  }
  
  public void renderSprite(Sprite sprite, int xPos, int yPos, int xZoom, int yZoom, boolean fixed) {
    renderArray(sprite.getPixels(), sprite.getWidth(), sprite.getHeight(), xPos, yPos, xZoom, yZoom, fixed);
  }
  
  public void renderRectangle(Rectangle rectangle, int xZoom, int yZoom, boolean fixed) {
    int[] rectanglePixels = rectangle.getPixels();
    if (rectanglePixels != null) {
      renderArray(rectanglePixels, rectangle.w, rectangle.h, rectangle.x, rectangle.y, xZoom, yZoom, fixed);
    }
  }
  
  public void renderRectangle(Rectangle rectangle,Rectangle offSet, int xZoom, int yZoom, boolean fixed){
    int[] rectanglePixels = rectangle.getPixels();
    if (rectanglePixels != null) {
      renderArray(rectanglePixels, rectangle.w, rectangle.h, rectangle.x + offSet.x, rectangle.y + offSet.y, xZoom, yZoom, fixed);
    }
  }
  
  public void renderArray(int[] renderPixels, int renderWidth, int renderHeight, int xPos, int yPos, int xZoom, int yZoom, boolean fixed) {
    for (int y = 0; y < renderHeight; y++)
      for (int x = 0; x < renderWidth; x++)
        for (int yZoomPos = 0; yZoomPos < yZoom; yZoomPos++)
          for (int xZoomPos = 0; xZoomPos < xZoom; xZoomPos++)
            setPixel(renderPixels[x + y * renderWidth], ((x * xZoom) + xPos + xZoomPos), ((y * yZoom) + yPos + yZoomPos), fixed);
  }
  
  private void setPixel(int pixel, int x, int y, boolean fixed) {
    int pixelIndex = 0;
    if (!fixed) {
      //checks if pixel is on camera
      if (x >= camera.x && y >= camera.y && x <= camera.x + camera.w && y <= camera.y + camera.h) {
        pixelIndex = (x - camera.x) + (y - camera.y) * view.getWidth();
      }
    } else {
      if (x >= 0 && y >= 0 && x <= camera.w && y <= camera.h) {
        pixelIndex = x + y * view.getWidth();
      }
    }
    //checks if pixel is on frame(just to be safe)
    if (pixels.length > pixelIndex && pixel != Game.alpha) {
      pixels[pixelIndex] = pixel;
    }
  }
    
    public Rectangle getCamera () {
      return camera;
    }
    
    public void clear () {
      for (int i = 0; i < pixels.length; i++) {
        pixels[i] = 0;
      }
    }
  }

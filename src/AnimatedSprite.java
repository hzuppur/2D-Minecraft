import java.awt.image.BufferedImage;

public class AnimatedSprite extends Sprite implements GameObject {
  private Sprite[] sprites;
  private int currentSprite = 0;
  private int speed;
  private int counter = 0;
  
  private int startSprite = 0;
  private int endSprite;
  
  public AnimatedSprite(SpriteSheet sheet, Rectangle[] positions, int speed) {
    sprites = new Sprite[positions.length];
    this.speed = speed;
    this.endSprite = positions.length -1;
    
    for (int i = 0; i < positions.length; i++) {
      sprites[i] = new Sprite(sheet, positions[i].x, positions[i].y, positions[i].w, positions[i].h);
    }
  }
  
  public AnimatedSprite(SpriteSheet sheet, int speed) {
    sprites = sheet.getLoadSprites();
    this.speed = speed;
    this.endSprite = sprites.length -1;
  }
  
  //speed represents how many frames pass until sprite changes
  public AnimatedSprite(BufferedImage[] images, int speed) {
    this.endSprite = images.length -1;
    sprites = new Sprite[images.length];
    this.speed = speed;
    
    for (int i = 0; i < images.length; i++) {
      sprites[i] = new Sprite(images[i]);
    }
  }
  
  public void setAnimationRange(int startSprite, int endSprite){
    this.startSprite = startSprite;
    this.endSprite = endSprite;
    reset();
  }
  
  public void reset(){
    counter = 0;
    currentSprite = startSprite;
  }
  
  public int getWidth() {
    return sprites[currentSprite].getWidth();
  }
  
  public int getHeight() {
    return sprites[currentSprite].height;
  }
  
  public int[] getPixels() {
    return sprites[currentSprite].getPixels();
  }
  
  //Render is dealt specifically with the Layer class
  public void render(RenderHandler renderer, int xZoom, int yZoom) {
  
  }
  
  //call whatever mouse is clicked on Canvas
  public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera,  int xZoom, int yZoom){return false;}
  
  public void update(Game game) {
    counter++;
    if (counter >= speed) {
      counter = 0;
      incrementSprite();
    }
  }
  
  public void incrementSprite() {
    currentSprite++;
    if (currentSprite >= endSprite) {
      currentSprite = startSprite;
    }
  }
}

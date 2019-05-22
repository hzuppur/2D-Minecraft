public class MapObject implements GameObject {
  private SpriteSheet sheet;
  private Rectangle objectRect;
  private Rectangle spriteRect;
  private Rectangle hitBox;
  private int tileWidth;
  private int tileHeight;
  private int xZoom;
  private int yZoom;
  private int tileIDX;
  private int tileIDY;
  private int ID;
  
  public MapObject(SpriteSheet sheet, Rectangle spriteRect, int tileSize, int xPos, int yPos, int xZoom, int yZoom, int ID) {
    this.tileWidth = tileSize * xZoom;
    this.tileHeight = tileSize * yZoom;
    this.sheet = sheet;
    this.spriteRect = spriteRect;
    this.xZoom = xZoom;
    this.yZoom = yZoom;
    this.ID = ID;
    this.tileIDX = xPos;
    this.tileIDY = yPos;
    
    objectRect = new Rectangle(xPos * tileWidth - (spriteRect.w - 1) * tileWidth, yPos * tileHeight - (spriteRect.h - 1) * tileHeight, spriteRect.w * tileSize, spriteRect.h * tileSize);
    objectRect.generateGraphics(3, 0xFF0000);
  }
  
  public void setHitBox(int inSpriteX, int inSpriteY, int width, int height) {
    hitBox = new Rectangle(objectRect.x + inSpriteX, objectRect.y + inSpriteY, width * xZoom, height * yZoom);
    hitBox.generateGraphics(1, 0xFF0000);
  }
  
  public Rectangle getObjectRect() {
    return objectRect;
  }
  
  public Rectangle getHitBox() {
    return hitBox;
  }
  
  public boolean checkIfselected(int xPos, int yPos) {
    if (xPos == tileIDX && yPos == tileIDY)
      return true;
    return false;
  }
  
  public void render(RenderHandler renderer, int xZoom, int yZoom) {
    for (int y = 0; y < spriteRect.h; y++) {
      for (int x = 0; x < spriteRect.w; x++) {
        renderer.renderSprite(sheet.getSprite(spriteRect.x + x, spriteRect.y + y), objectRect.x + x * tileWidth, objectRect.y + y * tileHeight, xZoom, yZoom, false);
      }
    }
    //renderer.renderRectangle(hitBox, xZoom, yZoom, false);
  }
  
  public void update(Game game) {
  
  }
  
  public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
    return false;
  }
  
  @Override
  public String toString() {
    return "object," + tileIDX + "," + tileIDY + "," + ID;
  }
}

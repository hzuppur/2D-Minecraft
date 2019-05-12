public class Player implements GameObject {
  private Rectangle playerRectangel;
  private int speed = 10;
  private Sprite sprite;
  private AnimatedSprite animatedSprite = null;
  //0-down, 1-left, 2-right, 3-up
  private int direction = 0;
  
  public Player(Sprite sprite) {
    this.sprite = sprite;
    if (sprite != null && sprite instanceof AnimatedSprite) {
      animatedSprite = (AnimatedSprite) sprite;
    }
    updateDirection();
    playerRectangel = new Rectangle(32, 16, 16, 32);
    playerRectangel.generateGraphics(3, 0xFF0000);
  }
  
  private void updateDirection() {
    if (animatedSprite != null) {
      // 8-is the number of sprites right now in one moving animation
      animatedSprite.setAnimationRange(direction * 4, direction * 4 + 3);
    }
  }
  
  //call every time physicaly possible
  public void render(RenderHandler renderer, int xZoom, int yZoom) {
    if (animatedSprite != null){
      renderer.renderSprite(animatedSprite, playerRectangel.x, playerRectangel.y, xZoom, yZoom, false);
    }else if (sprite != null){
      renderer.renderSprite(sprite, playerRectangel.x, playerRectangel.y, xZoom, yZoom, false);
    }else {
      renderer.renderRectangle(playerRectangel, xZoom, yZoom, false);
    }
  }
  
  //call whatever mouse is clicked on Canvas
  public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom){return false;}
  
  //call at 60 fps rate
  public void update(Game game) {
    KeyBoardListener keyListener = game.getKeyListener();
    boolean didMoved = false;
    int newDirection = direction;
    
    if (keyListener.left()) {
      playerRectangel.x -= speed;
      didMoved = true;
      newDirection = 1;
    }
    if (keyListener.right()) {
      playerRectangel.x += speed;
      didMoved = true;
      newDirection = 2;
    }
    if (keyListener.up()) {
      playerRectangel.y -= speed;
      didMoved = true;
      newDirection = 3;
    }
    if (keyListener.down()) {
      playerRectangel.y += speed;
      didMoved = true;
      newDirection = 0;
    }
    
    if (newDirection != direction){
      direction = newDirection;
      updateDirection();
    }
    uptadeCamera(game.getRenderer().getCamera());
    if(didMoved){
      animatedSprite.update(game);
    }else {
      animatedSprite.reset();
    }
    
  }
  
  public void uptadeCamera(Rectangle camera) {
    camera.x = playerRectangel.x - (camera.w / 2);
    camera.y = playerRectangel.y - (camera.h / 2);
  }
}

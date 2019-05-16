public class StartScreen extends GUI {
  public StartScreen(Sprite backgroundSprite, GUIButton[] buttons, int x, int y, boolean fixed) {
    super(backgroundSprite, buttons, x, y, fixed);
  }
  
  public StartScreen(GUIButton[] buttons, int x, int y, boolean fixed) {
    super(buttons, x, y, fixed);
  }
  
  public void render(RenderHandler renderer, int xZoom, int yZoom) {
    if (backgroundSprite != null)
      renderer.renderSprite(backgroundSprite, rect.x, rect.y, xZoom, yZoom, fixed);
    
    if (buttons != null)
      for (int i = 0; i < buttons.length; i++)
        buttons[i].render(renderer, xZoom, yZoom, rect);
  }
  
  public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
    boolean stopChecking = false;
    
    mouseRectangle = new Rectangle(mouseRectangle.x, mouseRectangle.y, 1, 1);
    
    if (rect.w == 0 || rect.h == 0 || mouseRectangle.intersects(rect)) {
      mouseRectangle.x -= rect.x;
      mouseRectangle.y -= rect.y;
      
      for (int i = 0; i < buttons.length; i++) {
        
        boolean result = buttons[i].handleMouseClick(mouseRectangle, camera, xZoom, yZoom);
        if (!stopChecking)
          stopChecking = result;
      }
    }
    
    return stopChecking;
  }
}

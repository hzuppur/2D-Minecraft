public interface GameObject {
  
  //call every time physicaly possible
  public void render(RenderHandler renderer, int xZoom, int yZoom);
  
  //call at 60 fps rate
  public void update(Game game);
  
  //call whatever mouse is clicked on Canvas
  //return true to stop checking other clicks
  public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom);
}

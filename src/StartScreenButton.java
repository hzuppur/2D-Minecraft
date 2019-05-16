public class StartScreenButton extends GUIButton {
  Game game;
  
  public StartScreenButton(Sprite sprite, Rectangle rect, boolean fixed, Game game) {
    super(sprite, rect, fixed);
    this.game = game;
    rect.generateGraphics(3, 0xFF0000);
  }
  
  @Override
  public void activate() {
    game.setInStart(false);
  }
  @Override
  public void render(RenderHandler renderer, int xZoom, int yZoom) {
    renderer.renderSprite(sprite, 0, 0, xZoom, yZoom, true);
    renderer.renderRectangle(rect, xZoom, yZoom, true);
  }
}

public class SDKButton extends GUIButton {
  private Game game;
  private int tileID;
  private boolean isSelected = false;
  
  public SDKButton(Game game, int tileID, Sprite tileSprite, Rectangle rect) {
    super(tileSprite, rect, true);
    this.game = game;
    this.tileID = tileID;
    rect.generateGraphics(0xFFDB3D);
  }
  
  @Override
  public void update(Game game){
    if(tileID == game.getSelectedTile()){
      if(!isSelected){
        rect.generateGraphics(0x66FF3D);
        isSelected = true;
      }
    }else {
      if(isSelected){
        rect.generateGraphics(0xFFDB3D);
        isSelected = false;
      }
    }
  }
  
  @Override
  public void render(RenderHandler renderer, int xZoom, int yZoom, Rectangle interfaceRect) {
    renderer.renderRectangle(rect, interfaceRect, 1, 1, fixed);
    
    if (2 - sprite.height / 32 < 2- sprite.width /32) {
      renderer.renderSprite(sprite,
              rect.x + interfaceRect.x + (xZoom - (xZoom - 1)) * rect.w / 2 / xZoom,
              rect.y + interfaceRect.y + (yZoom - (yZoom - 1)) * rect.h / 2 / yZoom,
              2 - sprite.height / 32,
              2 - sprite.height / 32,
              fixed);
    }else {
      renderer.renderSprite(sprite,
              rect.x + interfaceRect.x + (xZoom - (xZoom - 1)) * rect.w / 2 / xZoom,
              rect.y + interfaceRect.y + (yZoom - (yZoom - 1)) * rect.h / 2 / yZoom,
              0 - sprite.width / 32,
              0 - sprite.width / 32,
              fixed);
    }
  }
  
  public void activate() {
    game.changeTile(tileID);
  }
}

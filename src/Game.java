import java.awt.Canvas;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Runnable;
import java.lang.Thread;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends JFrame implements Runnable {
  public static int alpha = 0xFFFF00DC;
  
  private Canvas canvas = new Canvas();
  private RenderHandler renderer;
  
  private SpriteSheet sheet;
  private SpriteSheet playerSheet;
  
  private int selectedTileID = 2;
  
  private Tiles tiles;
  private Map map;
  
  private GameObject[] objects;
  private KeyBoardListener keyListener = new KeyBoardListener(this);
  private MouseEventListener mouseListener = new MouseEventListener(this);
  
  private Player player;
  
  private int xZoom = 3;
  private int yZoom = 3;
  
  public Game() {
    //Make our program shutdown when we exit out.
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    //Set the position and size of our frame.
    setBounds(0, 0, 1000, 800);
    
    //Put our frame in the center of the screen.
    setLocationRelativeTo(null);
    
    //Add our graphics compoent
    add(canvas);
    
    //Make our frame visible.
    setVisible(true);
    
    //Create our object for buffer strategy.
    canvas.createBufferStrategy(3);
    
    renderer = new RenderHandler(getWidth(), getHeight());
    //load Assets
    BufferedImage sheetImage = loadImage("recourses\\TileSheets\\Tiles1.png");
    sheet = new SpriteSheet(sheetImage);
    sheet.loadSprites(16, 16);
    
    //BufferedImage playerSheetImage = loadImage("recourses\\Player.png");
    BufferedImage playerSheetImage = loadImage("recourses\\Players\\rincewind.png");
    playerSheet = new SpriteSheet(playerSheetImage);
    playerSheet.loadSprites(32, 48);
    
    //player animated sprites
    AnimatedSprite playerAnimations = new AnimatedSprite(playerSheet, 5);
    
    //load tiles
    tiles = new Tiles(new File("Tiles.txt"), sheet);
    
    //load map
    map = new Map(new File("Map.txt"), tiles);
    
    //load SDK GUI
    GUIButton[] buttons = new GUIButton[tiles.size()];
    Sprite[] tileSprites = tiles.getSprites();
    for (int i = 0; i < buttons.length; i++) {
      // Rectangle(0, i * (16 * xZoom + 1), 16, 16) in here the +1 is margin between each square
      Rectangle tileRectangle = new Rectangle(0, i * (16 * xZoom + 1), 16*xZoom, 16*yZoom);
      
      buttons[i] = new SDKButton(this, i, tileSprites[i], tileRectangle);
    }
    GUI gui = new GUI(buttons, 5, 5, true);
    
    
    //load Objectsd
    objects = new GameObject[2];
    player = new Player(playerAnimations);
    objects[0] = player;
    objects[1] = gui;
    
    
    //Add listeners
    canvas.addKeyListener(keyListener);
    canvas.addFocusListener(keyListener);
    canvas.addMouseListener(mouseListener);
    canvas.addMouseMotionListener(mouseListener);
    
    //testImage = loadImage("GrassTile.png");
    //testSprite = sheet.getSprite(4, 2);
    //testRectangle.generateGraphics(5, 0xFF0000);
  }
  
  
  public void update() {
    for (int i = 0; i < objects.length; i++) {
      objects[i].update(this);
    }
  }
  
  private BufferedImage loadImage(String path) {
    try {
      BufferedImage loadedImage = ImageIO.read(Game.class.getResource(path));
      BufferedImage formattedImage = new BufferedImage(loadedImage.getWidth(), loadedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
      formattedImage.getGraphics().drawImage(loadedImage, 0, 0, null);
      
      return formattedImage;
    } catch (IOException exception) {
      exception.printStackTrace();
      return null;
    }
  }
  
  public void leftClick(int x, int y) {
    Rectangle mouseRectangle = new Rectangle(x, y, 1, 1);
    boolean stopChecking = false;
    
    for (int i = 0; i < objects.length; i++) {
      if(!stopChecking)
        stopChecking = objects[i].handleMouseClick(mouseRectangle, renderer.getCamera(), xZoom, yZoom);
    }
    
    if(!stopChecking) {
      x = (int) Math.floor((x + renderer.getCamera().x) / (16.0 * xZoom));
      y = (int) Math.floor((y + renderer.getCamera().y) / (16.0 * yZoom));
      map.setTile(x, y, selectedTileID);
    }
  }
  
  public void rightClick(int x, int y) {
    x = (int) Math.floor((x + renderer.getCamera().x) / (16.0 * xZoom));
    y = (int) Math.floor((y + renderer.getCamera().y) / (16.0 * yZoom));
    map.removeTile(x, y);
  }
  
  public void handleCTRL(boolean[] keys) {
    if (keys[KeyEvent.VK_S]) {
      map.saveMap();
    }
  }
  
  
  public void render() {
    BufferStrategy bufferStrategy = canvas.getBufferStrategy();
    Graphics graphics = bufferStrategy.getDrawGraphics();
    super.paint(graphics);
    
    map.render(renderer, xZoom, yZoom);
    for (int i = 0; i < objects.length; i++) {
      objects[i].render(renderer, xZoom, xZoom);
    }
    
    renderer.render(graphics);
    
    graphics.dispose();
    bufferStrategy.show();
    renderer.clear();
    
    //renderer.renderImage(testImage, 0, 0, 5, 5);
    //renderer.renderSprite(testSprite, 300, 300, 10, 10);
    //renderer.renderRectangle(testRectangle, 1, 1);
    //tiles.renderTile(2, renderer, 0,0,5,5);
  }
  
  public void changeTile(int tileID) {
    selectedTileID = tileID;
  }
  
  public int getSelectedTile(){
    return selectedTileID;
  }
  
  public void run() {
    BufferStrategy bufferStrategy = canvas.getBufferStrategy();
    int i = 0;
    int x = 0;
    
    long lastTime = System.nanoTime(); //long 2^63
    double nanoSecondConversion = 1000000000.0 / 60; //60 frames per second
    double changeInSeconds = 0;
    
    while (true) {
      long now = System.nanoTime();
      
      changeInSeconds += (now - lastTime) / nanoSecondConversion;
      while (changeInSeconds >= 1) {
        update();
        changeInSeconds--;
      }
      
      render();
      lastTime = now;
    }
  }
  
  public KeyBoardListener getKeyListener() {
    return keyListener;
  }
  
  public MouseEventListener getMouseListener() {
    return mouseListener;
  }
  
  public RenderHandler getRenderer() {
    return renderer;
  }
  
  public static void main(String[] args) {
    Game game = new Game();
    Thread gameThread = new Thread(game);
    gameThread.start();
  }
  
}

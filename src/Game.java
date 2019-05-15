import java.awt.Canvas;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Runnable;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends JFrame implements Runnable {
  public static int alpha = 0xFFFF00DC;
  
  private Canvas canvas = new Canvas();
  private RenderHandler renderer;
  
  private SpriteSheet sheet;
  private SpriteSheet playerSheet;
  
  private int selectedTileID = 0;
  
  private Tiles tiles;
  private WorldMap worldMap;
  private SDK sdk;
  
  private List<GameObject> objects;
  private List<MapObject> mapObjects;
  private Map MapObjectsMap;
  private KeyBoardListener keyListener = new KeyBoardListener(this);
  private MouseEventListener mouseListener = new MouseEventListener(this);
  
  private Player player;
  
  private int xZoom = 2;
  private int yZoom = 2;
  private int tileSize = 32;
  
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
    BufferedImage sheetImage = loadImage("recourses\\TileSheets\\SpriteSheet.png");
    sheet = new SpriteSheet(sheetImage);
    sheet.loadSprites(tileSize, tileSize);
    
    BufferedImage playerSheetImage = loadImage("recourses\\Players\\rincewind.png");
    playerSheet = new SpriteSheet(playerSheetImage);
    playerSheet.loadSprites(32, 48);
    
    //player animated sprites
    AnimatedSprite playerAnimations = new AnimatedSprite(playerSheet, 5);
    
    //load tiles
    tiles = new Tiles(new File("Tiles.txt"), sheet);
    
    //load worldMap
    mapObjects = new ArrayList<>();
    worldMap = new WorldMap(new File("Map.txt"), tiles, tileSize, this);
    
    //load SDK GUI
    sdk = new SDK(new File("SKD_Buttons.txt"), tileSize, xZoom, yZoom, this, tiles.getSprites(), sheet);
    
    
    //load Objectsd
    objects = new ArrayList<>();
    player = new Player(playerAnimations, this);
    objects.add(player);
    objects.add(sdk.getGui());
    MapObjectsMap = sdk.getMapObjects();
    worldMap.loadObjects();
    
    
    //Add listeners
    canvas.addKeyListener(keyListener);
    canvas.addFocusListener(keyListener);
    canvas.addMouseListener(mouseListener);
    canvas.addMouseMotionListener(mouseListener);
    canvas.addMouseWheelListener(mouseListener);
  }
  
  
  public void update() {
    for (int i = 0; i < objects.size(); i++) {
      objects.get(i).update(this);
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
    
    for (int i = 0; i < objects.size(); i++) {
      if (!stopChecking)
        stopChecking = objects.get(i).handleMouseClick(mouseRectangle, renderer.getCamera(), xZoom, yZoom);
    }
    
    if (!stopChecking) {
      
      x = (int) Math.floor((x + renderer.getCamera().x) / ((double) tileSize * xZoom));
      y = (int) Math.floor((y + renderer.getCamera().y) / ((double) tileSize * yZoom));
      
      
      if (MapObjectsMap.containsKey(selectedTileID)) {
        placeObject(x, y, selectedTileID);
      } else {
        worldMap.setTile(x, y, sdk.getSpriteID(selectedTileID));
      }
    }
  }
  
  public void rightClick(int x, int y) {
    boolean clickedOnObject = false;
    x = (int) Math.floor((x + renderer.getCamera().x) / ((double) tileSize * xZoom));
    y = (int) Math.floor((y + renderer.getCamera().y) / ((double) tileSize * yZoom));
    
    for (int i = 0; i < mapObjects.size(); i++) {
      if (mapObjects.get(i).checkIfselected(x, y)) {
        mapObjects.remove(i);
        clickedOnObject = true;
        break;
      }
    }
    if (!clickedOnObject) {
      worldMap.removeTile(x, y);
    }
  }
  
  public void mouseWeel(boolean up) {
    if (up)
      sdk.setScroll(tileSize);
    else
      sdk.setScroll(-tileSize);
  }
  
  public void handleCTRL(boolean[] keys) {
    if (keys[KeyEvent.VK_S]) {
      worldMap.saveMap();
    }
  }
  
  public void placeObject(int x, int y, int selectedTileID) {
    int[] currentObjectValues = (int[]) MapObjectsMap.get(selectedTileID);
    
    MapObject currentObject = new MapObject(sheet, new Rectangle(currentObjectValues[0], currentObjectValues[1], currentObjectValues[2], currentObjectValues[3]), tileSize, x, y, xZoom, yZoom, selectedTileID);
  
    currentObject.setHitBox(currentObjectValues[4] * xZoom, currentObjectValues[5] * yZoom, currentObjectValues[6], currentObjectValues[7]);
    
    mapObjects.add(currentObject);
  }
  
  
  public void render() {
    BufferStrategy bufferStrategy = canvas.getBufferStrategy();
    Graphics graphics = bufferStrategy.getDrawGraphics();
    super.paint(graphics);
    
    worldMap.render(renderer, xZoom, yZoom);
    
    for (int i = 0; i < mapObjects.size(); i++) {
      mapObjects.get(i).render(renderer, xZoom, xZoom);
    }
    
    for (int i = 0; i < objects.size(); i++) {
      objects.get(i).render(renderer, xZoom, xZoom);
    }
    
    renderer.render(graphics);
    
    graphics.dispose();
    bufferStrategy.show();
    renderer.clear();
    
  }
  
  public void changeTile(int tileID) {
    selectedTileID = tileID;
  }
  
  public int getSelectedTile() {
    return selectedTileID;
  }
  
  public List<MapObject> getMapObjects() {
    return mapObjects;
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
  
  public int getxZoom() {
    return xZoom;
  }
  
  public int getyZoom() {
    return yZoom;
  }
  
  public static void main(String[] args) {
    Game game = new Game();
    Thread gameThread = new Thread(game);
    gameThread.start();
  }
  
}

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.HashMap;
import java.util.Map;

public class SDK {
  private int tileSize;
  private int xZoom;
  private int yZoom;
  private Game game;
  private Sprite[] tileSprites;
  private SpriteSheet sheet;
  private List<Integer> spriteID = new ArrayList<>();
  private Map<Integer, int[]> MapObjects = new HashMap<>();
  int x = 0;
  int y = 0;
  
  private GUI gui;
  
  public SDK(File buttonsFile, int tileSize, int xZoom, int yZoom, Game game, Sprite[] tileSprites, SpriteSheet sheet) {
    this.tileSize = tileSize;
    this.xZoom = xZoom;
    this.yZoom = yZoom;
    this.game = game;
    this.tileSprites = tileSprites;
    this.sheet = sheet;
    
    try {
      Scanner scanner = new Scanner(buttonsFile);
      int i = 0;
      List<SDKButton> buttons = new ArrayList<>();
      Sprite icon = null;
      
      while (scanner.hasNextLine()) {
        //read each line and create a tile
        String line = scanner.nextLine();
        //does not read comments in file
        if (!line.startsWith("//") && !line.equals("")) {
          String[] splitLine = line.split("-");
          if (splitLine.length == 10) {
            MapObjects.put(Integer.parseInt(splitLine[1]),
                    new int[]{
                    Integer.parseInt(splitLine[2]),
                    Integer.parseInt(splitLine[3]),
                    Integer.parseInt(splitLine[4]),
                    Integer.parseInt(splitLine[5]),
                    Integer.parseInt(splitLine[6]),
                    Integer.parseInt(splitLine[7]),
                    Integer.parseInt(splitLine[8]),
                    Integer.parseInt(splitLine[9])});
  
            icon = new Sprite(sheet,
                    Integer.parseInt(splitLine[2])*tileSize,
                    Integer.parseInt(splitLine[3])*tileSize,
                    Integer.parseInt(splitLine[4])*tileSize,
                    Integer.parseInt(splitLine[5])*tileSize);
            
          }
          Rectangle tileRectangle = new Rectangle(x, y + i * (tileSize * xZoom + 1), tileSize * xZoom, tileSize * yZoom);
          //buttons.add(new SDKButton(game, Integer.parseInt(splitLine[1]), tileSprites[Integer.parseInt(splitLine[2])], tileRectangle));
          if (icon == null)
            buttons.add(new SDKButton(game, Integer.parseInt(splitLine[1]), tileSprites[i], tileRectangle));
          else
            buttons.add(new SDKButton(game, Integer.parseInt(splitLine[1]), icon, tileRectangle));
          
          spriteID.add(Integer.parseInt(splitLine[2]));
          
          i++;
        }
      }
      SDKButton[] buttonsArray = new SDKButton[buttons.size()];
      buttonsArray = buttons.toArray(buttonsArray);
      
      gui = new GUI(buttonsArray, 5, 5, true);
      
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
  
  public GUI getGui() {
    return gui;
  }
  
  public int getSpriteID(int ID) {
    return spriteID.get(ID);
  }
  
  public Map<Integer, int[]> getMapObjects() {
    return MapObjects;
  }
  
  public void setScroll(int scrollAmout) {
    gui.setScroll(scrollAmout);
  }
}

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SDK {
  private int tileSize;
  private int xZoom;
  private int yZoom;
  private Game game;
  private Sprite[] tileSprites;
  private List<Integer> spriteID = new ArrayList<>();
  int x = 0;
  int y = 0;
  
  private GUI gui;
  
  public SDK(File buttonsFile, int tileSize, int xZoom, int yZoom, Game game, Sprite[] tileSprites){
    this.tileSize = tileSize;
    this.xZoom = xZoom;
    this.yZoom = yZoom;
    this.game = game;
    this.tileSprites = tileSprites;
    
    try {
      Scanner scanner = new Scanner(buttonsFile);
      int i = 0;
      List<SDKButton> buttons = new ArrayList<>();
      
      while (scanner.hasNextLine()) {
        //read each line and create a tile
        String line = scanner.nextLine();
        //does not read comments in file
        if (!line.startsWith("//") && !line.equals("")) {
          String[] splitLine = line.split("-");
          Rectangle tileRectangle = new Rectangle(x, y + i * (tileSize * xZoom + 1), tileSize * xZoom, tileSize * yZoom);
          buttons.add(new SDKButton(game, Integer.parseInt(splitLine[1]), tileSprites[Integer.parseInt(splitLine[2])], tileRectangle));
          
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
  
  public void setScroll(int scrollAmout) {
    gui.setScroll(scrollAmout);
  }
}

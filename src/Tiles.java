import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Tiles {

    private SpriteSheet spriteSheet;
    private List<Tile> tilesList = new ArrayList<>();

    //This will only work when sprites in spritesheet have been loaded
    public Tiles(File tilesFile, SpriteSheet spriteSheet){
        this.spriteSheet = spriteSheet;
        Scanner scanner = null;
        try {
            scanner = new Scanner(tilesFile);
            while (scanner.hasNextLine()){
                //read each line and create a tile
                String line = scanner.nextLine();
                //does not read comments in file
                if (!line.startsWith("//")){
                    String[] splitString = line.split("-");
                    String tileName = splitString[0];
                    int spriteX = Integer.parseInt(splitString[1]);
                    int spriteY = Integer.parseInt(splitString[2]);
                    Tile tile = new Tile(tileName, spriteSheet.getSprite(spriteX,spriteY));
                    tilesList.add(tile);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    public void renderTile(int tileID, RenderHandler renderer, int xPos, int yPos, int xZoom, int yZoom){
        if(tileID >= 0 && tilesList.size() > tileID){
            renderer.renderSprite(tilesList.get(tileID).sprite, xPos, yPos, xZoom, yZoom, false);
        }else {
            System.out.println("TileID " + tileID + "is not in range of tilesList" + tilesList.size());
        }
    }
    
    public int size(){
        return tilesList.size();
    }
    
    public Sprite[] getSprites(){
        Sprite[] sprites = new Sprite[size()];
        for (int i = 0; i < sprites.length; i++) {
            sprites[i] = tilesList.get(i).sprite;
        }
        return sprites;
    }

    class Tile{
        public String tileName;
        public Sprite sprite;

        public Tile(String tileName, Sprite sprite){
            this.tileName = tileName;
            this.sprite = sprite;
        }
    }
}

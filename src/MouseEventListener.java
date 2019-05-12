import java.awt.event.*;

public class MouseEventListener implements MouseListener, MouseMotionListener, MouseWheelListener {
  private Game game;
  
  public MouseEventListener(Game game) {
    this.game = game;
  }
  
  public void mouseClicked(MouseEvent e) {
  
  }
  
  public void mousePressed(MouseEvent e) {
    if (e.getButton() == MouseEvent.BUTTON1) {
      game.leftClick(e.getX(), e.getY());
    }
    if (e.getButton() == MouseEvent.BUTTON3) {
      game.rightClick(e.getX(), e.getY());
    }
  }
  
  public void mouseReleased(MouseEvent e) {
  
  }
  
  public void mouseEntered(MouseEvent e) {
  
  }
  
  public void mouseExited(MouseEvent e) {
  
  }
  
  public void mouseDragged(MouseEvent e) {
  
  }
  
  public void mouseMoved(MouseEvent e) {
  
  }
  
  public void mouseWheelMoved(MouseWheelEvent e) {
    int notches = e.getWheelRotation();
    
    if (notches < 0) {
      game.mouseWeel(true);
    } else {
      game.mouseWeel(false);
    }
  }
}

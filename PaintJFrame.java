import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**Custom JFrame Class that allows a user to paint and erase basic scribbles and rectangles on their screen.
 * Utilizes ArrayLists and Matrices to store users previous and latest input to circumvent the use of super.paintComponent. 
 * Every call of repaint utilizes the stored coordinates to paint scribbles and rectangles on the users screen.
 * @author Daniel Kiousis
 * @version 1.0
 * @since 01/05/2024
 */
public class PaintJFrame extends JFrame {
	private static final long serialVersionUID = -3524814533491947282L;
	
	private DrawingPanel p;
	private JMenuBar menuBar;
	private JMenu colorMenu;
	private JMenu eraseOrClearMenu;
	private JMenu shapeMenu;
	private JMenuItem black;
	private JMenuItem red;
	private JMenuItem yellow;
	private JMenuItem blue;
	private JMenuItem green;
	private JMenuItem startErase;
	private JMenuItem endErase;
	private JMenuItem clear;
	private JMenuItem rectangle;
	private Color color;
	private Color colorTemp;
	private ArrayList<Integer> x;
	private ArrayList<Integer> y;
	private ArrayList<ArrayList<Integer>> xMatrix;
	private ArrayList<ArrayList<Integer>> yMatrix;
	private ArrayList<Integer> indexList;
	private ArrayList<ArrayList<Integer>> indexMatrix;
	private int numRectangles;
	private ArrayList<Integer> numRectList;
	private final int size = 6;
	private ArrayList<Color> colorMemory;
	private boolean erasing;
	private boolean painting;
	
	/**Constructs a PaintJFrame and initializes private instance variables and menu items.
	 * Creates and implements an actionListener for menu items and a panel for user to paint on.
	 */
	PaintJFrame()
	{
		setSize(1020,820);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		
		color = Color.BLACK;
		
		ActionListener menuListener = new InnerActionListener();
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		colorMenu = new JMenu("Color");
		menuBar.add(colorMenu);
		black = new JMenuItem("Black");
		black.addActionListener(menuListener);
		colorMenu.add(black);
		red = new JMenuItem("Red");
		red.addActionListener(menuListener);
		colorMenu.add(red);
		yellow = new JMenuItem("Yellow");
		yellow.addActionListener(menuListener);
		colorMenu.add(yellow);
		blue = new JMenuItem("Blue");
		blue.addActionListener(menuListener);
		colorMenu.add(blue);
		green = new JMenuItem("Green");
		green.addActionListener(menuListener);
		colorMenu.add(green);
		
		eraseOrClearMenu = new JMenu("Erase and Clear Options");
		menuBar.add(eraseOrClearMenu);
		startErase = new JMenuItem("Start Erasing");
		startErase.addActionListener(menuListener);
		eraseOrClearMenu.add(startErase); 
		endErase = new JMenuItem("Stop Erasing");
		endErase.addActionListener(menuListener);
		eraseOrClearMenu.add(endErase);
		clear = new JMenuItem("Clear All Drawings");
		clear.addActionListener(menuListener);
		eraseOrClearMenu.add(clear); 
		erasing = false;
		colorTemp = Color.BLACK;
		
		shapeMenu = new JMenu("Shape Options");
		menuBar.add(shapeMenu);
		rectangle = new JMenuItem("Rectangle");
		rectangle.addActionListener(menuListener);
		shapeMenu.add(rectangle);
		painting = true;
		
		colorMemory = new ArrayList<Color>();

		p = new DrawingPanel();
		add(p);
		setVisible(true);
	}
	
	/**Main method that initializes a PaintJFrame object and gives it a title.
	 * @param args
	 */
	public static void main(String[] args)
	{
		PaintJFrame frame = new PaintJFrame();
		frame.setTitle("Paint");
	}
	
	/**Inner class that functions as an actionListnener for any PaintJFrame menu buttons.
	 */
	class InnerActionListener implements ActionListener
	{

		/**Senses and executes depending on which menu item performed an action.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			String str = e.getActionCommand();
			
			if(str.equals("Rectangle") && painting == true)
			{
				painting = false;
			}
			else
			{
				xMatrix.add(new ArrayList<Integer>(x));
				yMatrix.add(new ArrayList<Integer>(y));
				indexMatrix.add(new ArrayList<Integer>(indexList));
				numRectList.add(numRectangles);
				numRectangles = 0;
				x.clear();
				y.clear();
				indexList.clear();
			}
			
			if(str.equals("Black"))
			{
				color = Color.BLACK;
				erasing = false;
				painting = true;
			}
			if(str.equals("Red"))
			{
				color = Color.RED;
				erasing = false;
				painting = true;
			}
			if(str.equals("Yellow"))
			{
				color = Color.YELLOW;
				erasing = false;
				painting = true;
			}
			if(str.equals("Blue"))
			{
				color = Color.BLUE;
				erasing = false;
				painting = true;
			}
			if(str.equals("Green"))
			{
				color = Color.GREEN;
				erasing = false;
				painting = true;
			}
			if(str.equals("Start Erasing"))
			{
				colorTemp = color;
				color = Color.WHITE;
				erasing = true;
				painting = true;
			}
			if(str.equals("Stop Erasing") && erasing == true)
			{
				color = colorTemp;
				painting = true;
			}
			
			if(!str.equals("Rectangle"))
				colorMemory.add(color);
			
			if(str.equals("Clear All Drawings"))
			{
				xMatrix.clear();
				yMatrix.clear();
				indexMatrix.clear();
				numRectList.clear();
				colorMemory.clear();
				colorMemory.add(color);
				p.repaint();
			}
		}
		
	}

	
	/**Inner class that creates a custom JPanel that allows a user to paint.
	 */
	class DrawingPanel extends JPanel {
		private static final long serialVersionUID = 6563099866835699484L;

		/**Constructor for the custom JPanel that initializes arrayLists and mouseActionListeners.
		 */
		DrawingPanel()
		{
			setBackground(Color.WHITE);
			x = new ArrayList<Integer>();
			y = new ArrayList<Integer>();
			xMatrix = new ArrayList<ArrayList<Integer>>();
			yMatrix = new ArrayList<ArrayList<Integer>>();
			
			colorMemory.add(color);
			
			indexList = new ArrayList<Integer>();
			indexMatrix = new ArrayList<ArrayList<Integer>>();
			numRectangles = 0;
			numRectList = new ArrayList<Integer>();
			
			MouseMotionListener mouseListener = new InnerMouseMotionListener();
			this.addMouseMotionListener(mouseListener);
			MouseListener mouseShapeListener = new InnerMouseListener();
			this.addMouseListener(mouseShapeListener);
		}
		
		/**Paints the users screen based on inputs stored in ArrayLists and ArrayList Matrices based on colors chosen.
		 * Allows super.paintComponent to be called while preserving users previous inputs and painting them.
		 */
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
			for(int j = 0; j < xMatrix.size(); j++)
			{
				int count = 0;
				
				for(int i = 0; i < xMatrix.get(j).size(); i++)
				{
					g.setColor(colorMemory.get(j));
					
					if(indexMatrix.get(j).size()<=numRectList.get(j) && !indexMatrix.get(j).isEmpty() && count!= indexMatrix.get(j).size() && i == indexMatrix.get(j).get(count))
					{
						int ind = indexMatrix.get(j).get(count);
						count++;
						int width = Math.abs(xMatrix.get(j).get(ind+1) - xMatrix.get(j).get(ind));
						int height = Math.abs(yMatrix.get(j).get(ind+1) - yMatrix.get(j).get(ind));
						int x1 = Math.min(xMatrix.get(j).get(ind),xMatrix.get(j).get(ind+1));
						int y1 = Math.min(yMatrix.get(j).get(ind),yMatrix.get(j).get(ind+1));
						g.fillRect(x1, y1, width, height);
						i++;
					}
					else
					{
						g.fillOval(xMatrix.get(j).get(i)-5, yMatrix.get(j).get(i)-5, size, size);
					}
				}
			}
			
			int count = 0;
			for(int i = 0; i < x.size(); i++)
			{
				g.setColor(color);
				
				if(indexList.size()<=numRectangles && !indexList.isEmpty() && count!= indexList.size() && i == indexList.get(count))
				{
					int ind = indexList.get(count);
					int width = Math.abs(x.get(ind+1) - x.get(ind));
					int height = Math.abs(y.get(ind+1) - y.get(ind));
					int x1 = Math.min(x.get(ind),x.get(ind+1));
					int y1 = Math.min(y.get(ind),y.get(ind+1));
					g.fillRect(x1, y1, width, height);
					count++;
					i++;
				}
				else
				{
					g.fillOval(x.get(i)-5, y.get(i)-5, size, size);
				}
			}
		}

		
		/**Inner MouseMotionListener of and used by DrawingPanel
		 */
		class InnerMouseMotionListener implements MouseMotionListener
		{

			/**Senses when the user is dragging their mouse across their screen and stores these x and y coordinates-
			 * -to paint a scribble.
			 * Only occurs if the user is drawing scribbles and not rectangles.
			 */
			@Override
			public void mouseDragged(MouseEvent e) { 
				if(painting)
				{
					x.add(e.getX());
					y.add(e.getY());
					repaint();
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
			}
		}
		
		/**Inner MouseListener of and used by DrawingPanel
		 */
		class InnerMouseListener implements MouseListener
		{

			/**Paints a dot if a user clicks within the DrawingPanel.
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				if(painting)
				{
					x.add(e.getX());
					y.add(e.getY());
					repaint();
				}
			}

			/**Stores the coordinates of the first point where a user desires a rectangle to be painted.
			 * Only occurs if not drawing and scribbles and the Rectangle JMenuItem is chosen.
			 */
			@Override
			public void mousePressed(MouseEvent e) {
				if(!painting)
				{
					x.add(e.getX());
					y.add(e.getY());
					int index = x.indexOf(x.getLast());
					indexList.add(index);
				}
			}

			/**Senses when the user releases their mouse and stores the coordinates as the last point of a users desired-
			 *-rectangle. Also changes back from rectangle mode to scribble mode.
			 */
			@Override
			public void mouseReleased(MouseEvent e) {
				if(!painting)
				{
					x.add(e.getX());
					y.add(e.getY());
					numRectangles++;
					repaint();
				}
				painting = true;
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
			
		}
	}
}



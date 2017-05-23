import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.*;

public class AnimationFrame extends Frame implements CompareSwapListener, SortListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int numberOfValues, minValue, maxValue;
	private AnimationType animationType;
	
	private TextField[] numBox;
	private Rectangle[] rect;
	private MyArray<Integer> num;
	private Sorter sorter;
	private int previousI, previousJ;
	private Color defaultColor, currentColor;
	private ArrayList<SortListener> subscribers;
	private ArrayList<CompareSwapListener> compareSwapListeners;
	private int maxWidth, maxHeight;
	
	public AnimationFrame(String title, int numberOfValues, int min, int max, AnimationType t){
		super(title);
		
		this.numberOfValues = numberOfValues;
		this.minValue = min;
		this.maxValue = max;
		this.animationType = t;
		
		num = new MyArray<Integer>(true);
	
		Random rnd = new Random();
		
		int bound =  maxValue - minValue + 1;
		
		for(int i = 0; i < numberOfValues; i++)
		{
			Integer rdInt = rnd.nextInt(bound) + minValue;
			num.Append(rdInt);
		}
		
		defaultColor = Color.LIGHT_GRAY;
		currentColor = defaultColor;
		
		initializeLayout();
	
		addWindowListener(new MyWindowAdapter());
		subscribers = new ArrayList<SortListener>();
		subscribers.add(this);
		
		compareSwapListeners = new ArrayList<CompareSwapListener>();
		compareSwapListeners.add(this);
	}
	
	private void initializeLayout()
	{
		
		this.setLayout(new FlowLayout());
		
		if(animationType == AnimationType.Boxes)
		{
			maxWidth = 600;
			maxHeight = 200;
			
			this.setSize(maxWidth, maxHeight);
			
			setForeground(Color.BLACK);
			numBox = new TextField[numberOfValues];			
			for(int i = 0; i < numberOfValues; i++)
			{
				numBox[i] = new TextField();
				numBox[i].setColumns(5);
				numBox[i].setText(String.valueOf(num.At(i)));
				numBox[i].setEditable(false);
				numBox[i].setBackground(defaultColor);
				this.add(numBox[i]);
			}
			
		}
		
		else
		{
			setForeground(defaultColor);
			
			maxWidth = 500;
			maxHeight = 500;
			
			int X = 0;
			int minHeight;			
			int range = maxValue - minValue;
			
			if(range == 0)
				minHeight = 30;
			else
				minHeight = 0;
			
			int scaledRange = maxHeight - minHeight;
			
			if(numberOfValues > maxWidth)
				maxWidth = numberOfValues;
			
			int width = (int)Math.floor((maxWidth * 1.0f) / numberOfValues);
			maxWidth = width * numberOfValues;
			this.setSize(maxWidth, maxHeight);
			
			rect = new Rectangle[numberOfValues];
			for(int i = 0; i < numberOfValues; i++)
			{
				int height = num.At(i);
				int scaledHeight;
				
				if(range == 0)
					scaledHeight = minHeight;
				else
					scaledHeight = Math.round((((height - minValue) / (range * 1.0f)) * scaledRange) + minHeight);
				
				rect[i] = new Rectangle(X, maxHeight - scaledHeight, width, scaledHeight);
				X += width;
			}
		
			repaint();
			
		}
		
	}
		
	
	public void paint(Graphics g)
	{
		
		synchronized (g) {
			
			if(animationType == AnimationType.Bars)
			{
				for(Rectangle r : rect)
				{
					if(r.color != defaultColor)
					{
						g.setColor(r.color);
						g.fillRect(r.X, r.Y, r.width, r.height);
						g.setColor(defaultColor);
					}
					
					else
					{
						g.fillRect(r.X, r.Y, r.width, r.height);
					}

				}
			}
		}
		
		
	}
	
	public void iRepaint(int index)
	{
		iRepaint(rect[index]);
	}
	
	public void iRepaint(Rectangle r)
	{
		Graphics g = getGraphics();
		
		synchronized (g) {
			g.setColor(r.color);
			g.fillRect(r.X, r.Y, r.width, r.height);
		}
		
	}
	
	public static enum AnimationType
	{
		Bars,
		Boxes
	}
	
	public static enum SortType
	{
		Bubble,
		Quick
	}

	public void startButtonClicked(SortType sortType, int delay)
	{	
		if(delay >= 0)
			num.setDelay(delay);
		
		if(sorter == null)
		{
			sorter = new Sorter(sortType, num);
			
			for(SortListener sub : subscribers)
				sorter.addSortListener(sub);
			
			for(CompareSwapListener sub: compareSwapListeners)
				num.addActionListener(sub);
			
			sorter.start();
		}
		
		else
		{
			num.setWait(false);
			num.nextWait();
		}
		
	}
	
	public void stopButtonClicked()
	{
		if(sorter != null)
		{
			num.setWait(true);
		}
	}
	
	public void stepButtonClicked(SortType sortType)
	{
		if(sorter == null)
		{
			num.setWait(true);
			sorter = new Sorter(sortType, num);
			
			for(SortListener sub : subscribers)
				sorter.addSortListener(sub);
			
			for(CompareSwapListener sub: compareSwapListeners)
				num.addActionListener(sub);
			
			sorter.start();
		}
		
		else
		{
			num.nextWait();
		}
	}

	@Override
	public synchronized void ComparePerformed(int i, int j, int comparisions) {
		// TODO Auto-generated method stub
		
		currentColor = Color.ORANGE;
		
		if(animationType == AnimationType.Boxes)
		{
			numBox[previousI].setBackground(defaultColor);
			numBox[previousJ].setBackground(defaultColor);
			
			numBox[i].setBackground(currentColor);
			numBox[j].setBackground(currentColor);
		}
		
		else
		{
			rect[previousI].color = defaultColor;
			rect[previousJ].color = defaultColor;
			
			iRepaint(previousI);
			iRepaint(previousJ);
			
			rect[i].color = currentColor;	
			rect[j].color = currentColor;
			
			iRepaint(i);
			iRepaint(j);
			
			//repaint();
			
		}
		
		previousI = i;
		previousJ = j;
	}

	@Override
	public synchronized void SwapPerformed(int i, int j, int swaps) {
		// TODO Auto-generated method stub
		
		currentColor = Color.GREEN;
		
		if(animationType == AnimationType.Boxes)
		{
			numBox[previousI].setBackground(defaultColor);
			numBox[previousJ].setBackground(defaultColor);
			
			String temp = numBox[i].getText();
			numBox[i].setText(numBox[j].getText());
			numBox[j].setText(temp);
			
			numBox[i].setBackground(currentColor);
			numBox[j].setBackground(currentColor);
		}
		else
		{
			rect[previousI].color = defaultColor;
			rect[previousJ].color = defaultColor;
			
			iRepaint(previousI);
			iRepaint(previousJ);
			
			Rectangle ri = new Rectangle(rect[i].X, 0, rect[i].width, maxHeight, getBackground());
			Rectangle rj = new Rectangle(rect[j].X, 0, rect[j].width, maxHeight, getBackground());
			
			iRepaint(ri);
			iRepaint(rj);
			
			int y = rect[i].Y;
			int height = rect[i].height;
			
			rect[i].Y = rect[j].Y;
			rect[i].height = rect[j].height;
			
			rect[j].Y = y;
			rect[j].height = height;
			
			rect[i].color = currentColor;
			rect[j].color = currentColor;
			
			iRepaint(i);
			iRepaint(j);
			
		}

		previousI = i;
		previousJ = j;
	}

	@Override
	public void SortFinished() {
		// TODO Auto-generated method stub
		
		if(animationType == AnimationType.Boxes)
		{
			numBox[previousI].setBackground(defaultColor);
			numBox[previousJ].setBackground(defaultColor);
		}
		
		else
		{
			rect[previousI].color = defaultColor;
			rect[previousJ].color = defaultColor;
			repaint();
		}
		
		sorter = null;
	}
	
	public void addSortListner(SortListener obj)
	{
		if(!subscribers.contains(obj))
		{
			subscribers.add(obj);
		}
	}
	
	public void addCompareSwapListener(CompareSwapListener obj)
	{
		if(!compareSwapListeners.contains(obj))
			compareSwapListeners.add(obj);
	}

	
	
	class MyWindowAdapter extends WindowAdapter
	{
		
		public void windowClosing(WindowEvent we)
		{
			setVisible(false);
			cleanup();
		}
	}
	
	public void cleanup()
	{
		//sort is still running or waiting
		if(sorter != null)
		{
			num.disableDelay();
			num.setWait(false);
			num.nextWait();
			
			sorter.stop();
			sorter = null;
		}
	}
	
	class Rectangle
	{
		int X;
		int Y;
		int width;
		int height;
		Color color;
		
		public Rectangle(int X, int Y, int width, int height)
		{
			this(X,Y,width, height, defaultColor);
		}
		
		public Rectangle(int X, int Y, int width, int height, Color color)
		{
			this.X = X;
			this.Y = Y;
			this.width = width;
			this.height = height;
			this.color = color;
		}
		
		
	}
	
	public synchronized void animationChanged(AnimationType animation)
	{
		this.animationType = animation;
		
		if(animationType == AnimationType.Bars)
		{
			this.removeAll();
			initializeLayout();
			rect[previousI].color = currentColor;
			rect[previousJ].color = currentColor;
			iRepaint(previousI);
			iRepaint(previousJ);
			
		}
		
		else
		{
			repaint();
			initializeLayout();
			numBox[previousI].setBackground(currentColor);
			numBox[previousJ].setBackground(currentColor);
			//repaint();
		}
	}
	
	
}




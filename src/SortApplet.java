import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

public class SortApplet extends Applet implements ActionListener, ItemListener,SortListener, CompareSwapListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Label[] labels;
	private Button[] buttons;
	private TextField[] textFields;
	private Choice sortSelect;
	private Label animation;
	private Checkbox bars, boxes;
	private CheckboxGroup cbg;
	private AnimationFrame animationFrame;
	private int totalComparisions;
	private int totalSwaps;
	
	public void init()
	{
		initializeLayout();
		
		for(Button button : buttons)
		{
			button.addActionListener(this);
		}
		
		bars.addItemListener(this);
		boxes.addItemListener(this);
		
	}
	
	public void destroy()
	{
		if(animationFrame != null)
		{
			animationFrame.cleanup();
		}
	}
	
	public void paint(Graphics g)
	{
		showStatus("* = Mandatory Fields");
	}
	
	private void initializeLayout()
	{
		setSize(400,200);
		GridBagLayout gbag = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		setLayout(gbag);
		
		labels = new Label[4];
		
		labels[0] = new Label("No. of Values*");
		labels[1]= new Label("Min Value*");
		labels[2] = new Label("Max Value*");
		labels[3] = new Label("Delay");
		
		textFields = new TextField[4];
		for(int i = 0; i < 4; i++)
		{
			textFields[i] = new TextField();
			textFields[i].setColumns(3);
		}
		
		for(int i = 0; i <4; i++)
		{
			for(int j = 0; j < 2; j++)
			{
				gbc.gridx = j;
				gbc.gridy = i;
				
				if(j == 0)
				{
					gbc.insets = new Insets(10,10,10,0);
					gbag.setConstraints(labels[i], gbc);
				}
				else
				{
					gbc.insets = new Insets(10,0,10,10);
					gbag.setConstraints(textFields[i], gbc);
				}	
			}
		}
		
		
		buttons = new Button[4];
		buttons[0] = new Button("Populate");
		buttons[1] = new Button("Start");
		buttons[2] = new Button("Stop");
		buttons[3] = new Button("Step");
		
		for(int i = 1; i < 4; i++)
			buttons[i].setEnabled(false);
		
		for(int i = 0; i < 4; i++)
		{
			gbc.gridx = 7;
			gbc.gridy = i;
			gbc.insets = new Insets(10,10,10,10);
			gbag.setConstraints(buttons[i], gbc);
		}
		
		sortSelect = new Choice();
		sortSelect.add("Bubble Sort");
		sortSelect.add("Quick Sort");
		
		gbc.gridx = 4;
		gbc.gridy = 0;
		gbc.insets = new Insets(10,10,10,10);
		gbag.setConstraints(sortSelect, gbc);
		
		animation = new Label("Animation");
		gbc.gridx = 4;
		gbc.gridy = 1;
		gbc.insets =  new Insets(10,10,10,10);
		gbag.setConstraints(animation, gbc);
		 
		cbg = new CheckboxGroup();
		bars = new Checkbox("Bars", cbg, false);
		boxes = new Checkbox("Boxes", cbg, true);
		
		gbc.gridx = 4;
		gbc.gridy = 2;
		gbc.insets =  new Insets(10,10,10,10);
		gbag.setConstraints(bars, gbc);
		
		gbc.gridx = 4;
		gbc.gridy = 3;
		gbc.insets =  new Insets(10,10,10,10);
		gbag.setConstraints(boxes, gbc);
		
		
		for(int i = 0; i < 4; i++)
		{
			add(labels[i]);
			add(textFields[i]);
			add(buttons[i]);
		}
		
		add(sortSelect);
		add(animation);
		add(bars);
		add(boxes);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		Button source  = (Button)e.getSource();
		
		//populate button
		if(source == buttons[0])
		{
			if(animationFrame == null)
			{
				
				
				int numOfValues = 0;
				int minValue = 0;
				int maxValue = 0;
				
				try
				{
					numOfValues = Integer.parseInt(textFields[0].getText());
					minValue = Integer.parseInt(textFields[1].getText());
					maxValue = Integer.parseInt(textFields[2].getText());
					
					if(numOfValues < 1)
					{
						showStatus("Invalid Input: Number of values must be greater than 0");
						return;
					}
					
					if(maxValue < minValue)
					{
						showStatus("Invalid Input: Min value can not be greater than Max value");
						return;
					}
				}
				
				catch(NumberFormatException e1)
				{
					showStatus("Invalid Input!");
					return;
				}
				
				showStatus("");
				
				buttons[0].setEnabled(false);
				
				Checkbox animation = cbg.getSelectedCheckbox();
				AnimationFrame.AnimationType t;
				
				if(animation == bars)
				{
					t = AnimationFrame.AnimationType.Bars;
				}
				
				else
				{
					t = AnimationFrame.AnimationType.Boxes;
				}
				
				animationFrame = new AnimationFrame("Sort Animation", numOfValues, minValue, maxValue, t);
				animationFrame.setVisible(true);
				animationFrame.addWindowListener(new FrameWindowAdapter());
				animationFrame.addSortListner(this);
				animationFrame.addCompareSwapListener(this);
				
				
				//bars.setEnabled(false);
				//boxes.setEnabled(false);
			}
		}
		
		//start button
		else if(source == buttons[1])
		{
			int delay = 0;
			try
			{
				String txt = textFields[3].getText();
				
				if(txt == null || txt.trim().isEmpty())
					delay = 0;
				else
					delay = Integer.parseInt(txt);
			}
			
			catch(NumberFormatException e2)
			{
				showStatus("Invalid Input!");
				return;
			}
			
			buttons[1].setEnabled(false);
			buttons[2].setEnabled(true);
			buttons[3].setEnabled(false);

			
			textFields[3].setEnabled(false);
			sortSelect.setEnabled(false);
			animationFrame.startButtonClicked(getSortType(), delay);
		}
		
		// stop button
		else if(source == buttons[2])
		{
			buttons[1].setEnabled(true);
			buttons[2].setEnabled(false);
			buttons[3].setEnabled(true);
			textFields[3].setEnabled(true);
			animationFrame.stopButtonClicked();
		}
		
		//step button
		else if(source == buttons[3])
		{
			if(sortSelect.isEnabled())
				sortSelect.setEnabled(false);
			
			animationFrame.stepButtonClicked(getSortType());
		}
		
	}
	
	private AnimationFrame.SortType getSortType()
	{
		String sort  = sortSelect.getSelectedItem();
		AnimationFrame.SortType sortType;
		
		if(sort == "Bubble Sort")
			sortType = AnimationFrame.SortType.Bubble;
		else
			sortType = AnimationFrame.SortType.Quick;
		
		return sortType;
	}
	
	
	class FrameWindowAdapter extends WindowAdapter
	{
		
		public void windowClosing(WindowEvent we)
		{
			//populateButton
			buttons[0].setEnabled(true);
			animationFrame = null;
			
			for(int i = 1; i < 4; i++)
				buttons[i].setEnabled(false);
			
			 for(int i = 0; i <4; i++)
				 textFields[i].setEnabled(true);
			 
			 sortSelect.setEnabled(true);
			 
			totalComparisions = 0;
			totalSwaps = 0;
			
			//bars.setEnabled(true);
			//boxes.setEnabled(true);
			
			 showStatus("");
		}
		
		 public void windowOpened(WindowEvent e) 
		 {
			 //start
			 buttons[1].setEnabled(true);
			 //step
			 buttons[3].setEnabled(true);
			 
			 for(int i = 0; i <3; i++)
				 textFields[i].setEnabled(false);
		 }
		
	}


	@Override
	public void SortFinished() {
		// TODO Auto-generated method stub
		for(int i = 0; i < 4; i++)
		{
			buttons[i].setEnabled(false);
		}
		
		showStatus("Comparisions = " + totalComparisions + "   Swaps = " + totalSwaps + "   Sort Finished.");
	}

	@Override
	public void ComparePerformed(int i, int j, int comparisions) {
		// TODO Auto-generated method stub
		totalComparisions = comparisions;
		updateStatus();
	}

	@Override
	public void SwapPerformed(int i, int j, int swaps) {
		// TODO Auto-generated method stub
		totalSwaps = swaps;
		updateStatus();
	}
	
	private void updateStatus()
	{
		showStatus("Comparisions = " + totalComparisions + "   Swaps = " + totalSwaps);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
		if(animationFrame != null)
		{
			Checkbox animation = cbg.getSelectedCheckbox();
			
			if(animation == bars)
			{
				animationFrame.animationChanged(AnimationFrame.AnimationType.Bars);
			}
			
			else
			{
				animationFrame.animationChanged(AnimationFrame.AnimationType.Boxes);
			}
		}
		
	}
}





import java.util.*;

public class Sorter implements Runnable
{
	private Thread t;
	private MyArray<Integer> data;
	private AnimationFrame.SortType sortType;
	private ArrayList<SortListener> subscribers;
	private boolean isSortTerminated;
	BubbleSort bSorter;
	QuickSort qSorter;
	
	
	public Sorter(AnimationFrame.SortType sortType, MyArray<Integer> nums)
	{
		subscribers = new ArrayList<SortListener>();
		this.sortType = sortType;
		this.data = nums;
		isSortTerminated = false;
	}
	
	public void start()
	{
		t = new Thread(this);
		t.start();
	}

	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		if(sortType == AnimationFrame.SortType.Bubble)
		{
			bSorter = new BubbleSort();
			bSorter.start(data);
		}
		
		else
		{
			qSorter = new QuickSort();
			qSorter.start(data);
		}
		
		if(!isSortTerminated)
		{
			for(SortListener obj : subscribers)
				obj.SortFinished();
		}
	}
	
	public void addSortListener(SortListener obj)
	{
		subscribers.add(obj);
	}
	
	public void removeSortListener(SortListener obj)
	{
		if(subscribers.contains(obj))
			subscribers.remove(obj);
	}
	
	public synchronized void stop()
	{
		if(t != null && t.isAlive())
		{
			isSortTerminated = true;
			
			if(sortType == AnimationFrame.SortType.Bubble)
			{
				bSorter.stop();
			}
			
			else
			{
				qSorter.stop();
			}
		}
	}
		
	
}


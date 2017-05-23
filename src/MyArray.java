import java.util.*;

public class MyArray<T extends Comparable<T>> {

	private ArrayList<T> objects;
	private int numberOfComparisons;
	private int numberOfSwaps;
	private boolean verbose;
	private ArrayList<CompareSwapListener> subscribers;
	private boolean delay;
	private int delayInms;
	private boolean stepWise;
	
	public MyArray()
	{
		this(false);
	}
	
	public MyArray(boolean verbose)
	{
		objects = new ArrayList<T>();
		this.verbose = verbose;	
		numberOfComparisons = 0;
		numberOfSwaps = 0;
		subscribers = new ArrayList<CompareSwapListener>();
		delay = false;
		stepWise = false;
	}
	
	public int Compare(int i, int j)
	{
		T obj1 = objects.get(i);
		T obj2 = objects.get(j);
		
		int result = obj1.compareTo(obj2);
		
		if (verbose)
		{
			numberOfComparisons++;
			
			for(CompareSwapListener obj : subscribers)
			{
				obj.ComparePerformed(i, j, numberOfComparisons);
			}
		}
		
		checkDelay();
		checkWait();
		
		return result;
	}
	
	public void Swap(int i, int j)
	{
		T obj1 = objects.get(i);
		T obj2 = objects.get(j);
		
		objects.set(i, obj2);
		objects.set(j, obj1);
		
		if (verbose)
		{
			numberOfSwaps++;
			for(CompareSwapListener obj : subscribers)
			{
				obj.SwapPerformed(i, j, numberOfSwaps);
			}
		}
		
		checkDelay();
		checkWait();
	}
		

	
	public int Append(T obj)
	{
		objects.add(obj);
		return objects.size()-1;
	}
	
	public T Remove(int i)
	{
		return objects.remove(i);
	}
	
	public int Size()
	{
		return objects.size();
	}
	
	public T At(int i)
	{
		return objects.get(i);
	}
	
	public int getSwaps()
	{
		return numberOfSwaps;
	}
	
	public int getComparisions()
	{
		return numberOfComparisons;
	}
	
	public void addActionListener(CompareSwapListener obj)
	{
		subscribers.add(obj);
	}
	
	public void removeActionListener(CompareSwapListener obj)
	{
		subscribers.remove(obj);
	}
	
	public void setDelay(int milliSeconds)
	{
		delay = true;
		delayInms = milliSeconds;
	}
	
	private synchronized void checkWait()
	{
		
		
		if(stepWise)
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	private void checkDelay()
	{
		if(delay)
		{
			try
			{
				Thread.sleep(delayInms);
			}
			
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
			
		}
	}

	public synchronized void setWait(boolean val)
	{
		stepWise = val;
	}
	
	public synchronized void nextWait()
	{
		notify();
	}
	
	public void disableDelay()
	{
		delay = false;
	}
}




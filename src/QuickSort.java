import java.util.Random;

public class QuickSort {

	private boolean terminate = false;
	
	public  void start(MyArray<Integer> strs)
	{
		quickSort(strs, 0, strs.Size()-1);
	}
	
	private void quickSort(MyArray<Integer> data, int start, int end)
	{
		synchronized (this) {
			if(terminate)
			{
				//System.out.println("QuickSort(): Exiting Quick Sort");
				return;
			}
		}
		
		if(end > start)
		{
			int p = partition(data, start, end);
			if(p == -1)
				return;
			
			quickSort(data, start, p-1);
			quickSort(data, p+1, end);
		}
		
	}
	
	private Random rd = new Random();
	
	private  int partition(MyArray<Integer> data, int start, int end)
	{
		int pivotPos = start + rd.nextInt(end-start + 1);
		data.Swap(pivotPos, start);
		pivotPos = start;
		int i, j;
		i = start;
		for(j = start+1; j <= end; j++)
		{
			synchronized (this) {
				if(terminate)
				{
					//System.out.println("partition(): Terminating Quick Sort");
					return -1;
				}
			}
			
			if(data.Compare(j, pivotPos) < 0)
			{
				i++;
				if (i!=j)
				{
					data.Swap(i, j);
				}
					
			}
		}
		
		if(i!= start)
		{
			data.Swap(start, i);
		}
		
		pivotPos = i;
		
		return pivotPos;
		
	}

	public synchronized void stop()
	{
		terminate = true;
	}
}

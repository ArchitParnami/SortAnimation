
public class BubbleSort {
	
	private boolean terminate = false;
	
	public void start(MyArray<Integer> nums)
	{
		for(int i = 0; i < nums.Size(); i++)
		{
			boolean swap = false;
			
			for(int j = 0; j < nums.Size() - i - 1; j++)
			{
				synchronized (this) {
					if(terminate){
						//System.out.println("Terminating Bubble Sort");
						return;
					}
					
				} 
				
				if (nums.Compare(j, j+1) > 0)
				{
					nums.Swap(j, j+1);
					swap = true;
				}	
			}
			
			if (!swap)
				break;
		}
	}
	
	public synchronized void stop()
	{
		terminate = true;
	}
}

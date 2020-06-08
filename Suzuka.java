import java.util.LinkedList; 
import java.util.Queue; 
class Suzuka
{
	short numnodes = 10;
	short whoHasToken = -1; 
	nodes sites[] = new nodes[10];    //number of sites initialised
	int LR[] = {0,0,0,0,0,0,0,0,0,0};
	short Q[] = new short[10];
	Queue<Short> q = new LinkedList<>();
	public static void main(String args[]) throws Exception //main loop
	{
		
		Suzuka init = new Suzuka();  //create instance of main class
		init.logme("Suzuka Initialized"); //debugger initialized
		init.mainLoop();
		
	}
	public void mainLoop()
	{
		
		short idTemp;
		for(int count = 0; count <=9; count++)
			sites[count] = new nodes();
		logme("I was also able to initialize nodes aswell");
		int tick = 0;
		for(tick =0;tick<=100;tick++)
		{	
			idTemp = whichCSRequest();
			if(willCSrequest((int)((Math.random())*100)))  //node decides whether to request CS
			{		
					
					logme("CS is requested by: Node "+String.valueOf(idTemp)+" in Tick: " +tick);
					if(sites[idTemp].isCSRequested)
					{
						logme("Already in Queue, skipping.");
						continue;
					}
					else
					{
						logme("Diffusing Request Message to N-1 Nodes whoHasToken = " +whoHasToken);
						diffuseMsg(idTemp, sites); 
					}
					
					    // call messaging algo
			}
			else
			{	
				//logme("No CS Requests this Tick: "+tick+" checking if Token needs to be transferred");
				if(willTokenRelease((int)((Math.random())*100)))  // has someone completed CS
				{
					logme("Token needs to be transferred");
					if(whoHasToken != -1)
						transferToken(idTemp);
				}
			}
			try
			{
			Thread.sleep(250);
			}
			catch(Exception e)
			{e.printStackTrace();}
		}
	}
	public void transferToken(short idTemp)
	{
		LR[whoHasToken] = sites[whoHasToken].RN[whoHasToken]; //set LR[A] == ++
		logme("Node "+q.remove()+" removed from Queue"); // remove node whohastoken from queue
		
		for(short count = 0;count<numnodes;count++)
		{
			if(sites[whoHasToken].RN[count] > LR[count])  //check if RN[] > LR[]
			{	
				if(!q.contains(count))
				{
					q.add(count);  // add node count to queue
					logme("Node " + count +" added to Queue " + q);
				}
				else
					logme("Node "+count+" already in Queue, skipping add to queue.");
			}
		}
		//updateQueue();
		sites[whoHasToken].isCSRequested = false;  //set requested flag off
		if(q.peek() == null)
			whoHasToken = -1; //assign token to next in queue
		else
			whoHasToken = q.peek();
		
	}
	
	public boolean diffuseMsg(short idTemp, nodes sites[])
	{	
		//init.logme("Reached Message Diffuser");
		if(whoHasToken == -1)
		{
			whoHasToken = idTemp;
			q.add(idTemp);
		}
		sites[idTemp].isCSRequested = true;
		for(short count = 0;count<=9;count++)
			sites[count].RN[idTemp]++;
		//logme("Sites notified");
		return true;
		
	}
	public short whichCSRequest()
	{
		return (short)(Math.random()*numnodes);
	}
	public boolean willCSrequest(int chance)
	{
		if(chance > 80)
		return true;
		else
		return false;
	}
	public boolean willTokenRelease(int chance)
	{
		if(chance > 85)
		return true;
		else
		return false;
	}
	public boolean logme(String msg)
	{
		try
		{
			System.out.println(msg);
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	class nodes
	{
		int RN[] = new int[numnodes];
		boolean isCSRequested;
		public nodes()
		{	
			for(short count = 0; count<numnodes; count++)
				RN[count] = 0;
			isCSRequested = false;
		}
		
	}
}



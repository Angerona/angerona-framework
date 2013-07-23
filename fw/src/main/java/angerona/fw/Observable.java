package angerona.fw;


public interface Observable {
	
	public boolean register(Observer o);
	
	public boolean remove(Observer o);
	
	public void informAll();
	
	public void inform(Observer o);

}

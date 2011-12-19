package com.whiplash.threads;

import java.util.*;

/**
 * A post box thread implements a method and a data structure to receive messages.
 * @author Matthias Thimm
 *
 */
public abstract class PostboxThread<T> extends Thread {

	/** The standard number of milliseconds this thread waits until it checks for mail. */
	public static final int STANDARD_WAIT_TIME = 200;
	
	/** The number of milliseconds this thread waits until it checks for mail. */
	private int waitMillis;
	
	/** A queue of messages. */
	private volatile Queue<T> messages;
		
	/** A flag indicating whether this thread has to stop. */
	private boolean discontinue = false;
	
	/** Creates a new post box thread. */
	public PostboxThread(){
		this(PostboxThread.STANDARD_WAIT_TIME);
	}
	
	/** Creates a new post box thread.
	 * @param millis the number of milliseconds this thread waits until it checks for mail. */
	public PostboxThread(int millis){
		this.messages = new LinkedList<T>();
		this.waitMillis = millis;
	}
	
	/** Sends this thread the given object.
	 * @param message some object.
	 */
	public synchronized void mail(T message){
		if(message == null)
			this.discontinue = true;
		else this.messages.add(message);
	}
	
	/** Informs this thread that it got mail.
	 * @param object some message
	 */
	public abstract void gotMail(T message);
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run(){
		while(!this.discontinue){
			while(this.messages.size() > 0)
				this.gotMail(this.messages.poll());
			try {
				Thread.sleep(this.waitMillis);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

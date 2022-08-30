package jmt.engine.QueueNet;

import java.util.LinkedList;

public class CacheItem {

	private int id;

	private LinkedList<Double> accessTimes;
	private int cumulateAccessCount;

	// Time to alive
	private double ttl;

	private boolean isCached;

	public CacheItem(int id) {
		this.accessTimes = new LinkedList<Double>();
		this.isCached = false;
		this.cumulateAccessCount = 0;
		this.id = id;
		this.ttl = -1;
	}

	public int getId() {
		return id;
	}

//	public void setId(int id) {
//		this.id = id;
//	}

	public LinkedList<Double> getAccessTimes() {
		return accessTimes;
	}

	public double getFirstAccessTime() {
		return accessTimes.getFirst();
	}

	public double getLastAccessTime() {
		return accessTimes.getLast();
	}

	public int getNumberOfAccess() {
		return accessTimes.size();
	}

	public int getCumulateAccessCount(){
		return cumulateAccessCount;
	}

	public boolean isCached() {
		return isCached;
	}

	private void setCached(boolean cached) {
		isCached = cached;
	}

	public void access(double time) {
		if(!isCached){
			// for the first time enter the cache, clean the list.
			this.accessTimes.clear();
			setCached(true);
		}
		this.cumulateAccessCount++;
		this.accessTimes.addLast(time);
	}

	public void clear() {
		setCached(false);
		this.accessTimes.clear();
	}

	public double getTTL() {
		return ttl;
	}

	public void setTTL(double ttl) {
		this.ttl = ttl;
	}
}

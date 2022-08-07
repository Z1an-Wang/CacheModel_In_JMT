package jmt.engine.QueueNet;

public class CacheItem {

	private int id;

	private double firstaccessTime;

	private double lastAccessTime;

	private boolean isCached;

	private int numberOfAccess;

	public CacheItem(int i) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

//	public void setId(int id) {
//		this.id = id;
//	}

	public double getFirstaccessTime() {
		return firstaccessTime;
	}

	public void setFirstaccessTime(double firstaccessTime) {
		this.firstaccessTime = firstaccessTime;
	}

	public double getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(double lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public boolean isCached() {
		return isCached;
	}

	public void setCached(boolean cached) {
		isCached = cached;
	}

	public int getNumberOfAccess() {
		return numberOfAccess;
	}

	public void setNumberOfAccess(int numberOfAccess) {
		this.numberOfAccess = numberOfAccess;
	}
}

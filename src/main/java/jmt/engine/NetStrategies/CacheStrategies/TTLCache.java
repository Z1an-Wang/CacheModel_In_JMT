package jmt.engine.NetStrategies.CacheStrategies;

import jmt.engine.NetStrategies.CacheStrategy;
import jmt.engine.QueueNet.CacheItem;

import java.util.LinkedList;

public class TTLCache extends CacheStrategy {

	private double ttl;
	private CacheStrategy replacePolicy;

	public TTLCache(Double ttl, CacheStrategy replacePolicy) {
		this.ttl = ttl;
		this.replacePolicy = replacePolicy;
	}

	/**
	 * When all the item in cached items list are alive, remove the item follow the specific rule.
	 * @param caches
	 * @return
	 */
	@Override
	public CacheItem getRemoveItem(LinkedList<CacheItem> caches) {
		return replacePolicy.getRemoveItem(caches);
	}

	/**
	 * Check each item whether its expired or not.
	 * Once expired, remove it from the cached item list.
	 */
	public void cleanExpiredItem(LinkedList<CacheItem> caches){
		for(int i=0; i<caches.size(); i++){
			if (isExpired(caches.get(i))) {
				caches.get(i).clear();
				caches.remove(caches.get(i));
			}
		}
	}

	public double getTTL() {
		return ttl;
	}

	private boolean isExpired(CacheItem ci){
		return ci.getLastAccessTime()+ci.getTTL() < netSystem.getTime();
	}
}

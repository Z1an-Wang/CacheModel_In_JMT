package jmt.engine.NetStrategies.CacheStrategies;

import jmt.engine.NetStrategies.CacheStrategy;
import jmt.engine.QueueNet.CacheItem;
import jmt.engine.QueueNet.Job;
import jmt.engine.QueueNet.JobClass;
import jmt.engine.QueueNet.NetNode;

import java.util.LinkedList;

public class FIFOCache extends CacheStrategy {

	public FIFOCache(){}

	@Override
	public CacheItem getRemoveItem(LinkedList<CacheItem> caches) {
		return caches.getFirst();
	}

//	@Override
//	public boolean needToCache(NetNode ownerNode){
//		return true;
//	}
//
//	@Override
//	public Job getRemoveJob(NetNode ownerNode) {return null;}
}

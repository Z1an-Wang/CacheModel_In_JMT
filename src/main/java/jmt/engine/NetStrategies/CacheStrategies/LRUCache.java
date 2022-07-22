package jmt.engine.NetStrategies.CacheStrategies;

import jmt.engine.NetStrategies.CacheStrategy;
import jmt.engine.QueueNet.CacheItem;
import jmt.engine.QueueNet.Job;
import jmt.engine.QueueNet.NetNode;
import jmt.engine.random.engine.RandomEngine;

import java.util.LinkedList;

public class LRUCache extends CacheStrategy{

	public LRUCache(){}

	@Override
	public CacheItem getRemoveItem(LinkedList<CacheItem> caches) {
		int lastAccessTime = 999999999;
		int index = -1;
		for(int i=0; i<=caches.size(); i++){
			int temp = caches.get(i).getAccessTimes();
			if( temp<lastAccessTime ){
				lastAccessTime = temp;
				index = i;
			}
		}
		return caches.get(index);
	}
//
//	@Override
//	public boolean needToCache (NetNode ownerNode){
//		return true;
//	}
//
//	@Override
//	public Job getRemoveJob(NetNode ownerNode) {return null;}

}

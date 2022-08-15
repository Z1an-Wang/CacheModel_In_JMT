package jmt.engine.NetStrategies.CacheStrategies;

import jmt.engine.NetStrategies.CacheStrategy;
import jmt.engine.QueueNet.CacheItem;

import java.util.LinkedList;

public class LRUCache extends CacheStrategy{

	public LRUCache(){}

	@Override
	public CacheItem getRemoveItem(LinkedList<CacheItem> caches) {
		int lastAccessTime = Integer.MIN_VALUE;
		int index = -1;
		for(int i=0; i<caches.size(); i++){
			int temp = caches.get(i).getNumberOfAccess();
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

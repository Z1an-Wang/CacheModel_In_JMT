package jmt.engine.NetStrategies.CacheStrategies;

import jmt.engine.NetStrategies.CacheStrategy;
import jmt.engine.QueueNet.CacheItem;

import java.util.LinkedList;

public class LFUCache extends CacheStrategy {

	public LFUCache(){}

	@Override
	public CacheItem getRemoveItem(LinkedList<CacheItem> caches) {
		int numberOfAccess = Integer.MAX_VALUE;
		int index = -1;
		for(int i=0; i<caches.size(); i++){
//			int temp = caches.get(i).getNumberOfAccess();			// Each time the item has been evicted, set the frequency to zero
			int temp = caches.get(i).getCumulateAccessCount();		// Account for the cumulate frequency
			// If two items have the same frequency, apply FIFO.
			if( temp < numberOfAccess ){
				numberOfAccess = temp;
				index = i;
			}
		}
		return caches.get(index);
	}

}

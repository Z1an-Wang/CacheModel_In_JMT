package jmt.engine.NetStrategies.CacheStrategies;

import jmt.engine.NetStrategies.CacheStrategy;
import jmt.engine.QueueNet.CacheItem;
import jmt.engine.QueueNet.Job;
import jmt.engine.QueueNet.JobClass;
import jmt.engine.QueueNet.NetNode;
import jmt.engine.random.engine.RandomEngine;

import java.util.LinkedList;

public class RandomCache extends CacheStrategy {

	public RandomCache(){}

	@Override
	public CacheItem getRemoveItem(LinkedList<CacheItem> caches) {
		return caches.get(new java.util.Random().nextInt(caches.size()));
	}

//	@Override
//	public boolean needToCache (NetNode ownerNode){
//		RandomEngine randomEngine = ownerNode.getNetSystem().getEngine();
//		int temp = randomEngine.nextInt();
//		return (temp&0x01) == 1;
//	}
//
//	@Override
//	public Job getRemoveJob(NetNode ownerNode) {return null;}

}

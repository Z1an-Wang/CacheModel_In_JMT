package jmt.engine.NetStrategies;


import jmt.common.AutoCheck;
import jmt.engine.QueueNet.*;

import java.util.LinkedList;

public abstract class CacheStrategy implements AutoCheck{

//	public abstract boolean needToCache(NetNode ownerNode);

//	public boolean getToCache(NodeSection section){
//		return needToCache(section.getOwnerNode());
//	}

	public abstract CacheItem getRemoveItem(LinkedList<CacheItem> caches);

	public boolean check() { return true; }
}

/**
 * Copyright (C) 2016, Laboratorio di Valutazione delle Prestazioni - Politecnico di Milano

 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */

package jmt.engine.NodeSections;

import jmt.common.exception.NetException;
import jmt.engine.NetStrategies.CacheStrategy;
import jmt.engine.QueueNet.*;
import jmt.engine.random.discrete.*;

import java.util.TreeMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This class implements a class switch.
 *
 * @author Sebatiano Spicuglia, Arif Canakoglu
 */
public class Cache extends ServiceSection {


	private int maxItems;
	private int cacheCapacity;

	private ArrayList<CacheItem> items;		// once created, only retrieve and update.
	private LinkedList<CacheItem> caches;	// since we need to insert or delete it frequently.

	private CacheStrategy replacePolicy;
	private DiscreteDistribution popularity;

	private JobInfoList nodeJobsList;
	// also have a field `section_JobsList` inherited from NodeSection.

//	TreeMap<Integer, Integer> count;		// variable used for test


	public Cache(Integer maxItems, Integer cacheCapacity, CacheStrategy replacePolicy, DiscreteDistribution pop){
		this.maxItems = maxItems;
		this.cacheCapacity = cacheCapacity;
		this.replacePolicy = replacePolicy;
		this.popularity = pop;
//		this.count = new TreeMap<>();		// variable used for test
	}

	/**
	 *  when the node section itself is linked to the owner node.
	 * 	This method should be used to set node section properties depending on
	 *  the owner node ones.
	 * @param ownerNode
	 * @throws NetException
	 */
	@Override
	protected void nodeLinked(NetNode ownerNode) throws NetException {
		this.replacePolicy.setRandomEngine(getNetSytem().getEngine());
		this.popularity.setRandomEngine(getNetSytem().getEngine());
		nodeJobsList = ownerNode.getJobInfoList();

		items = new ArrayList<CacheItem>(maxItems);
		for(int i=1; i<=maxItems; i++){
			items.add(new CacheItem(i));
		}
		caches = new LinkedList<CacheItem>();
//		TestDistribution(popularity);		// method used for test
	}

	@Override
	protected int process(NetMessage message) throws NetException {
		switch (message.getEvent()) {

			case NetEvent.EVENT_START:
				break;

			case NetEvent.EVENT_JOB:
				Job job = message.getJob();
				JobClass originalClass = job.getJobClass();

				// if this job is not a belong to a cache class, forward it to the next section.
				if(!job.getJobClass().isHasCachePair()){
					sendForward(NetEvent.EVENT_JOB, job, 0.0);
					sendBackward(NetEvent.EVENT_ACK, job, 0.0);
					break;
				}

				// get the request item that follow the specific popularity.
				int requsetId = 0;
				CacheItem targetItem = null;
				do{
					requsetId = popularity.nextRand();	// Id from 1 to numberOfItems generated by popularity.
				}
				while((targetItem = getItemById(items, requsetId)) == null);
//				count = TestNextRand(count, requsetId);		// To test the popularity function.

				// if the items has already cached.
				if (cacheContains(requsetId)) {
					// update cache item information
					targetItem.setLastAccessTime(job.getNetSystem().getTime());
					targetItem.setNumberOfAccess(targetItem.getNumberOfAccess() + 1);

					// record the cache hit count to the jobListInfo and update hitRate measure.
					jobsList.CacheJob(originalClass, true);
				}
				// Cache miss
				else {
					// get the cache Miss Class through cache pairs.
					JobClass cacheMissClass = job.getJobClass().getCachePairClass();

					// else check if the cache is full, apply the replace policy
					if (caches.size() == cacheCapacity) {
						// execute the replace policy
						CacheItem remove = replacePolicy.getRemoveItem(caches);
						for (int i = 0; i < caches.size(); i++) {
							if (caches.get(i).getId() == remove.getId()) {
								caches.remove(i);
								remove.setCached(false);
								remove.setNumberOfAccess(0);
							}
						}
					}
					// else cache is not full, directly add this new items to cache.
					// update cache item information
					targetItem.setFirstaccessTime(job.getNetSystem().getTime());
					targetItem.setCached(true);
					targetItem.setLastAccessTime(job.getNetSystem().getTime());
					targetItem.setNumberOfAccess(1);
					caches.add(targetItem);

					// switch the class
					job.setJobClass(cacheMissClass);
					JobInfo jobInfo = jobsList.lookFor(job);
					jobsList.getInternalJobInfoList(cacheMissClass).add(jobInfo);
					jobsList.getInternalJobInfoList(originalClass).remove(jobInfo);

					JobInfo nodeJobInfo = nodeJobsList.lookFor(job);
					nodeJobsList.getInternalJobInfoList(cacheMissClass).add(nodeJobInfo);
					nodeJobsList.getInternalJobInfoList(originalClass).remove(nodeJobInfo);

					if (!(job instanceof ForkJob)) {
						GlobalJobInfoList global = getOwnerNode().getQueueNet().getJobInfoList();
						global.performJobClassSwitch(originalClass, cacheMissClass);
					}

					// record the cache miss count to the jobListInfo and update hitRate measure.
					jobsList.CacheJob(cacheMissClass, false);
				}
				sendForward(NetEvent.EVENT_JOB, job, 0.0);
				sendBackward(NetEvent.EVENT_ACK, job, 0.0);
				break;

			case NetEvent.EVENT_ACK:
				break;

			case NetEvent.EVENT_STOP:
				break;

			default:
				return MSG_NOT_PROCESSED;
		}

		return MSG_PROCESSED;
	}


	private CacheItem getItemById(List<CacheItem> itemsList, int id){
		for( int i=0; i<itemsList.size(); i++){
			if(itemsList.get(i).getId() == id){
				return itemsList.get(i);
			}
		}
		return null;
	}

	private boolean cacheContains(int id){
		return getItemById(this.caches, id) != null;
	}

	/**********************************************************
	 *  These are test functions for the popularity module.	  *
	 ***********************************************************/
	private TreeMap<Integer, Integer> TestNextRand(TreeMap<Integer, Integer> count, int nextRand){
		Integer target = count.get(Integer.valueOf(nextRand));
		System.out.println("=== Current request Id is "+nextRand+" ===");
		if (target == null) {
			count.put(nextRand, 1);
		} else {
			count.put(nextRand, count.get(nextRand) + 1);
		}
		Iterator it = count.keySet().iterator();
		while (it.hasNext()) {
			Integer key = (Integer) it.next();
			System.out.println("Key: " + key + " is: " + count.get(key));
		}
		System.out.println("=== END ===");
		return count;
	}

	private void TestDistribution(DiscreteDistribution ds){
		int upper = ds.getUpper();
		int lower = ds.getlower();
		for(int i=lower; i<=upper; i++){
			System.out.println("CDF: "+ds.cdf(i)+" \tPMF: "+ ds.pmf(i));
		}
		System.out.println("=======");
		System.out.println("Mean: "+ds.theorMean());
		System.out.println("Variance: "+ds.theorVariance());
	}
}

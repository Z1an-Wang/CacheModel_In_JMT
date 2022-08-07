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
import jmt.engine.random.engine.RandomEngine;
import jmt.engine.random.discrete.*;

import java.util.LinkedList;

/**
 * This class implements a class switch.
 *
 * @author Sebatiano Spicuglia, Arif Canakoglu
 */
public class Cache extends ServiceSection {


	static final String CACHE_HIT_CLASS = "Cache Hit";
	static final String CACHE_MISS_CLASS = "Cache Miss";


	private int maxItems;
	private int cacheCapacity;

	private JobClass hitClass;
	private JobClass missClass;

	private LinkedList<CacheItem> items;
	private LinkedList<CacheItem> caches;

	private CacheStrategy replacePolicy;
	private DiscreteDistribution popularity;

	private JobInfoList node_JobsList;
	// also have a field `section_JobsList` inherited from NodeSection.


	public Cache(Integer maxItems, Integer cacheCapacity, CacheStrategy replacePolicy, DiscreteDistribution pop){
		this.maxItems = maxItems;
		this.cacheCapacity = cacheCapacity;
		this.replacePolicy = replacePolicy;
		this.popularity = pop;
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
		System.out.println("Sucess.");
		node_JobsList = ownerNode.getJobInfoList();
		JobClassList jobClasses = getJobClasses();
		this.hitClass = jobClasses.get(CACHE_HIT_CLASS);
		this.missClass = jobClasses.get(CACHE_MISS_CLASS);

		items = new LinkedList<CacheItem>();
		for(int i=0; i<maxItems; i++){
			items.add(new CacheItem(i));
		}
		caches = new LinkedList<CacheItem>();

//		if(popularity instanceof ZipfDistribution){
//			((ZipfDistribution) popularity)
//		}
	}

	@Override
	protected int process(NetMessage message) throws NetException {
		switch (message.getEvent()) {

			case NetEvent.EVENT_START:
				break;

			case NetEvent.EVENT_JOB:
				Job job = message.getJob();
				JobClass originalClass = job.getJobClass();

				int requsetId = ((Zipf)popularity).nextRand();
				CacheItem targetItem = items.get(requsetId);

				JobInfo jobInfo = jobsList.lookFor(job);
				jobsList.getInternalJobInfoList(originalClass).remove(jobInfo);

				// if the items has already cached.
				if (cacheContains(requsetId)) {
					targetItem.setLastAccessTime(job.getNetSystem().getTime());
					targetItem.setNumberOfAccess(targetItem.getNumberOfAccess() + 1);
					job.setJobClass(hitClass);
					jobsList.getInternalJobInfoList(hitClass).add(jobInfo);
				} else {
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
					// else cache is not full, add this new items to cache.
					}
					targetItem.setFirstaccessTime(job.getNetSystem().getTime());
					targetItem.setCached(true);
					targetItem.setLastAccessTime(job.getNetSystem().getTime());
					targetItem.setNumberOfAccess(1);
					caches.addLast(targetItem);
					job.setJobClass(missClass);
					jobsList.getInternalJobInfoList(missClass).add(jobInfo);
				}

//				if (!(job instanceof ForkJob)) {
//					GlobalJobInfoList global = getOwnerNode().getQueueNet().getJobInfoList();
//					global.performJobClassSwitch(inClass, outclass);
//				}
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


	private CacheItem getItemById(LinkedList<CacheItem> cachesList, int id){
		for( int i=0; i<cachesList.size(); i++){
			if(cachesList.get(i).getId() == id){
				return cachesList.get(i);
			}
		}
		return null;
	}

	private boolean cacheContains(int id){
		return getItemById(this.caches, id) != null;
	}

	private JobClass getJobClassByName(String name){
		for(int i=0; i<getJobClasses().size(); i++){
			if(getJobClasses().get(i).getName().equals(name));
			return getJobClasses().get(i);
		}
		return null;
	}


	/**
	 * It choose randomly a position of @row.
	 * Let SUM the sum of all elements of @row.
	 * The probability that this method chooses an
	 * index i is row[i]/SUM.
	 * @param row
	 * @return
	 */
	private int chooseOutClass(Float[] row) {
		float sum;
		float random;
		int i;

		sum = 0;
		for (i = 0; i < row.length; i++) {
			sum += row[i];
		}

		RandomEngine engine = this.getNetSytem().getEngine();
		random = (float) (engine.raw() * sum);
		for (i = 0; i < row.length; i++) {
			random -= row[i];
			if (random <= 0)
				return i;
		}
		return i-1;
	}

}

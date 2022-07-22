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

import java.util.LinkedList;
import java.util.ListIterator;

import jmt.common.exception.NetException;
import jmt.engine.NetStrategies.ServiceStrategy;
import jmt.engine.QueueNet.GlobalJobInfoList;
import jmt.engine.QueueNet.Job;
import jmt.engine.QueueNet.JobClass;
import jmt.engine.QueueNet.JobClassList;
import jmt.engine.QueueNet.JobInfo;
import jmt.engine.QueueNet.JobInfoList;
import jmt.engine.QueueNet.NetEvent;
import jmt.engine.QueueNet.NetMessage;
import jmt.engine.QueueNet.NetNode;
import jmt.engine.QueueNet.NetSystem;

/**
 * This class implements a job source (generator): a job is created every
 * <i>t</i> simulation time units using a statistical distribution according
 * to its class.
 * @author Francesco Radaelli, Federico Granata
 *
 */
public class RandomSource extends InputSection {

	private boolean coolStart;

	private LinkedList<Job> waitingJobs;

	private ServiceStrategy[] strategies;

	private JobClassList jobClasses;

	private JobInfoList nodeJobsList;

	private GlobalJobInfoList netJobsList;

	/**
	 * Creates a new instance of RandomSource.
	 * strategies[i] = null, if the i-th class is closed.
	 */

	// 初始化一个RandomSource节点，通过数组 指定每个class 的 serviceStrategy
	// <parameter array="true" classPath="jmt.engine.NetStrategies.ServiceStrategy" name="ServiceStrategy">
	// 创建serviceStrategy时，提供相应的构造函数的参数 `public ServiceTimeStrategy(Distribution distribution, Parameter parameter)`

	/**
	 *  <subParameter classPath="jmt.engine.NetStrategies.ServiceStrategies.ServiceTimeStrategy" name="ServiceTimeStrategy">
 *         <subParameter classPath="jmt.engine.random.Exponential" name="Exponential"/>
 *         <subParameter classPath="jmt.engine.random.ExponentialPar" name="distrPar">
 *             <subParameter classPath="java.lang.Double" name="lambda">
 *                 <value>0.5</value>
 *             </subParameter>
 *         </subParameter>
	 *  </subParameter>
	 * @param strategies
	 */

	public RandomSource(ServiceStrategy[] strategies) {
		this.strategies = strategies;
		waitingJobs = new LinkedList<Job>();
		coolStart = true;
	}

	//函数调用在 NodeSection.setOwnerNode(ownerNode); ===>  NetNode.inputSection.setOwnerNode(this);
	//相当于这个对象的 initialize() 函数。
	@Override
	protected void nodeLinked(NetNode node) throws NetException {
		// Sets netnode dependent properties

		// 从该NodeSection的主节点处获取JobClasses。
		jobClasses = getJobClasses();

		// 获取指定节点的JobList
		nodeJobsList = node.getJobInfoList();
	}

	@Override
	protected int process(NetMessage message) throws NetException {
		Job job;
		int c;
		double delay;

		switch (message.getEvent()) {

		case NetEvent.EVENT_START: // 事件开始

			//case EVENT_START:
			//the random source creates all the jobs requested by each class.
			//for each job created, it sends to itself a message whose delay is the time of
			//departure of the job, calculated using the strategy of the corresponding class

			netJobsList = getOwnerNode().getQueueNet().getJobInfoList();

			// 遍历该节点的jobClass，如果有，判断class类型（open or close）
			// 即对 jobClass 中的每一个class创建一个实例。
			ListIterator<JobClass> it = jobClasses.listIterator();
			while (it.hasNext()) {
				JobClass jobClass = it.next();
				if (jobClass.getType() == JobClass.CLOSED_CLASS) {
					//closed class: no arrivals
					continue;
				}

				// 获取jobClass 的 Id
				c = jobClass.getId();
				// Calculates the delay of departure (1/lambda)

				// 如果 策略组中有对应的jobClass， 则初始化一个该jobClass的实例，即：Class
				if (strategies[c] != null) {
					job = new Job(jobClass, netJobsList);
					job.initialize(this.getNetSystem());
					updateVisitPath(job);

					// 执行该策略组的等待策略（serviceTime策略，按概率分布产生事件），进而产生以下一个job。
					delay = strategies[c].wait(this, jobClass);
					sendMe(job, delay);		// 发出 NetEvent.EVENT_JOB，信号。
				}
			}
			break;

		case NetEvent.EVENT_JOB:

			//case EVENT_JOB
			//if coolStart=false adds the job to the list of waiting jobs.
			//
			//if coolStart=true (no waiting jobs)  the job is forwarded, an ack message
			//is sent to the source of the message and a new job is created (the random source
			//sends to itself a message, whose delay is the time of departure of the new job).

			// Gets the job from the message
			job = message.getJob();
			if (coolStart) {
				// Gets the class of the job
				c = job.getJobClass().getId(); // 获取jobClass 的 Id

				//no control is made on the number of jobs created
				//it is an open class

				// in RandomSource the job is created (--> SystemEnteringTime is initialized)
				// but then is delayed as long as the random interarrival time ("delay")
				//
				// to compute system response time, the job starting time must be
				// reset (otherwise it will correspond to the creation time and not to the
				// leaving time, which is "delay" seconds after)

				// Signals to section jobInfoList new added job
				jobsList.add(new JobInfo(job));
				// Signals to node jobInfoList new added job
				nodeJobsList.add(new JobInfo(job));
				// Signals to global jobInfoList new added job
				netJobsList.addJob(job);

				// 发出 NodeSection.SERVICE 信号。
				sendForward(job, 0.0);

				// 创建下一个 job 对象。
				// Create a new job and send it to me delayed
				job = new Job(job.getJobClass(), netJobsList);
                job.initialize(this.getNetSystem());
				updateVisitPath(job);

				// 同样执行等待策略。
				delay = strategies[c].wait(this, job.getJobClass());
				sendMe(job, delay);

				// Sets coolStart to false, next job should wait ack
				coolStart = false;
			} else {
				//coolStart is false: there are waiting jobs. Add the received job.
				waitingJobs.add(job);
			}
			break;

		case NetEvent.EVENT_ACK:

			//case EVENT_ACK:
			//if there are waiting jobs, takes the first, set its bornTime and
			//forwards it to the service section.
			//then it creates a new job and sends to itself a message whose delay is the time of
			//departure of that job.
			//otherwise, if there are no waiting jobs, sets coolstart=true

			if (waitingJobs.size() != 0) {
				job = waitingJobs.removeFirst();
				c = job.getJobClass().getId();

				// in RandomSource the job is created (--> SystemEnteringTime is initialized)
				// but then is delayed as long as the random interarrival time ("delay")
				//
				// to compute system response time, the job starting time must be
				// reset (otherwise it will correspond to the creation time and not to the
				// leaving time, which is "delay" seconds after)

				// Signals to section jobInfoList new added job
				jobsList.add(new JobInfo(job));
				// Signals to node jobInfoList new added job
				nodeJobsList.add(new JobInfo(job));
				// Signals to global jobInfoList new added job
				netJobsList.addJob(job);

				sendForward(job, 0.0);

				// Create a new job and send it to me delayed
				job = new Job(jobClasses.get(c), netJobsList);
                job.initialize(this.getNetSystem()); 
				updateVisitPath(job);
				delay = strategies[c].wait(this, jobClasses.get(c));
				sendMe(job, delay);
			} else {
				coolStart = true;
			}
			break;

		case NetEvent.EVENT_STOP:
			break;

		default:
			return MSG_NOT_PROCESSED;
		}

		return MSG_PROCESSED;
	}
	
	private NetSystem getNetSystem(){
		return this.getOwnerNode().getNetSystem();
	}

}

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.test.beans;



/**
 *
 */
public class QueueServiceImpl implements QueueService
{

	/*
	 * private Map<BusinessProcessModel, BlockingQueue<ActionExecution>> nodeExecutions;
	 * 
	 * @Override public void init() {
	 * 
	 * nodeExecutions = Collections.synchronizedMap(LazyMap.decorate( new HashMap<BusinessProcessModel,
	 * BlockingQueue<ActionExecution>>(), new Factory() {
	 * 
	 * @Override public Object create() { return new SynchronousQueue<ActionExecution>(); } })); }
	 * 
	 * @Override public void actionExecuted(final BusinessProcessModel process, final AbstractAction action) throws
	 * Exception { if (nodeExecutions == null) {
	 * Logger.getLogger(this.getClass()).error("No nodeExecutions available"); } else { final
	 * BlockingQueue<ActionExecution> queue = nodeExecutions.get(process); queue.offer(new ActionExecution(process,
	 * action), 30, TimeUnit.SECONDS); Logger.getLogger(this.getClass()).info("actionExecuted " + action.getClass()); }
	 * 
	 * 
	 * }
	 * 
	 * @Override public ActionExecution pollQueue(final BusinessProcessModel process) throws InterruptedException { final
	 * BlockingQueue<ActionExecution> queue = nodeExecutions.get(process); final ActionExecution exec = queue.poll(30,
	 * TimeUnit.SECONDS); assertNotNull("No node execution in queue. Timeout?", exec);
	 * 
	 * Logger.getLogger(this.getClass()).info("pollQueue " + exec.getAction().getClass()); return exec; }
	 * 
	 * @Override public void destroy() { if (nodeExecutions != null) { for (final BlockingQueue<ActionExecution> queue :
	 * nodeExecutions.values()) { queue.drainTo(new ArrayList<ActionExecution>()); } nodeExecutions = null; } }
	 */
}

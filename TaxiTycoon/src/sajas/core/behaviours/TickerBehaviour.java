/*****************************************************************
 SAJaS - Simple API for JADE-based Simulations is a framework to 
 facilitate running multi-agent simulations using the JADE framework.
 Copyright (C) 2015 Henrique Lopes Cardoso
 Universidade do Porto

 GNU Lesser General Public License

 This file is part of SAJaS.

 SAJaS is free software: you can redistribute it and/or modify
 it under the terms of the GNU Lesser General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 SAJaS is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License
 along with SAJaS.  If not, see <http://www.gnu.org/licenses/>.
 *****************************************************************/

package sajas.core.behaviours;

import sajas.core.Agent;
import jade.core.*;

/**
 * @see jade.core.behaviours.TickerBehaviour
 * @author hlc
 *
 */
public abstract class TickerBehaviour extends SimpleBehaviour {
	private long wakeupTime, period;
	private boolean finished;
	private int tickCount = 0;

	private boolean fixedPeriod = false;
	private long startTime;
	
	/**
	 * Construct a <code>TickerBehaviour</code> that call its 
	 * <code>onTick()</code> method every <code>period</code> ms.
	 * @param a is the pointer to the agent
	 * @param period the tick period in ms
	 */
	public TickerBehaviour(Agent a, long period) {
		super(a);
		if (period <= 0) {
			throw new IllegalArgumentException("Period must be greater than 0");
		}
		this.period = period;
	}
	
	public void onStart() {
		startTime = System.currentTimeMillis();
		wakeupTime =  startTime + period;
	}
	
	public final void action() {
		// Someone else may have stopped us in the meanwhile
		if (!finished) {
			long blockTime = wakeupTime - System.currentTimeMillis();
			if (blockTime <= 0) {
				// Timeout is expired --> execute the user defined action and
				// re-initialize wakeupTime
				tickCount++;
				onTick();
				
				long currentTime = System.currentTimeMillis();
				if (fixedPeriod) {
					wakeupTime = startTime + (tickCount + 1) * period;
				}
				else {
					wakeupTime = currentTime + period;
				}
				blockTime = wakeupTime - currentTime;
			}
			// Maybe this behaviour has been removed within the onTick() method
			if (myAgent != null && !finished && blockTime > 0) {
				block(blockTime);
			}
		}
	} 
	
	public final boolean done() {
		return finished;
	}
	
	/**
	 This method is invoked periodically with the period defined in the
	 constructor.
	 Subclasses are expected to define this method specifying the action
	 that must be performed at every tick.
	 */
	protected abstract void onTick();
	
	/**
	 * Turn on/off the "fixed period" mode. Given a period P, when fixed period mode is off (default), 
	 * this behaviour will wait for P milliseconds from the end of the n-th execution of the onTick() method
	 * to the beginning of the n+1-th execution.   
	 * When fixed period is on, this behaviour will execute the onTick() method exactly every P milliseconds.
	 * @param fixedPeriod A boolean value indicating whether the fixed period mode must be turned on or off.
	 */
	public void setFixedPeriod(boolean fixedPeriod) {
		this.fixedPeriod = fixedPeriod;
	}
	
	/**
	 * This method must be called to reset the behaviour and starts again
	 * @param period the new tick time
	 */
	public void reset(long period) {
		this.reset();
		if (period <= 0) {
			throw new IllegalArgumentException("Period must be greater than 0");
		}
		this.period = period;
	}
	
	/**
	 * This method must be called to reset the behaviour and starts again
	 * @param timeout indicates in how many milliseconds from now the behaviour
	 * must be waken up again. 
	 */
	public void reset() {
		super.reset();
		finished = false;
		tickCount = 0;
	}
	
	/**
	 * Make this <code>TickerBehaviour</code> terminate.
	 * Calling stop() has the same effect as removing this TickerBehaviour, but is Thread safe
	 */
	public void stop() {
		finished = true;
		restart();
	}
	
	/**
	 * Retrieve how many ticks were done (i.e. how many times this
	 * behaviour was executed) since the last reset.
	 * @return The number of ticks since the last reset
	 */
	public final int getTickCount() {
		return tickCount;
	}
	
	protected long getPeriod() {
		return period;
	}
	//#MIDP_EXCLUDE_BEGIN
	
	// For persistence service
	private void setTickCount(int tc) {
		tickCount = tc;
	}
	
	// For persistence service
	private void setPeriod(long p) {
		period = p;
	}
	
	// For persistence service
	private void setWakeupTime(long wt) {
		wakeupTime = wt;
	}
	
	// For persistence service
	private long getWakeupTime() {
		return wakeupTime;
	}
	
	
	//#MIDP_EXCLUDE_END
	
	
}

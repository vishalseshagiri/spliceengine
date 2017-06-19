/*
 * Copyright (c) 2012 - 2017 Splice Machine, Inc.
 *
 * This file is part of Splice Machine.
 * Splice Machine is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either
 * version 3, or (at your option) any later version.
 * Splice Machine is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with Splice Machine.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package com.splicemachine.spark.splicemachine

import java.sql.{Connection, Timestamp}
import com.splicemachine.si.api.txn.WriteConflict
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite, Matchers}
import org.junit.Assert._

import com.splicemachine.spark.splicemachine.Timeline
import scala.util.Random
import scala.collection.mutable.Queue
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter


@RunWith(classOf[JUnitRunner])
class SupplyChainSimulator extends Timeline  {

	//Utility Class for Random Number generation
	object RandomGen {
	  private var random = new util.Random
	
	  // Returns a random integer in range [0,range-1]
	  def rand(range: Int): Int = random.nextInt(range)
	
	  // Returns a random integer in range [first,last]
	  def randBetween(first: Int, last: Int): Int = first + rand(last-first+1)
	
	  // Create a new random number generator with the given Int "seed".
	  def setRandSeed(seed: Int)  { random = new Random(seed) }
	 
	}
	/**
		*Baseclass for all Order Events
		* The behaviors of specific Event subtypes must be defined implementation for method processEvent.
		*
		* @epart : Part ID of the order event
		* @eorderDate : Shipping Date as a string
		* @edeliveryDate : Delivery Date as a String
		* @eqty : Quantity of the order event
		*/

	abstract class Event(epart: Integer,
											 eorderDate: String,
											 edeliveryDate: String,
											 eqty: Long)  {
		def processEvent

		def part: Int = epart
		def orderDate: String = eorderDate
		def deliveryDate: String = edeliveryDate
		def qty: Long = eqty
	}

	/** Simulation Driver Class
		* Provides the Event Queue and method to process the Queue
		*/
	class Simulation() {

		var eventQueue = new collection.mutable.Queue[Event]  //  events queue

		// Add newEvent to  events queue
		def scheduleEvent(newEvent: Event) {
			eventQueue.enqueue(newEvent)
		}
		//  method for simulation of events
		final def run {
			while (!eventQueue.isEmpty) {        // while more events to process
			val nextEvent = eventQueue.dequeue //   get  event
				nextEvent.processEvent             //   execute the event
			}
		}
	}


	/*******
		* PURCHASE ORDER EVENTS *
		********/

	/** Event to Create Purchase Order
		*
		* @part : Part ID of the order
		* @orderDate : Shipping Date as a string
		* @deliveryDate : Delivery Date as a String
		* @qty : Quantity of the order
		*/

	class POCreateEvent(part: Int, orderDate: String, deliveryDate: String, qty: Long ) extends Event(part, orderDate, deliveryDate, qty) {
		def processEvent {
			println( this.toString )
			PurchaseOrder.create(part, orderDate, deliveryDate, qty)
		}

		override def toString = "POCreateEvent(" + part + "," + orderDate  + "," + deliveryDate  + "," + qty  + ")"
	}

	/** Event  to Change Purchase Order Quantity
		*
		* @part : Part ID of the order
		* @orderDate : Shipping Date as a string
		* @deliveryDate : Delivery Date as a String
		* @qty : Original Quantity of the order
		* @newQty : New Quantity to set
		*/
	class POChangeQtyEvent(part: Int, orderDate: String, deliveryDate: String, qty: Long, newQty: Long )  extends Event(part, orderDate, deliveryDate, qty) {
		def processEvent {
			println( this.toString )
			PurchaseOrder.changeQty(part, orderDate, deliveryDate, qty, newQty)

		}

		override def toString = "POChangeQty(" + part + "," + orderDate  + "," + deliveryDate  + "," + qty  +"," + newQty +  ")"
	}

	/** Event  to Change Purchase Order Delivery Date
		*
		* @part : Part ID of the order
		* @orderDate : Shipping Date as a string
		* @deliveryDate : Original Delivery Date as a String
		* @newDeliveryDate: New Delivery Date as a String
		* @qty :  Quantity of the order
		*
		*/
	class POChangeDeliveryEvent(part: Int, orderDate: String, deliveryDate: String, newDeliveryDate: String,  qty: Long )  extends Event(part, orderDate, deliveryDate, qty) {
		def processEvent {
			println( this.toString )
			PurchaseOrder.changeDelivery(part, orderDate, deliveryDate,newDeliveryDate, qty)

		}

		override def toString = "POChangeDelivery(" + part + "," + orderDate  + "," + deliveryDate  + "," + newDeliveryDate + ","  + qty  + ")"
	}



	/*******
		* SALES ORDER EVENTS *
		********/
	/** Event to Create Sales Order
		*
		* @part : Part ID of the order
		* @orderDate : Shipping Date as a string
		* @deliveryDate : Delivery Date as a String
		* @qty : Quantity of the order
		*/
	class SOCreateEvent(part: Int, orderDate: String, deliveryDate: String, qty: Long ) extends Event(part, orderDate, deliveryDate, qty) {
		def processEvent {
			println( this.toString )
			SalesOrder.create(part, orderDate, deliveryDate, qty)
		}

		override def toString = "SOCreateEvent(" + part + "," + orderDate  + "," + deliveryDate  + "," + qty  + ")"
	}

	/** Event to Change Sales Order Quantity
		*
		* @part : Part ID of the order
		* @orderDate : Shipping Date as a string
		* @deliveryDate : Delivery Date as a String
		* @qty : Original Quantity of the order
		* @newQty : New Quantity to set
		*/
	class SOChangeQtyEvent(part: Int, orderDate: String, deliveryDate: String, qty: Long, newQty: Long )  extends Event(part, orderDate, deliveryDate, qty) {
		def processEvent {
			println( this.toString )
			SalesOrder.changeQty(part, orderDate, deliveryDate, qty, newQty)
		}

		override def toString = "SOChangeQty(" + part + "," + orderDate  + "," + deliveryDate  + "," + qty  +"," +newQty +  ")"
	}

	/** Event  to Change Sales Order Delivery Date
		*
		* @part : Part ID of the order
		* @orderDate : Shipping Date as a string
		* @deliveryDate : Original Delivery Date as a String
		* @newDeliveryDate: New Delivery Date as a String
		* @qty :  Quantity of the order
		*
		*/
	class SOChangeDeliveryEvent(part: Int, orderDate: String, deliveryDate: String, newDeliveryDate: String,  qty: Long )  extends Event(part, orderDate, deliveryDate, qty) {
		def processEvent {
			println( this.toString )
			SalesOrder.changeDelivery(part, orderDate, deliveryDate,newDeliveryDate, qty)
		}
		override def toString = "SOChangeDelivery(" + part + "," + orderDate  + "," + deliveryDate  + "," + newDeliveryDate + ","  + qty  + ")"
	}


	/*******
		* TRANSFER ORDER EVENTS *
		********/
	/** Event to Create Tranfer Order
		*
		* @srcPart : Original Part ID of the order
		* @destPart : New Part ID of the order
		* @orderDate : Shipping Date as a string
		* @deliveryDate : Delivery Date as a String
		* @qty : Quantity of the order
		*/
	class TOCreateEvent (srcPart: Int, destPart: Int, orderDate: String, deliveryDate: String, qty: Long ) extends Event(srcPart, orderDate, deliveryDate, qty) {
		def processEvent {
			println( this.toString )
			TransferOrder.create(srcPart, destPart, orderDate, deliveryDate, qty)

		}

		override def toString = "TOCreateEvent(" +srcPart + "," + destPart + "," + orderDate  + "," + deliveryDate  + "," + qty  + ")"
	}

	/**Event to Change Transfer Order Delivery Date
		*
		* @srcPart : Original Part ID of the order
		* @destPart : New Part ID of the order
		* @orderDate : Shipping Date as a string
		* @deliveryDate : Delivery Date as a String
		* @newDeliveryDate: New Delivery Date as a String
		* @qty : Quantity of the order
		*
		*/
	class TOChangeDeliveryEvent(srcPart: Int, destPart: Int, orderDate: String, deliveryDate: String, newDeliveryDate: String,  qty: Long )  extends Event(srcPart, orderDate, deliveryDate, qty) {
		def processEvent {
			println( this.toString )
			TransferOrder.changeDelivery(srcPart, destPart, orderDate, deliveryDate, newDeliveryDate, qty)
		}
		override def toString = "TOChangeDelivery(" + srcPart + "," + destPart + "," + orderDate  + "," + deliveryDate  + "," + newDeliveryDate + ","  + qty  + ")"
	}

	/**Event to Change Transfer Order Quantity
		*
		* @srcPart : Original Part ID of the order
		* @destPart : New Part ID of the order
		* @orderDate : Shipping Date as a string
		* @deliveryDate : Delivery Date as a String
		* @qty : Quantity of the order
		* @newQty : New Quantity of the order
		*/
	class TOChangeQtyEvent (srcPart: Int, destPart: Int, orderDate: String, deliveryDate: String,  qty: Long,  newQty: Long )  extends Event(srcPart, orderDate, deliveryDate, qty) {
		def processEvent {
			println( this.toString )
			TransferOrder.changeQty(srcPart, destPart, orderDate, deliveryDate, qty, newQty)
		}

		override def toString = "TOChangeQtyEvent(" +srcPart + "," + destPart + "," + orderDate  + "," + deliveryDate  + "," + qty  + "," + newQty +")"
	}

	/**Test Event
		*
		*/

	class TestEvent (srcPart: Int, destPart: Int, orderDate: String, deliveryDate: String,  qty: Long,  newQty: Long )  extends Event(srcPart, orderDate, deliveryDate, qty) {
		def processEvent {
			println( this.toString )
			// TransferOrder.changeQty(srcPart, destPart, orderDate, deliveryDate, qty, newQty)
		}

		override def toString = "TestEvent(" +srcPart + "," + destPart + "," + orderDate  + "," + deliveryDate  + "," + qty  + "," + newQty +")"
	}


	/**Simulator of Order Events
		*
		*
		*/

	object SupplyChain {

		val BEGIN_ORDER_DATE  ="2017-5-05 00:00:00"    // beginning of simulation
		val TOTAL_TICKS     = 10  // Number of ticks in simulation
		val TICK_LENGTH_DAYS  = 1 // Lenght in days between each Tick in realtime
		val TICKINTERVAL  =10 // UI interval between ticks

		val MIN_ENDDAYS    = 1    // minimum delivery duration from orderdate
		val MAX_ENDDAYS    = 7   // maximum delivery duration fro orderdate

		val MIN_QTY    = 1    // minimum order qty
		val MAX_QTY    = 1000   // maximum order qty

		val fmt   = org.joda.time.format.DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

		val parts = Array(100, 200, 300, 400, 500)
		val partsCnt = parts.length

		val theSimulation = new Simulation()

		def simulate (pTotalTicks :Int, pEventTypes :String, pInit :String) {
			var totalTicks = pTotalTicks
			if(totalTicks == 0)
				totalTicks = TOTAL_TICKS

			var eventTypesStr = pEventTypes
			if(eventTypesStr.length ==0 )
				eventTypesStr= "1,2,3"

			var eventTypesList = eventTypesStr.split(",")


			//First load intial Inventory
			if(pInit.equals("1")) {
				createTimeline(internalTN)
				for (part <- parts) {
					Inventory.create(part)
				}
			}

			// load queue with some number of events
			var t = 0
			var curOrderDate = org.joda.time.DateTime.parse(BEGIN_ORDER_DATE, fmt)

			while (t < totalTicks) {
				//Purchase Order Create Events
				if( eventTypesList contains "1"){
					for (part <- parts) {
						if ( RandomGen.rand(100) >25) {
							theSimulation.scheduleEvent(
								new POCreateEvent(part,  curOrderDate.toString(fmt), (curOrderDate.plusDays(RandomGen.randBetween(MIN_ENDDAYS,MAX_ENDDAYS))).toString(fmt),
									RandomGen.randBetween(MIN_QTY,MAX_QTY)) )

						}
					}
				}

				//Purchase Order Change Qty
				if( eventTypesList contains "2"){
					for (part <- parts) {
						if ( RandomGen.rand(100) >75) {
							theSimulation.scheduleEvent(
								new POChangeQtyEvent(part,  curOrderDate.toString(fmt), (curOrderDate.plusDays(RandomGen.randBetween(MIN_ENDDAYS,MAX_ENDDAYS))).toString(fmt),
									RandomGen.randBetween(MIN_QTY,MAX_QTY),  RandomGen.randBetween(MIN_QTY,MAX_QTY)) )

						}
					}
				}
				//Purchase Order DeliveryDate Qty
				if( eventTypesList contains "3"){
					for (part <- parts) {
						if ( RandomGen.rand(100) >75) {
							theSimulation.scheduleEvent(
								new POChangeDeliveryEvent(part,  curOrderDate.toString(fmt), (curOrderDate.plusDays(RandomGen.randBetween(MIN_ENDDAYS,MAX_ENDDAYS))).toString(fmt),
									(curOrderDate.plusDays(RandomGen.randBetween(MIN_ENDDAYS,MAX_ENDDAYS))).toString(fmt),
									RandomGen.randBetween(MIN_QTY,MAX_QTY)) )

						}
					}
				}

				//Sales ORder Create Events
				if( eventTypesList contains "4"){
					for (part <- parts) {
						if ( RandomGen.rand(100) >25) {
							theSimulation.scheduleEvent(
								new SOCreateEvent(part,  curOrderDate.toString(fmt), (curOrderDate.plusDays(RandomGen.randBetween(MIN_ENDDAYS,MAX_ENDDAYS))).toString(fmt),
									RandomGen.randBetween(MIN_QTY,MAX_QTY)) )

						}
					}
				}

				//Sales Order Change Qty
				if( eventTypesList contains "5"){
					for (part <- parts) {
						if ( RandomGen.rand(100) >75) {
							theSimulation.scheduleEvent(
								new SOChangeQtyEvent(part,  curOrderDate.toString(fmt), (curOrderDate.plusDays(RandomGen.randBetween(MIN_ENDDAYS,MAX_ENDDAYS))).toString(fmt),
									RandomGen.randBetween(MIN_QTY,MAX_QTY),  RandomGen.randBetween(MIN_QTY,MAX_QTY)) )

						}
					}
				}

				//Sales Order DeliveryDate Qty
				if( eventTypesList contains "6"){
					for (part <- parts) {
						if ( RandomGen.rand(100) >75) {
							theSimulation.scheduleEvent(
								new SOChangeDeliveryEvent(part,  curOrderDate.toString(fmt), (curOrderDate.plusDays(RandomGen.randBetween(MIN_ENDDAYS,MAX_ENDDAYS))).toString(fmt),
									(curOrderDate.plusDays(RandomGen.randBetween(MIN_ENDDAYS,MAX_ENDDAYS))).toString(fmt),
									RandomGen.randBetween(MIN_QTY,MAX_QTY)) )

						}
					}
				}

				//Transfer ORder Create Events
				if( eventTypesList contains "7"){
					for (part <- parts) {
						if ( RandomGen.rand(100) >50) {
							var destination = parts(RandomGen.randBetween(0,partsCnt-1))
							if(part !=destination ) {
								theSimulation.scheduleEvent(
									new TOCreateEvent(part,destination, curOrderDate.toString(fmt), (curOrderDate.plusDays(RandomGen.randBetween(MIN_ENDDAYS,MAX_ENDDAYS))).toString(fmt),
										RandomGen.randBetween(MIN_QTY,MAX_QTY)) )
							}
						}
					}
				}

				//Transfer Order Change Qty
				if( eventTypesList contains "8"){
					for (part <- parts) {
						if ( RandomGen.rand(100) >75) {
							var destination = parts(RandomGen.randBetween(0,partsCnt-1))
							if(part !=destination ) {
								theSimulation.scheduleEvent(
									new TOChangeQtyEvent(part ,destination, curOrderDate.toString(fmt), (curOrderDate.plusDays(RandomGen.randBetween(MIN_ENDDAYS,MAX_ENDDAYS))).toString(fmt),
										RandomGen.randBetween(MIN_QTY,MAX_QTY),  RandomGen.randBetween(MIN_QTY,MAX_QTY)) )
							}
						}
					}
				}

				//Trasfer Order DeliveryDate Qty
				if( eventTypesList contains "9"){
					for (part <- parts) {
						if ( RandomGen.rand(100) >75) {
							var destination = parts(RandomGen.randBetween(0,partsCnt-1))
							if(part !=destination ) {
								theSimulation.scheduleEvent(
									new TOChangeDeliveryEvent(part ,destination, curOrderDate.toString(fmt), (curOrderDate.plusDays(RandomGen.randBetween(MIN_ENDDAYS,MAX_ENDDAYS))).toString(fmt),
										(curOrderDate.plusDays(RandomGen.randBetween(MIN_ENDDAYS,MAX_ENDDAYS))).toString(fmt),   RandomGen.randBetween(MIN_QTY,MAX_QTY)) )
							}
						}
					}
				}




				theSimulation.run
				t += 1
				curOrderDate = curOrderDate.plusDays(TICK_LENGTH_DAYS);
				//wait for tick interval
				Thread.sleep(TICKINTERVAL)
			}
		}

		// convenience methods to delegate calls to the Simulation instance
		def scheduleEvent(newEvent: Event) { theSimulation.scheduleEvent(newEvent) }
	}


	test("Supply Chain Simulator  ") {


		//Generate Purchase Orders for 1 day, after clearing database
		var noOfDays = 1
		var eventTypes = "1"
		var clearDB = "1"

		var df = sqlContext.read.options(internalOptions).splicemachine
		assert(df.count > 0)

		//Generate Purchase Orders for 2 days, after clearing database
		noOfDays = 2
		eventTypes = "1"
		clearDB = "1"
		SupplyChain.simulate(noOfDays,eventTypes ,clearDB )
		df = sqlContext.read.options(internalOptions).splicemachine
		assert(df.count > 0)


		//Generate fPurchase Order for 1 day without clearing db

		noOfDays = 1
		eventTypes = "1"
		clearDB = "2"
		SupplyChain.simulate(noOfDays,eventTypes ,clearDB )
		df = sqlContext.read.options(internalOptions).splicemachine
		assert(df.count > 0)

		//Geenrate TransferOrder for 1  day after clearing database
		noOfDays = 1
		eventTypes = "1,7"
		clearDB = "1"
		SupplyChain.simulate(noOfDays,eventTypes ,clearDB )
		df = sqlContext.read.options(internalOptions).splicemachine
		assert(df.count > 0)


	}



}
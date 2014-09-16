package com.splicemachine.constants;

import com.google.common.collect.Lists;
import com.splicemachine.utils.SpliceLogUtils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SpliceConstants {
    private static final Logger LOG = Logger.getLogger(SpliceConstants.class);

    @Parameter public static final String ROLL_FORWARD_SEGMENTS = "splice.rollforward.numSegments";
    @DefaultValue(ROLL_FORWARD_SEGMENTS) public static final int DEFAULT_ROLLFORWARD_SEGMENTS = 4; //by default, split into 4 segments
    public static int numRollForwardSegments;

    @Parameter public static final String ROLL_FORWARD_ROW_THRESHOLD = "splice.rollforward.rowThreshold";
    @DefaultValue(ROLL_FORWARD_ROW_THRESHOLD) public static final int DEFAULT_ROLLFOWARD_ROW_THRESHOLD=1<<14; //about 16K
    public static int rollForwardRowThreshold;

    @Parameter public static final String ROLL_FORWARD_TXN_THRESHOLD = "splice.rollforward.rowThreshold";
    @DefaultValue(ROLL_FORWARD_TXN_THRESHOLD) public static final int DEFAULT_ROLLFOWARD_TXN_THRESHOLD=1<<10; //about 1K
    public static int rollForwardTxnThreshold;

    @Parameter public static final String MAX_DDL_WAIT = "splice.ddl.maxWaitSeconds";
    @DefaultValue(MAX_DDL_WAIT) public static final int DEFAULT_MAX_DDL_WAIT=240;
    public static long maxDdlWait;

    public enum AuthenticationType {NONE,LDAP,NATIVE,CUSTOM};

		@Parameter public static final String SEQUENTIAL_IMPORT_THREASHOLD="splice.import.sequentialFileSize";
		@DefaultValue(SEQUENTIAL_IMPORT_THREASHOLD) public static final long DEFAULT_SEQUENTIAL_IMPORT_THRESHOLD = 1024*1024*1024; //defaults to 1GB
		public static long sequentialImportThreashold;
		public static int sampleTimingSize = 10000;
		
		@Parameter public static final String SEQUENTIAL_IMPORT_FILESIZE_THREASHOLD="splice.import.sequentialImportFileSizeThreshold";
		@DefaultValue(SEQUENTIAL_IMPORT_FILESIZE_THREASHOLD) public static final long DEFAULT_SEQUENTIAL_FILESIZE_IMPORT_THRESHOLD = 5L*1024L*1024L*1024L; //defaults to 5GB
		public static long sequentialImportFileSizeThreshold;

		@SpliceConstants.Parameter public static final String CONSTRAINTS_ENABLED ="splice.constraints.enabled";
		@DefaultValue(CONSTRAINTS_ENABLED) public static final boolean DEFAULT_CONSTRAINTS_ENABLED = true;
		public static volatile boolean constraintsEnabled;

		@Parameter public static final String IMPORT_LOG_QUEUE_SIZE = "splice.import.badRecords.queueSize";
		@DefaultValue(IMPORT_LOG_QUEUE_SIZE) private static final int DEFAULT_IMPORT_LOG_QUEUE_SIZE = 1000;
		public static int importLogQueueSize;

		@Parameter public static final String PUSH_FORWARD_RING_BUFFER_SIZE = "splice.rollforward.pushForwardRingBufferSize";
		@DefaultValue(PUSH_FORWARD_RING_BUFFER_SIZE) private static final int DEFAULT_PUSH_FORWARD_RING_BUFFER_SIZE = 4096;
		public static int pushForwardRingBufferSize;

		@Parameter public static final String FLUSH_QUEUE_SIZE_BLOCK = "splice.client.write.flushQueueSizeBlock";
		@DefaultValue(FLUSH_QUEUE_SIZE_BLOCK) private static final int DEFAULT_FLUSH_QUEUE_SIZE_BLOCK = 2;
		public static int flushQueueSizeBlock;
						
		@Parameter public static final String COMPACTION_QUEUE_SIZE_BLOCK = "splice.client.write.compactionQueueSizeBlock";
		@DefaultValue(COMPACTION_QUEUE_SIZE_BLOCK) private static final int DEFAULT_COMPACTION_QUEUE_SIZE_BLOCK = 1000;
		public static int compactionQueueSizeBlock;
		
		@Parameter public static final String PUSH_FORWARD_WRITE_BUFFER_SIZE = "splice.rollforward.pushForwardWriteBufferSize";
		@DefaultValue(PUSH_FORWARD_WRITE_BUFFER_SIZE) private static final int DEFAULT_PUSH_FORWARD_WRITE_BUFFER_SIZE = 2048;
		public static int pushForwardWriteBufferSize;
		
		@Parameter public static final String DELAYED_FORWARD_RING_BUFFER_SIZE = "splice.rollforward.delayedForwardRingBufferSize";
		@DefaultValue(PUSH_FORWARD_RING_BUFFER_SIZE) private static final int DEFAULT_DELAYED_FORWARD_RING_BUFFER_SIZE = 4096;
		public static int delayedForwardRingBufferSize;

		@Parameter public static final String DELAYED_FORWARD_QUEUE_LIMIT = "splice.rollforward.delayedForwardQueueLimit";
		@DefaultValue(DELAYED_FORWARD_QUEUE_LIMIT) private static final int DEFAULT_DELAYED_FORWARD_QUEUE_LIMIT = 10;
		public static int delayedForwardQueueLimit;
		
		@Parameter public static final String DELAYED_FORWARD_WRITE_BUFFER_SIZE = "splice.rollforward.delayedForwardWriteBufferSize";
		@DefaultValue(DELAYED_FORWARD_WRITE_BUFFER_SIZE) private static final int DEFAULT_DELAYED_FORWARD_WRITE_BUFFER_SIZE = 2048;
		public static int delayedForwardWriteBufferSize;

		@Parameter public static final String DELAYED_FORWARD_ASYNCH_WRITE_DELAY = "splice.rollforward.delayedForwardAsynchWriteDelay";
		@DefaultValue(DELAYED_FORWARD_ASYNCH_WRITE_DELAY) private static final int DEFAULT_DELAYED_FORWARD_ASYNCH_WRITE_DELAY = 400;
		public static int delayedForwardAsyncWriteDelay;
		
		@Parameter public static final String IMPORT_LOG_QUEUE_WAIT_TIME = "splice.import.badRecords.queueWaitTime";
		@DefaultValue(IMPORT_LOG_QUEUE_WAIT_TIME) private static final long DEFAULT_IMPORT_LOG_QUEUE_WAIT_TIME = TimeUnit.MINUTES.toMillis(1); //1 minute
		public static long importLogQueueWaitTimeMs;

		@Parameter public static final String USE_READ_AHEAD_SCANNER = "splice.scan.useReadAhead";
		@DefaultValue(USE_READ_AHEAD_SCANNER) private static final boolean DEFAULT_USE_READ_AHEAD_SCANNER = false;
		public static boolean useReadAheadScanner;

		@Retention(RetentionPolicy.SOURCE)
		protected @interface Parameter{

		}

		@Retention(RetentionPolicy.SOURCE)
		protected @interface DefaultValue{
				String value();
		}

		// Splice Configuration
		public static Configuration config = SpliceConfiguration.create();

		// Zookeeper Default Paths
		/**
		 * The Path in zookeeper to store task information. Defaults to /spliceTasks
		 */
		@Parameter public static final String BASE_TASK_QUEUE_NODE = "splice.task_queue_node";
		@DefaultValue(BASE_TASK_QUEUE_NODE) public static final String DEFAULT_BASE_TASK_QUEUE_NODE = "/spliceTasks";
		public static String zkSpliceTaskPath;

		/**
		 * The Path in zookeeper for broadcasting messages to all servers
		 * Defaults to /broadcast
		 */
		@Parameter public static final String BROADCAST_PATH = "splice.broadcast_node";
		@DefaultValue(BROADCAST_PATH) public static final String DEFAULT_BROADCAST_PATH = "/broadcast";
		public static String zkSpliceBroadcastPath;
		public static String zkSpliceBroadcastActiveServersPath;
		public static String zkSpliceBroadcastMessagesPath;

		/**
		 * The Path in zookeeper for manipulating DDL information and coordination.
		 * Defaults to /ddl
		 */
		@Parameter public static final String DDL_PATH = "splice.ddl_node";
		@DefaultValue(DDL_PATH) public static final String DEFAULT_DDL_PATH = "/ddl";
		public static String zkSpliceDDLPath;
		public static String zkSpliceDDLActiveServersPath;
		public static String zkSpliceDDLOngoingTransactionsPath;

		/**
		 * The Path in zookeeper to store job information. Defaults to /spliceJobs
		 */
		@Parameter public static final String BASE_JOB_QUEUE_NODE = "splice.job_queue_node";
		@DefaultValue(BASE_JOB_QUEUE_NODE) public static final String DEFAULT_BASE_JOB_QUEUE_NODE = "/spliceJobs";
		public static String zkSpliceJobPath;

		/**
		 * The Path in zookeeper for manipulating transactional information.
		 * Defaults to /transactions
		 */
		@Parameter public static final String TRANSACTION_PATH = "splice.transactions_node";
		@DefaultValue(TRANSACTION_PATH) public static final String DEFAULT_TRANSACTION_PATH = "/transactions";
		public static String zkSpliceTransactionPath;

		/**
		 * The Path in zookeeper for storing the maximum reserved timestamp
		 * from the SpliceTimestampSource implementation.
		 * Defaults to /transactions/maxReservedTimestamp
		 */
		@Parameter public static final String MAX_RESERVED_TIMESTAMP_PATH = "splice.max_reserved_timestamp_node";
		@DefaultValue(MAX_RESERVED_TIMESTAMP_PATH) public static final String DEFAULT_MAX_RESERVED_TIMESTAMP_PATH = "/transactions/maxReservedTimestamp";
		public static String zkSpliceMaxReservedTimestampPath;

		/**
		 * The Path in zookeeper for storing the minimum active transaction.
		 * Defaults to /transactions/minimum
		 */
		@Parameter public static final String MINIMUM_ACTIVE_PATH = "splice.minimum_active_node";
		@DefaultValue(MINIMUM_ACTIVE_PATH) public static final String DEFAULT_MINIMUM_ACTIVE_PATH = "/transactions/minimum";
		public static String zkSpliceMinimumActivePath;

		/**
		 * Path in ZooKeeper for manipulating Conglomerate information.
		 * Defaults to /conglomerates
		 */
		@Parameter public static final String CONGLOMERATE_SCHEMA_PATH = "splice.conglomerates_node";
		@DefaultValue(CONGLOMERATE_SCHEMA_PATH) public static final String DEFAULT_CONGLOMERATE_SCHEMA_PATH = "/conglomerates";
		public static String zkSpliceConglomeratePath;
		public static String zkSpliceConglomerateSequencePath;

		/**
		 * Path in ZooKeeper for storing Derby properties information.
		 * Defaults to /derbyPropertyPath
		 */
		@Parameter public static final String DERBY_PROPERTY_PATH = "splice.derby_property_node";
		@DefaultValue(DERBY_PROPERTY_PATH) public static final String DEFAULT_DERBY_PROPERTY_PATH = "/derbyPropertyPath";
		public static String zkSpliceDerbyPropertyPath;
		public static String zkSpliceQueryNodePath;

		/**
		 * Location of Startup node in ZooKeeper. The presence of this node
		 * indicates whether or not Splice needs to attempt to recreate
		 * System tables (i.e. whether or not Splice has been installed and
		 * set up correctly).
		 * Defaults to /startupPath
		 */
		@Parameter public static final String STARTUP_PATH = "splice.startup_path";
		@DefaultValue(STARTUP_PATH) public static final String DEFAULT_STARTUP_PATH = "/startupPath";
		public static String zkSpliceStartupPath;

		/**
		 * Location of Leader Election path in ZooKeeper.
		 * Defaults to /leaderElection
		 */
		@Parameter public static final String LEADER_ELECTION = "splice.leader_election";
		@DefaultValue(LEADER_ELECTION) public static final String DEFAULT_LEADER_ELECTION = "/leaderElection";
		public static String zkLeaderElection;

    /* Derby configuration settings */

		/**
		 * The IP address to bind the Derby connection to.
		 * Defaults to 0.0.0.0
		 */
		@Parameter public static final String DERBY_BIND_ADDRESS = "splice.server.address";
		@DefaultValue(DERBY_BIND_ADDRESS) public static final String DEFAULT_DERBY_BIND_ADDRESS = "0.0.0.0";
		public static String derbyBindAddress;

		/**
		 * The Port to bind the Derby connection to.
		 * Defaults to 1527
		 */
		@Parameter public static final String DERBY_BIND_PORT = "splice.server.port";
		@DefaultValue(DERBY_BIND_PORT) public static final int DEFAULT_DERBY_BIND_PORT = 1527;
		public static int derbyBindPort;

		// Splice timestamp server (generator) settings */

		/**
		 * The IP address to bind the Timestamp Server connection to.
		 * Defaults to 0.0.0.0
		 */
		@Parameter public static final String TIMESTAMP_SERVER_BIND_ADDRESS = "splice.timestamp_server.address";
		@DefaultValue(TIMESTAMP_SERVER_BIND_ADDRESS) public static final String DEFAULT_TIMESTAMP_SERVER_BIND_ADDRESS = "0.0.0.0";
		public static String timestampServerBindAddress;

		/**
		 * The Port to bind the Timestamp Server connection to
		 * Defaults to 60012
		 */
		@Parameter public static final String TIMESTAMP_SERVER_BIND_PORT = "splice.timestamp_server.port";
		@DefaultValue(TIMESTAMP_SERVER_BIND_PORT) public static final int DEFAULT_TIMESTAMP_SERVER_BIND_PORT = 60012;
		public static int timestampServerBindPort;

		/**
		 * The number of timestamps to 'reserve' at a time in the Timestamp Server.
		 * Defaults to 8192
     */
    @Parameter public static final String TIMESTAMP_BLOCK_SIZE = "splice.timestamp_server.blocksize";
    @DefaultValue(TIMESTAMP_BLOCK_SIZE) public static final int DEFAULT_TIMESTAMP_BLOCK_SIZE = 8192;
    public static int timestampBlockSize;

    /**
     * The number of milliseconds the timestamp client should wait for the response.
     * Defaults to 60000 (60 seconds)
     */
    @Parameter public static final String TIMESTAMP_CLIENT_WAIT_TIME = "splice.timestamp_server.clientWaitTime";
    @DefaultValue(TIMESTAMP_CLIENT_WAIT_TIME) public static final int DEFAULT_TIMESTAMP_CLIENT_WAIT_TIME = 60000;
    public static int timestampClientWaitTime;

    /*Task and Job management*/
		/**
		 * The priority under which to run user operation tasks. This can be any positive number, the higher
		 * the priority, the sooner operations will be executed, relative to other prioritized tasks (such
		 * as imports, TEMP cleaning, etc.)
		 * Defaults to 3
		 */
		@Parameter public static final String OPERATION_PRIORITY = "splice.task.operationPriority";
		@DefaultValue(OPERATION_PRIORITY) public static final int DEFAULT_IMPORT_TASK_PRIORITY = 3;
		public static int operationTaskPriority;

		@Parameter public static final String SI_DELAY_ROLL_FORWARD_MAX_SIZE = "splice.si.delayRollForwardMaxSize";
		@DefaultValue(SI_DELAY_ROLL_FORWARD_MAX_SIZE) public static final int DEFAULT_SI_DELAY_ROLL_FORWARD_MAX_SIZE = 300;
		public static int siDelayRollForwardMaxSize;

		
		@SpliceConstants.Parameter public static final String TOTAL_WORKERS = "splice.task.maxWorkers";
		@SpliceConstants.DefaultValue(TOTAL_WORKERS) public static final int DEFAULT_TOTAL_WORKERS=Runtime.getRuntime().availableProcessors()*2;
		public static int taskWorkers;

		@SpliceConstants.Parameter public static final String NUM_PRIORITY_TIERS = "splice.task.numPriorities";
		@DefaultValue(NUM_PRIORITY_TIERS) public static final int DEFAULT_NUM_PRIORITY_TIERS=4;
		public static int numPriorityTiers;

		@SpliceConstants.Parameter public static final String MAX_PRIORITY = "splice.task.maxPriority";
		@DefaultValue(MAX_PRIORITY) public static final int DEFAULT_MAX_PRIORITY=100;
		public static int maxPriority;

		/**
		 *
		 * The Priority with which to assign import tasks. Setting this number higher than the
		 * operation priority will make imports run preferentially to operation tasks; setting it lower
		 * will make operations run preferentially to import tasks.
		 * Defaults to 3
		 */
		@Parameter public static final String IMPORT_TASK_PRIORITY = "splice.task.importTaskPriority";
		@DefaultValue(IMPORT_TASK_PRIORITY) public static final int DEFAULT_OPERATION_PRIORITY = 3;
		public static int importTaskPriority;

		/**
		 * The number of regions that we much import before we attempt to presplit. In general, when
		 * importing, if the file is going to require more than this number of regions, we will split
		 * the table in order to improve overall performance. Turn this number up if it is splitting
		 * too much on small files. Turn it down if it is not splitting sufficiently often.
		 * Defaults to 2.
		 */
		@Parameter public static final String IMPORT_SPLIT_FACTOR = "splice.import.splitRatio";
		@DefaultValue(IMPORT_SPLIT_FACTOR) public static final int DEFAULT_IMPORT_SPLIT_FACTOR=2;
		public static int importSplitFactor;

		/**
		 * The number of threads which will be used to process rows from import files. Increasing this
		 * number will result in a higher number of concurrent table writes, but setting it too high
		 * will result in outpacing the system's ability to read a block of data from disk.
		 * Defaults to 3
		 */
		@Parameter private static final String IMPORT_MAX_PROCESSING_THREADS = "splice.import.maxProcessingThreads";
		@DefaultValue(IMPORT_MAX_PROCESSING_THREADS) private static final int DEFAULT_IMPORT_MAX_PROCESSING_THREADS = 3;
		public static int maxImportProcessingThreads;

		/**
		 * This the number of rows to read before doing an interrupt loop check.  If you kill a statement, it will interrupt
		 * the thread and be caught after 0 to limit n rows set with this parameter.
		 */
		@Parameter private static final String INTERRUPT_LOOP_CHECK = "splice.interrupt.loop.check";
		@DefaultValue(INTERRUPT_LOOP_CHECK) private static final int DEFAULT_INTERRUPT_LOOP_CHECK = 1000;
		public static int interruptLoopCheck;


		/**
		 * The maximum size of the read buffer for importing data. When data is imported, it is read off
		 * the filesystem(HDFS) and pushed into a fixed-size buffer, where it is read by many processing threads.
		 * When the processing threads (set by splice.import.maxProcessingThreads) are set very low, the reading
		 * can outpace the writing, which will fill the buffer and force disk reads to wait for processing. In
		 * this situation, increasing the buffer size will help reduce the amount of time the reader spends
		 * waiting for a processing thread to complete its tasks.
		 *
		 * However, if the setting is too high, and the processing threads are slow (e.g. because of slow
		 * network write speed), then excessive memory can be consumed. Turn this down to relieve memory-pressure
		 * related issues.
		 * Defaults to 1000
		 */
		@Parameter private static final String IMPORT_MAX_READ_BUFFER_SIZE = "splice.import.maxReadBufferSize";
		@DefaultValue(IMPORT_MAX_READ_BUFFER_SIZE) private static final int DEFAULT_IMPORT_MAX_READ_BUFFER_SIZE= 2048;
		public static int maxImportReadBufferSize;

		//common SI fields
		public static final String NA_TRANSACTION_ID = "NA_TRANSACTION_ID";
		public static final String SI_EXEMPT = "si-exempt";

    /*Writer configuration*/
		// Constants

		public static long tablePoolCleanerInterval;

		/**
		 * The maximum number of HTable instances to pool for reuse. It is generally
		 * not necessary to adjust this, unless working in a very constrained memory environment.
		 *
		 * Default is no limit.
		 */
		@Parameter private static final String POOL_MAX_SIZE = "splice.table.pool.maxsize";
		@DefaultValue(POOL_MAX_SIZE) public static final int DEFAULT_POOL_MAX_SIZE = Integer.MAX_VALUE;
		public static int tablePoolMaxSize;

		/**
		 * The core number of HTable instances to pool for reuse. This is the number of HTable
		 * instances that will be kept open no matter what. It is generally not necessary to adjust this,
		 * unless working in a very constrained memory environment.
		 *
		 * Default is 100
		 */
		@Parameter private static final String POOL_CORE_SIZE = "splice.table.pool.coresize";
		@DefaultValue(POOL_CORE_SIZE) public static final int DEFAULT_POOL_CORE_SIZE = 100;
		public static int tablePoolCoreSize;

		/**
		 * The interval(in seconds) at which out HTable instances will be removed from the pool.
		 * It is generally not necessary to adjust this unless working in a very constrained
		 * memory environment.
		 *
		 * Default is 60 seconds.
		 */
		@Parameter private static final String POOL_CLEANER_INTERVAL = "splice.table.pool.cleaner.interval";
		@DefaultValue(POOL_CLEANER_INTERVAL) public static final long DEFAULT_POOL_CLEANER_INTERVAL = 60;

		public static final long DEFAULT_CACHE_UPDATE_PERIOD = 120000;
		public static final long DEFAULT_CACHE_EXPIRATION = 180;

		/**
		 * The maximum size(in bytes) that an individual write buffer will keep in memory before automatically
		 * flushing those writes to the destination Table. Increasing this will reduce network overhead during
		 * heavy write operations, but may result in a large amount of heap to be occupied during the write
		 * process itself (Since Splice uses a compact network representation that differs from its disk representation,
		 * the process of preparing disk writes has the effect of exploding heap space). This memory pressure
		 * is especially problematic for tables with very few, small columns.
		 *
		 * This parameter may be adjusted in real time using JMX.
		 *
		 * Default is 2MB
		 */
		@Parameter private static final String WRITE_BUFFER_SIZE = "hbase.client.write.buffer";
		@DefaultValue(WRITE_BUFFER_SIZE) public static final long DEFAULT_WRITE_BUFFER_SIZE = 2097152;
		public static long writeBufferSize;

		/**
		 * The maximum number of rows that an individual write buffer will keep in memory before automatically
		 * flushing those writes to the destination Table. Increasing this will allow more rows to be sent over
		 * the network at the same time; this reduces the network overhead (potentially improving performance), but
		 * may result in a large amount of heap being occupied during the local-disk write process itself (Since
		 * Splice uses a compact network representation that differs from its disk representation, the process of
		 * preparing local-disk writes has the effect of exploding the heap space used).
		 *
		 * This parameter may be adjusted in real time using JMX.
		 *
		 * Default is 1000
		 */
		@Parameter private static final String BUFFER_ENTRIES = "hbase.client.write.buffer.maxentries";
		@DefaultValue(BUFFER_ENTRIES)public static final int DEFAULT_MAX_BUFFER_ENTRIES = 1024;
		public static int maxBufferEntries;

		/**
		 * The maximum number of threads which may be used to concurrently write data to any HBase table.
		 * In order to prevent potential deadlock situations, this parameter cannot be higher than the
		 * number of available IPC threads (hbase.regionserver.handler.count); setting the max threads
		 * to a number higher than the available IPC threads will have no effect.
		 *
		 * This parameter may be adjusted in real time using JMX.
		 *
		 * Default is 20.
		 */
		@Parameter private static final String WRITE_THREADS_MAX = "splice.writer.maxThreads";
		@DefaultValue(WRITE_THREADS_MAX) public static final int DEFAULT_WRITE_THREADS_MAX = 20;
		public static int maxThreads;

		/**
		 * The number of write threads to allow to remain alive even when the maximum number of threads
		 * is not required. Adjusting this only affects how quickly a write thread is allowed to proceed
		 * in some cases, and the number of threads which are alive in the overall system without at any
		 * given point in time. * This generally does not require adjustment, unless thread-management is
		 * problematic.
		 *
		 * Default is 5.
		 */
		@Parameter private static final String WRITE_THREADS_CORE = "splice.writer.coreThreads";
		@DefaultValue(WRITE_THREADS_CORE) public static final int DEFAULT_WRITE_THREADS_CORE = 5;
		public static int coreWriteThreads;
		
		
		// Optimizer Items
		
		
        public static long regionMaxFileSize;

        /**
         * 
         * This metric is multiplied by number of rows and cost to determine an effect of 1..n extra qualifiers on the source result set.
         * 
         */
		@Parameter private static final String OPTIMIZER_EXTRA_QUALIFIER_MULTIPLIER = "splice.optimizer.extraQualifierMultiplier";
		@DefaultValue(OPTIMIZER_EXTRA_QUALIFIER_MULTIPLIER) public static final double DEFAULT_OPTIMIZER_EXTRA_QUALIFIER_MULTIPLIER = 0.9d;
        public static double extraQualifierMultiplier;

        /**
         * This multiplier is applied to single region tables where their is a start stop qualifier (i.e. constrained on the first column).  This is 
         * a rough estimate for cardinality (yikes).
         * 
         */
		@Parameter private static final String OPTIMIZER_EXTRA_START_STOP_QUALIFIER_MULTIPLIER = "splice.optimizer.extraStartStopQualifierMultiplier";
		@DefaultValue(OPTIMIZER_EXTRA_QUALIFIER_MULTIPLIER) public static final double DEFAULT_OPTIMIZER_EXTRA_START_STOP_QUALIFIER_MULTIPLIER = 0.5d;
        public static double extraStartStopQualifierMultiplier;

        /**
         * 
         * The in-memory cost of hashing a number of records.  This cost is applied to make sure merge join is promoted for not having to perform a hash.
         * 
         */
		@Parameter private static final String OPTIMIZER_HASH_COST = "splice.optimizer.hashCost";
		@DefaultValue(OPTIMIZER_HASH_COST) public static final double DEFAULT_OPTIMIZER_HASH_COST = 0.01;
        public static double optimizerHashCost;

        /**
         * Network cost of calls.  This corresponds to how many network hops while reading remote data.
         * 
         */
		@Parameter private static final String OPTIMIZER_NETWORK_COST = "splice.optimizer.networkCost";
		@DefaultValue(OPTIMIZER_NETWORK_COST) public static final double DEFAULT_OPTIMIZER_NETWORK_COST = 2.00;
        public static double optimizerNetworkCost;        

        /**
         * 
         * The cost of writing data in the case where you need to reshuffle the data.  This is a pretty expensive operation.
         * 
         */
		@Parameter private static final String OPTIMIZER_WRITE_COST = "splice.optimizer.writeCost";
		@DefaultValue(OPTIMIZER_NETWORK_COST) public static final double DEFAULT_OPTIMIZER_WRITE_COST = 3.00;
        public static double optimizerWriteCost;        

		@Parameter private static final String HASHNLJ_LEFTROWBUFFER_SIZE = "splice.hashnlj.leftrowbuffersize";
		@DefaultValue(HASHNLJ_LEFTROWBUFFER_SIZE) public static final int DEFAULT_HASHNLJ_LEFTROWBUFFER_SIZE = 1024;
        public static int hashNLJLeftRowBufferSize;

		@Parameter private static final String HASHNLJ_RIGHTHASHTABLE_SIZE = "splice.hashnlj.rightHashTableSize";
		@DefaultValue(HASHNLJ_RIGHTHASHTABLE_SIZE) public static final int DEFAULT_HASHNLJ_RIGHTHASHTABLE_SIZE = 1024;
        public static int hashNLJRightHashTableSize;

        /**
         * Threshold in megabytes for the broadcast join region size.
         * 
         */
		@Parameter private static final String BROADCAST_REGION_MB_THRESHOLD = "splice.optimizer.broadcastRegionMBThreshold";
		@DefaultValue(BROADCAST_REGION_MB_THRESHOLD) public static final int DEFAULT_BROADCAST_REGION_MB_THRESHOLD = (int) (Runtime.getRuntime().maxMemory() / (1024l * 1024l * 100l));
        public static int broadcastRegionMBThreshold;

        /**
         * Estimate of the number of rows in a region.
         * 
         */
		@Parameter private static final String HBASE_REGION_ROWS_ESTIMATE = "splice.optimizer.hbaseRegionRowsEstimate";
		@DefaultValue(HBASE_REGION_ROWS_ESTIMATE) public static final long DEFAULT_HBASE_REGION_ROWS_ESTIMATE = 5000000;
        public static long hbaseRegionRowEstimate;

        /**
         * 
         * Cost per Row for an Index.  The cost adjustment is really driving the percentage of columns in the index vs. the base table.
         * 
         */
		@Parameter private static final String INDEX_PER_ROW_COST = "splice.optimizer.indexPerRowCost";
		@DefaultValue(INDEX_PER_ROW_COST) public static final double DEFAULT_INDEX_PER_ROW_COST = 1.00d;
		public static double indexPerRowCost;
		
		/**
		 * 
		 * Base Table Per Row Cost Multiplier.
		 * 
		 */
		@Parameter private static final String BASE_TABLE_PER_ROW_COST = "splice.optimizer.baseTablePerRowCost";
		@DefaultValue(BASE_TABLE_PER_ROW_COST) public static final double DEFAULT_BASE_TABLE_PER_ROW_COST = 1.0d;		
		public static double baseTablePerRowCost;

		/**
		 * 
		 * Cost for a random read from the base table from a sorted index (expensive).
		 * 
		 */
		@Parameter private static final String FETCH_FROM_ROW_LOCATION_COST = "splice.optimizer.fetchFromRowLocationCost";
		@DefaultValue(FETCH_FROM_ROW_LOCATION_COST) public static final double DEFAULT_FETCH_FROM_ROW_LOCATION_COST = 7.0d;
		public static double fetchFromRowLocationCost;

		/**
		 * 
		 * A fetch from a primary key on a base table/
		 * 
		 */
		@Parameter private static final String GET_BASE_TABLE_FETCH_FROM_FULL_KEY_COST = "splice.optimizer.getBaseTableFetchFromFullKeyCost";
		@DefaultValue(GET_BASE_TABLE_FETCH_FROM_FULL_KEY_COST) public static final double DEFAULT_GET_BASE_TABLE_FETCH_FROM_FULL_KEY_COST = 1.0d;				
		public static double getBaseTableFetchFromFullKeyCost;

		/**
		 * 
		 * Cost for doing a single fetch from an index (cheap).
		 * 
		 */
		@Parameter private static final String GET_INDEX_FETCH_FROM_FULL_KEY_COST = "splice.optimizer.getIndexFetchFromFullKeyCost";
		@DefaultValue(GET_INDEX_FETCH_FROM_FULL_KEY_COST) public static final double DEFAULT_GET_INDEX_FETCH_FROM_FULL_KEY_COST = 0.1d;						
		public static double getIndexFetchFromFullKeyCost;

		/**
		 * 
		 * The minimum number of rows for the optimizer to consider during a scan against an index or table.
		 * 
		 */
		@Parameter private static final String OPTIMIZER_TABLE_MINIMAL_ROWS = "splice.optimizer.minimalRows";
		@DefaultValue(OPTIMIZER_TABLE_MINIMAL_ROWS) public static final long DEFAULT_OPTIMIZER_TABLE_MINIMAL_ROWS = 20;						
		public static long optimizerTableMinimalRows;
        
		/**
		 * The length of time (in seconds) to wait before killing a write thread which is not in use. Turning
		 * this up will result in more threads being available for writes after longer periods of inactivity,
		 * but will cause higher thread counts in the system overall. Turning this down will result in fewer
		 * threads being maintained in the system at any given point in time, but will also require more
		 * thread startups (potentially affecting performance). This generally does not require adjustment,
		 * unless thread-management is problematic or context switching is knowng to be an issue.
		 *
		 * Default is 60 seconds.
		 */
		@Parameter public static final String HBASE_HTABLE_THREADS_KEEPALIVETIME = "hbase.htable.threads.keepalivetime";
		@DefaultValue(HBASE_HTABLE_THREADS_KEEPALIVETIME) public static final long DEFAULT_HBASE_HTABLE_THREADS_KEEPALIVETIME = 60;

		/**
		 * The amount of time (in milliseconds) to pause before retrying a network operation.
		 *
		 * This parameter is tightly connected to hbase.client.retries.number, as it determines
		 * how long to wait in between each retry. If the pause time is 1 second and the number
		 * of retries is 10, then the total time taken before a write can fail is 71 seconds. If
		 * the pause time is 500 ms, the total time before failing is 35.5 seconds. If the pause
		 * time is 2 seconds, the total time before failing is 142 seconds.
		 *
		 * Turning this setting up is recommended if you are seeing a large number of operations
		 * failing with NotServingRegionException or IndexNotSetUpException errors, or if it
		 * is known that the mean time to recovery of a single region is longer than the total
		 * time before failure.
		 *
		 * This setting may be adjusted in real time using JMX.
		 *
		 * Defaults to 1000 ms (1 second)
		 */
		@Parameter public static final String CLIENT_PAUSE = "hbase.client.pause";
		@DefaultValue(CLIENT_PAUSE) public static final long DEFAULT_CLIENT_PAUSE = 1000;
		public static long pause;

		/**
		 * The number of times to retry a network operation before failing.  Turning this up will reduce the number of spurious
		 * failures caused by network events (NotServingRegionException, IndexNotSetUpException, etc.), but will also lengthen
		 * the time taken by a query before a failure is detected. Turning this down will decrease the latency required
		 * before detecting a failure, but may result in more spurious failures (especially during large writes).
		 *
		 * Generally, the order of retries is as follows:
		 *
		 * try 1, pause, try 2, pause, try 3, pause,try 4, 2*pause, try 5, 2*pause, try 6, 4*pause, try 7, 4*pause,
		 * try 8, 8*pause, try 9, 16*pause, try 10, 32*pause,try 11, 32*pause,...try {max}, fail
		 *
		 * So if the pause time (hbase.client.pause) is set to 1 second, and the number of retries is 10, the total time
		 * before a write can fail is 71 seconds. If the number of retries is 5, then the total time before failing is
		 * 5 seconds. If the number of retries is 20, the total time before failing is 551 seconds(approximately 10 minutes).
		 *
		 * It is recommended to turn this setting up if you are seeing a large number of operations failing with
		 * NotServingRegionException, WrongRegionException, or IndexNotSetUpException errors, or if it is known
		 * that the mean time to recovery of a single region is longer than the total time before failure.
		 *
		 * Defaults to 10.
		 */
		@Parameter public static final String HBASE_CLIENT_RETRIES_NUMBER = "hbase.client.retries.number";
		@DefaultValue(HBASE_CLIENT_RETRIES_NUMBER) public static final int DEFAULT_HBASE_CLIENT_RETRIES_NUMBER = HConstants.DEFAULT_HBASE_CLIENT_RETRIES_NUMBER;
		public static int numRetries;

		/**
		 * The type of compression to use when compressing Splice Tables. This is set the same way
		 * HBase sets table compression, and has the same codecs available to it (GZIP,Snappy, or
		 * LZO depending on what is installed).
		 *
		 * Defaults to none
		 */
		@Parameter public static final String COMPRESSION = "splice.compression";
		@DefaultValue(COMPRESSION) public static final String DEFAULT_COMPRESSION = "none";
		public static String compression;

		/**
		 * The type of algorithm to use for native encryption.  Optional values are
		 *  MD5, SHA-256, and SHA-512 (Default).
		 *
		 * Defaults to none
		 */
		@Parameter public static final String AUTHENTICATION = "splice.authentication";
		@DefaultValue(AUTHENTICATION) public static final String DEFAULT_AUTHENTICATION = "NONE";
		public static String authentication;
		
		@Parameter public static final String AUTHENTICATION_LDAP_SERVER = "splice.authentication.ldap.server";
		@DefaultValue(AUTHENTICATION_LDAP_SERVER) public static final String DEFAULT_AUTHENTICATION_LDAP_SERVER = "localhost:389";
		public static String authenticationLDAPServer;
		
		@Parameter public static final String AUTHENTICATION_LDAP_SEARCHAUTHDN = "splice.authentication.ldap.searchAuthDN";
		@DefaultValue(AUTHENTICATION_LDAP_SEARCHAUTHDN) public static final String DEFAULT_AUTHENTICATION_LDAP_SEARCHAUTHDN = "";
		public static String authenticationLDAPSearchAuthDN;

		@Parameter public static final String AUTHENTICATION_LDAP_SEARCHAUTHPW = "splice.authentication.ldap.searchAuthPW";
		@DefaultValue(AUTHENTICATION_LDAP_SEARCHAUTHPW) public static final String DEFAULT_AUTHENTICATION_LDAP_SEARCHAUTHPW = "";
		public static String authenticationLDAPSearchAuthPW;

		@Parameter public static final String AUTHENTICATION_LDAP_SEARCHBASE = "splice.authentication.ldap.searchBase";
		@DefaultValue(AUTHENTICATION_LDAP_SEARCHBASE) public static final String DEFAULT_AUTHENTICATION_LDAP_SEARCHBASE = "";
		public static String authenticationLDAPSearchBase;

		@Parameter public static final String AUTHENTICATION_LDAP_SEARCHFILTER = "splice.authentication.ldap.searchFilter";
		@DefaultValue(AUTHENTICATION_LDAP_SEARCHFILTER) public static final String DEFAULT_AUTHENTICATION_LDAP_SEARCHFILTER = "";
		public static String authenticationLDAPSearchFilter;		
		
		@Parameter public static final String AUTHENTICATION_NATIVE_ALGORITHM = "splice.authentication.native.algorithm";
		@DefaultValue(AUTHENTICATION_NATIVE_ALGORITHM) public static final String DEFAULT_AUTHENTICATION_NATIVE_ALGORITHM = "SHA-512";
		public static String authenticationNativeAlgorithm;
		
		@Parameter public static final String AUTHENTICATION_CUSTOM_PROVIDER = "splice.authentication.custom.provider";
		@DefaultValue(AUTHENTICATION_CUSTOM_PROVIDER) public static final String DEFAULT_AUTHENTICATION_CUSTOM_PROVIDER = "com.splicemachine.derby.authentication.SpliceUserAuthentication";
		public static String authenticationCustomProvider;					
		
		@Parameter public static final String MULTICAST_GROUP_ADDRESS = "splice.multicast_group_address";
		@DefaultValue(MULTICAST_GROUP_ADDRESS) public static final String DEFAULT_MULTICAST_GROUP_ADDRESS = "230.0.0.1";
		public static String multicastGroupAddress;

		@Parameter public static final String MULTICAST_GROUP_PORT = "splice.multicast_group_port";
		@DefaultValue(MULTICAST_GROUP_PORT) public static final int DEFAULT_MULTICAST_GROUP_PORT = 4446;
		public static int multicastGroupPort;

		@Parameter public static final String RMI_PORT = "splice.rmi_port";
		@DefaultValue(RMI_PORT) public static final int DEFAULT_RMI_PORT = 40001;
		public static int rmiPort;

		public static final int DEFAULT_RMI_REMOTE_OBJECT_PORT = 47000;

		/**
		 * The amount of time (in milliseconds) to wait during index initialization before
		 * forcing a write to return. This setting prevents deadlocks during startup in small clusters,
		 * and is also the source of IndexNotSetUpExceptions.
		 *
		 * If an excessively high number of IndexNotSetUpExceptions are being seen, consider increasing
		 * this setting. However, if set too high, this may result in deadlocks on small clusters.
		 *
		 * Defaults to 1000 ms (1 s)
		 */
		@Parameter private static final String STARTUP_LOCK_WAIT_PERIOD = "splice.startup.lockWaitPeriod";
		@DefaultValue(STARTUP_LOCK_WAIT_PERIOD) public static final int DEFAULT_STARTUP_LOCK_PERIOD=1000;

		/**
		 * The maximum number of entries to hold in aggregate/distinct ring buffers before forcing
		 * an eviction.
		 *
		 * The higher this is set, the more rows will be aggregated during the map stage
		 * of aggregate operations, but the more memory will be occupied by the buffer.
		 *
		 * Turn this setting up to improve aggregate performance with unsorted data. Turn this
		 * setting down if memory pressure is high during aggregations.
		 */
		@Parameter private static final String RING_BUFFER_SIZE = "splice.ring.bufferSize";
		@DefaultValue(RING_BUFFER_SIZE) public static final int DEFAULT_RING_BUFFER_SIZE=1<<14; //~ 16K
		public static int ringBufferSize;

		/**
		 * The number of index rows to bulk fetch at a single time.
		 *
		 * Index lookups are bundled together into a single network operation for many rows.
		 * This setting determines the maximum number of rows which are fetched in a single
		 * network operation.
		 *
		 * Defaults to 4000
		 */
		@Parameter private static final String INDEX_BATCH_SIZE = "splice.index.batchSize";
		@DefaultValue(INDEX_BATCH_SIZE) public static final int DEFAULT_INDEX_BATCH_SIZE=4000;
		public static int indexBatchSize;

		/**
		 * The number of concurrent bulk fetches a single index operation can initiate
		 * at a time. If fewer than that number of fetches are currently in progress, the
		 * index operation will submit a new bulk fetch. Once this setting's number of bulk
		 * fetches has been reached, the index lookup must wait for one of the previously
		 * submitted fetches to succeed before continuing.
		 *
		 * Index lookups will only submit a new bulk fetch if existing data is not already
		 * available.
		 *
		 * Defaults to 5
		 */
		@Parameter private static final String INDEX_LOOKUP_BLOCKS = "splice.index.numConcurrentLookups";
		@DefaultValue(INDEX_LOOKUP_BLOCKS) private static final int DEFAULT_INDEX_LOOKUP_BLOCKS = 5;
		public static int indexLookupBlocks;

		/**
		 * The maximum number of Kryo objects to pool for reuse. This setting is generally
		 * not necessary to adjust unless there are an extremely large number of concurrent
		 * operations allowed on the system. Adjusting this down may lengthen the amount of
		 * time required to perform an operation slightly.
		 *
		 * Defaults to 50.
		 */
		@Parameter private static final String KRYO_POOL_SIZE = "splice.marshal.kryoPoolSize";
		@DefaultValue(KRYO_POOL_SIZE) public static final int DEFAULT_KRYO_POOL_SIZE=16000;
		public static int kryoPoolSize;


		/**
		 * The Default Cache size for Scans.
		 *
		 * This determines the default number of rows that will be cached on each scan returned.
		 *
		 * We make it a power of two to make it easier to write buffers which are powers of 2.
		 */
		public static final int DEFAULT_CACHE_SIZE = (1<<10);


		/*
		 * Setting the cache update interval <0 indicates that caching is to be turned off.
		 * This is a performance killer, but is useful when debugging issues.
		 */
		public static final String CACHE_UPDATE_PERIOD = "hbase.htable.regioncache.updateinterval";
		public static final String CACHE_EXPIRATION = "hbase.htable.regioncache.expiration";
		public static final String RMI_REMOTE_OBJECT_PORT = "splice.rmi_remote_object_port";

		//debug options
		/**
		 * For debugging an operation, this will force the query parser to dump any generated
		 * class files to the HBASE_HOME directory. This is not useful for anything except
		 * debugging certain kinds of errors, and is NOT recommended enabled in a production
		 * environment.
		 *
		 * Defaults to false (off)
		 */
		@Parameter private static final String DEBUG_DUMP_CLASS_FILE = "splice.debug.dumpClassFile";
		@DefaultValue(DEBUG_DUMP_CLASS_FILE) public static final boolean DEFAULT_DUMP_CLASS_FILE=false;
		public static boolean dumpClassFile;

		/**
		 * For debugging statements issued in derby.  This is on by default, but will hurt you in the case of an OLTP
		 * workload.
		 * 
		 * 
		 */
		@Parameter private static final String DEBUG_LOG_STATEMENT_CONTEXT = "splice.debug.logStatementContext";
		@DefaultValue(DEBUG_DUMP_CLASS_FILE) public static final boolean DEFAULT_LOG_STATEMENT_CONTEXT=true;
		public static boolean logStatementContext;


		//internal debugging tools
		public static final String DEBUG_FAIL_TASKS_RANDOMLY = "splice.debug.failTasksRandomly";
		public static final boolean DEFAULT_DEBUG_FAIL_TASKS_RANDOMLY=false;
		public static boolean debugFailTasksRandomly;
		public static final String DEBUG_TASK_FAILURE_RATE = "splice.debug.taskFailureRate";
		public static double debugTaskFailureRate;
		public static final double DEFAULT_DEBUG_TASK_FAILURE_RATE= 0.1; //fail 10% of tasks when enabled

		/**
		 * When enabled, will collect timing stats for TableScans, Index lookups, and a few other
		 * things, and log those results to JMX and/or a logger.
		 *
		 * This is useful for performance analysis, but it does incur overhead. Enabling
		 * it is not recommended for production environments.
		 *
		 * Defaults to false (off)
		 */
		@Parameter public static final String COLLECT_PERF_STATS ="splice.collectTimingStatistics";
		@DefaultValue(COLLECT_PERF_STATS) public static final boolean DEFAULT_COLLECT_STATS = false;
		public static boolean collectStats;

		/**
		 * Amount of time(in milliseconds) taken to wait for a Region split to occur before checking on that
		 * split's status during internal Split operations. It is generally not recommended
		 * to adjust this setting unless Region splits take an incredibly short or long amount
		 * of time to complete.
		 *
		 * Defaults to 500 ms.
		 */
		@Parameter public static final String SPLIT_WAIT_INTERVAL = "splice.splitWaitInterval";
		@DefaultValue(SPLIT_WAIT_INTERVAL) public static final long DEFAULT_SPLIT_WAIT_INTERVAL = 500l;
		public static Long sleepSplitInterval;

		/**
		 * The maximum number of operations which will be executed in parallel during tree-parsing phase.
		 *
		 * This is primarily of use to operations which can multiply their actions (e.g. nested MergeSortJoins).
		 * It generally does not require adjustment.
		 *
		 * Defaults to 20
		 */
		@Parameter private static final String MAX_CONCURRENT_OPERATIONS = "splice.tree.maxConcurrentOperations";
		@DefaultValue(MAX_CONCURRENT_OPERATIONS) private static final int DEFAULT_MAX_CONCURRENT_OPERATIONS = 20; //probably too low
		public static int maxTreeThreads; //max number of threads for concurrent stack execution

		public static long threadKeepAlive;
		public static boolean enableRegionCache;
		public static long cacheExpirationPeriod;
		public static int rmiRemoteObjectPort;
		public static int startupLockWaitPeriod;

		/**
		 * The number of sequential entries to reserve in a single sequential block.
		 *
		 * Splice uses weakly-ordered sequential generation, in that each RegionServer will perform
		 * one network operation to "reserve" a block of adjacent numbers, then it will sequentially
		 * use those numbers until the block is exhausted, before fetching another block. The result
		 * of which is that two different RegionServers operating concurrently with the same sequence
		 * will see blocks out of order, but numbers ordered within those blocks.
		 *
		 * This setting configures how large those blocks may be. Turning it up will result in fewer
		 * network operations during large-scale sequential id generation, and also less block-reordering
		 * due to the weak-ordering. However, it will also result in a greater number of "missing" ids, since
		 * a block, once allocated, can never be allocated again.
		 *
		 * Defaults to 1000
		 */
		@Parameter private static final String SEQUENCE_BLOCK_SIZE = "splice.sequence.allocationBlockSize";
		@DefaultValue(SEQUENCE_BLOCK_SIZE) private static final int DEFAULT_SEQUENCE_BLOCK_SIZE = 1000;
		public static long sequenceBlockSize;

		/**
		 * The initial wait in milliseconds when a DDL operation waits for all concurrent transactions to finish before
		 * proceeding.
		 *
		 * The operation will wait progressively longer until the DDL_DRAINING_MAXIMUM_WAIT is reached, then it will
		 * block concurrent transactions from writing to the affected tables.
		 *
		 * Defaults to 1000 (1 second)
		 */
		@Parameter private static final String DDL_DRAINING_INITIAL_WAIT = "splice.ddl.drainingWait.initial";
		@DefaultValue(DDL_DRAINING_INITIAL_WAIT) private static final long DEFAULT_DDL_DRAINING_INITIAL_WAIT = 1000;
		public static long ddlDrainingInitialWait;

		/**
		 * The maximum wait in milliseconds a DDL operation will wait for concurrent transactions to finish before
		 * blocking them from writing to the affected tables.
		 *
		 * Defaults to 100000 (100 seconds)
		 */
		@Parameter private static final String DDL_DRAINING_MAXIMUM_WAIT = "splice.ddl.drainingWait.maximum";
		@DefaultValue(DDL_DRAINING_MAXIMUM_WAIT) private static final long DEFAULT_DDL_DRAINING_MAXIMUM_WAIT = 100000;
		public static long ddlDrainingMaximumWait;

		/**
		 * The lease duration for metadata caches in milliseconds.
		 *
		 * If the duration is bigger, Splice servers cache metadata information for longer, putting less pressure on the
		 * metadata regions and reducing latency for DML operations. On the other hand, this increases the latency
		 * for DDL operations.
		 *
		 * Defaults to 1000 (1 second)
		 */
		@Parameter private static final String METADATA_CACHE_LEASE_DURATION = "splice.metadata.cache.lease";
		// TODO change to something reasonable
		@DefaultValue(METADATA_CACHE_LEASE_DURATION) private static final long DEFAULT_METADATA_CACHE_LEASE_DURATION = 0;
		public static long metadataCacheLease;

		@SpliceConstants.Parameter private static final String INTER_REGION_TASK_SPLIT_THRESHOLD_BYTES="splice.interRegion.splitThresholdBytes";
		@SpliceConstants.DefaultValue(INTER_REGION_TASK_SPLIT_THRESHOLD_BYTES) private static final long DEFAULT_INTER_REGION_TASK_SPLIT_THRESHOLD_BYTES=32*1024*1024l;
		public static long interRegionTaskSplitThresholdBytes;

		@Parameter private static final String MAX_INTER_REGION_TASK_SPLITS="splice.interRegion.maxSplits";
		@DefaultValue(MAX_INTER_REGION_TASK_SPLITS) private static final int DEFAULT_MAX_INTER_REGION_TASK_SPLITS=8;
		public static int maxInterRegionTaskSplits;

		/*
		 * Setting the cache update interval <0 indicates that caching is to be turned off.
		 * This is a performance killer, but is useful when debugging issues.
		 */
		public static long cacheUpdatePeriod;


		// Splice Internal Tables
		public static final String TEMP_TABLE = "SPLICE_TEMP";
		public static final String TEST_TABLE = "SPLICE_TEST";
		public static final String TRANSACTION_TABLE = "SPLICE_TXN";
		public static final String TENTATIVE_TABLE = "TENTATIVE_DDL";
		public static final int TRANSACTION_TABLE_BUCKET_COUNT = 16; //must be a power of 2
		public static final String CONGLOMERATE_TABLE_NAME = "SPLICE_CONGLOMERATE";
		public static final String SEQUENCE_TABLE_NAME = "SPLICE_SEQUENCES";
		public static final String SYSSCHEMAS_CACHE = "SYSSCHEMAS_CACHE";
		public static final String SYSSCHEMAS_INDEX1_ID_CACHE = "SYSSCHEMAS_INDEX1_ID_CACHE";
		public static final String[] SYSSCHEMAS_CACHES = {SYSSCHEMAS_CACHE,SYSSCHEMAS_INDEX1_ID_CACHE};

		public static byte[] TEMP_TABLE_BYTES = Bytes.toBytes(TEMP_TABLE);
		public static final byte[] TRANSACTION_TABLE_BYTES = Bytes.toBytes(TRANSACTION_TABLE);
		public static final byte[] TENTATIVE_TABLE_BYTES = Bytes.toBytes(TENTATIVE_TABLE);
		public static final byte[] CONGLOMERATE_TABLE_NAME_BYTES = Bytes.toBytes(CONGLOMERATE_TABLE_NAME);
		public static final byte[] SEQUENCE_TABLE_NAME_BYTES = Bytes.toBytes(SEQUENCE_TABLE_NAME);
		public static final int PACKED_COLUMN = 7;
		public static final String PACKED_COLUMN_STRING = PACKED_COLUMN+"";
		public static final byte[] PACKED_COLUMN_BYTES = Bytes.toBytes(PACKED_COLUMN_STRING);
		public static final byte PACKED_COLUMN_BYTE = PACKED_COLUMN_BYTES[0];		

		
		
		// Splice Family Information
		public static final String DEFAULT_FAMILY = "V";
		public static final byte[] DEFAULT_FAMILY_BYTES = Bytes.toBytes(DEFAULT_FAMILY);

		public static final String SI_PERMISSION_FAMILY = "P";

		//TEMP Table task column--used for filtering out failed tasks from the temp
		//table

		// Splice Default Table Definitions
		public static final Boolean DEFAULT_IN_MEMORY = HColumnDescriptor.DEFAULT_IN_MEMORY;
		public static final Boolean DEFAULT_BLOCKCACHE=HColumnDescriptor.DEFAULT_BLOCKCACHE;
		public static final int DEFAULT_TTL = HColumnDescriptor.DEFAULT_TTL;
		public static final String DEFAULT_BLOOMFILTER = HColumnDescriptor.DEFAULT_BLOOMFILTER;

		// Default Constants
		public static final String SUPPRESS_INDEXING_ATTRIBUTE_NAME = "iu";
		public static final byte[] SUPPRESS_INDEXING_ATTRIBUTE_VALUE = new byte[]{};
		public static final String CHECK_BLOOM_ATTRIBUTE_NAME = "cb";
		public static final String SPLICE_DB = "splicedb";

		public static final String ENTRY_PREDICATE_LABEL= "p";

		// Default Configuration Options

		/**
		 * The maximum number of concurrent buffer flushes that are allowed to be directed to a single
		 * region by a single write operation. This helps to prevent overloading an individual region,
		 * at the cost of reducing overall throughput to that region.
		 *
		 * Turn this setting down if you encounter an excessive number of RegionTooBusyExceptions. Turn
		 * this setting up if system load is lower than expected during large writes, and the number of write
		 * threads are not fully utilized.
		 *
		 * This setting becomes useless once set higher than the maximum number of write threads (splice.writer.maxThreads),
		 * as a single region can never allocate more than the maximum total number of write threads.
		 *
		 * Defaults to 5
		 */
		@Parameter public static final String WRITE_MAX_FLUSHES_PER_REGION = "splice.writer.maxFlushesPerRegion";
		@DefaultValue(WRITE_MAX_FLUSHES_PER_REGION) public static final int WRITE_DEFAULT_MAX_FLUSHES_PER_REGION = 5;
		public static int maxFlushesPerRegion;

		@SpliceConstants.Parameter public static final String PAST_STATEMENT_BUFFER_SIZE = "splice.monitoring.pastStatementBufferSize";
		@DefaultValue(PAST_STATEMENT_BUFFER_SIZE) public static final int DEFAULT_PAST_STATEMENT_BUFFER_SIZE = 100;
		public static int pastStatementBufferSize;

		public static final String TEMP_MAX_FILE_SIZE = "splice.temp.maxFileSize";
		public static long tempTableMaxFileSize;

		public static enum TableEnv {
				TRANSACTION_TABLE,
				ROOT_TABLE,
				META_TABLE,
				DERBY_SYS_TABLE,
				USER_INDEX_TABLE,
				USER_TABLE
		}

		static {
				setParameters();
				SIConstants.setParameters(config);
		}

		public static int ipcThreads;

		public static List<String> zookeeperPaths = Lists.newArrayList(
			zkSpliceTaskPath,
			zkSpliceJobPath,
			zkSpliceConglomeratePath,
			zkSpliceConglomerateSequencePath,
			zkSpliceDerbyPropertyPath,
			zkSpliceQueryNodePath,
			zkSpliceTransactionPath,
			zkSpliceMaxReservedTimestampPath,
			zkSpliceMinimumActivePath
		);

		public static void setParameters() {
				zkSpliceTaskPath = config.get(BASE_TASK_QUEUE_NODE,DEFAULT_BASE_TASK_QUEUE_NODE);
				zkSpliceDDLPath = config.get(DDL_PATH,DEFAULT_DDL_PATH);
				zkSpliceDDLActiveServersPath = zkSpliceDDLPath + "/activeServers";
				zkSpliceDDLOngoingTransactionsPath = zkSpliceDDLPath + "/ongoingChanges";
				zkSpliceBroadcastPath = config.get(BROADCAST_PATH,DEFAULT_BROADCAST_PATH);
				zkSpliceBroadcastActiveServersPath = zkSpliceBroadcastPath + "/activeServers";
				zkSpliceBroadcastMessagesPath = zkSpliceBroadcastPath + "/messages";
				zkSpliceJobPath = config.get(BASE_JOB_QUEUE_NODE,DEFAULT_BASE_JOB_QUEUE_NODE);
				zkSpliceTransactionPath = config.get(TRANSACTION_PATH,DEFAULT_TRANSACTION_PATH);
				zkSpliceMaxReservedTimestampPath = config.get(MAX_RESERVED_TIMESTAMP_PATH,DEFAULT_MAX_RESERVED_TIMESTAMP_PATH);
				zkSpliceMinimumActivePath = config.get(MINIMUM_ACTIVE_PATH,DEFAULT_MINIMUM_ACTIVE_PATH);
				zkSpliceConglomeratePath = config.get(CONGLOMERATE_SCHEMA_PATH,DEFAULT_CONGLOMERATE_SCHEMA_PATH);
				zkSpliceConglomerateSequencePath = zkSpliceConglomeratePath+"/__CONGLOM_SEQUENCE";
				zkSpliceDerbyPropertyPath = config.get(DERBY_PROPERTY_PATH,DEFAULT_DERBY_PROPERTY_PATH);
				zkSpliceQueryNodePath = config.get(CONGLOMERATE_SCHEMA_PATH,DEFAULT_CONGLOMERATE_SCHEMA_PATH);
				zkLeaderElection = config.get(LEADER_ELECTION,DEFAULT_LEADER_ELECTION);
				sleepSplitInterval = config.getLong(SPLIT_WAIT_INTERVAL, DEFAULT_SPLIT_WAIT_INTERVAL);
				zkSpliceStartupPath = config.get(STARTUP_PATH,DEFAULT_STARTUP_PATH);
				derbyBindAddress = config.get(DERBY_BIND_ADDRESS, DEFAULT_DERBY_BIND_ADDRESS);
				derbyBindPort = config.getInt(DERBY_BIND_PORT, DEFAULT_DERBY_BIND_PORT);
				timestampServerBindAddress = config.get(TIMESTAMP_SERVER_BIND_ADDRESS, DEFAULT_TIMESTAMP_SERVER_BIND_ADDRESS);
				timestampServerBindPort = config.getInt(TIMESTAMP_SERVER_BIND_PORT, DEFAULT_TIMESTAMP_SERVER_BIND_PORT);
				timestampBlockSize = config.getInt(TIMESTAMP_BLOCK_SIZE, DEFAULT_TIMESTAMP_BLOCK_SIZE);
				timestampClientWaitTime = config.getInt(TIMESTAMP_CLIENT_WAIT_TIME, DEFAULT_TIMESTAMP_CLIENT_WAIT_TIME);
				operationTaskPriority = config.getInt(OPERATION_PRIORITY, DEFAULT_OPERATION_PRIORITY);
				importTaskPriority = config.getInt(IMPORT_TASK_PRIORITY, DEFAULT_IMPORT_TASK_PRIORITY);
				tablePoolMaxSize = config.getInt(POOL_MAX_SIZE,DEFAULT_POOL_MAX_SIZE);
				tablePoolCoreSize = config.getInt(POOL_CORE_SIZE, DEFAULT_POOL_CORE_SIZE);
				tablePoolCleanerInterval = config.getLong(POOL_CLEANER_INTERVAL, DEFAULT_POOL_CLEANER_INTERVAL);
				writeBufferSize = config.getLong(WRITE_BUFFER_SIZE, DEFAULT_WRITE_BUFFER_SIZE);
				maxBufferEntries = config.getInt(BUFFER_ENTRIES, DEFAULT_MAX_BUFFER_ENTRIES);
				maxThreads = config.getInt(WRITE_THREADS_MAX,DEFAULT_WRITE_THREADS_MAX);
				maxTreeThreads = config.getInt(MAX_CONCURRENT_OPERATIONS,DEFAULT_MAX_CONCURRENT_OPERATIONS);
				siDelayRollForwardMaxSize = config.getInt(SI_DELAY_ROLL_FORWARD_MAX_SIZE, DEFAULT_SI_DELAY_ROLL_FORWARD_MAX_SIZE);
				ipcThreads = config.getInt("hbase.regionserver.handler.count",maxThreads);

				// Optimizer Settings
				
		        hashNLJLeftRowBufferSize = SpliceConstants.config.getInt(HASHNLJ_LEFTROWBUFFER_SIZE, DEFAULT_HASHNLJ_LEFTROWBUFFER_SIZE);
		        hashNLJRightHashTableSize = SpliceConstants.config.getInt(HASHNLJ_RIGHTHASHTABLE_SIZE, DEFAULT_HASHNLJ_RIGHTHASHTABLE_SIZE);
				regionMaxFileSize = (long) (( (float) SpliceConstants.config.getLong(HConstants.HREGION_MAX_FILESIZE,1024 * 1024 * 1024L))/( (float)1024*1024));
				hbaseRegionRowEstimate = SpliceConstants.config.getLong(HBASE_REGION_ROWS_ESTIMATE, DEFAULT_HBASE_REGION_ROWS_ESTIMATE);
				broadcastRegionMBThreshold = SpliceConstants.config.getInt(BROADCAST_REGION_MB_THRESHOLD,DEFAULT_BROADCAST_REGION_MB_THRESHOLD);
				indexPerRowCost = SpliceConstants.config.getFloat(INDEX_PER_ROW_COST, (float)DEFAULT_INDEX_PER_ROW_COST);
				optimizerHashCost = SpliceConstants.config.getFloat(OPTIMIZER_HASH_COST, (float)DEFAULT_OPTIMIZER_HASH_COST);
				extraQualifierMultiplier = SpliceConstants.config.getFloat(OPTIMIZER_EXTRA_QUALIFIER_MULTIPLIER, (float) DEFAULT_OPTIMIZER_EXTRA_QUALIFIER_MULTIPLIER);
				extraStartStopQualifierMultiplier = SpliceConstants.config.getFloat(OPTIMIZER_EXTRA_START_STOP_QUALIFIER_MULTIPLIER, (float) DEFAULT_OPTIMIZER_EXTRA_START_STOP_QUALIFIER_MULTIPLIER);
				optimizerNetworkCost = SpliceConstants.config.getFloat(OPTIMIZER_NETWORK_COST, (float)DEFAULT_OPTIMIZER_NETWORK_COST);
				optimizerWriteCost = SpliceConstants.config.getFloat(OPTIMIZER_WRITE_COST, (float)DEFAULT_OPTIMIZER_WRITE_COST);				
				baseTablePerRowCost = SpliceConstants.config.getFloat(BASE_TABLE_PER_ROW_COST, (float) DEFAULT_BASE_TABLE_PER_ROW_COST);
				fetchFromRowLocationCost = SpliceConstants.config.getFloat(FETCH_FROM_ROW_LOCATION_COST, (float) DEFAULT_FETCH_FROM_ROW_LOCATION_COST);
				getBaseTableFetchFromFullKeyCost = SpliceConstants.config.getFloat(GET_BASE_TABLE_FETCH_FROM_FULL_KEY_COST, (float) DEFAULT_GET_BASE_TABLE_FETCH_FROM_FULL_KEY_COST);
				getIndexFetchFromFullKeyCost = SpliceConstants.config.getFloat(GET_INDEX_FETCH_FROM_FULL_KEY_COST, (float) DEFAULT_GET_INDEX_FETCH_FROM_FULL_KEY_COST);
				optimizerTableMinimalRows = SpliceConstants.config.getLong(OPTIMIZER_TABLE_MINIMAL_ROWS, DEFAULT_OPTIMIZER_TABLE_MINIMAL_ROWS);
				if(ipcThreads < maxThreads){
            /*
             * Some of our writes will also write out to indices and/or read data from HBase, which
             * may be located on the same region. Thus, if we allow unbounded writer threads, we face
             * a nasty situation where we are writing to a bunch of regions which are all located on the same
             * node, and they attempt to write out to indices which are ALSO on the same node. Since we are
             * using up all the IPC threads to do the initial writes, the writes out to the index tables are blocked.
             * But since the writes to the main table cannot complete before the index writes complete, the
             * main table writes cannot proceed, resulting in a deadlock (in pathological circumstances).
             *
             * This deadlock can be manually recovered from by moving regions around, but it's bad form to
             * deadlock periodically just because HBase isn't arranged nicely. Thus, we bound down the number
             * of write threads to be strictly less than the number of ipcThreads, so as to always leave some
             * IPC threads available (preventing deadlock).
             *
             * I more or less arbitrarily decided to make it 5 fewer, but that seems like a good balance
             * between having a lot of write threads and still allowing writes through.
             */
						maxThreads = ipcThreads-5;
				}
				if(maxThreads<=0)
						maxThreads = 1;
				coreWriteThreads = config.getInt(WRITE_THREADS_CORE,DEFAULT_WRITE_THREADS_CORE);
				if(coreWriteThreads>maxThreads){
						//default the core write threads to 10% of the maximum available
						coreWriteThreads = maxThreads/10;
				}
				if(coreWriteThreads<0)
						coreWriteThreads=0;

				threadKeepAlive = config.getLong(HBASE_HTABLE_THREADS_KEEPALIVETIME, DEFAULT_HBASE_HTABLE_THREADS_KEEPALIVETIME);
				numRetries = config.getInt(HBASE_CLIENT_RETRIES_NUMBER, DEFAULT_HBASE_CLIENT_RETRIES_NUMBER);
				cacheUpdatePeriod = config.getLong(CACHE_UPDATE_PERIOD, DEFAULT_CACHE_UPDATE_PERIOD);
				enableRegionCache = cacheUpdatePeriod>0l;
				cacheExpirationPeriod = config.getLong(CACHE_EXPIRATION,DEFAULT_CACHE_EXPIRATION);
				compression = config.get(COMPRESSION, DEFAULT_COMPRESSION);
				authentication = config.get(AUTHENTICATION, DEFAULT_AUTHENTICATION);
				authenticationNativeAlgorithm = config.get(AUTHENTICATION_NATIVE_ALGORITHM, DEFAULT_AUTHENTICATION_NATIVE_ALGORITHM);
				
				authenticationCustomProvider = config.get(AUTHENTICATION_CUSTOM_PROVIDER,DEFAULT_AUTHENTICATION_CUSTOM_PROVIDER);
				
				authenticationLDAPServer = config.get(AUTHENTICATION_LDAP_SERVER,DEFAULT_AUTHENTICATION_LDAP_SERVER);
				authenticationLDAPSearchAuthDN = config.get(AUTHENTICATION_LDAP_SEARCHAUTHDN,DEFAULT_AUTHENTICATION_LDAP_SEARCHAUTHDN);
				authenticationLDAPSearchAuthPW = config.get(AUTHENTICATION_LDAP_SEARCHAUTHPW,DEFAULT_AUTHENTICATION_LDAP_SEARCHAUTHPW);
				authenticationLDAPSearchBase = config.get(AUTHENTICATION_LDAP_SEARCHBASE,DEFAULT_AUTHENTICATION_LDAP_SEARCHBASE);
				authenticationLDAPSearchFilter = config.get(AUTHENTICATION_LDAP_SEARCHFILTER,DEFAULT_AUTHENTICATION_LDAP_SEARCHFILTER);
				
				delayedForwardRingBufferSize = config.getInt(DELAYED_FORWARD_RING_BUFFER_SIZE, DEFAULT_DELAYED_FORWARD_RING_BUFFER_SIZE);
				pushForwardRingBufferSize = config.getInt(PUSH_FORWARD_RING_BUFFER_SIZE, DEFAULT_PUSH_FORWARD_RING_BUFFER_SIZE);
				pushForwardWriteBufferSize = config.getInt(PUSH_FORWARD_WRITE_BUFFER_SIZE, DEFAULT_PUSH_FORWARD_WRITE_BUFFER_SIZE);
				delayedForwardWriteBufferSize = config.getInt(DELAYED_FORWARD_WRITE_BUFFER_SIZE, DEFAULT_DELAYED_FORWARD_WRITE_BUFFER_SIZE);
				delayedForwardAsyncWriteDelay = config.getInt(DELAYED_FORWARD_ASYNCH_WRITE_DELAY, DEFAULT_DELAYED_FORWARD_ASYNCH_WRITE_DELAY);
				delayedForwardQueueLimit = config.getInt(DELAYED_FORWARD_QUEUE_LIMIT, DEFAULT_DELAYED_FORWARD_QUEUE_LIMIT);
				
				multicastGroupAddress = config.get(MULTICAST_GROUP_ADDRESS,DEFAULT_MULTICAST_GROUP_ADDRESS);
				multicastGroupPort = config.getInt(MULTICAST_GROUP_PORT, DEFAULT_MULTICAST_GROUP_PORT);
				rmiPort = config.getInt(RMI_PORT, DEFAULT_RMI_PORT);
				rmiRemoteObjectPort = config.getInt(RMI_REMOTE_OBJECT_PORT, DEFAULT_RMI_REMOTE_OBJECT_PORT);
				dumpClassFile = config.getBoolean(DEBUG_DUMP_CLASS_FILE, DEFAULT_DUMP_CLASS_FILE);
				logStatementContext = config.getBoolean(DEBUG_LOG_STATEMENT_CONTEXT, DEFAULT_LOG_STATEMENT_CONTEXT);
				startupLockWaitPeriod = config.getInt(STARTUP_LOCK_WAIT_PERIOD, DEFAULT_STARTUP_LOCK_PERIOD);
				ringBufferSize = config.getInt(RING_BUFFER_SIZE, DEFAULT_RING_BUFFER_SIZE);
				indexBatchSize = config.getInt(INDEX_BATCH_SIZE,DEFAULT_INDEX_BATCH_SIZE);
				indexLookupBlocks = config.getInt(INDEX_LOOKUP_BLOCKS,DEFAULT_INDEX_LOOKUP_BLOCKS);
				kryoPoolSize = config.getInt(KRYO_POOL_SIZE,DEFAULT_KRYO_POOL_SIZE);
				debugFailTasksRandomly = config.getBoolean(DEBUG_FAIL_TASKS_RANDOMLY,DEFAULT_DEBUG_FAIL_TASKS_RANDOMLY);
				debugTaskFailureRate = config.getFloat(DEBUG_TASK_FAILURE_RATE,(float)DEFAULT_DEBUG_TASK_FAILURE_RATE);

				flushQueueSizeBlock = config.getInt(FLUSH_QUEUE_SIZE_BLOCK, DEFAULT_FLUSH_QUEUE_SIZE_BLOCK);
				compactionQueueSizeBlock = config.getInt(COMPACTION_QUEUE_SIZE_BLOCK, DEFAULT_COMPACTION_QUEUE_SIZE_BLOCK);
				
				sequenceBlockSize = config.getInt(SEQUENCE_BLOCK_SIZE,DEFAULT_SEQUENCE_BLOCK_SIZE);

				maxImportProcessingThreads = config.getInt(IMPORT_MAX_PROCESSING_THREADS,DEFAULT_IMPORT_MAX_PROCESSING_THREADS);
				interruptLoopCheck = config.getInt(INTERRUPT_LOOP_CHECK,DEFAULT_INTERRUPT_LOOP_CHECK);
				maxImportReadBufferSize = config.getInt(IMPORT_MAX_READ_BUFFER_SIZE,DEFAULT_IMPORT_MAX_READ_BUFFER_SIZE);

				maxFlushesPerRegion = config.getInt(WRITE_MAX_FLUSHES_PER_REGION,WRITE_DEFAULT_MAX_FLUSHES_PER_REGION);

				long regionMaxFileSize = config.getLong(HConstants.HREGION_MAX_FILESIZE,HConstants.DEFAULT_MAX_FILE_SIZE);
				tempTableMaxFileSize = config.getLong(TEMP_MAX_FILE_SIZE,100*1024 * 1024 * 1024L); // 100 Gigs...

				collectStats = config.getBoolean(COLLECT_PERF_STATS,DEFAULT_COLLECT_STATS);
				pause = config.getLong(CLIENT_PAUSE,DEFAULT_CLIENT_PAUSE);

				importSplitFactor = config.getInt(IMPORT_SPLIT_FACTOR,DEFAULT_IMPORT_SPLIT_FACTOR);
				taskWorkers = config.getInt(TOTAL_WORKERS,DEFAULT_TOTAL_WORKERS);
				if (taskWorkers > DEFAULT_TOTAL_WORKERS)
					SpliceLogUtils.warn(LOG, "your task workers are set at {%d} and that is more than 2*(Number of Java Cores) {%d}", taskWorkers,DEFAULT_TOTAL_WORKERS);
				numPriorityTiers = config.getInt(NUM_PRIORITY_TIERS,DEFAULT_NUM_PRIORITY_TIERS);
				maxPriority = config.getInt(MAX_PRIORITY,DEFAULT_MAX_PRIORITY);

				pastStatementBufferSize = config.getInt(PAST_STATEMENT_BUFFER_SIZE,DEFAULT_PAST_STATEMENT_BUFFER_SIZE);

				sequentialImportThreashold = config.getLong(SEQUENTIAL_IMPORT_THREASHOLD,DEFAULT_SEQUENTIAL_IMPORT_THRESHOLD);
				sequentialImportFileSizeThreshold = config.getLong(SEQUENTIAL_IMPORT_FILESIZE_THREASHOLD,DEFAULT_SEQUENTIAL_FILESIZE_IMPORT_THRESHOLD);

				constraintsEnabled = config.getBoolean(CONSTRAINTS_ENABLED,DEFAULT_CONSTRAINTS_ENABLED);

				importLogQueueSize = config.getInt(IMPORT_LOG_QUEUE_SIZE, DEFAULT_IMPORT_LOG_QUEUE_SIZE);
				if(importLogQueueSize<=0){
						Logger.getRootLogger().error("Unable to set import log queue size to a value <= 0. Setting it to the default of "+ DEFAULT_IMPORT_LOG_QUEUE_SIZE);
						importLogQueueSize = DEFAULT_IMPORT_LOG_QUEUE_SIZE;
				}

				importLogQueueWaitTimeMs = config.getLong(IMPORT_LOG_QUEUE_WAIT_TIME,DEFAULT_IMPORT_LOG_QUEUE_WAIT_TIME);
				useReadAheadScanner = config.getBoolean(USE_READ_AHEAD_SCANNER,DEFAULT_USE_READ_AHEAD_SCANNER);

        ddlDrainingMaximumWait = config.getLong(DDL_DRAINING_MAXIMUM_WAIT,DEFAULT_DDL_DRAINING_MAXIMUM_WAIT);
        ddlDrainingInitialWait = config.getLong(DDL_DRAINING_INITIAL_WAIT,DEFAULT_DDL_DRAINING_INITIAL_WAIT);


				interRegionTaskSplitThresholdBytes = config.getLong(INTER_REGION_TASK_SPLIT_THRESHOLD_BYTES,DEFAULT_INTER_REGION_TASK_SPLIT_THRESHOLD_BYTES);
				maxInterRegionTaskSplits = config.getInt(MAX_INTER_REGION_TASK_SPLITS,DEFAULT_MAX_INTER_REGION_TASK_SPLITS);

        numRollForwardSegments = config.getInt(ROLL_FORWARD_SEGMENTS,DEFAULT_ROLLFORWARD_SEGMENTS);
        rollForwardRowThreshold = config.getInt(ROLL_FORWARD_ROW_THRESHOLD,DEFAULT_ROLLFOWARD_ROW_THRESHOLD);
        rollForwardTxnThreshold = config.getInt(ROLL_FORWARD_TXN_THRESHOLD,DEFAULT_ROLLFOWARD_TXN_THRESHOLD);

        maxDdlWait = config.getInt(MAX_DDL_WAIT,DEFAULT_MAX_DDL_WAIT);
		}

		public static void reloadConfiguration(Configuration configuration) {
				HBaseConfiguration.merge(config,configuration);
				setParameters();
		}

}

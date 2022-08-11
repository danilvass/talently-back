package com.tripled.talentlyapp.core.async

import com.tripled.talentlyapp.utils.loggerFor
import org.slf4j.Logger
import java.util.concurrent.*

class AsyncExecutor(defaultQueueCapacity: Int,
                    initialPoolSize: Int,
                    maxPoolSize: Int,
                    keepAliveTimeInMin: Int) {

    private companion object {
        val logger: Logger = loggerFor(AsyncExecutor::class.java)
    }

    private val workQueue: BlockingQueue<Runnable>
    private val threadFactory: ThreadFactory
    private val threadPoolExecutor: ThreadPoolExecutor

    init {
        workQueue = LinkedBlockingQueue(defaultQueueCapacity)
        threadFactory = OperationThreadFactory()
        threadPoolExecutor = ThreadPoolExecutor(
                initialPoolSize,
                maxPoolSize,
                keepAliveTimeInMin.toLong(),
                TimeUnit.MINUTES,
                workQueue,
                threadFactory
        )
    }

    //Mark: - Public API
    fun execute(runnable: Runnable) {
        threadPoolExecutor.execute(runnable)
        logger.debug("Pool size: ${threadPoolExecutor.poolSize}")
        logger.debug("Work queue remaining capacity: ${threadPoolExecutor.queue.remainingCapacity()}")
    }

    private class OperationThreadFactory : ThreadFactory {
        private var counter = 0

        override fun newThread(runnable: Runnable): Thread {
            val threadName: String = THREAD_NAME + counter++
            logger.debug("Created new thread: $threadName")
            return Thread(runnable, threadName)
        }

        companion object {
            private const val THREAD_NAME = "talently.async.thread"
        }
    }

}
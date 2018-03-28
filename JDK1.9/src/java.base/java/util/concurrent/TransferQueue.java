/*
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

/*
 *
 *
 *
 *
 *
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util.concurrent;

/**
 * 参考说明博文：http://ifeve.com/java-transfer-queue/
 * TransferQueue继承自BlockingQueue，并扩展了一些新方法。TransferQueue比BlockingQueue
 * 在功能上更进一步：生产者调用Transfer方法向队列添加一个元素时，它会一直阻塞到该元素被消费者使用
 * 为止。
 *
 * A {@link BlockingQueue} in which producers may wait for consumers
 * to receive elements.  A {@code TransferQueue} may be useful for
 * example in message passing applications in which producers
 * sometimes (using method {@link #transfer}) await receipt of
 * elements by consumers invoking {@code take} or {@code poll}, while
 * at other times enqueue elements (via method {@code put}) without
 * waiting for receipt.
 *
 *
 * {@linkplain #tryTransfer(Object) Non-blocking} and
 * {@linkplain #tryTransfer(Object,long,TimeUnit) time-out} versions of
 * {@code tryTransfer} are also available.
 * A {@code TransferQueue} may also be queried, via {@link
 * #hasWaitingConsumer}, whether there are any threads waiting for
 * items, which is a converse analogy to a {@code peek} operation.
 *
 * <p>Like other blocking queues, a {@code TransferQueue} may be
 * capacity bounded.  If so, an attempted transfer operation may
 * initially block waiting for available space, and/or subsequently
 * block waiting for reception by a consumer.  Note that in a queue
 * with zero capacity, such as {@link SynchronousQueue}, {@code put}
 * and {@code transfer} are effectively synonymous.
 *
 * <p>This interface is a member of the
 * <a href="{@docRoot}/java/util/package-summary.html#CollectionsFramework">
 * Java Collections Framework</a>.
 *
 * @since 1.7
 * @author Doug Lea
 * @param <E> the type of elements held in this queue
 */
public interface TransferQueue<E> extends BlockingQueue<E> {
    /**
     * 非阻塞。
     * 尝试立即将元素e转交给消费者，如果当前存在正在等待的消费者，那么该方法成功，返回true。
     * 否则，返回false，元素不会被添加到TransferQueue中。
     *
     * Transfers the element to a waiting consumer immediately, if possible.
     *
     * <p>More precisely, transfers the specified element immediately
     * if there exists a consumer already waiting to receive it (in
     * {@link #take} or timed {@link #poll(long,TimeUnit) poll}),
     * otherwise returning {@code false} without enqueuing the element.
     *
     * @param e the element to transfer
     * @return {@code true} if the element was transferred, else
     *         {@code false}
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified
     *         element prevents it from being added to this queue
     */
    boolean tryTransfer(E e);

    /**
     * 无限期阻塞。
     * 将元素e加入到TransferQueue中并无限期等待，直到该元素被消费者接收。
     *
     * Transfers the element to a consumer, waiting if necessary to do so.
     *
     * <p>More precisely, transfers the specified element immediately
     * if there exists a consumer already waiting to receive it (in
     * {@link #take} or timed {@link #poll(long,TimeUnit) poll}),
     * else waits until the element is received by a consumer.
     *
     * @param e the element to transfer
     * @throws InterruptedException if interrupted while waiting,
     *         in which case the element is not left enqueued
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified
     *         element prevents it from being added to this queue
     */
    void transfer(E e) throws InterruptedException;

    /**
     * 有限时间阻塞。
     * 尝试在指定的时间段里将元素e转交给消费者。如果在指定时间里，该元素被消费者接收，
     * 那么立即返回true。否则，返回false，元素留在队列中。
     * Transfers the element to a consumer if it is possible to do so
     * before the timeout elapses.
     *
     * <p>More precisely, transfers the specified element immediately
     * if there exists a consumer already waiting to receive it (in
     * {@link #take} or timed {@link #poll(long,TimeUnit) poll}),
     * else waits until the element is received by a consumer,
     * returning {@code false} if the specified wait time elapses
     * before the element can be transferred.
     *
     * @param e the element to transfer
     * @param timeout how long to wait before giving up, in units of
     *        {@code unit}
     * @param unit a {@code TimeUnit} determining how to interpret the
     *        {@code timeout} parameter
     * @return {@code true} if successful, or {@code false} if
     *         the specified waiting time elapses before completion,
     *         in which case the element is not left enqueued
     * @throws InterruptedException if interrupted while waiting,
     *         in which case the element is not left enqueued
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified
     *         element prevents it from being added to this queue
     */
    boolean tryTransfer(E e, long timeout, TimeUnit unit)
        throws InterruptedException;

    /**
     * 检测是否有消费者在等待，返回的结果代表检测瞬间的状态。
     * Returns {@code true} if there is at least one consumer waiting
     * to receive an element via {@link #take} or
     * timed {@link #poll(long,TimeUnit) poll}.
     * The return value represents a momentary state of affairs.
     *
     * @return {@code true} if there is at least one waiting consumer
     */
    boolean hasWaitingConsumer();

    /**
     * 返回正在等待的消费者数量的估计值。这个值可能并不准确，可以用于检测，但是
     * 不能用于同步控制。
     * Returns an estimate of the number of consumers waiting to
     * receive elements via {@link #take} or timed
     * {@link #poll(long,TimeUnit) poll}.  The return value is an
     * approximation of a momentary state of affairs, that may be
     * inaccurate if consumers have completed or given up waiting.
     * The value may be useful for monitoring and heuristics, but
     * not for synchronization control.  Implementations of this
     * method are likely to be noticeably slower than those for
     * {@link #hasWaitingConsumer}.
     *
     * @return the number of consumers waiting to receive elements
     */
    int getWaitingConsumerCount();
}

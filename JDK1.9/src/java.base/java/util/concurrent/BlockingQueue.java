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

import java.util.Collection;
import java.util.Queue;

/**
 * Queue的一个子接口，它支持如下操作：
 * 当执行出队操作时，如果队列为空，则等待
 * 当执行如队操作时，如果队列已满，则等待
 * A {@link java.util.Queue} that additionally supports operations
 * that wait for the queue to become non-empty when retrieving an
 * element, and wait for space to become available in the queue when
 * storing an element.
 *
 * 阻塞队列的方法有四种形式，每种方式返回的结果不同，以处理不同的情形。
 * <p>{@code BlockingQueue} methods come in four forms, with different ways
 * of handling operations that cannot be satisfied immediately, but may be
 * satisfied at some point in the future:
 *
 * 一种抛出异常，
 * 第二种返回一个特别的值（通常是null或false），
 * 第三种无限期阻塞当前的线程，直到条件满足（有元素或有空间），
 * 第四种仅阻塞一个指定的时间。
 * one throws an exception, the second returns a special value (either
 * {@code null} or {@code false}, depending on the operation), the third
 * blocks the current thread indefinitely until the operation can succeed,
 * and the fourth blocks for only a given maximum time limit before giving
 * up.  These methods are summarized in the following table:
 *
 * <table class="plain">
 * <caption>Summary of BlockingQueue methods</caption>
 *  <tr>
 *    <td></td>
 *    <td style="text-align:center"><em>Throws exception</em></td>
 *    <td style="text-align:center"><em>Special value</em></td>
 *    <td style="text-align:center"><em>Blocks</em></td>
 *    <td style="text-align:center"><em>Times out</em></td>
 *  </tr>
 *  <tr>
 *    <td><b>Insert</b></td>
 *    <td>{@link #add add(e)}</td>
 *    <td>{@link #offer offer(e)}</td>
 *    <td>{@link #put put(e)}</td>
 *    <td>{@link #offer(Object, long, TimeUnit) offer(e, time, unit)}</td>
 *  </tr>
 *  <tr>
 *    <td><b>Remove</b></td>
 *    <td>{@link #remove remove()}</td>
 *    <td>{@link #poll poll()}</td>
 *    <td>{@link #take take()}</td>
 *    <td>{@link #poll(long, TimeUnit) poll(time, unit)}</td>
 *  </tr>
 *  <tr>
 *    <td><b>Examine</b></td>
 *    <td>{@link #element element()}</td>
 *    <td>{@link #peek peek()}</td>
 *    <td><em>not applicable</em></td>
 *    <td><em>not applicable</em></td>
 *  </tr>
 * </table>
 *
 * 阻塞队列不接受null值，如果传入null值，那么会抛出NullPointerException异常
 * <p>A {@code BlockingQueue} does not accept {@code null} elements.
 * Implementations throw {@code NullPointerException} on attempts
 * to {@code add}, {@code put} or {@code offer} a {@code null}.  A
 * {@code null} is used as a sentinel value to indicate failure of
 * {@code poll} operations.
 *
 *  一个阻塞队列通常是容量有限的
 * <p>A {@code BlockingQueue} may be capacity bounded. At any given
 * time it may have a {@code remainingCapacity} beyond which no
 * additional elements can be {@code put} without blocking.
 *
 * 一个没有容量限制的阻塞队列的remaining capacity属性总是为Integer.MAX_VALUE
 * A {@code BlockingQueue} without any intrinsic capacity constraints always
 * reports a remaining capacity of {@code Integer.MAX_VALUE}.
 *
 *  阻塞队列接口的实现，通过被用作生产者-消费者队列。但同时它还具有Collection集合的
 *  方法。因此，还可以通过Remove(x)方法移除队列中的任意一个元素。但是，类似的操作
 *  效率并不高，并且只是偶尔使用（如当一个队列信息被取消时）。
 *
 * <p>{@code BlockingQueue} implementations are designed to be used
 * primarily for producer-consumer queues, but additionally support
 * the {@link java.util.Collection} interface.  So, for example, it is
 * possible to remove an arbitrary element from a queue using
 * {@code remove(x)}. However, such operations are in general
 * <em>not</em> performed very efficiently, and are intended for only
 * occasional use, such as when a queued message is cancelled.
 *
 * BlockingQueue是线程安全的，所有的队列操作都自动地使用内部锁或其他同步控制。
 * 然而，涉及到大量元素操作的Colleciotn方法，如addAll、containsAll、retainAll等
 *并不会自动地同步，除非具体的实现自行保证。因此，addAll这类方法可能会在执行过程中失败。
 * <p>{@code BlockingQueue} implementations are thread-safe.  All
 * queuing methods achieve their effects atomically using internal
 * locks or other forms of concurrency control. However, the
 * <em>bulk</em> Collection operations {@code addAll},
 * {@code containsAll}, {@code retainAll} and {@code removeAll} are
 * <em>not</em> necessarily performed atomically unless specified
 * otherwise in an implementation. So it is possible, for example, for
 * {@code addAll(c)} to fail (throwing an exception) after adding
 * only some of the elements in {@code c}.
 *
 * 一个BlockingQueue本身并不支持close、shutdown等表明不能再添加元素的操作。
 * 这些特性由需要的子类去自行实现。
 * <p>A {@code BlockingQueue} does <em>not</em> intrinsically support
 * any kind of &quot;close&quot; or &quot;shutdown&quot; operation to
 * indicate that no more items will be added.  The needs and usage of
 * such features tend to be implementation-dependent. For example, a
 * common tactic is for producers to insert special
 * <em>end-of-stream</em> or <em>poison</em> objects, that are
 * interpreted accordingly when taken by consumers.
 *
 * 以下是基于典型生产者消费者场景的BlockingQueue使用示例。
 * <p>
 * Usage example, based on a typical producer-consumer scenario（场景）.
 * Note that a {@code BlockingQueue} can safely be used with multiple
 * producers and multiple consumers.
 * <pre> {@code
 * class Producer implements Runnable {
 *   private final BlockingQueue queue;
 *   Producer(BlockingQueue q) { queue = q; }
 *   public void run() {
 *     try {
 *       while (true) { queue.put(produce()); }
 *     } catch (InterruptedException ex) { ... handle ...}
 *   }
 *   Object produce() { ... }
 * }
 *
 * class Consumer implements Runnable {
 *   private final BlockingQueue queue;
 *   Consumer(BlockingQueue q) { queue = q; }
 *   public void run() {
 *     try {
 *       while (true) { consume(queue.take()); }
 *     } catch (InterruptedException ex) { ... handle ...}
 *   }
 *   void consume(Object x) { ... }
 * }
 *
 * class Setup {
 *   void main() {
 *     BlockingQueue q = new SomeQueueImplementation();
 *     Producer p = new Producer(q);
 *     Consumer c1 = new Consumer(q);
 *     Consumer c2 = new Consumer(q);
 *     new Thread(p).start();
 *     new Thread(c1).start();
 *     new Thread(c2).start();
 *   }
 * }}</pre>
 *
 * 内存一致性效果：在一个线程中对BlockingQueue的插入操作，先行于其他线程中对同一个元素的读取
 * 和移除操作
 * <p>Memory consistency effects: As with other concurrent
 * collections, actions in a thread prior to placing an object into a
 * {@code BlockingQueue}
 * <a href="package-summary.html#MemoryVisibility"><i>happen-before</i></a>
 * actions subsequent to the access or removal of that element from
 * the {@code BlockingQueue} in another thread.
 *
 * <p>This interface is a member of the
 * <a href="{@docRoot}/java/util/package-summary.html#CollectionsFramework">
 * Java Collections Framework</a>.
 *
 * @since 1.5
 * @author Doug Lea
 * @param <E> the type of elements held in this queue
 */
public interface BlockingQueue<E> extends Queue<E> {
    /**
     * Inserts the specified element into this queue if it is possible to do
     * so immediately without violating capacity restrictions, returning
     * {@code true} upon success and throwing an
     * {@code IllegalStateException} if no space is currently available.
     * When using a capacity-restricted queue, it is generally preferable to
     * use {@link #offer(Object) offer}.
     *
     * @param e the element to add
     * @return {@code true} (as specified by {@link Collection#add})
     * @throws IllegalStateException if the element cannot be added at this
     *         time due to capacity restrictions
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified
     *         element prevents it from being added to this queue
     */
    boolean add(E e);

    /**
     * Inserts the specified element into this queue if it is possible to do
     * so immediately without violating capacity restrictions, returning
     * {@code true} upon success and {@code false} if no space is currently
     * available.  When using a capacity-restricted queue, this method is
     * generally preferable to {@link #add}, which can fail to insert an
     * element only by throwing an exception.
     *
     * @param e the element to add
     * @return {@code true} if the element was added to this queue, else
     *         {@code false}
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified
     *         element prevents it from being added to this queue
     */
    boolean offer(E e);

    /**
     * Inserts the specified element into this queue, waiting if necessary
     * for space to become available.
     *
     * @param e the element to add
     * @throws InterruptedException if interrupted while waiting
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified
     *         element prevents it from being added to this queue
     */
    void put(E e) throws InterruptedException;

    /**
     * Inserts the specified element into this queue, waiting up to the
     * specified wait time if necessary for space to become available.
     *
     * @param e the element to add
     * @param timeout how long to wait before giving up, in units of
     *        {@code unit}
     * @param unit a {@code TimeUnit} determining how to interpret the
     *        {@code timeout} parameter
     * @return {@code true} if successful, or {@code false} if
     *         the specified waiting time elapses before space is available
     * @throws InterruptedException if interrupted while waiting
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified
     *         element prevents it from being added to this queue
     */
    boolean offer(E e, long timeout, TimeUnit unit)
        throws InterruptedException;

    /**
     * Retrieves and removes the head of this queue, waiting if necessary
     * until an element becomes available.
     *
     * @return the head of this queue
     * @throws InterruptedException if interrupted while waiting
     */
    E take() throws InterruptedException;

    /**
     * Retrieves and removes the head of this queue, waiting up to the
     * specified wait time if necessary for an element to become available.
     *
     * @param timeout how long to wait before giving up, in units of
     *        {@code unit}
     * @param unit a {@code TimeUnit} determining how to interpret the
     *        {@code timeout} parameter
     * @return the head of this queue, or {@code null} if the
     *         specified waiting time elapses before an element is available
     * @throws InterruptedException if interrupted while waiting
     */
    E poll(long timeout, TimeUnit unit)
        throws InterruptedException;

    /**
     * 返回当前BlockingQueueQueue还能插入的元素数量
     * Returns the number of additional elements that this queue can ideally
     * (in the absence of memory or resource constraints) accept without
     * blocking, or {@code Integer.MAX_VALUE} if there is no intrinsic
     * limit.
     *
     * <p>Note that you <em>cannot</em> always tell if an attempt to insert
     * an element will succeed by inspecting {@code remainingCapacity}
     * because it may be the case that another thread is about to
     * insert or remove an element.
     *
     * @return the remaining capacity
     */
    int remainingCapacity();

    /**
     * 删除队列中的元素o。
     * Removes a single instance of the specified element from this queue,
     * if it is present.  More formally, removes an element {@code e} such
     * that {@code o.equals(e)}, if this queue contains one or more such
     * elements.
     * Returns {@code true} if this queue contained the specified element
     * (or equivalently, if this queue changed as a result of the call).
     *
     * @param o element to be removed from this queue, if present
     * @return {@code true} if this queue changed as a result of the call
     * @throws ClassCastException if the class of the specified element
     *         is incompatible with this queue
     * (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null
     * (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    boolean remove(Object o);

    /**
     * 判断队列中是否有该元素。
     * Returns {@code true} if this queue contains the specified element.
     * More formally, returns {@code true} if and only if this queue contains
     * at least one element {@code e} such that {@code o.equals(e)}.
     *
     * @param o object to be checked for containment in this queue
     * @return {@code true} if this queue contains the specified element
     * @throws ClassCastException if the class of the specified element
     *         is incompatible with this queue
     * (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null
     * (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    boolean contains(Object o);

    /**
     * 将队列中的所有元素移除并添加到传入的集合c中。这个操作比重复地调用poll更加有效
     * Removes all available elements from this queue and adds them
     * to the given collection.  This operation may be more
     * efficient than repeatedly polling this queue.  A failure
     * encountered while attempting to add elements to
     * collection {@code c} may result in elements being in neither,
     * either or both collections when the associated exception is
     * thrown.  Attempts to drain a queue to itself result in
     * {@code IllegalArgumentException}. Further, the behavior of
     * this operation is undefined if the specified collection is
     * modified while the operation is in progress.
     *
     * @param c the collection to transfer elements into
     * @return the number of elements transferred
     * @throws UnsupportedOperationException if addition of elements
     *         is not supported by the specified collection
     * @throws ClassCastException if the class of an element of this queue
     *         prevents it from being added to the specified collection
     * @throws NullPointerException if the specified collection is null
     * @throws IllegalArgumentException if the specified collection is this
     *         queue, or some property of an element of this queue prevents
     *         it from being added to the specified collection
     */
    int drainTo(Collection<? super E> c);

    /**
     * 移除给定个数的元素，并将其添加到初入的集合c中
     * Removes at most the given number of available elements from
     * this queue and adds them to the given collection.  A failure
     * encountered while attempting to add elements to
     * collection {@code c} may result in elements being in neither,
     * either or both collections when the associated exception is
     * thrown.  Attempts to drain a queue to itself result in
     * {@code IllegalArgumentException}. Further, the behavior of
     * this operation is undefined if the specified collection is
     * modified while the operation is in progress.
     *
     * @param c the collection to transfer elements into
     * @param maxElements the maximum number of elements to transfer
     * @return the number of elements transferred
     * @throws UnsupportedOperationException if addition of elements
     *         is not supported by the specified collection
     * @throws ClassCastException if the class of an element of this queue
     *         prevents it from being added to the specified collection
     * @throws NullPointerException if the specified collection is null
     * @throws IllegalArgumentException if the specified collection is this
     *         queue, or some property of an element of this queue prevents
     *         it from being added to the specified collection
     */
    int drainTo(Collection<? super E> c, int maxElements);
}

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

package java.util;

/**
 * 一种用于在处理前保存元素的集合,即队列。除了继承自Collection的基本操作外，该接口还
 * 提供了额外的插入、提取和检查操作。它的这些方法都存在两种形式：当操作失败时，一种抛出
 * 异常，一种返回一个特殊的值（null或者false）。也正是由于第二种形式的存在，该接口不允许null值。
 * A collection designed for holding elements prior to processing.
 * Besides basic {@link java.util.Collection Collection} operations,
 * queues provide additional insertion, extraction, and inspection
 * operations.  Each of these methods exists in two forms: one throws
 * an exception if the operation fails, the other returns a special
 * value (either {@code null} or {@code false}, depending on the
 * operation).  The latter form of the insert operation is designed
 * specifically for use with capacity-restricted {@code Queue}
 * implementations; in most implementations, insert operations cannot
 * fail.
 *
 * <table class="plain">
 * <caption>Summary of Queue methods</caption>
 *  <tr>
 *    <td></td>
 *    <td style="text-align:center"><em>Throws exception</em></td>
 *    <td style="text-align:center"><em>Returns special value</em></td>
 *  </tr>
 *  <tr>
 *    <td><b>Insert</b></td>
 *    <td>{@link Queue#add add(e)}</td>
 *    <td>{@link Queue#offer offer(e)}</td>
 *  </tr>
 *  <tr>
 *    <td><b>Remove</b></td>
 *    <td>{@link Queue#remove remove()}</td>
 *    <td>{@link Queue#poll poll()}</td>
 *  </tr>
 *  <tr>
 *    <td><b>Examine</b></td>
 *    <td>{@link Queue#element element()}</td>
 *    <td>{@link Queue#peek peek()}</td>
 *  </tr>
 * </table>
 *  典型的队列都是按照先进先出的顺序，但是这并不是一定。例如优先级队列PriorityQueue。
 *  该队列按照元素的优先级对元素进行排序。
 *  在先进先出的队列里，元素都是从尾部插入的。
 * <p>Queues typically, but do not necessarily, order elements in a
 * FIFO (first-in-first-out) manner.  Among the exceptions are
 * priority queues, which order elements according to a supplied
 * comparator, or the elements' natural ordering, and LIFO queues (or
 * stacks) which order the elements LIFO (last-in-first-out).
 * Whatever the ordering used, the <em>head</em> of the queue is that
 * element which would be removed by a call to {@link #remove() } or
 * {@link #poll()}.  In a FIFO queue, all new elements are inserted at
 * the <em>tail</em> of the queue. Other kinds of queues may use
 * different placement rules.  Every {@code Queue} implementation
 * must specify its ordering properties.
 *
 * 在容量固定的队列，如生产者消费者队列中，队列容量有限，插入和失败操作都是很正常的，
 * 因此需要有offer、poll这样失败不会抛异常，而是仅仅返回一个标志的方法。
 * <p>The {@link #offer offer} method inserts an element if possible,
 * otherwise returning {@code false}.  This differs from the {@link
 * java.util.Collection#add Collection.add} method, which can fail to
 * add an element only by throwing an unchecked exception.  The
 * {@code offer} method is designed for use when failure is a normal,
 * rather than exceptional occurrence, for example, in fixed-capacity
 * (or &quot;bounded&quot;) queues.
 *
 * remove和poll方法用来移除队列头部的元素。当队列为空时，remove会抛出异常，而
 * poll仅仅返回false。
 *
 * <p>The {@link #remove()} and {@link #poll()} methods remove and
 * return the head of the queue.
 * Exactly which element is removed from the queue is a
 * function of the queue's ordering policy, which differs from
 * implementation to implementation. The {@code remove()} and
 * {@code poll()} methods differ only in their behavior when the
 * queue is empty: the {@code remove()} method throws an exception,
 * while the {@code poll()} method returns {@code null}.
 *
 * element和peek方法返回队列的头部元素值，但是并不删除头节点。
 * 却别在于，队列为空时，element将抛出异常。而peek返回false
 *
 * <p>The {@link #element()} and {@link #peek()} methods return, but do
 * not remove, the head of the queue.
 *
 * 用于多线程环境中的队列是java.util.concurrent.BlockingQueue。这个队列实现了本接口
 * <p>The {@code Queue} interface does not define the <i>blocking queue
 * methods</i>, which are common in concurrent programming.  These methods,
 * which wait for elements to appear or for space to become available, are
 * defined in the {@link java.util.concurrent.BlockingQueue} interface, which
 * extends this interface.
 *
 * 队列的实现通常不允许插入null值，但是一些实现，如LinkedList并不禁止null值的插入。
 * <p>{@code Queue} implementations generally do not allow insertion
 * of {@code null} elements, although some implementations, such as
 * {@link LinkedList}, do not prohibit insertion of {@code null}.
 *
 * 但就算在这些不禁止的实现中，null值也是不应该别插入到队列中的，因为在poll方法中，
 * null作为出队失败的标志。
 *
 * Even in the implementations that permit it, {@code null} should
 * not be inserted into a {@code Queue}, as {@code null} is also
 * used as a special return value by the {@code poll} method to
 * indicate that the queue contains no elements.
 *
 * 队列通常不实现自己的equals和hashCode方法，而是使用继承自Object的这些方法。
 * 因为对于相同元素但不同顺序的队列，基于队列值的equals和hashCode方法并不总是表现的很好。
 * <p>{@code Queue} implementations generally do not define
 * element-based versions of methods {@code equals} and
 * {@code hashCode} but instead inherit the identity based versions
 * from class {@code Object}, because element-based equality is not
 * always well-defined for queues with the same elements but different
 * ordering properties.
 *
 * <p>This interface is a member of the
 * <a href="{@docRoot}/java/util/package-summary.html#CollectionsFramework">
 * Java Collections Framework</a>.
 *
 * @since 1.5
 * @author Doug Lea
 * @param <E> the type of elements held in this queue
 */
public interface Queue<E> extends Collection<E> {
    /**
     * 向队列尾部插入一个值，如果成功则返回true。如果没有空间，则抛出异常
     * Inserts the specified element into this queue if it is possible to do so
     * immediately without violating capacity restrictions, returning
     * {@code true} upon success and throwing an {@code IllegalStateException}
     * if no space is currently available.
     *
     * @param e the element to add
     * @return {@code true} (as specified by {@link Collection#add})
     * @throws IllegalStateException if the element cannot be added at this
     *         time due to capacity restrictions
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null and
     *         this queue does not permit null elements
     * @throws IllegalArgumentException if some property of this element
     *         prevents it from being added to this queue
     */
    boolean add(E e);

    /**
     * 向队列尾部插入一个元素，容量不足则返回false。
     * 在容量有限的队列中，推荐实用该方法，而不是add。
     * Inserts the specified element into this queue if it is possible to do
     * so immediately without violating capacity restrictions.
     * When using a capacity-restricted queue, this method is generally
     * preferable to {@link #add}, which can fail to insert an element only
     * by throwing an exception.
     *
     * @param e the element to add
     * @return {@code true} if the element was added to this queue, else
     *         {@code false}
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null and
     *         this queue does not permit null elements
     * @throws IllegalArgumentException if some property of this element
     *         prevents it from being added to this queue
     */
    boolean offer(E e);

    /**
     * 将队列头部的元素删除并返回，队列为空则抛异常。
     * Retrieves and removes the head of this queue.  This method differs
     * from {@link #poll poll} only in that it throws an exception if this
     * queue is empty.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    E remove();

    /**
     * 将队列头部的元素删除并返回，队列为空则返回false。
     * Retrieves and removes the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    E poll();

    /**
     * 返回队列头部元素值，但不删除。如果队列为null则抛出异常
     * Retrieves, but does not remove, the head of this queue.  This method
     * differs from {@link #peek peek} only in that it throws an exception
     * if this queue is empty.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    E element();

    /**
     * 返回队列头部元素值，但不删除。如果队列为空则返回null。
     * Retrieves, but does not remove, the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    E peek();
}

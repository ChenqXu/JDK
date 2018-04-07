/*
 * Copyright (c) 1998, 2013, Oracle and/or its affiliates. All rights reserved.
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

package java.util;

/**
 * SortedSet能保持元素整体有序。元素根据自身的原始顺序或指定的Comparator进行大小比较。
 * 迭代器遍历的顺序使升序的。该类为利用有序性额外提供几个方法。
 * A {@link Set} that further provides a <i>total ordering</i> on its elements.
 * The elements are ordered using their {@linkplain Comparable natural
 * ordering}, or by a {@link Comparator} typically provided at sorted
 * set creation time.  The set's iterator will traverse the set in
 * ascending element order. Several additional operations are provided
 * to take advantage of the ordering.  (This interface is the set
 * analogue of {@link SortedMap}.)
 *
 * 所有被插入Set中的元素都必须实现Comparable接口，或者通过构造器传入一个Comparator。
 * 对任意的两个元素e1、e2，都需要能通过e1.compareTo(e2)或者comparator.compare(e1, e2)
 * 进行比较。
 * <p>All elements inserted into a sorted set must implement the {@code Comparable}
 * interface (or be accepted by the specified comparator).  Furthermore, all
 * such elements must be <i>mutually comparable</i>: {@code e1.compareTo(e2)}
 * (or {@code comparator.compare(e1, e2)}) must not throw a
 * {@code ClassCastException} for any elements {@code e1} and {@code e2} in
 * the sorted set.  Attempts to violate this restriction will cause the
 * offending method or constructor invocation to throw a
 * {@code ClassCastException}.
 *
 * <p>Note that the ordering maintained by a sorted set (whether or not an
 * explicit comparator is provided) must be <i>consistent with equals</i> if
 * the sorted set is to correctly implement the {@code Set} interface.  (See
 * the {@code Comparable} interface or {@code Comparator} interface for a
 * precise definition of <i>consistent with equals</i>.)  This is so because
 * the {@code Set} interface is defined in terms of the {@code equals}
 * operation, but a sorted set performs all element comparisons using its
 * {@code compareTo} (or {@code compare}) method, so two elements that are
 * deemed equal by this method are, from the standpoint of the sorted set,
 * equal.  The behavior of a sorted set <i>is</i> well-defined even if its
 * ordering is inconsistent with equals; it just fails to obey the general
 * contract of the {@code Set} interface.
 *
 * 所有通用的有序set实现类都必须拥有4个标准构造器：
 * 1. 一个无参构造器用于创建一个空的set，根据元素的自然顺序进行排序。
 * 2. 只含有一个Comparator参数的构造器。用于创建一个空的set，根据指定的比较器进行排序
 * 3. 只含有一个Collection类型元素的构造器。创建一个包含此元素的set，根据这个元素的自然顺序
 * 进行排序
 * 4. 只包含一个SortedSet类型元素的构造器。创建一个新的和传入set相同的元素和顺序的SortedSet
 * 由于接口中不能包含构造器，因此上面的要求并不能强制要求。
 * <p>All general-purpose sorted set implementation classes should
 * provide four "standard" constructors: 1) A void (no arguments)
 * constructor, which creates an empty sorted set sorted according to
 * the natural ordering of its elements.  2) A constructor with a
 * single argument of type {@code Comparator}, which creates an empty
 * sorted set sorted according to the specified comparator.  3) A
 * constructor with a single argument of type {@code Collection},
 * which creates a new sorted set with the same elements as its
 * argument, sorted according to the natural ordering of the elements.
 * 4) A constructor with a single argument of type {@code SortedSet},
 * which creates a new sorted set with the same elements and the same
 * ordering as the input sorted set.  There is no way to enforce this
 * recommendation, as interfaces cannot contain constructors.
 *
 * 注意：几个方法可以返回指定范围的子集合。这个范围是半开半闭的（包含开始点，不包含
 * 结束点）。如果你需要指定一个闭区间，可以按照以下方式：
 * SortedSet<String> sub = s.subSet(log,high+"\0");
 * <p>Note: several methods return subsets with restricted ranges.
 * Such ranges are <i>half-open</i>, that is, they include their low
 * endpoint but not their high endpoint (where applicable).
 * If you need a <i>closed range</i> (which includes both endpoints), and
 * the element type allows for calculation of the successor of a given
 * value, merely request the subrange from {@code lowEndpoint} to
 * {@code successor(highEndpoint)}.  For example, suppose that {@code s}
 * is a sorted set of strings.  The following idiom obtains a view
 * containing all of the strings in {@code s} from {@code low} to
 * {@code high}, inclusive:<pre>
 *   SortedSet&lt;String&gt; sub = s.subSet(low, high+"\0");</pre>
 *
 * 同样的方法可以用来构造一个全开范围：
 * SortedSet<String> sub = s.subSet(low+"\0", high);
 * A similar technique can be used to generate an <i>open range</i> (which
 * contains neither endpoint).  The following idiom obtains a view
 * containing all of the Strings in {@code s} from {@code low} to
 * {@code high}, exclusive:<pre>
 *   SortedSet&lt;String&gt; sub = s.subSet(low+"\0", high);</pre>
 *
 * <p>This interface is a member of the
 * <a href="{@docRoot}/java/util/package-summary.html#CollectionsFramework">
 * Java Collections Framework</a>.
 *
 * @param <E> the type of elements maintained by this set
 *
 * @author  Josh Bloch
 * @see Set
 * @see TreeSet
 * @see SortedMap
 * @see Collection
 * @see Comparable
 * @see Comparator
 * @see ClassCastException
 * @since 1.2
 */

public interface SortedSet<E> extends Set<E> {
    /**
     * 返回该集合使用的比较器，如果使用元素的自然顺序，那么返回null。
     * Returns the comparator used to order the elements in this set,
     * or {@code null} if this set uses the {@linkplain Comparable
     * natural ordering} of its elements.
     *
     * @return the comparator used to order the elements in this set,
     *         or {@code null} if this set uses the natural ordering
     *         of its elements
     */
    Comparator<? super E> comparator();

    /**
     * 返回一个指定范围的集合视图，类似于ArrayList中的subList方法。
     * 当from和to相等时，返回空的子视图。
     * Returns a view of the portion of this set whose elements range
     * from {@code fromElement}, inclusive, to {@code toElement},
     * exclusive.  (If {@code fromElement} and {@code toElement} are
     * equal, the returned set is empty.)  The returned set is backed
     * 返回的子集合依赖于父集合，所有对子集合的修改都会映射到父集合中，反之也一样。
     * by this set, so changes in the returned set are reflected in
     * this set, and vice-versa.  The returned set supports all
     * optional set operations that this set supports.
     *
     * 如果尝试在子集合范围外插入一个元素，那么会抛出IllegalArgumentException
     *
     * <p>The returned set will throw an {@code IllegalArgumentException}
     * on an attempt to insert an element outside its range.
     *
     * @param fromElement low endpoint (inclusive) of the returned set
     * @param toElement high endpoint (exclusive) of the returned set
     * @return a view of the portion of this set whose elements range from
     *         {@code fromElement}, inclusive, to {@code toElement}, exclusive
     * @throws ClassCastException if {@code fromElement} and
     *         {@code toElement} cannot be compared to one another using this
     *         set's comparator (or, if the set has no comparator, using
     *         natural ordering).  Implementations may, but are not required
     *         to, throw this exception if {@code fromElement} or
     *         {@code toElement} cannot be compared to elements currently in
     *         the set.
     * @throws NullPointerException if {@code fromElement} or
     *         {@code toElement} is null and this set does not permit null
     *         elements
     * @throws IllegalArgumentException if {@code fromElement} is
     *         greater than {@code toElement}; or if this set itself
     *         has a restricted range, and {@code fromElement} or
     *         {@code toElement} lies outside the bounds of the range
     */
    SortedSet<E> subSet(E fromElement, E toElement);

    /**
     * 返回toElement之前的所有元素组成的一个视图。子集合拥有所有父集合拥有的操作。
     * Returns a view of the portion of this set whose elements are
     * strictly less than {@code toElement}.  The returned set is
     * backed by this set, so changes in the returned set are
     * reflected in this set, and vice-versa.  The returned set
     * supports all optional set operations that this set supports.
     *
     * <p>The returned set will throw an {@code IllegalArgumentException}
     * on an attempt to insert an element outside its range.
     *
     * @param toElement high endpoint (exclusive) of the returned set
     * @return a view of the portion of this set whose elements are strictly
     *         less than {@code toElement}
     * @throws ClassCastException if {@code toElement} is not compatible
     *         with this set's comparator (or, if the set has no comparator,
     *         if {@code toElement} does not implement {@link Comparable}).
     *         Implementations may, but are not required to, throw this
     *         exception if {@code toElement} cannot be compared to elements
     *         currently in the set.
     * @throws NullPointerException if {@code toElement} is null and
     *         this set does not permit null elements
     * @throws IllegalArgumentException if this set itself has a
     *         restricted range, and {@code toElement} lies outside the
     *         bounds of the range
     */
    SortedSet<E> headSet(E toElement);

    /**
     * 返回从from（包含）开始的所有元素组成的视图。
     * Returns a view of the portion of this set whose elements are
     * greater than or equal to {@code fromElement}.  The returned
     * set is backed by this set, so changes in the returned set are
     * reflected in this set, and vice-versa.  The returned set
     * supports all optional set operations that this set supports.
     *
     * <p>The returned set will throw an {@code IllegalArgumentException}
     * on an attempt to insert an element outside its range.
     *
     * @param fromElement low endpoint (inclusive) of the returned set
     * @return a view of the portion of this set whose elements are greater
     *         than or equal to {@code fromElement}
     * @throws ClassCastException if {@code fromElement} is not compatible
     *         with this set's comparator (or, if the set has no comparator,
     *         if {@code fromElement} does not implement {@link Comparable}).
     *         Implementations may, but are not required to, throw this
     *         exception if {@code fromElement} cannot be compared to elements
     *         currently in the set.
     * @throws NullPointerException if {@code fromElement} is null
     *         and this set does not permit null elements
     * @throws IllegalArgumentException if this set itself has a
     *         restricted range, and {@code fromElement} lies outside the
     *         bounds of the range
     */
    SortedSet<E> tailSet(E fromElement);

    /**
     * 返回最小元素
     * Returns the first (lowest) element currently in this set.
     *
     * @return the first (lowest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    E first();

    /**
     * 返回最大元素
     * Returns the last (highest) element currently in this set.
     *
     * @return the last (highest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    E last();

    /**
     * Creates a {@code Spliterator} over the elements in this sorted set.
     *
     * <p>The {@code Spliterator} reports {@link Spliterator#DISTINCT},
     * {@link Spliterator#SORTED} and {@link Spliterator#ORDERED}.
     * Implementations should document the reporting of additional
     * characteristic values.
     *
     * <p>The spliterator's comparator (see
     * {@link java.util.Spliterator#getComparator()}) must be {@code null} if
     * the sorted set's comparator (see {@link #comparator()}) is {@code null}.
     * Otherwise, the spliterator's comparator must be the same as or impose the
     * same total ordering as the sorted set's comparator.
     *
     * @implSpec
     * The default implementation creates a
     * <em><a href="Spliterator.html#binding">late-binding</a></em> spliterator
     * from the sorted set's {@code Iterator}.  The spliterator inherits the
     * <em>fail-fast</em> properties of the set's iterator.  The
     * spliterator's comparator is the same as the sorted set's comparator.
     * <p>
     * The created {@code Spliterator} additionally reports
     * {@link Spliterator#SIZED}.
     *
     * @implNote
     * The created {@code Spliterator} additionally reports
     * {@link Spliterator#SUBSIZED}.
     *
     * @return a {@code Spliterator} over the elements in this sorted set
     * @since 1.8
     */
    @Override
    default Spliterator<E> spliterator() {
        return new Spliterators.IteratorSpliterator<E>(
                this, Spliterator.DISTINCT | Spliterator.SORTED | Spliterator.ORDERED) {
            @Override
            public Comparator<? super E> getComparator() {
                return SortedSet.this.comparator();
            }
        };
    }
}

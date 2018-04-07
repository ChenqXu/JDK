/*
 * Copyright (c) 1997, 2013, Oracle and/or its affiliates. All rights reserved.
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

import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Collection接口是集合框架的根接口。JDK没有为该接口提供任何直接实现，而是提供了更细
 * 化的子接口的实现，如Set、List。
 * The root interface in the <i>collection hierarchy</i>.  A collection
 * represents a group of objects, known as its <i>elements</i>.  Some
 * collections allow duplicate elements and others do not.  Some are ordered
 * and others unordered.  The JDK does not provide any <i>direct</i>
 * implementations of this interface: it provides implementations of more
 * specific subinterfaces like {@code Set} and {@code List}.  This interface
 * is typically used to pass collections around and manipulate them where
 * maximum generality is desired.
 *
 * <p><i>Bags</i> or <i>multisets</i> (unordered collections that may contain
 * duplicate elements) should implement this interface directly.
 *
 * 所有通用的集合类（Collection子接口的实现类）都必须提供两个标准的构造器，一个是无参，
 * 另一个只包含一个Collection类型的参数，用来创建一个包含同样元素的新集合
 * <p>All general-purpose {@code Collection} implementation classes (which
 * typically implement {@code Collection} indirectly through one of its
 * subinterfaces) should provide two "standard" constructors: a void (no
 * arguments) constructor, which creates an empty collection, and a
 * constructor with a single argument of type {@code Collection}, which
 * creates a new collection with the same elements as its argument.  In
 * 实际上，后一个构造器允许用户将任何集合类型的元素拷贝到新的期望的集合类型中
 * effect, the latter constructor allows the user to copy any collection,
 * producing an equivalent collection of the desired implementation type.
 * There is no way to enforce this convention (as interfaces cannot contain
 * constructors) but all of the general-purpose {@code Collection}
 * implementations in the Java platform libraries comply.
 *
 * 执行不支持的操作可能抛出UnsupportedOperationException。如对不可变集合执行addAll()方法
 * <p>The "destructive" methods contained in this interface, that is, the
 * methods that modify the collection on which they operate, are specified to
 * throw {@code UnsupportedOperationException} if this collection does not
 * support the operation.  If this is the case, these methods may, but are not
 * required to, throw an {@code UnsupportedOperationException} if the
 * invocation would have no effect on the collection.  For example, invoking
 * the {@link #addAll(Collection)} method on an unmodifiable collection may,
 * but is not required to, throw the exception if the collection to be added
 * is empty.
 *
 * 不同的实现对装入的元素有不同的要求，如有的允许null值，有的不允许
 * <p><a id="optional-restrictions">
 * Some collection implementations have restrictions on the elements that
 * they may contain.</a>  For example, some implementations prohibit null elements,
 * and some have restrictions on the types of their elements.  Attempting to
 * add an ineligible element throws an unchecked exception, typically
 * {@code NullPointerException} or {@code ClassCastException}.  Attempting
 * to query the presence of an ineligible element may throw an exception,
 * or it may simply return false; some implementations will exhibit the former
 * behavior and some will exhibit the latter.  More generally, attempting an
 * operation on an ineligible element whose completion would not result in
 * the insertion of an ineligible element into the collection may throw an
 * exception or it may succeed, at the option of the implementation.
 * Such exceptions are marked as "optional" in the specification for this
 * interface.
 *
 *每个实现类的同步策略由自己决定
 * <p>It is up to each collection to determine its own synchronization
 * policy.  In the absence of a stronger guarantee by the
 * implementation, undefined behavior may result from the invocation
 * of any method on a collection that is being mutated by another
 * thread; this includes direct invocations（直接调用）, passing the collection to
 * a method that might perform invocations, and using an existing
 * iterator to examine the collection.
 *
 * 集合框架接口的很多方法都是基于Object.equals()方法，如contains(obj)
 * <p>Many methods in Collections Framework interfaces are defined in
 * terms of(依据） the {@link Object#equals(Object) equals} method.  For example,
 * the specification for the {@link #contains(Object) contains(Object o)}
 * method says: "returns {@code true} if and only if this collection
 * contains at least one element {@code e} such that
 * {@code (o==null ? e==null : o.equals(e))}."  This specification should
 * <i>not</i> be construed to imply that invoking {@code Collection.contains}
 * with a non-null argument {@code o} will cause {@code o.equals(e)} to be
 * invoked for any element {@code e}.  Implementations are free to implement
 * optimizations whereby the {@code equals} invocation is avoided, for
 * example, by first comparing the hash codes of the two elements.  (The
 * {@link Object#hashCode()} specification guarantees that two objects with
 * unequal hash codes cannot be equal.)  More generally, implementations of
 * the various Collections Framework interfaces are free to take advantage of
 * the specified behavior of underlying {@link Object} methods wherever the
 * implementor deems it appropriate.
 *
 * 有些方法可能会因为持有自己的引用（self-referential）而导致递归遍历失败，如clone()、equals()、
 * hashCode()、toString()
 * <p>Some collection operations which perform recursive traversal of the
 * collection may fail with an exception for self-referential instances where
 * the collection directly or indirectly contains itself. This includes the
 * {@code clone()}, {@code equals()}, {@code hashCode()} and {@code toString()}
 * methods. Implementations may optionally handle the self-referential scenario,
 * however most current implementations do not do so.
 *
 * <p>This interface is a member of the
 * <a href="{@docRoot}/java/util/package-summary.html#CollectionsFramework">
 * Java Collections Framework</a>.
 *
 * @implSpec
 * 默认的方法实现并不支持同步
 * The default method implementations (inherited or otherwise) do not apply any
 * synchronization protocol.  If a {@code Collection} implementation has a
 * specific synchronization protocol, then it must override default
 * implementations to apply that protocol.
 *
 * @param <E> the type of elements in this collection
 *
 * @author  Josh Bloch
 * @author  Neal Gafter
 * @see     Set
 * @see     List
 * @see     Map
 * @see     SortedSet
 * @see     SortedMap
 * @see     HashSet
 * @see     TreeSet
 * @see     ArrayList
 * @see     LinkedList
 * @see     Vector
 * @see     Collections
 * @see     Arrays
 * @see     AbstractCollection
 * @since 1.2
 */

public interface Collection<E> extends Iterable<E> {
    // Query Operations

    /**
     * 如果该集合所持有的元素大于Integer.MAX_VALUE，那么就返回Integer.MAX_VALUE
     * Returns the number of elements in this collection.  If this collection
     * contains more than {@code Integer.MAX_VALUE} elements, returns
     * {@code Integer.MAX_VALUE}.
     *
     * @return the number of elements in this collection
     */
    int size();

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    boolean isEmpty();

    /**
     * Returns {@code true} if this collection contains the specified element.
     * More formally, returns {@code true} if and only if this collection
     * contains at least one element {@code e} such that
     * {@code Objects.equals(o, e)}.
     *
     * @param o element whose presence in this collection is to be tested
     * @return {@code true} if this collection contains the specified
     *         element
     * @throws ClassCastException if the type of the specified element
     *         is incompatible with this collection
     *         (<a href="#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     *         collection does not permit null elements
     *         (<a href="#optional-restrictions">optional</a>)
     */
    boolean contains(Object o);

    /**
     * 返回该集合中元素上的迭代器，返回的元素并不保证次序，除非具体的实现提供了保证
     * Returns an iterator over the elements in this collection.  There are no
     * guarantees concerning the order in which the elements are returned
     * (unless this collection is an instance of some class that provides a
     * guarantee).
     *
     * @return an {@code Iterator} over the elements in this collection
     */
    Iterator<E> iterator();

    /**
     *返回的是当前集合的一个数组拷贝（新数组），因此，调用者可以自由地修改返回的数组.返回的
     * 数组的类型是Object。
     * Returns an array containing all of the elements in this collection.
     * If this collection makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the elements in
     * the same order.
     *
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this collection.  (In other words, this method must
     * allocate a new array even if this collection is backed by an array).
     * The caller is thus free to modify the returned array.
     *
     * <p>This method acts as bridge between array-based and collection-based
     * APIs.
     *
     * @return an array containing all of the elements in this collection
     */
    Object[] toArray();

    /**
     * 返回一个包含该集合所有元素的数组；返回数组的类型就是传入的数组类型。如果传入数组的
     * 大小和集合元素数量合适，那么就返回传入数组的引用（由于数组是引用类型，可以不需要返回值）
     * 否则，就返回一个新的数组，新数组的大小就是容器集合元素的数量。
     * Returns an array containing all of the elements in this collection;
     * the runtime type of the returned array is that of the specified array.
     * If the collection fits in the specified array, it is returned therein.
     * Otherwise, a new array is allocated with the runtime type of the
     * specified array and the size of this collection.
     *
     *如果参数数组长度比collection大，那么后面的位置都赋值为null。
     * <p>If this collection fits in the specified array with room to spare
     * (i.e., the array has more elements than this collection), the element
     * in the array immediately following the end of the collection is set to
     * {@code null}.  (This is useful in determining the length of this
     * collection <i>only</i> if the caller knows that this collection does
     * not contain any {@code null} elements.)
     *
     * 如果这个集合的迭代器返回的元素有序，那么返回的数组也保持相同的顺序
     * <p>If this collection makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the elements in
     * the same order.
     *
     * 同toArray()方法一样，这个方法也是基于数组和基于集合的API的桥梁。更重要的是，
     * 这个方法允许调用者对返回数组的类型进行精确控制。在特定环境中，也可以用来节省分配成本。
     * <p>Like the {@link #toArray()} method, this method acts as bridge between
     * array-based and collection-based APIs.  Further, this method allows
     * precise control over the runtime type of the output array, and may,
     * under certain circumstances, be used to save allocation costs.
     *
     *String[] y = x.toArray(new Object[0]) 的效果等同于
     * String[] y = x.toArray();
     * <p>Suppose {@code x} is a collection known to contain only strings.
     * The following code can be used to dump the collection into a newly
     * allocated array of {@code String}:
     *
     * <pre>
     *     String[] y = x.toArray(new String[0]);</pre>
     *
     * Note that {@code toArray(new Object[0])} is identical in function to
     * {@code toArray()}.
     *
     * @param <T> the runtime type of the array to contain the collection
     * @param a the array into which the elements of this collection are to be
     *        stored, if it is big enough; otherwise, a new array of the same
     *        runtime type is allocated for this purpose.
     * @return an array containing all of the elements in this collection
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this collection
     * @throws NullPointerException if the specified array is null
     */
    <T> T[] toArray(T[] a);

    // Modification Operations

    /**
     * 如果插入元素成功，则返回true；
     * 如果数组不允许重复，且该元素已经存在，则返回false
     * Ensures that this collection contains the specified element (optional
     * operation).  Returns {@code true} if this collection changed as a
     * result of the call.  (Returns {@code false} if this collection does
     * not permit duplicates and already contains the specified element.)<p>
     *
     * 支持此操作的集合通常会对传入的元素加以限制，例如，有的不允许传入null。
     * Collections that support this operation may place limitations on what
     * elements may be added to this collection.  In particular, some
     * collections will refuse to add {@code null} elements, and others will
     * impose restrictions on the type of elements that may be added.
     * Collection classes should clearly specify in their documentation any
     * restrictions on what elements may be added.<p>
     *
     *如果集合拒绝插入一个元素，要么是因为这个元素已经存在，要么就必须抛出异常，以跟前一种情况
     * 相区别。
     * If a collection refuses to add a particular element for any reason
     * other than that it already contains the element, it <i>must</i> throw
     * an exception (rather than returning {@code false}).  This preserves
     * the invariant that a collection always contains the specified element
     * after this call returns.
     *
     * @param e element whose presence in this collection is to be ensured
     * @return {@code true} if this collection changed as a result of the
     *         call
     * @throws UnsupportedOperationException if the {@code add} operation
     *         is not supported by this collection
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this collection
     * @throws NullPointerException if the specified element is null and this
     *         collection does not permit null elements
     * @throws IllegalArgumentException if some property of the element
     *         prevents it from being added to this collection
     * @throws IllegalStateException if the element cannot be added at this
     *         time due to insertion restrictions
     */
    boolean add(E e);

    /**
     * 删除指定的元素，根据Objects.equals(o,e)方法;如果存在指定的元素，就返回true
     * Removes a single instance of the specified element from this
     * collection, if it is present (optional operation).  More formally,
     * removes an element {@code e} such that
     * {@code Objects.equals(o, e)}, if
     * this collection contains one or more such elements.  Returns
     * {@code true} if this collection contained the specified element (or
     * equivalently, if this collection changed as a result of the call).
     *
     * @param o element to be removed from this collection, if present
     * @return {@code true} if an element was removed as a result of this call
     * @throws ClassCastException if the type of the specified element
     *         is incompatible with this collection
     *         (<a href="#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     *         collection does not permit null elements
     *         (<a href="#optional-restrictions">optional</a>)
     * @throws UnsupportedOperationException if the {@code remove} operation
     *         is not supported by this collection
     */
    boolean remove(Object o);


    // Bulk Operations

    /**
     * 如果本集合含有传入集合的所有元素，那么返回true；否则，返回false
     * Returns {@code true} if this collection contains all of the elements
     * in the specified collection.
     *
     * @param  c collection to be checked for containment in this collection
     * @return {@code true} if this collection contains all of the elements
     *         in the specified collection
     * @throws ClassCastException if the types of one or more elements
     *         in the specified collection are incompatible with this
     *         collection
     *         (<a href="#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified collection contains one
     *         or more null elements and this collection does not permit null
     *         elements
     *         (<a href="#optional-restrictions">optional</a>),
     *         or if the specified collection is null.
     * @see    #contains(Object)
     */
    boolean containsAll(Collection<?> c);

    /**
     * 将指定集合中的所有元素都加入到本集合中。如果在本方法执行的过程中，传入的集合被修改了，
     * 那么本方法的结果是未知的。
     * Adds all of the elements in the specified collection to this collection
     * (optional operation).  The behavior of this operation is undefined if
     * the specified collection is modified while the operation is in progress.
     * (This implies that the behavior of this call is undefined if the
     * specified collection is this collection, and this collection is
     * nonempty.)
     *
     * @param c collection containing elements to be added to this collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the {@code addAll} operation
     *         is not supported by this collection
     * @throws ClassCastException if the class of an element of the specified
     *         collection prevents it from being added to this collection
     * @throws NullPointerException if the specified collection contains a
     *         null element and this collection does not permit null elements,
     *         or if the specified collection is null
     * @throws IllegalArgumentException if some property of an element of the
     *         specified collection prevents it from being added to this
     *         collection
     * @throws IllegalStateException if not all the elements can be added at
     *         this time due to insertion restrictions
     * @see #add(Object)
     */
    boolean addAll(Collection<? extends E> c);

    /**
     * 删除本集合中与传入集合相同的元素。
     * Removes all of this collection's elements that are also contained in the
     * specified collection (optional operation).  After this call returns,
     * this collection will contain no elements in common with the specified
     * collection.
     *
     * @param c collection containing elements to be removed from this collection
     * @return {@code true} if this collection changed as a result of the
     *         call
     * @throws UnsupportedOperationException if the {@code removeAll} method
     *         is not supported by this collection
     * @throws ClassCastException if the types of one or more elements
     *         in this collection are incompatible with the specified
     *         collection
     *         (<a href="#optional-restrictions">optional</a>)
     * @throws NullPointerException if this collection contains one or more
     *         null elements and the specified collection does not support
     *         null elements
     *         (<a href="#optional-restrictions">optional</a>),
     *         or if the specified collection is null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    boolean removeAll(Collection<?> c);

    /**
     * 删除符合传入断言的所有元素
     * Removes all of the elements of this collection that satisfy the given
     * predicate.  Errors or runtime exceptions thrown during iteration or by
     * the predicate are relayed to the caller.
     *
     * @implSpec
     * The default implementation traverses all elements of the collection using
     * its {@link #iterator}.  Each matching element is removed using
     * {@link Iterator#remove()}.  If the collection's iterator does not
     * support removal then an {@code UnsupportedOperationException} will be
     * thrown on the first matching element.
     *
     * @param filter a predicate which returns {@code true} for elements to be
     *        removed
     * @return {@code true} if any elements were removed
     * @throws NullPointerException if the specified filter is null
     * @throws UnsupportedOperationException if elements cannot be removed
     *         from this collection.  Implementations may throw this exception if a
     *         matching element cannot be removed or if, in general, removal is not
     *         supported.
     * @since 1.8
     */
    default boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final Iterator<E> each = iterator();
        while (each.hasNext()) {
            if (filter.test(each.next())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }

    /**
     * 仅保留与传入集合相同的元素
     * Retains only the elements in this collection that are contained in the
     * specified collection (optional operation).  In other words, removes from
     * this collection all of its elements that are not contained in the
     * specified collection.
     *
     * @param c collection containing elements to be retained in this collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the {@code retainAll} operation
     *         is not supported by this collection
     * @throws ClassCastException if the types of one or more elements
     *         in this collection are incompatible with the specified
     *         collection
     *         (<a href="#optional-restrictions">optional</a>)
     * @throws NullPointerException if this collection contains one or more
     *         null elements and the specified collection does not permit null
     *         elements
     *         (<a href="#optional-restrictions">optional</a>),
     *         or if the specified collection is null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    boolean retainAll(Collection<?> c);

    /**
     * 移除集合中所有元素
     * Removes all of the elements from this collection (optional operation).
     * The collection will be empty after this method returns.
     *
     * @throws UnsupportedOperationException if the {@code clear} operation
     *         is not supported by this collection
     */
    void clear();


    // Comparison and hashing

    /**
     * 用来判断传入元素是否和集合内元素相等的方法
     * Compares the specified object with this collection for equality. <p>
     *
     * While the {@code Collection} interface adds no stipulations to the
     * general contract for the {@code Object.equals}, programmers who
     * implement the {@code Collection} interface "directly" (in other words,
     * create a class that is a {@code Collection} but is not a {@code Set}
     * or a {@code List}) must exercise care if they choose to override the
     * {@code Object.equals}.  It is not necessary to do so, and the simplest
     * course of action is to rely on {@code Object}'s implementation, but
     * the implementor may wish to implement a "value comparison" in place of
     * the default "reference comparison."  (The {@code List} and
     * {@code Set} interfaces mandate such value comparisons.)<p>
     *
     * The general contract（约定） for the {@code Object.equals} method states that
     * equals must be symmetric（对称的） (in other words, {@code a.equals(b)} if and
     * only if {@code b.equals(a)}).  The contracts for {@code List.equals}
     * and {@code Set.equals} state that lists are only equal to other lists,
     * and sets to other sets.  Thus, a custom {@code equals} method for a
     * collection class that implements neither the {@code List} nor
     * {@code Set} interface must return {@code false} when this collection
     * is compared to any list or set.  (By the same logic, it is not possible
     * to write a class that correctly implements both the {@code Set} and
     * {@code List} interfaces.)
     *
     * @param o object to be compared for equality with this collection
     * @return {@code true} if the specified object is equal to this
     * collection
     *
     * @see Object#equals(Object)
     * @see Set#equals(Object)
     * @see List#equals(Object)
     */
    boolean equals(Object o);

    /**
     *返回该集合的哈希码。如果重写Object.equals方法，那么一定要重写Object.hashCode方法
     * 以便他们相互匹配。
     * 如果c1.equals(c2)，那么一定有c1.hashCode() == c2.hashCode()
     * Returns the hash code value for this collection.  While the
     * {@code Collection} interface adds no stipulations（规定） to the general
     * contract for the {@code Object.hashCode} method, programmers should
     * take note that any class that overrides the {@code Object.equals}
     * method must also override the {@code Object.hashCode} method in order
     * to satisfy the general contract for the {@code Object.hashCode} method.
     * In particular, {@code c1.equals(c2)} implies that
     * {@code c1.hashCode()==c2.hashCode()}.
     *
     * @return the hash code value for this collection
     *
     * @see Object#hashCode()
     * @see Object#equals(Object)
     */
    int hashCode();

    /**
     * 可分割迭代器.Spliterator就是为了并行遍历元素而设计的一个迭代器
     * Creates a {@link Spliterator} over the elements in this collection.
     *
     * Implementations should document characteristic values reported by the
     * spliterator.  Such characteristic values are not required to be reported
     * if the spliterator reports {@link Spliterator#SIZED} and this collection
     * contains no elements.
     *
     * <p>The default implementation should be overridden by subclasses that
     * can return a more efficient spliterator.  In order to
     * preserve expected laziness behavior for the {@link #stream()} and
     * {@link #parallelStream()} methods, spliterators should either have the
     * characteristic of {@code IMMUTABLE} or {@code CONCURRENT}, or be
     * <em><a href="Spliterator.html#binding">late-binding</a></em>.
     * If none of these is practical, the overriding class should describe the
     * spliterator's documented policy of binding and structural interference,
     * and should override the {@link #stream()} and {@link #parallelStream()}
     * methods to create streams using a {@code Supplier} of the spliterator,
     * as in:
     * <pre>{@code
     *     Stream<E> s = StreamSupport.stream(() -> spliterator(), spliteratorCharacteristics)
     * }</pre>
     * <p>These requirements ensure that streams produced by the
     * {@link #stream()} and {@link #parallelStream()} methods will reflect the
     * contents of the collection as of initiation of the terminal stream
     * operation.
     *
     * @implSpec
     * The default implementation creates a
     * <em><a href="Spliterator.html#binding">late-binding</a></em> spliterator
     * from the collection's {@code Iterator}.  The spliterator inherits the
     * <em>fail-fast</em> properties of the collection's iterator.
     * <p>
     * The created {@code Spliterator} reports {@link Spliterator#SIZED}.
     *
     * @implNote
     * The created {@code Spliterator} additionally reports
     * {@link Spliterator#SUBSIZED}.
     *
     * <p>If a spliterator covers no elements then the reporting of additional
     * characteristic values, beyond that of {@code SIZED} and {@code SUBSIZED},
     * does not aid clients to control, specialize or simplify computation.
     * However, this does enable shared use of an immutable and empty
     * spliterator instance (see {@link Spliterators#emptySpliterator()}) for
     * empty collections, and enables clients to determine if such a spliterator
     * covers no elements.
     *
     * @return a {@code Spliterator} over the elements in this collection
     * @since 1.8
     */
    @Override
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, 0);
    }

    /**
     * 以这个集合作为源返回一个序列流。
     * Returns a sequential {@code Stream} with this collection as its source.
     *
     * <p>This method should be overridden when the {@link #spliterator()}
     * method cannot return a spliterator that is {@code IMMUTABLE},
     * {@code CONCURRENT}, or <em>late-binding</em>. (See {@link #spliterator()}
     * for details.)
     *
     * @implSpec
     * The default implementation creates a sequential {@code Stream} from the
     * collection's {@code Spliterator}.
     *
     * @return a sequential {@code Stream} over the elements in this collection
     * @since 1.8
     */
    default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * 以这个集合作为源返回一个并行流。也允许这个方法返回一个串行流。
     * Returns a possibly parallel {@code Stream} with this collection as its
     * source.  It is allowable for this method to return a sequential stream.
     *
     * <p>This method should be overridden when the {@link #spliterator()}
     * method cannot return a spliterator that is {@code IMMUTABLE},
     * {@code CONCURRENT}, or <em>late-binding</em>. (See {@link #spliterator()}
     * for details.)
     *
     * @implSpec
     * The default implementation creates a parallel {@code Stream} from the
     * collection's {@code Spliterator}.
     *
     * @return a possibly parallel {@code Stream} over the elements in this
     * collection
     * @since 1.8
     */
    default Stream<E> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }
}

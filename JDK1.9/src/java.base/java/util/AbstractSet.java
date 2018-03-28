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

/**
 * 该类是Set接口的一个框架性实现，目的是为了减小直接实现Set接口所带来的开销。
 * This class provides a skeletal implementation of the {@code Set}
 * interface to minimize the effort required to implement this
 * interface. <p>
 *
 * 除了一些独有的特性外（如Set不允许插入重复元素），继承该类等同于继承AbstractCollection
 *
 * The process of implementing a set by extending this class is identical
 * to that of implementing a Collection by extending AbstractCollection,
 * except that all of the methods and constructors in subclasses of this
 * class must obey the additional constraints imposed by the {@code Set}
 * interface (for instance, the add method must not permit addition of
 * multiple instances of an object to a set).<p>
 *
 * 注意，该类没有重写任何AbstractCollection的方法，它仅仅添加了equals和hashCode的实现。
 *
 * Note that this class does not override any of the implementations from
 * the {@code AbstractCollection} class.  It merely adds implementations
 * for {@code equals} and {@code hashCode}.<p>
 *
 * This class is a member of the
 * <a href="{@docRoot}/java/util/package-summary.html#CollectionsFramework">
 * Java Collections Framework</a>.
 *
 * @param <E> the type of elements maintained by this set
 *
 * @author  Josh Bloch
 * @author  Neal Gafter
 * @see Collection
 * @see AbstractCollection
 * @see Set
 * @since 1.2
 */

/**
 * Set是怎么实现不允许添加重复元素的？答案就在这个类中。
 * @param <E>
 */
public abstract class AbstractSet<E> extends AbstractCollection<E> implements Set<E> {
    /**
     * Sole constructor.  (For invocation by subclass constructors, typically
     * implicit.)
     */
    protected AbstractSet() {
    }

    // Comparison and hashing

    /**
     * 重写了equals方法，用来判断传入的对象是否和本集合“等同”。
     * 当传入的对象o也是一个set，从且两个集合的大小相等，一个集合中的所有元素也存在于
     * 另一个集合中（注意，没有要求顺序也相同，因为Set是不保证顺序的）。
     *
     * Compares the specified object with this set for equality.  Returns
     * {@code true} if the given object is also a set, the two sets have
     * the same size, and every member of the given set is contained in
     * this set.  This ensures that the {@code equals} method works
     * properly across different implementations of the {@code Set}
     * interface.<p>
     *
     * 调用了ContainsAll方法来判断元素都存在于对方集合中。
     * This implementation first checks if the specified object is this
     * set; if so it returns {@code true}.  Then, it checks if the
     * specified object is a set whose size is identical to the size of
     * this set; if not, it returns false.  If so, it returns
     * {@code containsAll((Collection) o)}.
     *
     * @param o object to be compared for equality with this set
     * @return {@code true} if the specified object is equal to this set
     */
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof Set))
            return false;
        Collection<?> c = (Collection<?>) o;
        if (c.size() != size())
            return false;
        try {
            return containsAll(c);
        } catch (ClassCastException unused)   {
            return false;
        } catch (NullPointerException unused) {
            return false;
        }
    }

    /**
     * 集合的hashCode即使所有元素HashCode的和
     * 这和AbstractList的hashCode不同，因为set无序，所以不用考虑顺序。
     * 而AbsctractList的所有实现都是有序的，它的它的hashCode方法将元素的位置也考虑了进去，
     * 具体为：
     * for (E e : this)
     * hashCode = 31*hashCode + (e==null ? 0 : e.hashCode());
     *
     * Returns the hash code value for this set.  The hash code of a set is
     * defined to be the sum of the hash codes of the elements in the set,
     * where the hash code of a {@code null} element is defined to be zero.
     * This ensures that {@code s1.equals(s2)} implies that
     * {@code s1.hashCode()==s2.hashCode()} for any two sets {@code s1}
     * and {@code s2}, as required by the general contract of
     * {@link Object#hashCode}.
     *
     * <p>This implementation iterates over the set, calling the
     * {@code hashCode} method on each element in the set, and adding up
     * the results.
     *
     * @return the hash code value for this set
     * @see Object#equals(Object)
     * @see Set#equals(Object)
     */
    public int hashCode() {
        int h = 0;
        Iterator<E> i = iterator();
        while (i.hasNext()) {
            E obj = i.next();
            if (obj != null)
                h += obj.hashCode();
        }
        return h;
    }

    /**
     *
     * Removes from this set all of its elements that are contained in the
     * specified collection (optional operation).  If the specified
     * collection is also a set, this operation effectively modifies this
     * set so that its value is the <i>asymmetric set difference</i> of
     * the two sets.
     * 哪个集合较小，就使用哪个集合的迭代器进行遍历。
     * 如果本Set较小，那么使用本集合的迭代器进行遍历，并使用iterator.remove方法进行删除
     * 如果传入的集合较小，那么使用传入集合的迭代器对其进行遍历，调用本集合的remove方法进行删除。
     * <p>This implementation determines which is the smaller of this set
     * and the specified collection, by invoking the {@code size}
     * method on each.  If this set has fewer elements, then the
     * implementation iterates over this set, checking each element
     * returned by the iterator in turn to see if it is contained in
     * the specified collection.  If it is so contained, it is removed
     * from this set with the iterator's {@code remove} method.  If
     * the specified collection has fewer elements, then the
     * implementation iterates over the specified collection, removing
     * from this set each element returned by the iterator, using this
     * set's {@code remove} method.
     *
     * <p>Note that this implementation will throw an
     * {@code UnsupportedOperationException} if the iterator returned by the
     * {@code iterator} method does not implement the {@code remove} method.
     *
     * @param  c collection containing elements to be removed from this set
     * @return {@code true} if this set changed as a result of the call
     * @throws UnsupportedOperationException if the {@code removeAll} operation
     *         is not supported by this set
     * @throws ClassCastException if the class of an element of this set
     *         is incompatible with the specified collection
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if this set contains a null element and the
     *         specified collection does not permit null elements
     * (<a href="Collection.html#optional-restrictions">optional</a>),
     *         or if the specified collection is null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;

        if (size() > c.size()) {
            for (Object e : c)
                modified |= remove(e);
        } else {
            for (Iterator<?> i = iterator(); i.hasNext(); ) {
                if (c.contains(i.next())) {
                    i.remove();
                    modified = true;
                }
            }
        }
        return modified;
    }

}

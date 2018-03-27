/*
 * Copyright (c) 1997, 2006, Oracle and/or its affiliates. All rights reserved.
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
 * 该类的设计使用了模板方法设计模式，将listIterator留给子类去实现，便于以后扩展。
 *
 * 该类提供了List接口的顺序访问(sequential access)实现框架。对于类似链表方式的
 * 类，应该优先考虑继承该类。
 *
 * iterator() {return listIterator();}该类的iterator是通过调用listIterator来实现的。
 * listIterator在本类中是抽象方法，留待子类实现。
 *
 * This class provides a skeletal implementation of the {@code List}
 * interface to minimize the effort required to implement this interface
 * backed by a "sequential access" data store (such as a linked list).  For
 * random access data (such as an array), {@code AbstractList} should be used
 * in preference to this class.<p>
 *
 * 该类的访问方式与AbstractList不同，AbstractList是通过Random Access方式访问元素，如数组。
 * 该类是通过Sequential Access方式访问元素，如链表。
 * This class is the opposite of the {@code AbstractList} class in the sense
 * that it implements the "random access" methods ({@code get(int index)},
 * {@code set(int index, E element)}, {@code add(int index, E element)} and
 * {@code remove(int index)}) on top of the list's list iterator, instead of
 * the other way around.<p>
 *
 *
 * To implement a list the programmer needs only to extend this class and
 * provide implementations for the {@code listIterator} and {@code size}
 * methods.  For an unmodifiable list, the programmer need only implement the
 * list iterator's {@code hasNext}, {@code next}, {@code hasPrevious},
 * {@code previous} and {@code index} methods.<p>
 *
 * For a modifiable list the programmer should additionally implement the list
 * iterator's {@code set} method.  For a variable-size list the programmer
 * should additionally implement the list iterator's {@code remove} and
 * {@code add} methods.<p>
 *
 * The programmer should generally provide a void (no argument) and collection
 * constructor, as per the recommendation in the {@code Collection} interface
 * specification.<p>
 *
 * This class is a member of the
 * <a href="{@docRoot}/java/util/package-summary.html#CollectionsFramework">
 * Java Collections Framework</a>.
 *
 * @author  Josh Bloch
 * @author  Neal Gafter
 * @see Collection
 * @see List
 * @see AbstractList
 * @see AbstractCollection
 * @since 1.2
 */

public abstract class AbstractSequentialList<E> extends AbstractList<E> {
    /**
     * Sole constructor.  (For invocation by subclass constructors, typically
     * implicit.)
     */
    protected AbstractSequentialList() {
    }

    /**
     * 通过listIterator遍历到该位置，然后使用iterator.next()方法返回该位置的元素
     * Returns the element at the specified position in this list.
     *
     * <p>This implementation first gets a list iterator pointing to the
     * indexed element (with {@code listIterator(index)}).  Then, it gets
     * the element using {@code ListIterator.next} and returns it.
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E get(int index) {
        try {
            return listIterator(index).next();
        } catch (NoSuchElementException exc) {
            throw new IndexOutOfBoundsException("Index: "+index);
        }
    }

    /**
     * 通过listIterator遍历到index处，然后调用listIterator的set方法替换元素。最后返回旧元素
     * Replaces the element at the specified position in this list with the
     * specified element (optional operation).
     *
     * <p>This implementation first gets a list iterator pointing to the
     * indexed element (with {@code listIterator(index)}).  Then, it gets
     * the current element using {@code ListIterator.next} and replaces it
     * with {@code ListIterator.set}.
     *
     * <p>Note that this implementation will throw an
     * {@code UnsupportedOperationException} if the list iterator does not
     * implement the {@code set} operation.
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     * @throws IndexOutOfBoundsException     {@inheritDoc}
     */
    public E set(int index, E element) {
        try {
            ListIterator<E> e = listIterator(index);
            E oldVal = e.next();
            e.set(element);
            return oldVal;
        } catch (NoSuchElementException exc) {
            throw new IndexOutOfBoundsException("Index: "+index);
        }
    }

    /**
     * 调用listIterator(index).add(element);方法插入元素
     * Inserts the specified element at the specified position in this list
     * (optional operation).  Shifts the element currently at that position
     * (if any) and any subsequent elements to the right (adds one to their
     * indices).
     *
     * <p>This implementation first gets a list iterator pointing to the
     * indexed element (with {@code listIterator(index)}).  Then, it
     * inserts the specified element with {@code ListIterator.add}.
     *
     * <p>Note that this implementation will throw an
     * {@code UnsupportedOperationException} if the list iterator does not
     * implement the {@code add} operation.
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     * @throws IndexOutOfBoundsException     {@inheritDoc}
     */
    public void add(int index, E element) {
        try {
            listIterator(index).add(element);
        } catch (NoSuchElementException exc) {
            throw new IndexOutOfBoundsException("Index: "+index);
        }
    }

    /**
     * 调用listIterator(index)定位到该位置
     * 调用e.next();获取该位置的值，并将cursor指向下一位
     * 调用e.remove()删除元素
     * 返回被删除的值
     *
     * Removes the element at the specified position in this list (optional
     * operation).  Shifts any subsequent elements to the left (subtracts one
     * from their indices).  Returns the element that was removed from the
     * list.
     *
     * <p>This implementation first gets a list iterator pointing to the
     * indexed element (with {@code listIterator(index)}).  Then, it removes
     * the element with {@code ListIterator.remove}.
     *
     * <p>Note that this implementation will throw an
     * {@code UnsupportedOperationException} if the list iterator does not
     * implement the {@code remove} operation.
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws IndexOutOfBoundsException     {@inheritDoc}
     */
    public E remove(int index) {
        try {
            ListIterator<E> e = listIterator(index);
            E outCast = e.next();
            e.remove();
            return outCast;
        } catch (NoSuchElementException exc) {
            throw new IndexOutOfBoundsException("Index: "+index);
        }
    }


    // Bulk Operations

    /**
     * 从index出开始，将c中的所有元素按顺序插入到本链表中。具体实现方式：
     * 1. 调用listIterator(index)定位到该处
     * 2. 使用for (E e : c)依次遍历，并插入元素e1.add(e);
     * 3. 如果链表被修改了，返回true
     *
     * 通过在ListIterator.add 后调用ListIterator.next，可以跳过本list的当前元素
     *
     * Inserts all of the elements in the specified collection into this
     * list at the specified position (optional operation).  Shifts the
     * element currently at that position (if any) and any subsequent
     * elements to the right (increases their indices).  The new elements
     * will appear in this list in the order that they are returned by the
     * specified collection's iterator.  The behavior of this operation is
     * undefined if the specified collection is modified while the
     * operation is in progress.  (Note that this will occur if the specified
     * collection is this list, and it's nonempty.)
     *
     * <p>This implementation gets an iterator over the specified collection and
     * a list iterator over this list pointing to the indexed element (with
     * {@code listIterator(index)}).  Then, it iterates over the specified
     * collection, inserting the elements obtained from the iterator into this
     * list, one at a time, using {@code ListIterator.add} followed by
     * {@code ListIterator.next} (to skip over the added element).
     *
     * <p>Note that this implementation will throw an
     * {@code UnsupportedOperationException} if the list iterator returned by
     * the {@code listIterator} method does not implement the {@code add}
     * operation.
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     * @throws IndexOutOfBoundsException     {@inheritDoc}
     */
    public boolean addAll(int index, Collection<? extends E> c) {
        try {
            boolean modified = false;
            ListIterator<E> e1 = listIterator(index);
            for (E e : c) {
                e1.add(e);
                modified = true;
            }
            return modified;
        } catch (NoSuchElementException exc) {
            throw new IndexOutOfBoundsException("Index: "+index);
        }
    }


    // Iterators

    /**
     * Returns an iterator over the elements in this list (in proper
     * sequence).<p>
     *
     * This implementation merely returns a list iterator over the list.
     *
     * @return an iterator over the elements in this list (in proper sequence)
     */
    public Iterator<E> iterator() {
        return listIterator();
    }

    /**
     * 模板方法设计模式，留待子类实现
     * Returns a list iterator over the elements in this list (in proper
     * sequence).
     *
     * @param  index index of first element to be returned from the list
     *         iterator (by a call to the {@code next} method)
     * @return a list iterator over the elements in this list (in proper
     *         sequence)
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public abstract ListIterator<E> listIterator(int index);
}

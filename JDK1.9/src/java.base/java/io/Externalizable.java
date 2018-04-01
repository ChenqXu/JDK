/*
 * Copyright (c) 1996, 2004, Oracle and/or its affiliates. All rights reserved.
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

package java.io;

import java.io.ObjectOutput;
import java.io.ObjectInput;

/**
 * 实现了该接口的类，需要自己承担保存和恢复实例内容的责任。
 * Only the identity of the class of an Externalizable instance is
 * written in the serialization stream and it is the responsibility
 * of the class to save and restore the contents of its instances.
 *
 * 子类通过实现Externalizable的writeExternal和readExternal方法来完善对序列化
 * 和反序列化内容和格式的控制。
 * The writeExternal and readExternal methods of the Externalizable
 * interface are implemented by a class to give the class complete
 * control over the format and contents of the stream for an object
 * 这些方法必须显式地与父类型协调以保存其状态。这些方法取代了对writeObject和readObject方法的自定义实现

 * and its supertypes. These methods must explicitly
 * coordinate with the supertype to save its state. These methods supersede
 * customized implementations of writeObject and readObject methods.<br>
 *
 * 对象序列化机制使用Serializable和Externalizable接口。对象持久化机制也可以使用它们。
 * 在实现了Externalizable接口的类中，每一个被存储的对象都要经过Externalizable接口的测试。
 * 如果这个对象支持Externalizable，那么该接口的writeExternal将会被调用。如果这个对象不支持
 * writeExternal但是实现了Serializable接口，那么这个对象将会通过ObjectOutputStream进行存储。
 *
 * Object Serialization uses the Serializable and Externalizable
 * interfaces.  Object persistence mechanisms can use them as well.  Each
 * object to be stored is tested for the Externalizable interface. If
 * the object supports Externalizable, the writeExternal method is called. If the
 * object does not support Externalizable and does implement
 * Serializable, the object is saved using
 * 当一个实现了Externalizable接口的对象被反序列化时，会首先调用该类的无参构造器创建一个对象，
 * 然后调用readExternal读入状态进行装填。而实现了Serializable接口的对象则是通过ObjectInputStream
 * 来读入对象的。
 * ObjectOutputStream. <br> When an Externalizable object is
 * reconstructed, an instance is created using the public no-arg
 * constructor, then the readExternal method called.  Serializable
 * objects are restored by reading them from an ObjectInputStream.<br>
 *
 * 一个Externalizable类型的实例可以通过writeReplace()方法和readResolve()来指定在实例化过程中的替代对象。
 * An Externalizable instance can designate a substitution object via
 * the writeReplace and readResolve methods documented in the Serializable
 * interface.<br>
 *
 * @author  unascribed
 * @see java.io.ObjectOutputStream
 * @see java.io.ObjectInputStream
 * @see java.io.ObjectOutput
 * @see java.io.ObjectInput
 * @see java.io.Serializable
 * @since   1.1
 */
public interface Externalizable extends java.io.Serializable {
    /**
     * 子类需要实现writeExternal方法来保存其内容，该方法是通过调用其原始值的DataOutput方法，
     * 或者调用对象、字符串和数组的ObjectOutput的writeObject方法。
     * The object implements the writeExternal method to save its contents
     * by calling the methods of DataOutput for its primitive values or
     * calling the writeObject method of ObjectOutput for objects, strings,
     * and arrays.
     *
     * @serialData Overriding methods should use this tag to describe
     *             the data layout of this Externalizable object.
     *             List the sequence of element types and, if possible,
     *             relate the element to a public/protected field and/or
     *             method of this Externalizable class.
     *
     * @param out the stream to write the object to
     * @exception IOException Includes any I/O exceptions that may occur
     */
    void writeExternal(ObjectOutput out) throws IOException;

    /**
     * 该方法通过调用原始类型的DataInput方法，或者对象、字符串、数组等引用类型的
     * readObject方法来读入字节流，并恢复对象的状态。
     * readExternal方法必须按照与writeExternal方法相同的顺序和类型来读取数据
     * The object implements the readExternal method to restore its
     * contents by calling the methods of DataInput for primitive
     * types and readObject for objects, strings and arrays.  The
     * readExternal method must read the values in the same sequence
     * and with the same types as were written by writeExternal.
     *
     * @param in the stream to read data from in order to restore the object
     * @exception IOException if I/O errors occur
     * @exception ClassNotFoundException If the class for an object being
     *              restored cannot be found.
     */
    void readExternal(ObjectInput in) throws IOException, ClassNotFoundException;
}

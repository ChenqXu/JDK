/*
 * Copyright (c) 1996, 2013, Oracle and/or its affiliates. All rights reserved.
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

/**
 * Serializable接口只是一个标记接口，里面的接口方法都是可选的（可实现可不实现，
 * 如果不实现则启用其自动序列化功能），接口方法并没有实际定义，见说明文档
 * 实现该接口的类将被允许进行序列化。没有实现该接口的类将不会获得任何序列化或者反序列化的状态
 * 所有实现该接口的类，都是本身可序列化的。
 * Serializability of a class is enabled by the class implementing the
 * java.io.Serializable interface. Classes that do not implement this
 * interface will not have any of their state serialized or
 * deserialized.  All subtypes of a serializable class are themselves
 * serializable.  The serialization interface has no methods or fields
 * and serves only to identify the semantics of being serializable. <p>
 *
 * 为了允许非序列化类的子类型进行序列化（例如，子类实现了Serializable接口，但是父类并没有
 * 实现），子类型需要承担父类的public、protected和package访问权限字段的存储和恢复状态。
 * 因此，需要父类提供一个无参的构造器，以便子类进行状态的初始化。
 * To allow subtypes of non-serializable classes to be serialized, the
 * subtype may assume responsibility for saving and restoring the
 * state of the supertype's public, protected, and (if accessible)
 * package fields.  The subtype may assume this responsibility only if
 * the class it extends has an accessible no-arg constructor to
 * initialize the class's state.  It is an error to declare a class
 * Serializable if this is not the case.  The error will be detected at
 * runtime. <p>
 *
 * 在反序列化期间，非序列化类的字段将会被public或者protected类型的无参构造器初始化。
 * 父类的构造器必须要被可序列化的子类所访问。可序列化子类的字段将会被流恢复。
 *
 * During deserialization, the fields of non-serializable classes will
 * be initialized using the public or protected no-arg constructor of
 * the class.  A no-arg constructor must be accessible to the subclass
 * that is serializable.  The fields of serializable subclasses will
 * be restored from the stream. <p>
 *
 * 当遍历一个图，可能会遇到一个不支持序列化接口的对象。在这种情况下，将会抛出NotSerializableException
 * 并标志该类为不可序列化。
 * When traversing a graph, an object may be encountered that does not
 * support the Serializable interface. In this case the
 * NotSerializableException will be thrown and will identify the class
 * of the non-serializable object. <p>
 *
 *
 *
 * Classes that require special handling during the serialization and
 * deserialization process must implement special methods with these exact
 * signatures:
 *
 * 一些在序列化或者反序列化期间需要做一些特别操作的类，必须实现下面这些完整签名的方法：
 * <PRE>
 * private void writeObject(java.io.ObjectOutputStream out)
 *     throws IOException
 * private void readObject(java.io.ObjectInputStream in)
 *     throws IOException, ClassNotFoundException;
 * private void readObjectNoData()
 *     throws ObjectStreamException;
 * </PRE>
 * writeObject()方法用于将对象的当前状态写入到一个对象输出流中，并由输出流决定余下的操作（如，是输出到
 * 本地文件还是网络中）
 * readObject()负责从指定对象输入流中读出并恢复类的字段。该方法可能会使用in.defaultReadObject来
 * 调用默认逻辑恢复对象的非static和非transient字段。
 * readObjectNoData()方法用于一个并不常见的情景：序列化者（写者）并没有父类，但是反序列化者（读者）有父类，
 * 在这种情况下，从输入流中不能得到父类的信息。此时，readObjectNoData()方法将会调用父类的构造器创建一个
 * 新的父类，使得反序列化得以进行。https://blog.csdn.net/dont27/article/details/38309061
 *
 * <p>The writeObject method is responsible for writing the state of the
 * object for its particular class so that the corresponding
 * readObject method can restore it.  The default mechanism for saving
 * the Object's fields can be invoked by calling
 * out.defaultWriteObject. The method does not need to concern
 * itself with the state belonging to its superclasses or subclasses.
 * State is saved by writing the individual fields to the
 * ObjectOutputStream using the writeObject method or by using the
 * methods for primitive data types supported by DataOutput.
 *
 * <p>The readObject method is responsible for reading from the stream and
 * restoring the classes fields. It may call in.defaultReadObject to invoke
 * the default mechanism for restoring the object's non-static and
 * non-transient fields.  The defaultReadObject method uses information in
 * the stream to assign the fields of the object saved in the stream with the
 * correspondingly named fields in the current object.  This handles the case
 * when the class has evolved to add new fields. The method does not need to
 * concern itself with the state belonging to its superclasses or subclasses.
 * State is restored by reading data from the ObjectInputStream for
 * the individual fields and making assignments to the appropriate fields
 * of the object. Reading primitive data types is supported by DataInput.
 *
 * <p>The readObjectNoData method is responsible for initializing the state of
 * the object for its particular class in the event that the serialization
 * stream does not list the given class as a superclass of the object being
 * deserialized.  This may occur in cases where the receiving party uses a
 * different version of the deserialized instance's class than the sending
 * party, and the receiver's version extends classes that are not extended by
 * the sender's version.  This may also occur if the serialization stream has
 * been tampered; hence, readObjectNoData is useful for initializing
 * deserialized objects properly despite（尽管） a "hostile" or incomplete source
 * stream.
 *
 *
 * <p>Serializable classes that need to designate（指定） an alternative（替代的）object
 * to be used when writing an object to the stream should implement this
 * special method with the exact signature:
 *如果一个类在序列化时，需要指定一个替代对象写入流中，他应该实现下面这个精确定义的方法：
 * <PRE>
 * ANY-ACCESS-MODIFIER Object writeReplace() throws ObjectStreamException;
 * </PRE><p>
 *
 * This writeReplace method is invoked by serialization if the method
 * exists and it would be accessible from a method defined within the
 * class of the object being serialized. Thus, the method can have private,
 * protected and package-private access. Subclass access to this method
 * follows java accessibility rules. <p>
 *
 * Classes that need to designate a replacement when an instance of it
 * is read from the stream should implement this special method with the
 * exact signature.
 * 保护性恢复对象（同时也可以替换对象）——readResolve
 * readResolve会在readObject调用之后自动调用，它最主要的目的就是让恢复的对象变个样，
 * 比如readObject已经反序列化好了一个Person对象，那么就可以在readResolve里再对该对象进行一定的修改，
 * 而最终修改后的结果将作为ObjectInputStream的readObject的返回结果；其最重要的应用就是保护性恢复
 * 自己手动实现的单例、枚举类型的对象（Java 5之后的版本都实现了enum类型的自动保护性恢复，但是Java
 * 5之前的老版本还是不行！）
 * https://blog.csdn.net/lirx_tech/article/details/51303966
 *
 * <PRE>
 * ANY-ACCESS-MODIFIER Object readResolve() throws ObjectStreamException;
 * </PRE><p>
 *
 * This readResolve method follows the same invocation rules and
 * accessibility rules as writeReplace.<p>
 *
 * serialVersionUID是Java序列化中用来验证版本一致性的标志。
 * 在进行反序列化时，JVM会把传来的字节流中的serialVersionUID与本地相应实体（类）的
 * serialVersionUID进行比较，如果相同就认为是一致的，可以进行反序列化，否则就会出现序列
 * 化版本不一致的异常。(InvalidCastException)
 * 一个可序列化的类可以通过设置一个名为serialVersionUID的字段来显示设置自己的serialVersionUID。
 * 这个字段必须严格遵循以下格式：
 * ANY-ACCESS-MODIFIER static final long serialVersionUID = 42L;
 * 如果一个类没有显示声明自己的serialVersionUID，那么运行时序列化系统会根据类的当前状态生成
 * 一个64位的hash码作为默认的serialVersionUID。
 * 然而，所有的可序列化类都被强烈建议显示声明自己的serialVersionUID，因为默认的serialVersionUID
 * 对那些编译器相关的细节高度敏感，这可能会导致在反序列化时产生意外的InvalidClassException。
 * 因此，为了保证在不同编译环境下serialVersionUID的一致性，一个可序列化类必须显示声明自己的
 * serialVersionUID值。
 * 同时，也强烈建议将serialVersionUID设置为private访问权限，这样，这个serialVersionUID就是类型
 * 唯一绑定的了，即使它的子类也不会使用该字段。
 * 数组（Array）类不能显示设置serialVersionUID，因此它们总是使用默认的serialVersionUID。但是
 * 数组类并不依赖serialVersionUID进行匹配。
 *
 * The serialization runtime associates with each serializable class a version
 * number, called a serialVersionUID, which is used during deserialization to
 * verify that the sender and receiver of a serialized object have loaded
 * classes for that object that are compatible with respect to serialization.
 * If the receiver has loaded a class for the object that has a different
 * serialVersionUID than that of the corresponding sender's class, then
 * deserialization will result in an {@link InvalidClassException}.  A
 * serializable class can declare its own serialVersionUID explicitly by
 * declaring a field named <code>"serialVersionUID"</code> that must be static,
 * final, and of type <code>long</code>:
 *
 * <PRE>
 * ANY-ACCESS-MODIFIER static final long serialVersionUID = 42L;
 * </PRE>
 *
 * If a serializable class does not explicitly declare a serialVersionUID, then
 * the serialization runtime will calculate a default serialVersionUID value
 * for that class based on various aspects of the class, as described in the
 * Java(TM) Object Serialization Specification.  However, it is <em>strongly
 * recommended</em> that all serializable classes explicitly declare
 * serialVersionUID values, since the default serialVersionUID computation is
 * highly sensitive to class details that may vary depending on compiler
 * implementations, and can thus result in unexpected
 * <code>InvalidClassException</code>s during deserialization.  Therefore, to
 * guarantee a consistent serialVersionUID value across different java compiler
 * implementations, a serializable class must declare an explicit
 * serialVersionUID value.  It is also strongly advised that explicit
 * serialVersionUID declarations use the <code>private</code> modifier where
 * possible, since such declarations apply only to the immediately declaring
 * class--serialVersionUID fields are not useful as inherited members. Array
 * classes cannot declare an explicit serialVersionUID, so they always have
 * the default computed value, but the requirement for matching
 * serialVersionUID values is waived for array classes.
 *
 * @author  unascribed
 * @see java.io.ObjectOutputStream
 * @see java.io.ObjectInputStream
 * @see java.io.ObjectOutput
 * @see java.io.ObjectInput
 * @see java.io.Externalizable
 * @since   1.1
 */
public interface Serializable {
}

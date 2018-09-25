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

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * Resizable-array implementation of the <tt>List</tt> interface.  Implements
 * all optional list operations, and permits all elements, including
 * <tt>null</tt>.  In addition to implementing the <tt>List</tt> interface,
 * this class provides methods to manipulate the size of the array that is
 * used internally to store the list.  (This class is roughly equivalent to
 * <tt>Vector</tt>, except that it is unsynchronized.)
 *
 * <p>The <tt>size</tt>, <tt>isEmpty</tt>, <tt>get</tt>, <tt>set</tt>,
 * <tt>iterator</tt>, and <tt>listIterator</tt> operations run in constant
 * time.  The <tt>add</tt> operation runs in <i>amortized constant time</i>,
 * that is, adding n elements requires O(n) time.  All of the other operations
 * run in linear time (roughly speaking).  The constant factor is low compared
 * to that for the <tt>LinkedList</tt> implementation.
 *
 * <p>Each <tt>ArrayList</tt> instance has a <i>capacity</i>.  The capacity is
 * the size of the array used to store the elements in the list.  It is always
 * at least as large as the list size.  As elements are added to an ArrayList,
 * its capacity grows automatically.  The details of the growth policy are not
 * specified beyond the fact that adding an element has constant amortized
 * time cost.
 *
 * <p>An application can increase the capacity of an <tt>ArrayList</tt> instance
 * before adding a large number of elements using the <tt>ensureCapacity</tt>
 * operation.  This may reduce the amount of incremental reallocation.
 *
 * <p><strong>Note that this implementation is not synchronized.</strong>
 * If multiple threads access an <tt>ArrayList</tt> instance concurrently,
 * and at least one of the threads modifies the list structurally, it
 * <i>must</i> be synchronized externally.  (A structural modification is
 * any operation that adds or deletes one or more elements, or explicitly
 * resizes the backing array; merely setting the value of an element is not
 * a structural modification.)  This is typically accomplished by
 * synchronizing on some object that naturally encapsulates the list.
 * <p>
 * If no such object exists, the list should be "wrapped" using the
 * {@link Collections#synchronizedList Collections.synchronizedList}
 * method.  This is best done at creation time, to prevent accidental
 * unsynchronized access to the list:<pre>
 *   List list = Collections.synchronizedList(new ArrayList(...));</pre>
 *
 * <p><a name="fail-fast">
 * The iterators returned by this class's {@link #iterator() iterator} and
 * {@link #listIterator(int) listIterator} methods are <em>fail-fast</em>:</a>
 * if the list is structurally modified at any time after the iterator is
 * created, in any way except through the iterator's own
 * {@link ListIterator#remove() remove} or
 * {@link ListIterator#add(Object) add} methods, the iterator will throw a
 * {@link ConcurrentModificationException}.  Thus, in the face of
 * concurrent modification, the iterator fails quickly and cleanly, rather
 * than risking arbitrary, non-deterministic behavior at an undetermined
 * time in the future.
 *
 * <p>Note that the fail-fast behavior of an iterator cannot be guaranteed
 * as it is, generally speaking, impossible to make any hard guarantees in the
 * presence of unsynchronized concurrent modification.  Fail-fast iterators
 * throw {@code ConcurrentModificationException} on a best-effort basis.
 * Therefore, it would be wrong to write a program that depended on this
 * exception for its correctness:  <i>the fail-fast behavior of iterators
 * should be used only to detect bugs.</i>
 *
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @author Josh Bloch
 * @author Neal Gafter
 * @see Collection
 * @see List
 * @see LinkedList
 * @see Vector
 * @since 1.2
 */

public class ArrayList<E> extends AbstractList<E>
		implements List<E>, RandomAccess, Cloneable, java.io.Serializable {
	private static final long serialVersionUID = 8683452581122892189L;

	/**
	 * 默认的容量大小
	 */
	private static final int DEFAULT_CAPACITY = 10;

	/**
	 * 默认的初始化容器对象，容器已经初始化了容量
	 */
	private static final Object[] EMPTY_ELEMENTDATA = {};

	/**
	 * 默认的初始化容器对象，与上面区别在于使用此对象表示，容器没有初始化容量
	 */
	private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

	/**
	 * 核心容器，未指定容量时=DEFAULTCAPACITY_EMPTY_ELEMENTDATA，第一次添加扩展为DEFAULT_CAPACITY
	 */
	transient Object[] elementData; // non-private to simplify nested class access

	/**
	 * 容器包含的元素个数
	 */
	private int size;

	/**
	 * 创建一个指定容量的容器
	 *
	 * @param initialCapacity 指定的容量
	 * @throws IllegalArgumentException 如果是负数抛出此异常
	 */
	public ArrayList(int initialCapacity) {
		if (initialCapacity > 0) {
			this.elementData = new Object[initialCapacity];
		} else if (initialCapacity == 0) {
			this.elementData = EMPTY_ELEMENTDATA;
		} else {
			throw new IllegalArgumentException("Illegal Capacity: " +
					initialCapacity);
		}
	}

	/**
	 * 创建一个空的容器
	 */
	public ArrayList() {
		this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
	}

	/**
	 * 根据另外一个容器初始化新的ArrayList
	 *
	 * @param c 要替换的容器
	 * @throws NullPointerException 如果容器是空的抛出此异常
	 */
	public ArrayList(Collection<? extends E> c) {
		elementData = c.toArray();
		if ((size = elementData.length) != 0) {
			// c.toArray might (incorrectly) not return Object[] (see 6260652)
			if (elementData.getClass() != Object[].class)
				elementData = Arrays.copyOf(elementData, size, Object[].class);
		} else {
			// replace with empty array.
			this.elementData = EMPTY_ELEMENTDATA;
		}
	}

	/**
	 * 重新调整集合大小，即把尾部多余的null元素删除
	 */
	public void trimToSize() {
		modCount++;
		// 如果size小于容器的容量，说明在 size 到 elementData.length之间有null元素
		if (size < elementData.length) {
			elementData = (size == 0)
					? EMPTY_ELEMENTDATA
					: Arrays.copyOf(elementData, size);
		}
	}

	/**
	 * 提供给外部调用的扩容方法
	 *
	 * @param minCapacity 期望扩容后的大小
	 */
	public void ensureCapacity(int minCapacity) {
		int minExpand = (elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA)
				// 不是默认容量，则扩容任意大小
				? 0
				// 已经初始化了，则必须要大于初始化容量（10）
				: DEFAULT_CAPACITY;
		if (minCapacity > minExpand) {
			ensureExplicitCapacity(minCapacity);
		}
	}

	/**
	 * 内部使用的扩容方法
	 *
	 * @param minCapacity 期望扩容后的大小
	 */
	private void ensureCapacityInternal(int minCapacity) {
		if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
			minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
		}

		ensureExplicitCapacity(minCapacity);
	}

	private void ensureExplicitCapacity(int minCapacity) {
		modCount++;

		// 期望的大小必须比容器容量大
		if (minCapacity - elementData.length > 0)
			grow(minCapacity);
	}

	/**
	 * The maximum size of array to allocate.
	 * Some VMs reserve some header words in an array.
	 * Attempts to allocate larger arrays may result in
	 * OutOfMemoryError: Requested array size exceeds VM limit
	 */
	private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

	/**
	 * 真正实现扩容的方法
	 *
	 * @param minCapacity 期望扩容后的大小
	 */
	private void grow(int minCapacity) {
		int oldCapacity = elementData.length;
		// 新容量 = 原容量 + 原容量 / 2
		int newCapacity = oldCapacity + (oldCapacity >> 1);
		// 只有期望大小 > 新容量，才会扩充为期望大小
		if (newCapacity - minCapacity < 0)
			newCapacity = minCapacity;
		// 判断是否溢出
		if (newCapacity - MAX_ARRAY_SIZE > 0)
			newCapacity = hugeCapacity(minCapacity);
		// 使用数组复制进行扩容
		elementData = Arrays.copyOf(elementData, newCapacity);
	}

	/**
	 * 判断是否超出最大值
	 *
	 * @param minCapacity
	 * @return
	 */
	private static int hugeCapacity(int minCapacity) {
		if (minCapacity < 0) // overflow
			throw new OutOfMemoryError();
		return (minCapacity > MAX_ARRAY_SIZE) ?
				Integer.MAX_VALUE :
				MAX_ARRAY_SIZE;
	}

	/**
	 * 返回当前容器的元素数量
	 *
	 * @return 返回当前容器的元素数量
	 */
	public int size() {
		return size;
	}

	/**
	 * 判断容器是不是空的
	 *
	 * @return 空返回true，否则返回false
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * 判断容器是否包含某个元素
	 *
	 * @param o 元素
	 * @return 如果包含返回true
	 */
	public boolean contains(Object o) {
		return indexOf(o) >= 0;
	}

	/**
	 * 返元素第一次出现的位置，没有返回-1
	 */
	public int indexOf(Object o) {
		if (o == null) {
			for (int i = 0; i < size; i++)
				if (elementData[i] == null)
					return i;
		} else {
			for (int i = 0; i < size; i++)
				if (o.equals(elementData[i]))
					return i;
		}
		return -1;
	}

	/**
	 * 返元素最后一次出现的位置，没有返回-1
	 */
	public int lastIndexOf(Object o) {
		if (o == null) {
			for (int i = size - 1; i >= 0; i--)
				if (elementData[i] == null)
					return i;
		} else {
			for (int i = size - 1; i >= 0; i--)
				if (o.equals(elementData[i]))
					return i;
		}
		return -1;
	}

	/**
	 * 克隆一个对象，并且通过副本复制的方式，两个对象完全隔离
	 *
	 * @return ArrayList的克隆对象
	 */
	public Object clone() {
		try {
			// 克隆出新的对象
			ArrayList<?> v = (ArrayList<?>) super.clone();
			// 数据复制
			v.elementData = Arrays.copyOf(elementData, size);
			// 重置修改次数
			v.modCount = 0;
			return v;
		} catch (CloneNotSupportedException e) {
			// this shouldn't happen, since we are Cloneable
			throw new InternalError(e);
		}
	}

	/**
	 * 使用数组复制的方式返回一个容器的数组
	 *
	 * @return 返回容器的副本
	 */
	public Object[] toArray() {
		return Arrays.copyOf(elementData, size);
	}

	/**
	 * 把容器的元素复制到数组中，如果传入数组长度比容器大则在后面补null <br>
	 * <strong> 传入数组为null会空指针</strong> <br>
	 * <strong> 传入数组的类型和当前容器泛型不匹配会ArrayStoreException</strong> <br>
	 *
	 * @param a 需要填充的数组
	 * @return 返回一个包含集合元素的数组
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		if (a.length < size)
			// Make a new array of a's runtime type, but my contents:
			return (T[]) Arrays.copyOf(elementData, size, a.getClass());
		System.arraycopy(elementData, 0, a, 0, size);
		if (a.length > size)
			a[size] = null;
		return a;
	}

	// Positional Access Operations

	@SuppressWarnings("unchecked")
	E elementData(int index) {
		return (E) elementData[index];
	}

	/**
	 * 返回指定索引的元素 <br>
	 *
	 * @param index 索引
	 * @return 返回index索引的元素
	 */
	public E get(int index) {
		// 越界检查
		rangeCheck(index);

		return elementData(index);
	}

	/**
	 * 给指定的索引赋值
	 *
	 * @param index   索引
	 * @param element 要赋的值
	 * @return 返回被覆盖的元素
	 */
	public E set(int index, E element) {
		// 越界检查
		rangeCheck(index);
		// 获取旧的元素
		E oldValue = elementData(index);
		// 在容器中覆盖此元素
		elementData[index] = element;
		return oldValue;
	}

	/**
	 * 在容器的size的位置加入一个元素
	 *
	 * @param e 需要添加的元素
	 * @return 如果添加成功返回true
	 */
	public boolean add(E e) {
		// 判断容器是否需要扩容才能装下，未初始化size的容器，首次扩容为10
		ensureCapacityInternal(size + 1);  // Increments modCount!!
		// 添加到容器中
		elementData[size++] = e;
		return true;
	}

	/**
	 * 在 [0,size] 区间的某个位置插入元素，超出此区间抛出IndexOutOfBoundsException
	 *
	 * @param index   插入的位置
	 * @param element 元素
	 */
	public void add(int index, E element) {
		rangeCheckForAdd(index);
		// 和add方法一样，判断是否需要扩容
		ensureCapacityInternal(size + 1);  // Increments modCount!!
		// 数组复制，从index开始之后的元素每个都后移1位
		// 如：[1,2,3,4,5,6] index=2 变为 [1,2,3,3,4,5,6]
		System.arraycopy(elementData, index, elementData, index + 1,
				size - index);
		// 添加element到index处
		elementData[index] = element;
		size++;
	}

	/**
	 * 移除容器中某个位置的元素
	 *
	 * @param index 移除的位置
	 * @return 返回被移除的元素
	 */
	public E remove(int index) {
		// 越界检查
		rangeCheck(index);
		// 修改次数++
		modCount++;
		E oldValue = elementData(index);
		// 从(index+1)开始后面的元素左移一位
		// 如[1,2,3,4,5,6] index=2变为 [1,2,4,5,6,6]
		int numMoved = size - index - 1;
		if (numMoved > 0)
			System.arraycopy(elementData, index + 1, elementData, index,
					numMoved);
		// 最后一位元素置空，让对象不可达
		elementData[--size] = null;

		return oldValue;
	}

	/**
	 * 删除指定的元素，如果有多个相同的则删除首次出现的那个
	 *
	 * @param o 需要删除的元素
	 * @return 如果包含返回true
	 */
	public boolean remove(Object o) {
		// 就是简单的for循环判断，没什么好写的
		if (o == null) {
			for (int index = 0; index < size; index++)
				if (elementData[index] == null) {
					fastRemove(index);
					return true;
				}
		} else {
			for (int index = 0; index < size; index++)
				if (o.equals(elementData[index])) {
					fastRemove(index);
					return true;
				}
		}
		return false;
	}

	/**
	 * 与{@link ArrayList#remove(int) remove(int)}类似，只是去掉了越界检查和返回值
	 */
	private void fastRemove(int index) {
		modCount++;
		int numMoved = size - index - 1;
		if (numMoved > 0)
			System.arraycopy(elementData, index + 1, elementData, index,
					numMoved);
		elementData[--size] = null; // clear to let GC do its work
	}

	/**
	 * 清空容器的元素，容器容量没变化
	 */
	public void clear() {
		modCount++;

		// 所有的引用清空
		for (int i = 0; i < size; i++)
			elementData[i] = null;

		size = 0;
	}

	/**
	 * 把集合c的所有元素加到容器中 <br>
	 * <strong>若集合c未指定泛型也可以加到容器中，但获取容器元素时会抛出{@link ClassCastException ClassCastException}</strong>
	 *
	 * @param c 指定的集合
	 * @return 如果集合有元素返回true否则返回false
	 * @throws NullPointerException 如果传入集合为null抛出此异常
	 */
	public boolean addAll(Collection<? extends E> c) {
		Object[] a = c.toArray();
		int numNew = a.length;
		// 判断是否需要扩容
		ensureCapacityInternal(size + numNew);  // Increments modCount
		// 在容器最后加上c的所有元素
		System.arraycopy(a, 0, elementData, size, numNew);
		size += numNew;
		return numNew != 0;
	}

	/**
	 * 在index位置后面加上c集合的所有元素
	 *
	 * @param index 插入的位置
	 * @param c     要插入的集合
	 * @return 如果集合有元素返回true否则返回false
	 * @throws IndexOutOfBoundsException 如果index超出[0,size]会抛出此异常
	 * @throws NullPointerException      如果传入集合为null抛出此异常
	 */
	public boolean addAll(int index, Collection<? extends E> c) {
		// 越界判断
		rangeCheckForAdd(index);

		Object[] a = c.toArray();
		int numNew = a.length;
		// modCount++，及扩容的判断
		ensureCapacityInternal(size + numNew);

		// 判断容器是否需要先复制元素，如果index != size 则需要先复制
		int numMoved = size - index;
		if (numMoved > 0)
			System.arraycopy(elementData, index, elementData, index + numNew,
					numMoved);
		// 和addAll(Collection c)一样，数组复制
		System.arraycopy(a, 0, elementData, index, numNew);
		size += numNew;
		return numNew != 0;
	}

	/**
	 * Removes from this list all of the elements whose index is between
	 * {@code fromIndex}, inclusive, and {@code toIndex}, exclusive.
	 * Shifts any succeeding elements to the left (reduces their index).
	 * This call shortens the list by {@code (toIndex - fromIndex)} elements.
	 * (If {@code toIndex==fromIndex}, this operation has no effect.)
	 *
	 * @throws IndexOutOfBoundsException if {@code fromIndex} or
	 *                                   {@code toIndex} is out of range
	 *                                   ({@code fromIndex < 0 ||
	 *                                   fromIndex >= size() ||
	 *                                   toIndex > size() ||
	 *                                   toIndex < fromIndex})
	 */
	protected void removeRange(int fromIndex, int toIndex) {
		modCount++;
		int numMoved = size - toIndex;
		System.arraycopy(elementData, toIndex, elementData, fromIndex,
				numMoved);

		// clear to let GC do its work
		int newSize = size - (toIndex - fromIndex);
		for (int i = newSize; i < size; i++) {
			elementData[i] = null;
		}
		size = newSize;
	}

	/**
	 * 判断数组是否越界
	 */
	private void rangeCheck(int index) {
		if (index >= size)
			throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
	}

	/**
	 * 判断是否越界，用在add和addAll所以允许index=size或index=0
	 */
	private void rangeCheckForAdd(int index) {
		if (index > size || index < 0)
			throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
	}

	/**
	 * IndexOutOfBoundsException 异常的详细信息描述
	 */
	private String outOfBoundsMsg(int index) {
		return "Index: " + index + ", Size: " + size;
	}

	/**
	 * 删除容器存在且c集合也存在的元素
	 *
	 * @param c 参考集合
	 * @return 容器是否修改过
	 * @throws ClassCastException   如果集合c和容器元素类型不匹配抛出此异常
	 * @throws NullPointerException 如果c == null 抛出此异常
	 */
	public boolean removeAll(Collection<?> c) {
		Objects.requireNonNull(c);
		return batchRemove(c, false);
	}

	/**
	 * 保留容器存在且c集合也存在的元素
	 *
	 * @param c 参考集合
	 * @return 容器是否修改过
	 * @throws ClassCastException   如果集合c和容器元素类型不匹配抛出此异常
	 * @throws NullPointerException 如果c == null 抛出此异常
	 */
	public boolean retainAll(Collection<?> c) {
		Objects.requireNonNull(c);
		return batchRemove(c, true);
	}

	/**
	 * 私有的批量处理方法，从容器中去除或保留，集合c中的元素
	 *
	 * @param c          参考集合
	 * @param complement true=保留，false=删除
	 * @return 容器是否修改过
	 */
	private boolean batchRemove(Collection<?> c, boolean complement) {
		final Object[] elementData = this.elementData;
		// r=容器遍历到的下标，w=满足条件元素的个数
		int r = 0, w = 0;
		// 标记是否修改过集合
		boolean modified = false;
		try {
			// 遍历容器所有的元素，把满足条件的元素放到最前面
			for (; r < size; r++)
				if (c.contains(elementData[r]) == complement)
					elementData[w++] = elementData[r];
		} finally {
			// 如果上面的c.contains()抛出了异常，会导致r != size
			if (r != size) {
				System.arraycopy(elementData, r,
						elementData, w,
						size - r);
				w += size - r;
			}
			// 不是所有元素都满足条件，把不满足条件的元素全部清除
			if (w != size) {
				// 清除元素
				for (int i = w; i < size; i++)
					elementData[i] = null;
				modCount += size - w;
				size = w;
				modified = true;
			}
		}
		return modified;
	}

	/**
	 * 对象序列化的方法不管
	 *
	 * @serialData The length of the array backing the <tt>ArrayList</tt>
	 * instance is emitted (int), followed by all of its elements
	 * (each an <tt>Object</tt>) in the proper order.
	 */
	private void writeObject(java.io.ObjectOutputStream s)
			throws java.io.IOException {
		// Write out element count, and any hidden stuff
		int expectedModCount = modCount;
		s.defaultWriteObject();

		// Write out size as capacity for behavioural compatibility with clone()
		s.writeInt(size);

		// Write out all elements in the proper order.
		for (int i = 0; i < size; i++) {
			s.writeObject(elementData[i]);
		}

		if (modCount != expectedModCount) {
			throw new ConcurrentModificationException();
		}
	}

	/**
	 * 对象序反列化的方法不管
	 */
	private void readObject(java.io.ObjectInputStream s)
			throws java.io.IOException, ClassNotFoundException {
		elementData = EMPTY_ELEMENTDATA;

		// Read in size, and any hidden stuff
		s.defaultReadObject();

		// Read in capacity
		s.readInt(); // ignored

		if (size > 0) {
			// be like clone(), allocate array based upon size not capacity
			ensureCapacityInternal(size);

			Object[] a = elementData;
			// Read in all elements in the proper order.
			for (int i = 0; i < size; i++) {
				a[i] = s.readObject();
			}
		}
	}

	/**
	 * 返回从指定位置开始的ListItr迭代器，如果越界会fast-fail <br>
	 * <strong>fast-fail：越界立即抛出IndexOutOfBoundsException<strong/>
	 */
	public ListIterator<E> listIterator(int index) {
		if (index < 0 || index > size)
			throw new IndexOutOfBoundsException("Index: " + index);
		return new ListItr(index);
	}

	/**
	 * 返回ListItr迭代器
	 */
	public ListIterator<E> listIterator() {
		return new ListItr(0);
	}

	/**
	 * 返回一个标准的迭代器
	 *
	 * @return 标准的迭代器类
	 */
	public Iterator<E> iterator() {
		return new Itr();
	}

	/**
	 * 标准的迭代器类
	 */
	private class Itr implements Iterator<E> {
		/**
		 * 游标下一个位置
		 */
		int cursor;
		/**
		 * 当前的游标位置（无元素=-1）
		 */
		int lastRet = -1;
		/**
		 * 期望的修改次数，判断是否出现并发修改用到
		 */
		int expectedModCount = modCount;

		public boolean hasNext() {
			return cursor != size;
		}

		@SuppressWarnings("unchecked")
		public E next() {
			// 判断是否出现并发修改
			checkForComodification();
			int i = cursor;
			// 判断是否越界
			if (i >= size)
				throw new NoSuchElementException();
			Object[] elementData = ArrayList.this.elementData;
			// 并发同一个listItr对象操作会出现 i >= elementData.length
			if (i >= elementData.length)
				throw new ConcurrentModificationException();
			// 记录下一个位置
			cursor = i + 1;
			// 取出对应的元素并返回
			return (E) elementData[lastRet = i];
		}

		public void remove() {
			// 未移动当前游标，不能删除
			if (lastRet < 0)
				throw new IllegalStateException();
			// 并发检查
			checkForComodification();

			try {
				// 调用remove方法
				ArrayList.this.remove(lastRet);
				cursor = lastRet;
				lastRet = -1;
				// 修改expectedModCount
				expectedModCount = modCount;
			} catch (IndexOutOfBoundsException ex) {
				throw new ConcurrentModificationException();
			}
		}

		/**
		 * 批量的修改某些元素，利用java8的特性，需要传入Consumer的实现类（修改的方式）<br>
		 * <strong>感觉这方法用处不大，和{@link ArrayList#forEach(Consumer) forEach}一个用法</strong>
		 *
		 * @param consumer accept方法写修改的具体实现
		 */
		@Override
		@SuppressWarnings("unchecked")
		public void forEachRemaining(Consumer<? super E> consumer) {
			Objects.requireNonNull(consumer);
			final int size = ArrayList.this.size;
			int i = cursor;
			if (i >= size) {
				return;
			}
			final Object[] elementData = ArrayList.this.elementData;
			if (i >= elementData.length) {
				throw new ConcurrentModificationException();
			}
			while (i != size && modCount == expectedModCount) {
				consumer.accept((E) elementData[i++]);
			}
			// update once at end of iteration to reduce heap write traffic
			cursor = i;
			lastRet = i - 1;
			checkForComodification();
		}

		final void checkForComodification() {
			// modCount 只能由迭代器控制，若外部对其值进行修改都将抛出此异常，因此fore循环中不能使用外部的API调整集合
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
		}
	}

	/**
	 * listItr迭代器实现，优化了Itr（增加了{@link ListItr#hasPrevious()}、{@link ListItr#previous()}等方法）
	 */
	private class ListItr extends Itr implements ListIterator<E> {
		ListItr(int index) {
			super();
			// cursor = 当前的位置
			cursor = index;
		}

		public boolean hasPrevious() {
			return cursor != 0;
		}

		public int nextIndex() {
			return cursor;
		}

		public int previousIndex() {
			return cursor - 1;
		}

		@SuppressWarnings("unchecked")
		public E previous() {
			checkForComodification();
			int i = cursor - 1;
			if (i < 0)
				throw new NoSuchElementException();
			Object[] elementData = ArrayList.this.elementData;
			if (i >= elementData.length)
				throw new ConcurrentModificationException();
			cursor = i;
			return (E) elementData[lastRet = i];
		}

		/**
		 * 调用{@link ArrayList#set(int, Object)}方法替换当前索引的元素
		 *
		 * @param e 替换的新元素
		 */
		public void set(E e) {
			if (lastRet < 0)
				throw new IllegalStateException();
			checkForComodification();

			try {
				ArrayList.this.set(lastRet, e);
			} catch (IndexOutOfBoundsException ex) {
				throw new ConcurrentModificationException();
			}
		}

		public void add(E e) {
			checkForComodification();

			try {
				int i = cursor;
				ArrayList.this.add(i, e);
				cursor = i + 1;
				lastRet = -1;
				// 修改expectedModCount
				expectedModCount = modCount;
			} catch (IndexOutOfBoundsException ex) {
				throw new ConcurrentModificationException();
			}
		}
	}

	/**
	 * Returns a view of the portion of this list between the specified
	 * {@code fromIndex}, inclusive, and {@code toIndex}, exclusive.  (If
	 * {@code fromIndex} and {@code toIndex} are equal, the returned list is
	 * empty.)  The returned list is backed by this list, so non-structural
	 * changes in the returned list are reflected in this list, and vice-versa.
	 * The returned list supports all of the optional list operations.
	 *
	 * <p>This method eliminates the need for explicit range operations (of
	 * the sort that commonly exist for arrays).  Any operation that expects
	 * a list can be used as a range operation by passing a subList view
	 * instead of a whole list.  For example, the following idiom
	 * removes a range of elements from a list:
	 * <pre>
	 *      list.subList(from, to).clear();
	 * </pre>
	 * Similar idioms may be constructed for {@link #indexOf(Object)} and
	 * {@link #lastIndexOf(Object)}, and all of the algorithms in the
	 * {@link Collections} class can be applied to a subList.
	 *
	 * <p>The semantics of the list returned by this method become undefined if
	 * the backing list (i.e., this list) is <i>structurally modified</i> in
	 * any way other than via the returned list.  (Structural modifications are
	 * those that change the size of this list, or otherwise perturb it in such
	 * a fashion that iterations in progress may yield incorrect results.)
	 *
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 * @throws IllegalArgumentException  {@inheritDoc}
	 */
	public List<E> subList(int fromIndex, int toIndex) {
		subListRangeCheck(fromIndex, toIndex, size);
		return new SubList(this, 0, fromIndex, toIndex);
	}

	static void subListRangeCheck(int fromIndex, int toIndex, int size) {
		if (fromIndex < 0)
			throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
		if (toIndex > size)
			throw new IndexOutOfBoundsException("toIndex = " + toIndex);
		if (fromIndex > toIndex)
			throw new IllegalArgumentException("fromIndex(" + fromIndex +
					") > toIndex(" + toIndex + ")");
	}

	private class SubList extends AbstractList<E> implements RandomAccess {
		private final AbstractList<E> parent;
		private final int parentOffset;
		private final int offset;
		int size;

		SubList(AbstractList<E> parent,
		        int offset, int fromIndex, int toIndex) {
			this.parent = parent;
			this.parentOffset = fromIndex;
			this.offset = offset + fromIndex;
			this.size = toIndex - fromIndex;
			this.modCount = ArrayList.this.modCount;
		}

		public E set(int index, E e) {
			rangeCheck(index);
			checkForComodification();
			E oldValue = ArrayList.this.elementData(offset + index);
			ArrayList.this.elementData[offset + index] = e;
			return oldValue;
		}

		public E get(int index) {
			rangeCheck(index);
			checkForComodification();
			return ArrayList.this.elementData(offset + index);
		}

		public int size() {
			checkForComodification();
			return this.size;
		}

		public void add(int index, E e) {
			rangeCheckForAdd(index);
			checkForComodification();
			parent.add(parentOffset + index, e);
			this.modCount = parent.modCount;
			this.size++;
		}

		public E remove(int index) {
			rangeCheck(index);
			checkForComodification();
			E result = parent.remove(parentOffset + index);
			this.modCount = parent.modCount;
			this.size--;
			return result;
		}

		protected void removeRange(int fromIndex, int toIndex) {
			checkForComodification();
			parent.removeRange(parentOffset + fromIndex,
					parentOffset + toIndex);
			this.modCount = parent.modCount;
			this.size -= toIndex - fromIndex;
		}

		public boolean addAll(Collection<? extends E> c) {
			return addAll(this.size, c);
		}

		public boolean addAll(int index, Collection<? extends E> c) {
			rangeCheckForAdd(index);
			int cSize = c.size();
			if (cSize == 0)
				return false;

			checkForComodification();
			parent.addAll(parentOffset + index, c);
			this.modCount = parent.modCount;
			this.size += cSize;
			return true;
		}

		public Iterator<E> iterator() {
			return listIterator();
		}

		public ListIterator<E> listIterator(final int index) {
			checkForComodification();
			rangeCheckForAdd(index);
			final int offset = this.offset;

			return new ListIterator<E>() {
				int cursor = index;
				int lastRet = -1;
				int expectedModCount = ArrayList.this.modCount;

				public boolean hasNext() {
					return cursor != SubList.this.size;
				}

				@SuppressWarnings("unchecked")
				public E next() {
					checkForComodification();
					int i = cursor;
					if (i >= SubList.this.size)
						throw new NoSuchElementException();
					Object[] elementData = ArrayList.this.elementData;
					if (offset + i >= elementData.length)
						throw new ConcurrentModificationException();
					cursor = i + 1;
					return (E) elementData[offset + (lastRet = i)];
				}

				public boolean hasPrevious() {
					return cursor != 0;
				}

				@SuppressWarnings("unchecked")
				public E previous() {
					checkForComodification();
					int i = cursor - 1;
					if (i < 0)
						throw new NoSuchElementException();
					Object[] elementData = ArrayList.this.elementData;
					if (offset + i >= elementData.length)
						throw new ConcurrentModificationException();
					cursor = i;
					return (E) elementData[offset + (lastRet = i)];
				}

				@SuppressWarnings("unchecked")
				public void forEachRemaining(Consumer<? super E> consumer) {
					Objects.requireNonNull(consumer);
					final int size = SubList.this.size;
					int i = cursor;
					if (i >= size) {
						return;
					}
					final Object[] elementData = ArrayList.this.elementData;
					if (offset + i >= elementData.length) {
						throw new ConcurrentModificationException();
					}
					while (i != size && modCount == expectedModCount) {
						consumer.accept((E) elementData[offset + (i++)]);
					}
					// update once at end of iteration to reduce heap write traffic
					lastRet = cursor = i;
					checkForComodification();
				}

				public int nextIndex() {
					return cursor;
				}

				public int previousIndex() {
					return cursor - 1;
				}

				public void remove() {
					if (lastRet < 0)
						throw new IllegalStateException();
					checkForComodification();

					try {
						SubList.this.remove(lastRet);
						cursor = lastRet;
						lastRet = -1;
						expectedModCount = ArrayList.this.modCount;
					} catch (IndexOutOfBoundsException ex) {
						throw new ConcurrentModificationException();
					}
				}

				public void set(E e) {
					if (lastRet < 0)
						throw new IllegalStateException();
					checkForComodification();

					try {
						ArrayList.this.set(offset + lastRet, e);
					} catch (IndexOutOfBoundsException ex) {
						throw new ConcurrentModificationException();
					}
				}

				public void add(E e) {
					checkForComodification();

					try {
						int i = cursor;
						SubList.this.add(i, e);
						cursor = i + 1;
						lastRet = -1;
						expectedModCount = ArrayList.this.modCount;
					} catch (IndexOutOfBoundsException ex) {
						throw new ConcurrentModificationException();
					}
				}

				final void checkForComodification() {
					if (expectedModCount != ArrayList.this.modCount)
						throw new ConcurrentModificationException();
				}
			};
		}

		public List<E> subList(int fromIndex, int toIndex) {
			subListRangeCheck(fromIndex, toIndex, size);
			return new SubList(this, offset, fromIndex, toIndex);
		}

		private void rangeCheck(int index) {
			if (index < 0 || index >= this.size)
				throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
		}

		private void rangeCheckForAdd(int index) {
			if (index < 0 || index > this.size)
				throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
		}

		private String outOfBoundsMsg(int index) {
			return "Index: " + index + ", Size: " + this.size;
		}

		private void checkForComodification() {
			if (ArrayList.this.modCount != this.modCount)
				throw new ConcurrentModificationException();
		}

		public Spliterator<E> spliterator() {
			checkForComodification();
			return new ArrayListSpliterator<E>(ArrayList.this, offset,
					offset + this.size, this.modCount);
		}
	}

	/**
	 * java提供的遍历方法，也是遍历数组<br>
	 * 不能对集合做修改不然会抛出ConcurrentModificationException
	 *
	 * @param action 处理
	 */
	@Override
	public void forEach(Consumer<? super E> action) {
		Objects.requireNonNull(action);
		final int expectedModCount = modCount;
		@SuppressWarnings("unchecked") final E[] elementData = (E[]) this.elementData;
		final int size = this.size;
		for (int i = 0; modCount == expectedModCount && i < size; i++) {
			action.accept(elementData[i]);
		}
		// 检查是否对集合修改
		if (modCount != expectedModCount) {
			throw new ConcurrentModificationException();
		}
	}

	/**
	 * Creates a <em><a href="Spliterator.html#binding">late-binding</a></em>
	 * and <em>fail-fast</em> {@link Spliterator} over the elements in this
	 * list.
	 *
	 * <p>The {@code Spliterator} reports {@link Spliterator#SIZED},
	 * {@link Spliterator#SUBSIZED}, and {@link Spliterator#ORDERED}.
	 * Overriding implementations should document the reporting of additional
	 * characteristic values.
	 *
	 * @return a {@code Spliterator} over the elements in this list
	 * @since 1.8
	 */
	@Override
	public Spliterator<E> spliterator() {
		return new ArrayListSpliterator<>(this, 0, -1, 0);
	}

	/**
	 * Index-based split-by-two, lazily initialized Spliterator
	 */
	static final class ArrayListSpliterator<E> implements Spliterator<E> {

		/*
		 * If ArrayLists were immutable, or structurally immutable (no
		 * adds, removes, etc), we could implement their spliterators
		 * with Arrays.spliterator. Instead we detect as much
		 * interference during traversal as practical without
		 * sacrificing much performance. We rely primarily on
		 * modCounts. These are not guaranteed to detect concurrency
		 * violations, and are sometimes overly conservative about
		 * within-thread interference, but detect enough problems to
		 * be worthwhile in practice. To carry this out, we (1) lazily
		 * initialize fence and expectedModCount until the latest
		 * point that we need to commit to the state we are checking
		 * against; thus improving precision.  (This doesn't apply to
		 * SubLists, that create spliterators with current non-lazy
		 * values).  (2) We perform only a single
		 * ConcurrentModificationException check at the end of forEach
		 * (the most performance-sensitive method). When using forEach
		 * (as opposed to iterators), we can normally only detect
		 * interference after actions, not before. Further
		 * CME-triggering checks apply to all other possible
		 * violations of assumptions for example null or too-small
		 * elementData array given its size(), that could only have
		 * occurred due to interference.  This allows the inner loop
		 * of forEach to run without any further checks, and
		 * simplifies lambda-resolution. While this does entail a
		 * number of checks, note that in the common case of
		 * list.stream().forEach(a), no checks or other computation
		 * occur anywhere other than inside forEach itself.  The other
		 * less-often-used methods cannot take advantage of most of
		 * these streamlinings.
		 */

		private final ArrayList<E> list;
		private int index; // current index, modified on advance/split
		private int fence; // -1 until used; then one past last index
		private int expectedModCount; // initialized when fence set

		/**
		 * Create new spliterator covering the given  range
		 */
		ArrayListSpliterator(ArrayList<E> list, int origin, int fence,
		                     int expectedModCount) {
			this.list = list; // OK if null unless traversed
			this.index = origin;
			this.fence = fence;
			this.expectedModCount = expectedModCount;
		}

		private int getFence() { // initialize fence to size on first use
			int hi; // (a specialized variant appears in method forEach)
			ArrayList<E> lst;
			if ((hi = fence) < 0) {
				if ((lst = list) == null)
					hi = fence = 0;
				else {
					expectedModCount = lst.modCount;
					hi = fence = lst.size;
				}
			}
			return hi;
		}

		public ArrayListSpliterator<E> trySplit() {
			int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
			return (lo >= mid) ? null : // divide range in half unless too small
					new ArrayListSpliterator<E>(list, lo, index = mid,
							expectedModCount);
		}

		public boolean tryAdvance(Consumer<? super E> action) {
			if (action == null)
				throw new NullPointerException();
			int hi = getFence(), i = index;
			if (i < hi) {
				index = i + 1;
				@SuppressWarnings("unchecked") E e = (E) list.elementData[i];
				action.accept(e);
				if (list.modCount != expectedModCount)
					throw new ConcurrentModificationException();
				return true;
			}
			return false;
		}

		public void forEachRemaining(Consumer<? super E> action) {
			int i, hi, mc; // hoist accesses and checks from loop
			ArrayList<E> lst;
			Object[] a;
			if (action == null)
				throw new NullPointerException();
			if ((lst = list) != null && (a = lst.elementData) != null) {
				if ((hi = fence) < 0) {
					mc = lst.modCount;
					hi = lst.size;
				} else
					mc = expectedModCount;
				if ((i = index) >= 0 && (index = hi) <= a.length) {
					for (; i < hi; ++i) {
						@SuppressWarnings("unchecked") E e = (E) a[i];
						action.accept(e);
					}
					if (lst.modCount == mc)
						return;
				}
			}
			throw new ConcurrentModificationException();
		}

		public long estimateSize() {
			return (long) (getFence() - index);
		}

		public int characteristics() {
			return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
		}
	}

	/**
	 * 删除满足条件的元素
	 *
	 * @param filter
	 * @return
	 */
	@Override
	public boolean removeIf(Predicate<? super E> filter) {
		Objects.requireNonNull(filter);
		// figure out which elements are to be removed
		// any exception thrown from the filter predicate at this stage
		// will leave the collection unmodified
		int removeCount = 0;
		final BitSet removeSet = new BitSet(size);
		final int expectedModCount = modCount;
		final int size = this.size;
		for (int i = 0; modCount == expectedModCount && i < size; i++) {
			@SuppressWarnings("unchecked") final E element = (E) elementData[i];
			if (filter.test(element)) {
				removeSet.set(i);
				removeCount++;
			}
		}
		// 判断是否有并发修改
		if (modCount != expectedModCount) {
			throw new ConcurrentModificationException();
		}

		// 数组移动，清除多余的元素
		final boolean anyToRemove = removeCount > 0;
		if (anyToRemove) {
			final int newSize = size - removeCount;
			for (int i = 0, j = 0; (i < size) && (j < newSize); i++, j++) {
				i = removeSet.nextClearBit(i);
				elementData[j] = elementData[i];
			}
			for (int k = newSize; k < size; k++) {
				elementData[k] = null;  // Let gc do its work
			}
			this.size = newSize;
			if (modCount != expectedModCount) {
				throw new ConcurrentModificationException();
			}
			modCount++;
		}

		return anyToRemove;
	}

	/**
	 * 替换集合的元素，不能对集合进行修改否则抛出ConcurrentModificationException
	 *
	 * @param operator
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void replaceAll(UnaryOperator<E> operator) {
		Objects.requireNonNull(operator);
		final int expectedModCount = modCount;
		final int size = this.size;
		for (int i = 0; modCount == expectedModCount && i < size; i++) {
			elementData[i] = operator.apply((E) elementData[i]);
		}
		if (modCount != expectedModCount) {
			throw new ConcurrentModificationException();
		}
		modCount++;
	}

	/**
	 * 根据传入的排序方式，对容器进行排序，内部使用数组的归并排序
	 *
	 * @param c 比较器
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void sort(Comparator<? super E> c) {
		final int expectedModCount = modCount;
		Arrays.sort((E[]) elementData, 0, size, c);
		if (modCount != expectedModCount) {
			throw new ConcurrentModificationException();
		}
		modCount++;
	}
}

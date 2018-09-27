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

/**
 * Doubly-linked list implementation of the {@code List} and {@code Deque}
 * interfaces.  Implements all optional list operations, and permits all
 * elements (including {@code null}).
 *
 * <p>All of the operations perform as could be expected for a doubly-linked
 * list.  Operations that index into the list will traverse the list from
 * the beginning or the end, whichever is closer to the specified index.
 *
 * <p><strong>Note that this implementation is not synchronized.</strong>
 * If multiple threads access a linked list concurrently, and at least
 * one of the threads modifies the list structurally, it <i>must</i> be
 * synchronized externally.  (A structural modification is any operation
 * that adds or deletes one or more elements; merely setting the value of
 * an element is not a structural modification.)  This is typically
 * accomplished by synchronizing on some object that naturally
 * encapsulates the list.
 * <p>
 * If no such object exists, the list should be "wrapped" using the
 * {@link Collections#synchronizedList Collections.synchronizedList}
 * method.  This is best done at creation time, to prevent accidental
 * unsynchronized access to the list:<pre>
 *   List list = Collections.synchronizedList(new LinkedList(...));</pre>
 *
 * <p>The iterators returned by this class's {@code iterator} and
 * {@code listIterator} methods are <i>fail-fast</i>: if the list is
 * structurally modified at any time after the iterator is created, in
 * any way except through the Iterator's own {@code remove} or
 * {@code add} methods, the iterator will throw a {@link
 * ConcurrentModificationException}.  Thus, in the face of concurrent
 * modification, the iterator fails quickly and cleanly, rather than
 * risking arbitrary, non-deterministic behavior at an undetermined
 * time in the future.
 *
 * <p>Note that the fail-fast behavior of an iterator cannot be guaranteed
 * as it is, generally speaking, impossible to make any hard guarantees in the
 * presence of unsynchronized concurrent modification.  Fail-fast iterators
 * throw {@code ConcurrentModificationException} on a best-effort basis.
 * Therefore, it would be wrong to write a program that depended on this
 * exception for its correctness:   <i>the fail-fast behavior of iterators
 * should be used only to detect bugs.</i>
 *
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @param <E> the type of elements held in this collection
 * @author Josh Bloch
 * @see List
 * @see ArrayList
 * @since 1.2
 */

public class LinkedList<E>
		extends AbstractSequentialList<E>
		implements List<E>, Deque<E>, Cloneable, java.io.Serializable {
	/**
	 * 记录当前容器的最大索引
	 */
	transient int size = 0;

	/**
	 * 头结点
	 */
	transient Node<E> first;

	/**
	 * 尾节点
	 */
	transient Node<E> last;

	/**
	 * 空构造函数
	 */
	public LinkedList() {
	}

	/**
	 * 创建一个含有集合c的链表类
	 *
	 * @param c 传入的集合
	 * @throws NullPointerException 集合为空抛出此异常
	 */
	public LinkedList(Collection<? extends E> c) {
		this();
		addAll(c);
	}

	/**
	 * 把元素插入到头结点
	 */
	private void linkFirst(E e) {
		final Node<E> f = first;
		final Node<E> newNode = new Node<>(null, e, f);
		first = newNode;
		// 原头结点是空，说明新的节点既是头也是尾
		if (f == null)
			last = newNode;
		else
			f.prev = newNode;
		size++;
		modCount++;
	}

	/**
	 * 在尾节点插入元素
	 */
	void linkLast(E e) {
		final Node<E> l = last;
		final Node<E> newNode = new Node<>(l, e, null);
		last = newNode;
		// 原尾结点是空，说明新的节点既是头也是尾
		if (l == null)
			first = newNode;
		else
			l.next = newNode;
		size++;
		modCount++;
	}

	/**
	 * 在元素succ前面插入元素e
	 */
	void linkBefore(E e, Node<E> succ) {
		// assert succ != null;
		final Node<E> pred = succ.prev;
		// 节点e的前一个是pred，后一个是succ
		final Node<E> newNode = new Node<>(pred, e, succ);
		// succ前一个变成为succ
		succ.prev = newNode;
		// succ原来的前一个节点为空，说明e就是头部
		if (pred == null)
			first = newNode;
		else
			pred.next = newNode;
		size++;
		modCount++;
	}

	/**
	 * 把头结点f删除
	 */
	private E unlinkFirst(Node<E> f) {
		// assert f == first && f != null;
		final E element = f.item;
		final Node<E> next = f.next;
		f.item = null;
		f.next = null; // help GC
		// f节点next作为头结点
		first = next;
		// f的next为空说明已经没有元素了
		if (next == null)
			last = null;
		else
			next.prev = null;
		size--;
		modCount++;
		return element;
	}

	/**
	 * 把尾部的节点l删除，思路和{#link {@link LinkedList#unlinkFirst(Node)}差不多
	 */
	private E unlinkLast(Node<E> l) {
		// assert l == last && l != null;
		final E element = l.item;
		final Node<E> prev = l.prev;
		l.item = null;
		l.prev = null; // help GC
		last = prev;
		if (prev == null)
			first = null;
		else
			prev.next = null;
		size--;
		modCount++;
		return element;
	}

	/**
	 * 删除链表中的x元素
	 */
	E unlink(Node<E> x) {
		// assert x != null;
		final E element = x.item;
		final Node<E> next = x.next;
		final Node<E> prev = x.prev;

		// x的pre为空，则x的next就是头节点
		if (prev == null) {
			first = next;
		}
		// x的pre不为空，x的pre节点的next=x的next
		else {
			prev.next = next;
			x.prev = null;
		}

		// x的next为空，说明x是尾节点
		if (next == null) {
			last = prev;
		}
		// next不为空，则x的next的pre就是x的pre
		else {
			next.prev = prev;
			x.next = null;
		}

		x.item = null;
		size--;
		modCount++;
		return element;
	}

	/**
	 * 获取头结点元素
	 *
	 * @throws NoSuchElementException 如果list为空，抛出此异常
	 */
	public E getFirst() {
		final Node<E> f = first;
		if (f == null)
			throw new NoSuchElementException();
		return f.item;
	}

	/**
	 * 获取尾结点元素
	 *
	 * @throws NoSuchElementException 如果list为空，抛出此异常
	 */
	public E getLast() {
		final Node<E> l = last;
		if (l == null)
			throw new NoSuchElementException();
		return l.item;
	}

	/**
	 * 删除链表的头结点
	 *
	 * @return 返回被删除的节点
	 * @throws NoSuchElementException 如果list为空，抛出此异常
	 */
	public E removeFirst() {
		final Node<E> f = first;
		if (f == null)
			throw new NoSuchElementException();
		return unlinkFirst(f);
	}

	/**
	 * 删除链表的尾结点删除
	 *
	 * @return 返回被删除的节点
	 * @throws NoSuchElementException 如果list为空，抛出此异常
	 */
	public E removeLast() {
		final Node<E> l = last;
		if (l == null)
			throw new NoSuchElementException();
		return unlinkLast(l);
	}

	/**
	 * 把元素e插入到头部
	 *
	 * @param e 需要插入的元素
	 */
	public void addFirst(E e) {
		linkFirst(e);
	}

	/**
	 * 把元素e插入到尾部
	 *
	 * <p>此方法相当于 {@link #add}.
	 *
	 * @param e 需要插入的元素
	 */
	public void addLast(E e) {
		linkLast(e);
	}

	/**
	 * 判断链表中是否包含指定元素，元素可以为null
	 *
	 * @param o 传入的元素
	 * @return 包含返回true，否则返回false
	 */
	public boolean contains(Object o) {
		return indexOf(o) != -1;
	}

	/**
	 * 返回链表中元素的个数
	 *
	 * @return 返回链表中元素的个数
	 */
	public int size() {
		return size;
	}

	/**
	 * 把元素e插入到尾部
	 *
	 * <p>此方法相当于 {@link #addLast}.
	 *
	 * @param e 元素
	 * @return 添加成功返回true
	 */
	public boolean add(E e) {
		linkLast(e);
		return true;
	}

	/**
	 * 移除元素o第一次出现的位置，o可以为null
	 *
	 * @param o 需要移除的元素o
	 * @return 找到并删除成功返回true，否则返回false
	 */
	public boolean remove(Object o) {
		if (o == null) {
			for (Node<E> x = first; x != null; x = x.next) {
				if (x.item == null) {
					unlink(x);
					return true;
				}
			}
		} else {
			for (Node<E> x = first; x != null; x = x.next) {
				if (o.equals(x.item)) {
					unlink(x);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 把集合c加入到链表的末端
	 *
	 * @param c 集合
	 * @return 集合c有元素且插入成功返回true，否则返回false
	 * @throws NullPointerException 集合c为null抛出此异常
	 */
	public boolean addAll(Collection<? extends E> c) {
		return addAll(size, c);
	}

	/**
	 * 从指定位置开始把集合加入到链表末端，index范围:[0,size]
	 *
	 * @param index 插入的位置
	 * @param c     集合
	 * @return 集合c有元素且插入成功返回true，否则返回false
	 * @throws IndexOutOfBoundsException index越界抛出此异常
	 * @throws NullPointerException      集合c为null抛出此异常
	 */
	public boolean addAll(int index, Collection<? extends E> c) {
		checkPositionIndex(index);

		// c集合转数组，这里感觉先取c.size判断拿到上面会好一些，如果没有元素没有必要new空数组了
		Object[] a = c.toArray();
		int numNew = a.length;
		if (numNew == 0)
			return false;

		// pred：要插入第一个元素的pre
		// succ：要插入最后一个元素的next
		Node<E> pred, succ;
		if (index == size) {
			succ = null;
			pred = last;
		} else {
			succ = node(index);
			pred = succ.prev;
		}

		for (Object o : a) {
			@SuppressWarnings("unchecked") E e = (E) o;
			Node<E> newNode = new Node<>(pred, e, null);
			// 当前节点没有pre，说明新的节点=头结点
			if (pred == null)
				first = newNode;
				// 当前节点的pre的next=当前节点
			else
				pred.next = newNode;
			// 后一个节点的pre = 前一个节点
			pred = newNode;
		}

		// 此处pred表示集合c的最后一个元素
		// 最后一个元素的next是空，则最后一个元素 = 尾节点
		if (succ == null) {
			last = pred;
		}
		// 否则c的最后一个元素的next=succ，succ的pre = 最后一个c的元素
		else {
			pred.next = succ;
			succ.prev = pred;
		}

		size += numNew;
		modCount++;
		return true;
	}

	/**
	 * 清空链表里面的所有元素
	 */
	public void clear() {
		// Clearing all of the links between nodes is "unnecessary", but:
		// - helps a generational GC if the discarded nodes inhabit
		//   more than one generation
		// - is sure to free memory even if there is a reachable Iterator
		// node的item、pre、next都赋为null
		for (Node<E> x = first; x != null; ) {
			Node<E> next = x.next;
			x.item = null;
			x.next = null;
			x.prev = null;
			x = next;
		}
		first = last = null;
		size = 0;
		modCount++;
	}


	// Positional Access Operations

	/**
	 * 获取指定索引的元素
	 *
	 * @param index 指定的索引
	 * @return 索引的元素
	 * @throws IndexOutOfBoundsException 索引越界抛出此异常
	 */
	public E get(int index) {
		// 越界校验
		checkElementIndex(index);
		return node(index).item;
	}

	/**
	 * 替换指定索引的元素
	 *
	 * @param index   要替换的索引
	 * @param element 替换的新元素
	 * @return 返回被替换的旧元素
	 * @throws IndexOutOfBoundsException 索引越界抛出此异常
	 */
	public E set(int index, E element) {
		// 越界校验
		checkElementIndex(index);
		// 获取指定索引的元素
		Node<E> x = node(index);
		// 替换item
		E oldVal = x.item;
		x.item = element;
		return oldVal;
	}

	/**
	 * 在index处插入元素element
	 *
	 * @param index   索引
	 * @param element 新元素
	 * @throws IndexOutOfBoundsException 可选位置越界抛出此异常
	 */
	public void add(int index, E element) {
		// 对可选位置校验
		checkPositionIndex(index);

		// 在最后位置插入
		if (index == size)
			linkLast(element);
		else
			linkBefore(element, node(index));
	}

	/**
	 * 删除指定索引的元素
	 *
	 * @param index 索引
	 * @return 返回被删除的元素
	 * @throws IndexOutOfBoundsException {@inheritDoc} 数组越界抛出此异常
	 */
	public E remove(int index) {
		checkElementIndex(index);
		return unlink(node(index));
	}

	/**
	 * 判断索引是否越界[0,size)
	 */
	private boolean isElementIndex(int index) {
		return index >= 0 && index < size;
	}

	/**
	 * 可选位置校验[0,size]
	 */
	private boolean isPositionIndex(int index) {
		return index >= 0 && index <= size;
	}

	/**
	 * 越界异常信息的构造方式
	 */
	private String outOfBoundsMsg(int index) {
		return "Index: " + index + ", Size: " + size;
	}

	/**
	 * 判断索引是否越界
	 */
	private void checkElementIndex(int index) {
		if (!isElementIndex(index))
			throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
	}

	/**
	 * 判断索引是否越界
	 */
	private void checkPositionIndex(int index) {
		if (!isPositionIndex(index))
			throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
	}

	/**
	 * 获取对应索引的元素，不做越界检查
	 */
	Node<E> node(int index) {
		// assert isElementIndex(index);

		// 做了优化，索引在前半部分从头遍历
		if (index < (size >> 1)) {
			Node<E> x = first;
			for (int i = 0; i < index; i++)
				x = x.next;
			return x;
		}
		// 索引在后半部分，从尾部开始遍历
		else {
			Node<E> x = last;
			for (int i = size - 1; i > index; i--)
				x = x.prev;
			return x;
		}
	}

	// Search Operations

	/**
	 * 从头部开始检索链表判断是否包含指定元素
	 *
	 * @param o 元素
	 * @return 如果包含返回指定元素的索引，否则返回-1
	 */
	public int indexOf(Object o) {
		int index = 0;
		// null元素
		if (o == null) {
			for (Node<E> x = first; x != null; x = x.next) {
				if (x.item == null)
					return index;
				index++;
			}
		}
		// 非null元素使用equals判断
		else {
			for (Node<E> x = first; x != null; x = x.next) {
				if (o.equals(x.item))
					return index;
				index++;
			}
		}
		return -1;
	}

	/**
	 * 元素o最后一次出现的位置 <br>
	 * 实现：从尾部开始查找
	 *
	 * @param o 指定元素
	 * @return 找到返回索引，否则返回-1
	 */
	public int lastIndexOf(Object o) {
		int index = size;
		if (o == null) {
			for (Node<E> x = last; x != null; x = x.prev) {
				index--;
				if (x.item == null)
					return index;
			}
		} else {
			for (Node<E> x = last; x != null; x = x.prev) {
				index--;
				if (o.equals(x.item))
					return index;
			}
		}
		return -1;
	}

	// Queue operations.

	/**
	 * 检索但不删除链表的头结点
	 *
	 * @return 返回链表的头结点，没有返回null
	 * @since 1.5
	 */
	public E peek() {
		final Node<E> f = first;
		return (f == null) ? null : f.item;
	}

	/**
	 * 检索但不删除头结点，同{@link LinkedList#getFirst()}
	 *
	 * @throws NoSuchElementException 如果链表为空，抛出此异常
	 * @since 1.5
	 */
	public E element() {
		return getFirst();
	}

	/**
	 * 检索同时删除头结点
	 *
	 * @return 链表为空，返回null，否则返回头结点
	 * @since 1.5
	 */
	public E poll() {
		final Node<E> f = first;
		return (f == null) ? null : unlinkFirst(f);
	}

	/**
	 * 检索同时删除头结点
	 *
	 * @return 头结点
	 * @throws NoSuchElementException 如果链表为空，抛出此异常
	 * @since 1.5
	 */
	public E remove() {
		return removeFirst();
	}

	/**
	 * 把元素e插入到尾部
	 *
	 * <p>此方法相当于 {@link #add}.
	 *
	 * @param e 元素
	 * @return 添加成功返回true
	 * @since 1.5
	 */
	public boolean offer(E e) {
		return add(e);
	}

	// Deque operations

	/**
	 * 把元素e插入到头部
	 * <p>此方法相当于 {@link #addFirst(Object)}.
	 *
	 * @param e 需要插入的元素
	 * @return 插入成功返回true
	 * @since 1.6
	 */
	public boolean offerFirst(E e) {
		addFirst(e);
		return true;
	}

	/**
	 * 把元素e插入到尾部
	 *
	 * <p>此方法相当于 {@link #add}.
	 *
	 * @param e 需要插入的元素
	 * @return 插入成功返回true
	 */
	public boolean offerLast(E e) {
		addLast(e);
		return true;
	}

	/**
	 * 检索但不删除链表的头结点，同{@link LinkedList#peek()}
	 *
	 * @return 返回链表的头结点，没有返回null
	 * @since 1.5
	 */
	public E peekFirst() {
		final Node<E> f = first;
		return (f == null) ? null : f.item;
	}

	/**
	 * 检索但不删除链表的尾结点
	 *
	 * @return 返回链表的尾结点，没有返回null
	 * @since 1.5
	 */
	public E peekLast() {
		final Node<E> l = last;
		return (l == null) ? null : l.item;
	}

	/**
	 * 检索同时删除头结点，同{@link LinkedList#poll()}
	 *
	 * @return 链表为空，返回null，否则返回头结点
	 * @since 1.6
	 */
	public E pollFirst() {
		final Node<E> f = first;
		return (f == null) ? null : unlinkFirst(f);
	}

	/**
	 * 检索同时删除尾结点
	 *
	 * @return 链表为空，返回null，否则返回尾结点
	 * @since 1.6
	 */
	public E pollLast() {
		final Node<E> l = last;
		return (l == null) ? null : unlinkLast(l);
	}

	/**
	 * 插入元素到头部 <br>
	 *
	 * <p>相当于 {@link #addFirst}
	 *
	 * @param e 所需的元素
	 * @since 1.6
	 */
	public void push(E e) {
		addFirst(e);
	}

	/**
	 * 出栈操作（把头部结点移除）
	 *
	 * <p>方法相当于 {@link #removeFirst()}.
	 *
	 * @return 返回被移除元素
	 * @throws NoSuchElementException 链表为空抛出此异常
	 * @since 1.6
	 */
	public E pop() {
		return removeFirst();
	}

	/**
	 * 移除元素第一次出现的位置<br>
	 * 方法相当于 {@link LinkedList#remove(Object)}
	 *
	 * @param o 需要移除的元素o
	 * @return 找到并删除成功返回true，否则返回false
	 * @since 1.6
	 */
	public boolean removeFirstOccurrence(Object o) {
		return remove(o);
	}

	/**
	 * 移除元素最后一次出现的位置<br>
	 * 实现：从尾部开始检索
	 *
	 * @param o 需要删除的元素
	 * @return 找到并删除成功返回true，否则返回false
	 * @since 1.6
	 */
	public boolean removeLastOccurrence(Object o) {
		if (o == null) {
			for (Node<E> x = last; x != null; x = x.prev) {
				if (x.item == null) {
					unlink(x);
					return true;
				}
			}
		} else {
			for (Node<E> x = last; x != null; x = x.prev) {
				if (o.equals(x.item)) {
					unlink(x);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 返回指定索引的List迭代器对象
	 *
	 * @param index 指定索引
	 * @return List迭代器对象
	 * @throws IndexOutOfBoundsException index超出[0,size]会抛出此异常
	 * @see List#listIterator(int)
	 */
	public ListIterator<E> listIterator(int index) {
		checkPositionIndex(index);
		return new ListItr(index);
	}

	/**
	 * list迭代器类，内部有并发修改校验
	 */
	private class ListItr implements ListIterator<E> {
		// 最后一次返回的节点
		private Node<E> lastReturned;
		// 下一个节点（可能是前也可能是后）
		private Node<E> next;
		// 下一个索引
		private int nextIndex;
		// 并发修改的参考值
		private int expectedModCount = modCount;

		ListItr(int index) {
			// assert isPositionIndex(index);
			// 如果index == size，则从定位最后一个节点，next = null
			next = (index == size) ? null : node(index);
			nextIndex = index;
		}

		public boolean hasNext() {
			return nextIndex < size;
		}

		public E next() {
			// 并发检查和越界检查
			checkForComodification();
			if (!hasNext())
				throw new NoSuchElementException();

			lastReturned = next;
			next = next.next;
			nextIndex++;
			return lastReturned.item;
		}

		public boolean hasPrevious() {
			return nextIndex > 0;
		}

		public E previous() {
			// 并发检查和越界检查
			checkForComodification();
			if (!hasPrevious())
				throw new NoSuchElementException();
			// next = null 说明索引在size处
			lastReturned = next = (next == null) ? last : next.prev;
			nextIndex--;
			return lastReturned.item;
		}

		public int nextIndex() {
			return nextIndex;
		}

		public int previousIndex() {
			return nextIndex - 1;
		}

		/**
		 * 移除最后一次返回的元素，该操作会把lastReturned = null
		 * 因此不能连续两次调用此方法
		 */
		public void remove() {
			// 并发检查
			checkForComodification();
			if (lastReturned == null)
				throw new IllegalStateException();

			Node<E> lastNext = lastReturned.next;
			unlink(lastReturned);
			// 上一次操作是previous时，next == lastReturned，所以需要指定next = lastNext
			if (next == lastReturned)
				next = lastNext;
			else
				nextIndex--;
			lastReturned = null;
			expectedModCount++;
		}

		/**
		 * 设置定节点的元素值
		 *
		 * @param e
		 */
		public void set(E e) {
			// 并发修改校验
			if (lastReturned == null)
				throw new IllegalStateException();
			checkForComodification();
			lastReturned.item = e;
		}

		/**
		 * 添加元素会把lastReturned = null
		 *
		 * @param e
		 */
		public void add(E e) {
			checkForComodification();
			lastReturned = null;
			if (next == null)
				linkLast(e);
			else
				linkBefore(e, next);
			nextIndex++;
			expectedModCount++;
		}

		/**
		 * 循环对元素做修改
		 *
		 * @param action
		 */
		public void forEachRemaining(Consumer<? super E> action) {
			Objects.requireNonNull(action);
			while (modCount == expectedModCount && nextIndex < size) {
				action.accept(next.item);
				lastReturned = next;
				next = next.next;
				nextIndex++;
			}
			checkForComodification();
		}

		final void checkForComodification() {
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
		}
	}

	/**
	 * 存储链表节点
	 */
	private static class Node<E> {
		E item;
		Node<E> next;
		Node<E> prev;

		Node(Node<E> prev, E element, Node<E> next) {
			this.item = element;
			this.next = next;
			this.prev = prev;
		}
	}

	/**
	 * 适配ListItr的降序迭代器，用得不多
	 *
	 * @since 1.6
	 */
	public Iterator<E> descendingIterator() {
		return new DescendingIterator();
	}

	/**
	 * 适配ListItr的降序迭代器类
	 */
	private class DescendingIterator implements Iterator<E> {
		private final ListItr itr = new ListItr(size());

		public boolean hasNext() {
			return itr.hasPrevious();
		}

		public E next() {
			return itr.previous();
		}

		public void remove() {
			itr.remove();
		}
	}

	/**
	 * 克隆方法
	 *
	 * @return 返回当前对象的一个副本
	 */
	@SuppressWarnings("unchecked")
	private LinkedList<E> superClone() {
		try {
			return (LinkedList<E>) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	/**
	 * 克隆方法
	 *
	 * @return 返回当前对象的一个副本
	 */
	public Object clone() {
		LinkedList<E> clone = superClone();

		// Put clone into "virgin" state
		clone.first = clone.last = null;
		clone.size = 0;
		clone.modCount = 0;

		// Initialize clone with our elements
		for (Node<E> x = first; x != null; x = x.next)
			clone.add(x.item);

		return clone;
	}

	/**
	 * 把链表转化为数组副本，并返回
	 *
	 * @return 新的数组副本
	 */
	public Object[] toArray() {
		Object[] result = new Object[size];
		int i = 0;
		for (Node<E> x = first; x != null; x = x.next)
			result[i++] = x.item;
		return result;
	}

	/**
	 * 把容器的元素复制到数组中，如果传入数组长度比容器大则在后面补null <br>
	 * <strong> 传入数组为null会空指针</strong> <br>
	 * <strong> 传入数组的类型和当前容器泛型不匹配会ArrayStoreException</strong> <br>
	 *
	 * @param a 需要填充的数组
	 * @return 返回一个包含集合元素的数组
	 * @throws ArrayStoreException  if the runtime type of the specified array
	 *                              is not a supertype of the runtime type of every element in
	 *                              this list
	 * @throws NullPointerException if the specified array is null
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		if (a.length < size)
			a = (T[]) java.lang.reflect.Array.newInstance(
					a.getClass().getComponentType(), size);
		int i = 0;
		Object[] result = a;
		for (Node<E> x = first; x != null; x = x.next)
			result[i++] = x.item;

		if (a.length > size)
			a[size] = null;

		return a;
	}

	private static final long serialVersionUID = 876323262645176354L;

	/**
	 * 对象序列化的方法不管
	 */
	private void writeObject(java.io.ObjectOutputStream s)
			throws java.io.IOException {
		// Write out any hidden serialization magic
		s.defaultWriteObject();

		// Write out size
		s.writeInt(size);

		// Write out all elements in the proper order.
		for (Node<E> x = first; x != null; x = x.next)
			s.writeObject(x.item);
	}

	/**
	 * 对象序反列化的方法不管
	 */
	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream s)
			throws java.io.IOException, ClassNotFoundException {
		// Read in any hidden serialization magic
		s.defaultReadObject();

		// Read in size
		int size = s.readInt();

		// Read in all elements in the proper order.
		for (int i = 0; i < size; i++)
			linkLast((E) s.readObject());
	}

	/**
	 * 创建一个可分割的迭代器，为了更好并行遍历而设计的
	 *
	 * @return a 返回一个拥有此集合的并行迭代器
	 * @implNote The {@code Spliterator} additionally reports {@link Spliterator#SUBSIZED}
	 * and implements {@code trySplit} to permit limited parallelism..
	 * @since 1.8
	 */
	@Override
	public Spliterator<E> spliterator() {
		return new LLSpliterator<E>(this, -1, 0);
	}

	/**
	 * A customized variant of Spliterators.IteratorSpliterator
	 */
	static final class LLSpliterator<E> implements Spliterator<E> {
		static final int BATCH_UNIT = 1 << 10;  // 批量数组大小的增量
		static final int MAX_BATCH = 1 << 25;  // max batch array size;
		final LinkedList<E> list; // 当前容器
		Node<E> current;      // 当前的元素，未初始化时为null
		int est;              // 容器总大小，初始值为-1
		int expectedModCount; // 期望修改的值，随着est一起初始化
		int batch;            // batch size for splits

		LLSpliterator(LinkedList<E> list, int est, int expectedModCount) {
			this.list = list;
			this.est = est;
			this.expectedModCount = expectedModCount;
		}

		/**
		 * 获取容器大小，若没有初始化，则初始化
		 *
		 * @return 容器的大小
		 */
		final int getEst() {
			int s; // force initialization
			final LinkedList<E> lst;
			if ((s = est) < 0) {
				if ((lst = list) == null)
					s = est = 0;
				else {
					expectedModCount = lst.modCount;
					current = lst.first;
					s = est = lst.size;
				}
			}
			return s;
		}

		/**
		 * 获取容器大小
		 *
		 * @return 容器的大小
		 */
		public long estimateSize() {
			return (long) getEst();
		}

		public Spliterator<E> trySplit() {
			Node<E> p;
			int s = getEst();
			if (s > 1 && (p = current) != null) {
				int n = batch + BATCH_UNIT;
				if (n > s)
					n = s;
				if (n > MAX_BATCH)
					n = MAX_BATCH;
				Object[] a = new Object[n];
				int j = 0;
				do {
					a[j++] = p.item;
				} while ((p = p.next) != null && j < n);
				current = p;
				batch = j;
				est = s - j;
				return Spliterators.spliterator(a, 0, j, Spliterator.ORDERED);
			}
			return null;
		}

		public void forEachRemaining(Consumer<? super E> action) {
			Node<E> p;
			int n;
			if (action == null) throw new NullPointerException();
			if ((n = getEst()) > 0 && (p = current) != null) {
				current = null;
				est = 0;
				do {
					E e = p.item;
					p = p.next;
					action.accept(e);
				} while (p != null && --n > 0);
			}
			if (list.modCount != expectedModCount)
				throw new ConcurrentModificationException();
		}

		public boolean tryAdvance(Consumer<? super E> action) {
			Node<E> p;
			if (action == null) throw new NullPointerException();
			if (getEst() > 0 && (p = current) != null) {
				--est;
				E e = p.item;
				current = p.next;
				action.accept(e);
				if (list.modCount != expectedModCount)
					throw new ConcurrentModificationException();
				return true;
			}
			return false;
		}

		public int characteristics() {
			return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
		}
	}

}

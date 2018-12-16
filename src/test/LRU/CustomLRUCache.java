package test.LRU;

/**
 * 自己写的LRU算法
 * 基本思想：基于链表实现，使用过的节点放到尾部，删除时从头开始删除，保证第一个一定是最少使用的
 */
public class CustomLRUCache<K, V> {

	/**
	 * 表示总容量
	 */
	private int size;

	/**
	 * 表示当前的元素数量
	 */
	private int currentLength;

	/**
	 * 表示头节点
	 */
	private Node head;

	/**
	 * 表示尾节点
	 */
	private Node tail;

	public CustomLRUCache(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("总容量必须大于0");
		}
		this.size = size;
	}

	// 把元素放到队尾
	public void put(K key, V value) {
		if (key == null) {
			throw new IllegalArgumentException("非法参数！");
		}
		// 如果已经存在key直接覆盖原来的
		Node n = getNode(key);
		if (n != null) {
			n.value = value;
		}
		// 不存在key，把新的节点放到队尾
		else {
			// 判断是否需要删除节点
			if (currentLength + 1 > size) {
				// 只有一个元素，直接移除
				if (currentLength == 1) {
					head = null;
					tail = null;
				} else {
					// 移除头节点
					Node next = head.next;
					head.next = null;
					next.prev = null;
					head = next;
				}
			} else {
				currentLength++;
			}
			putNode(key, value);
		}
	}

	public V get(K key) {
		Node node = getNode(key);
		if (node == null) {
			return null;
		}
		// 每次获取元素都把元素移动到队尾
		if (currentLength != 1 && node != tail) {
			Node nodePrev = node.prev;
			// 原来位置调整
			Node nodeNext = node.next;
			nodeNext.prev = nodePrev;
			if (nodePrev != null) {
				nodePrev.next = nodeNext;
			} else {
				head = nodeNext;
			}

			// 元素移动队尾
			node.next = null;
			Node t = tail;
			t.next = node;
			node.prev = t;
			tail = node;
		}
		return node.value;
	}

	/**
	 * 遍历链表找到对应的node
	 */
	private Node getNode(K key) {
		if (head == null) {
			return null;
		}
		// 遍历链表找到对应的key
		for (Node h = head; h != null; h = h.next) {
			if (h.key == key || h.key.equals(key)) {
				return h;
			}
		}
		return null;
	}

	/**
	 * 把元素放到队尾
	 */
	private void putNode(K key, V value) {
		Node node = new Node(key, value, tail, null);
		if (head == null) {
			head = node;
		} else {
			Node t = tail;
			t.next = node;
		}
		tail = node;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		// 遍历链表找到对应的key
		for (Node h = head; h != null; h = h.next) {
			builder.append(h.key).append("：").append(h.value).append("\n");
		}
		return builder.toString();
	}

	private class Node {

		Node prev;

		Node next;

		K key;

		V value;

		public Node(K key, V value, Node prev, Node next) {
			this.key = key;
			this.value = value;
			this.prev = prev;
			this.next = next;
		}
	}
}

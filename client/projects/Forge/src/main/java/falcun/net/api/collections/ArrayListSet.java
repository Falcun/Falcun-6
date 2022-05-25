package falcun.net.api.collections;

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ArrayListSet<V> extends ObjectLinkedOpenHashSet<V> {
	final ArrayList<V> arrayList;

	public ArrayListSet(Collection<? extends V> collection) {
		this(collection.size(), 0.75F);
		this.arrayList.addAll(collection);
		this.addAll(collection);
	}

	public ArrayListSet(int expected, float f) {
		super(expected, f);
		this.arrayList = new ArrayList(expected);
	}

	public ArrayListSet(int expected) {
		this(expected, 0.75F);
	}

	public ArrayListSet() {
		this(16, 0.75F);
	}

	public boolean add(V v) {
		if (super.add(v)) {
			this.arrayList.add(v);
			return true;
		} else {
			return false;
		}
	}

	public boolean remove(Object k) {
		if (super.remove(k)) {
			this.arrayList.remove(k);
			return true;
		} else {
			return false;
		}
	}

	public boolean removeAll(Collection<?> c) {
		this.arrayList.removeAll(c);
		return super.removeAll(c);
	}

	public void clear() {
		this.arrayList.clear();
		super.clear();
	}

	public V arrayObject(int index) {
		return this.arrayList.get(index);
	}

	public int arraySize() {
		return this.arrayList.size();
	}

	public List<V> toList() {
		return this.arrayList;
	}

	public ObjectListIterator<V> arrayIterator() {
		return new Itr(-1);
	}

	public Iterator<V> descendingIterator() {
		return new DescendingIterator();
	}

	private class DescendingIterator implements Iterator<V> {
		private final ArrayListSet<V>.Itr itr = ArrayListSet.this.new Itr(ArrayListSet.this.arraySize());

		public DescendingIterator() {
		}

		public boolean hasNext() {
			return this.itr.hasPrevious();
		}

		public V next() {
			return this.itr.previous();
		}
	}

	private class Itr implements ObjectListIterator<V> {
		protected int index;

		public Itr(int startingIndex) {
			this.index = startingIndex;
		}

		public V previous() {
			return ArrayListSet.this.arrayList.get(--this.index);
		}

		public int nextIndex() {
			return this.index + 1;
		}

		public int previousIndex() {
			return this.index - 1;
		}

		public boolean hasPrevious() {
			return this.index - 1 >= 0;
		}

		public boolean hasNext() {
			return this.index + 1 < ArrayListSet.this.arrayList.size();
		}

		public V next() {
			return ArrayListSet.this.arrayList.get(++this.index);
		}

		public int back(int n) {
			return this.index += n;
		}

		public int skip(int n) {
			return this.index -= n;
		}

		public void remove() {
			ArrayListSet.this.remove(ArrayListSet.this.arrayList.get(this.index));
		}

		public void set(V v) {
			throw new UnsupportedOperationException();
		}

		public void add(V v) {
			throw new UnsupportedOperationException();
		}
	}
}

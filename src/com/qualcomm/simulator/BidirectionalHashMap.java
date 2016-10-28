package com.qualcomm.simulator;

import java.util.HashMap;
import java.util.Map;

public class BidirectionalHashMap<K, V> extends HashMap<K, V> {

	private static final long serialVersionUID = 6269546023006024415L;

	private final HashMap<V, K> reverse;

	public BidirectionalHashMap() {
		super();
		this.reverse = new HashMap<>();
	}

	public BidirectionalHashMap(final int initialCapacity, final float loadFactor) {
		super(initialCapacity, loadFactor);
		this.reverse = new HashMap<>(initialCapacity, loadFactor);
	}

	public BidirectionalHashMap(final int initialCapacity) {
		super(initialCapacity);
		this.reverse = new HashMap<>(initialCapacity);
	}

	public BidirectionalHashMap(final Map<? extends K, ? extends V> m) {
		super(m);
		this.reverse = new HashMap<>();
		m.forEach((k, v) -> reverse.put(v, k));
	}

	public K getKey(final Object value) {
		return reverse.get(value);
	}

	@Override
	public boolean containsKey(final Object key) {
		return super.containsKey(key);
	}

	@Override
	public boolean containsValue(final Object value) {
		return reverse.containsKey(value);
	}

	@Override
	public V put(final K key, final V value) {
		reverse.put(value, key);
		return super.put(key, value);
	}

	@Override
	public V remove(final Object key) {
		final V ret = super.remove(key);
		reverse.remove(ret);
		return ret;
	}

	@Override
	public void clear() {
		super.clear();
		reverse.clear();
	}

}

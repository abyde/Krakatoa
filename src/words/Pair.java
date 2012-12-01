package words;

import java.io.Serializable;
import java.util.Map;

/**
 * Basic construct consisting of two typed objects.
 * 
 * @author abyde
 */
public class Pair<A, B> implements Map.Entry<A, B>, Serializable {
	private static final long serialVersionUID = 1L;
	protected A a;
	protected B b;

	/** Public no-arg constructor so we can serialize with MsgPack. */
	public Pair() {
	}

	public Pair(A a, B b) {
		this.a = a;
		this.b = b;
	}

	public Pair<A, B> copy() {
		return new Pair<A, B>(a, b);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Pair other = (Pair) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		return true;
	}

	@Override
	public A getKey() {
		return a;
	}

	@Override
	public B getValue() {
		return b;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		return result;
	}

	public void setFst(A _a) {
		a = _a;
	}

	@Override
	public B setValue(B value) {
		final B old = this.b;
		this.b = value;
		return old;
	}

	@Override
	public String toString() {
		return "(" + a + ", " + b + ")";
	}
}

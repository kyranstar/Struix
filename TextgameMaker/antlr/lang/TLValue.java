package lang;

import java.math.BigDecimal;

import lang.exceptions.TLValueInstantiationException;

public class TLValue implements Comparable<TLValue> {

	public static final TLValue NULL = new TLValue();
	public static final TLValue VOID = new TLValue();

	private final Object value;

	private TLValue() {
		// private constructor: only used for NULL, VOID and NO_VALUE_FOUND
		value = new Object();
	}
	public TLValue(Boolean v){
		if (v == null) { throw new TLValueInstantiationException("can't instantiate value as null"); }
		value = v;
	}
	public TLValue(BigDecimal v){
		if (v == null) { throw new TLValueInstantiationException("can't instantiate value as null"); }
		value = v;
	}
	public TLValue(String v){
		if (v == null) { throw new TLValueInstantiationException("can't instantiate value as null"); }
		value = v;
	}

	public Boolean asBoolean() {
		if (!(value instanceof Boolean)) { throw new IllegalArgumentException("Type is not boolean, is " + getType()); }
		return (Boolean) value;
	}

	public BigDecimal asNumber() {
		if (!(value instanceof BigDecimal)) { throw new IllegalArgumentException("Type is not number, is " + getType()); }
		return (BigDecimal) value;
	}

	public String asString() {
		if (!(value instanceof String)) { throw new IllegalArgumentException("Type is not String, is " + getType()); }
		return (String) value;
	}

	@Override
	public int compareTo(TLValue that) {
		if (isNumber() && that.isNumber()) {
			if (equals(that)) {
				return 0;
			} else {
				return ((BigDecimal) value).compareTo((BigDecimal) that.value);
			}
		} else if (isString() && that.isString()) {
			return asString().compareTo(that.asString());
		} else {
			throw new IllegalArgumentException("illegal expression: can't compare `" + this + "` to `" + that + "`");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == VOID || o == VOID) { throw new IllegalArgumentException("can't use VOID: " + this + " ==/!= " + o); }
		if (this == o) { return true; }
		if (o == null || this.getClass() != o.getClass()) { return false; }
		final TLValue that = (TLValue) o;
		if (isNumber() && that.isNumber()) {
			return asNumber().compareTo(that.asNumber()) == 0;
		} else {
			return value.equals(that.value);
		}
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	public boolean isBoolean() {
		return value instanceof Boolean;
	}

	public boolean isNumber() {
		return value instanceof BigDecimal;
	}
	public boolean isNull() {
		return this == NULL;
	}

	public boolean isVoid() {
		return this == VOID;
	}

	public boolean isString() {
		return value instanceof String;
	}

	@Override
	public String toString() {
		if (isNull()) { return "NULL"; }
		if (isVoid()) { return "VOID"; }
		if (isString()) { return value.toString(); }
		if (isNumber()) {
			return asNumber().stripTrailingZeros().toPlainString();
		} else {
			return value.toString();
		}
	}

	public String getType() {
		if (isBoolean()) { return "Boolean"; }
		if (isNumber()) { return "Number"; }
		if (isString()) { return "String"; }
		if (isNull()) { return "Null"; }
		if (isVoid()) { return "Void"; }
		throw new IllegalArgumentException("Unknown Type");
	}
}
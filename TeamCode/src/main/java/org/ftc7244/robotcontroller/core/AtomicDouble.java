package org.ftc7244.robotcontroller.core;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;

import static java.lang.Double.doubleToRawLongBits;
import static java.lang.Double.longBitsToDouble;

/**
 * Created by OOTB on 12/12/2016.
 */

public class AtomicDouble extends Number {


    private transient volatile long value;

    private static final AtomicLongFieldUpdater<AtomicDouble> updater = AtomicLongFieldUpdater.newUpdater(AtomicDouble.class, "value");

    public AtomicDouble(double initialValue) {
        value = doubleToRawLongBits(initialValue);
    }

    public AtomicDouble() {
        // assert doubleToRawLongBits(0.0) == 0L;
    }

    public final double get() {
        return longBitsToDouble(value);
    }

    public final void set(double newValue) {
        long next = doubleToRawLongBits(newValue);
        value = next;
    }

    public final void lazySet(double newValue) {
        long next = doubleToRawLongBits(newValue);
        updater.lazySet(this, next);
    }

    public final double getAndSet(double newValue) {
        long next = doubleToRawLongBits(newValue);
        return longBitsToDouble(updater.getAndSet(this, next));
    }

    public final boolean compareAndSet(double expect, double update) {
        return updater.compareAndSet(this,
                doubleToRawLongBits(expect),
                doubleToRawLongBits(update));
    }

    public final boolean weakCompareAndSet(double expect, double update) {
        return updater.weakCompareAndSet(this,
                doubleToRawLongBits(expect),
                doubleToRawLongBits(update));
    }

    public final double getAndAdd(double delta) {
        while (true) {
            long current = value;
            double currentVal = longBitsToDouble(current);
            double nextVal = currentVal + delta;
            long next = doubleToRawLongBits(nextVal);
            if (updater.compareAndSet(this, current, next)) {
                return currentVal;
            }
        }
    }

    public final double addAndGet(double delta) {
        while (true) {
            long current = value;
            double currentVal = longBitsToDouble(current);
            double nextVal = currentVal + delta;
            long next = doubleToRawLongBits(nextVal);
            if (updater.compareAndSet(this, current, next)) {
                return nextVal;
            }
        }
    }

    public String toString() {
        return Double.toString(get());
    }

    public int intValue() {
        return (int) get();
    }

    public long longValue() {
        return (long) get();
    }

    public float floatValue() {
        return (float) get();
    }

    public double doubleValue() {
        return get();
    }
}

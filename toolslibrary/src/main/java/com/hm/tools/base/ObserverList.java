package com.hm.tools.base;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ObserverList<E>
        implements Iterable<E>
{
    private final List<E> mObservers = new ArrayList();
    private int mIterationDepth = 0;
    private int mCount = 0;
    private boolean mNeedsCompact = false;

    public boolean addObserver(E obs)
    {
        if ((obs == null) || (this.mObservers.contains(obs))) {
            return false;
        }

        boolean result = this.mObservers.add(obs);
        assert (result);

        this.mCount += 1;
        return true;
    }

    public boolean removeObserver(E obs)
    {
        if (obs == null) {
            return false;
        }

        int index = this.mObservers.indexOf(obs);
        if (index == -1) {
            return false;
        }

        if (this.mIterationDepth == 0)
        {
            this.mObservers.remove(index);
        } else {
            this.mNeedsCompact = true;
            this.mObservers.set(index, null);
        }
        this.mCount -= 1;
        assert (this.mCount >= 0);

        return true;
    }

    public boolean hasObserver(E obs) {
        return this.mObservers.contains(obs);
    }

    public void clear() {
        this.mCount = 0;

        if (this.mIterationDepth == 0) {
            this.mObservers.clear();
            return;
        }

        int size = this.mObservers.size();
        this.mNeedsCompact |= size != 0;
        for (int i = 0; i < size; i++)
            this.mObservers.set(i, null);
    }

    public Iterator<E> iterator()
    {
        return new ObserverListIterator();
    }

    public RewindableIterator<E> rewindableIterator()
    {
        return new ObserverListIterator();
    }

    public int size()
    {
        return this.mCount;
    }

    public boolean isEmpty()
    {
        return this.mCount == 0;
    }

    private void compact()
    {
        assert (this.mIterationDepth == 0);
        for (int i = this.mObservers.size() - 1; i >= 0; i--)
            if (this.mObservers.get(i) == null)
                this.mObservers.remove(i);
    }

    private void incrementIterationDepth()
    {
        this.mIterationDepth += 1;
    }

    private void decrementIterationDepthAndCompactIfNeeded() {
        this.mIterationDepth -= 1;
        assert (this.mIterationDepth >= 0);
        if (this.mIterationDepth > 0) return;
        if (!this.mNeedsCompact) return;
        this.mNeedsCompact = false;
        compact();
    }

    private int capacity()
    {
        return this.mObservers.size();
    }

    private E getObserverAt(int index) {
        return this.mObservers.get(index);
    }

    private class ObserverListIterator
            implements ObserverList.RewindableIterator<E>
    {
        private int mListEndMarker;
        private int mIndex = 0;
        private boolean mIsExhausted = false;

        private ObserverListIterator() {
            ObserverList.this.incrementIterationDepth();
            this.mListEndMarker = ObserverList.this.capacity();
        }

        public void rewind()
        {
            compactListIfNeeded();
            ObserverList.this.incrementIterationDepth();
            this.mListEndMarker = ObserverList.this.capacity();
            this.mIsExhausted = false;
            this.mIndex = 0;
        }

        public boolean hasNext()
        {
            int lookupIndex = this.mIndex;

            while ((lookupIndex < this.mListEndMarker) && (ObserverList.this.getObserverAt(lookupIndex) == null)) {
                lookupIndex++;
            }
            if (lookupIndex < this.mListEndMarker) return true;

            compactListIfNeeded();
            return false;
        }

        public E next()
        {
            while ((this.mIndex < this.mListEndMarker) && (ObserverList.this.getObserverAt(this.mIndex) == null)) {
                this.mIndex += 1;
            }
            if (this.mIndex < this.mListEndMarker) return ObserverList.this.getObserverAt(this.mIndex++);

            compactListIfNeeded();
            throw new NoSuchElementException();
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        private void compactListIfNeeded() {
            if (!this.mIsExhausted) {
                this.mIsExhausted = true;
                ObserverList.this.decrementIterationDepthAndCompactIfNeeded();
            }
        }
    }

    public static abstract interface RewindableIterator<E> extends Iterator<E>
    {
        public abstract void rewind();
    }
}
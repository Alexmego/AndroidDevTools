package com.hm.tools.base;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

public final class CollectionUtil {
    @SafeVarargs
    public static <E> HashSet<E> newHashSet(E... elements) {
        HashSet set = new HashSet(elements.length);
        Collections.addAll(set, elements);
        return set;
    }

    @SafeVarargs
    public static <E> ArrayList<E> newArrayList(E... elements) {
        ArrayList list = new ArrayList(elements.length);
        Collections.addAll(list, elements);
        return list;
    }

    public static <E> ArrayList<E> newArrayList(Iterable<E> iterable) {
        ArrayList list = new ArrayList();
        for (Iterator i$ = iterable.iterator(); i$.hasNext(); ) {
            Object element = i$.next();
            list.add(element);
        }
        return list;
    }
}
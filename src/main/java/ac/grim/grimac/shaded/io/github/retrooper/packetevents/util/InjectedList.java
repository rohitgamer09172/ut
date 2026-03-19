/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.io.github.retrooper.packetevents.util;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;

public class InjectedList<E>
implements List<E> {
    private final List<E> originalList;
    private final Consumer<E> pushBackAction;

    public InjectedList(List<E> originalList, Consumer<E> pushBackAction) {
        for (E key : originalList) {
            pushBackAction.accept(key);
        }
        this.originalList = originalList;
        this.pushBackAction = pushBackAction;
    }

    public List<E> originalList() {
        return this.originalList;
    }

    public Consumer<E> pushBackAction() {
        return this.pushBackAction;
    }

    @Override
    public synchronized boolean add(E e) {
        this.pushBackAction.accept(e);
        return this.originalList.add(e);
    }

    @Override
    public synchronized boolean addAll(@NotNull Collection<? extends E> c) {
        for (E element : c) {
            this.pushBackAction.accept(element);
        }
        return this.originalList.addAll(c);
    }

    @Override
    public synchronized boolean addAll(int index, @NotNull Collection<? extends E> c) {
        for (E element : c) {
            this.pushBackAction.accept(element);
        }
        return this.originalList.addAll(index, c);
    }

    @Override
    public synchronized void add(int index, E element) {
        this.pushBackAction.accept(element);
        this.originalList.add(index, element);
    }

    @Override
    public synchronized int size() {
        return this.originalList.size();
    }

    @Override
    public synchronized boolean isEmpty() {
        return this.originalList.isEmpty();
    }

    @Override
    public synchronized boolean contains(Object o) {
        return this.originalList.contains(o);
    }

    @Override
    @NotNull
    public synchronized Iterator<E> iterator() {
        return this.originalList.iterator();
    }

    @Override
    @NotNull
    public synchronized Object[] toArray() {
        return this.originalList.toArray();
    }

    @Override
    @NotNull
    public synchronized <T> T[] toArray(@NotNull T[] a) {
        return this.originalList.toArray(a);
    }

    @Override
    public synchronized boolean remove(Object o) {
        return this.originalList.remove(o);
    }

    @Override
    public synchronized boolean containsAll(@NotNull Collection<?> c) {
        return this.originalList.containsAll(c);
    }

    @Override
    public synchronized boolean removeAll(@NotNull Collection<?> c) {
        return this.originalList.removeAll(c);
    }

    @Override
    public synchronized boolean retainAll(@NotNull Collection<?> c) {
        return this.originalList.retainAll(c);
    }

    @Override
    public synchronized void clear() {
        this.originalList.clear();
    }

    @Override
    public synchronized E get(int index) {
        return this.originalList.get(index);
    }

    @Override
    public synchronized E set(int index, E element) {
        return this.originalList.set(index, element);
    }

    @Override
    public synchronized E remove(int index) {
        return this.originalList.remove(index);
    }

    @Override
    public synchronized int indexOf(Object o) {
        return this.originalList.indexOf(o);
    }

    @Override
    public synchronized int lastIndexOf(Object o) {
        return this.originalList.lastIndexOf(o);
    }

    @Override
    @NotNull
    public synchronized ListIterator<E> listIterator() {
        return this.originalList.listIterator();
    }

    @Override
    @NotNull
    public synchronized ListIterator<E> listIterator(int index) {
        return this.originalList.listIterator(index);
    }

    @Override
    @NotNull
    public synchronized List<E> subList(int fromIndex, int toIndex) {
        return this.originalList.subList(fromIndex, toIndex);
    }
}


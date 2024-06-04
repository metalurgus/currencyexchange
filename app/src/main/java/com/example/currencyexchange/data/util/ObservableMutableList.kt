package com.example.currencyexchange.data.util

interface ListObserver {
    fun onListChanged()
}

class ObservableMutableList<T> : MutableList<T> {
    private val list: MutableList<T> = mutableListOf()
    private val observers: MutableList<ListObserver> = mutableListOf()

    fun addObserver(observer: ListObserver) {
        observers.add(observer)
    }

    fun removeObserver(observer: ListObserver) {
        observers.remove(observer)
    }

    override val size: Int
        get() = list.size

    override fun contains(element: T): Boolean = list.contains(element)

    override fun containsAll(elements: Collection<T>): Boolean = list.containsAll(elements)

    override fun get(index: Int): T = list[index]

    override fun indexOf(element: T): Int = list.indexOf(element)

    override fun isEmpty(): Boolean = list.isEmpty()

    override fun iterator(): MutableIterator<T> = list.iterator()

    override fun lastIndexOf(element: T): Int = list.lastIndexOf(element)

    override fun add(element: T): Boolean {
        val added = list.add(element)
        if (added) {
            observers.forEach { it.onListChanged() }
        }
        return added
    }

    override fun add(index: Int, element: T) {
        list.add(index, element)
        observers.forEach { it.onListChanged() }
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val added = list.addAll(index, elements)
        if (added) {
            observers.forEach { it.onListChanged() }
        }
        return added
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val added = list.addAll(elements)
        if (added) {
            observers.forEach { it.onListChanged() }
        }
        return added
    }

    override fun clear() {
        list.clear()
        observers.forEach { it.onListChanged() }
    }

    override fun listIterator(): MutableListIterator<T> = list.listIterator()

    override fun listIterator(index: Int): MutableListIterator<T> = list.listIterator(index)

    override fun remove(element: T): Boolean {
        val removed = list.remove(element)
        if (removed) {
            observers.forEach { it.onListChanged() }
        }
        return removed
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        val removed = list.removeAll(elements)
        if (removed) {
            observers.forEach { it.onListChanged() }
        }
        return removed
    }

    override fun removeAt(index: Int): T {
        val item = list.removeAt(index)
        observers.forEach { it.onListChanged() }
        return item
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        val retained = list.retainAll(elements)
        if (retained) {
            observers.forEach { it.onListChanged() }
        }
        return retained
    }

    override fun set(index: Int, element: T): T {
        val oldItem = list.set(index, element)
        observers.forEach { it.onListChanged() }
        return oldItem
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> = list.subList(fromIndex, toIndex)
}
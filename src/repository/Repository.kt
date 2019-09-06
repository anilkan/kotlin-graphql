package xyz.anilkan.kotlin.repository

interface Repository<D> {
    //fun add(element: D): Long
    //fun getAll(): Sequence<D>
    //fun remove(indexer: Long): Long
    //fun replace(indexer: Long, element: D): Long
    fun getElement(indexer: Int): D
    //fun filter(predicate: SqlExpressionBuilder.() -> Op<Boolean>): Sequence<D>
}
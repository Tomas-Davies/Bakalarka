package com.tomdev.logopadix.dataLayer.repositories


interface IRepository<TItem> {

    suspend fun loadData(): List<TItem>{
        throw NotImplementedError()
    }
}
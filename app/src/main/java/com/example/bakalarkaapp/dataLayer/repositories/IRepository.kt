package com.example.bakalarkaapp.dataLayer.repositories

interface IRepository<T> {
    val data: List<T>
}
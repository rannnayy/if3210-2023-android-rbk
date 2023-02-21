package com.example.majika.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CartViewModelFactory (var cartDatabase: CartDatabase): ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(modelClass:Class<T>): T {
        return CartViewModel(cartDatabase) as T
    }
}
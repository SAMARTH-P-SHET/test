package com.jewellerycalculator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jewellerycalculator.model.Bill
import com.jewellerycalculator.model.ExchangeItem
import com.jewellerycalculator.model.JewelleryItem
import com.jewellerycalculator.model.MetalType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BillViewModel : ViewModel() {
    private val _bill = MutableStateFlow(Bill())
    val bill: StateFlow<Bill> = _bill.asStateFlow()
    
    private val _goldRate = MutableStateFlow(0.0)
    val goldRate: StateFlow<Double> = _goldRate.asStateFlow()
    
    private val _silverRate = MutableStateFlow(0.0)
    val silverRate: StateFlow<Double> = _silverRate.asStateFlow()
    
    fun setRates(goldRate: Double, silverRate: Double) {
        viewModelScope.launch {
            _goldRate.value = goldRate
            _silverRate.value = silverRate
            _bill.value = _bill.value.copy(
                goldRate = goldRate,
                silverRate = silverRate
            )
        }
    }
    
    fun addItem(item: JewelleryItem) {
        viewModelScope.launch {
            val currentItems = _bill.value.items.toMutableList()
            currentItems.add(item)
            _bill.value = _bill.value.copy(items = currentItems)
        }
    }
    
    fun removeItem(item: JewelleryItem) {
        viewModelScope.launch {
            val currentItems = _bill.value.items.toMutableList()
            currentItems.remove(item)
            _bill.value = _bill.value.copy(items = currentItems)
        }
    }
    
    fun addExchangeItem(item: ExchangeItem) {
        viewModelScope.launch {
            val current = _bill.value.exchangeItems.toMutableList()
            current.add(item)
            _bill.value = _bill.value.copy(exchangeItems = current)
        }
    }

    fun removeExchangeItem(item: ExchangeItem) {
        viewModelScope.launch {
            val current = _bill.value.exchangeItems.toMutableList()
            current.remove(item)
            _bill.value = _bill.value.copy(exchangeItems = current)
        }
    }
    
    fun clearBill() {
        viewModelScope.launch {
            _bill.value = Bill(
                goldRate = _goldRate.value,
                silverRate = _silverRate.value
            )
        }
    }
}
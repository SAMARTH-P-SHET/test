package com.jewellerycalculator.model

data class Bill(
    val items: List<JewelleryItem> = emptyList(),
    val goldRate: Double = 0.0,
    val silverRate: Double = 0.0,
    val exchangeItems: List<ExchangeItem> = emptyList() // new
) {
    fun getSilverItems(): List<JewelleryItem> = items.filter { it.metalType == MetalType.SILVER }
    
    fun getGoldItems(): List<JewelleryItem> = items.filter { it.metalType == MetalType.GOLD }
    
    fun calculateSilverTotal(): Double = getSilverItems().sumOf { it.calculateFinalPrice(silverRate) }
    
    fun calculateGoldTotal(): Double = getGoldItems().sumOf { it.calculateFinalPrice(goldRate) }
    
    fun calculateGrandTotal(): Double = calculateSilverTotal() + calculateGoldTotal()

    fun calculateExchangeDeduction(): Double = exchangeItems.sumOf { it.calculateValue() }
    fun calculateAdjustedGrandTotal(): Double = calculateGrandTotal() - calculateExchangeDeduction()
}

data class ExchangeItem(
    val metalType: MetalType,
    val weight: Double,
    val wastage: Double,
    val purity: Double,
    val gramRate: Double
) {
    fun netGrams(): Double = (weight - wastage) * purity
    fun calculateValue(): Double = netGrams() * gramRate
}
package com.jewellerycalculator.model

data class JewelleryItem(
    val id: String = "",
    val name: String = "",
    val weight: Double = 0.0,
    val quantity: Int = 1,
    val makingCharge: Double = 0.0,
    val metalType: MetalType = MetalType.SILVER
) {
    fun calculateTotalWeight(): Double = weight * quantity
    
    fun calculateWastage(): Double = calculateTotalWeight() * 0.1
    
    fun calculateFinalPrice(metalRate: Double): Double {
        return (calculateTotalWeight() + calculateWastage()) * metalRate + makingCharge
    }
}

enum class MetalType {
    GOLD, SILVER
}
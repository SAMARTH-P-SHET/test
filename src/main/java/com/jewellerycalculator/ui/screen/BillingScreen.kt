package com.jewellerycalculator.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jewellerycalculator.model.Bill
import com.jewellerycalculator.model.JewelleryItem
import com.jewellerycalculator.model.MetalType
import com.jewellerycalculator.model.ExchangeItem
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillingScreen(
    bill: Bill,
    onAddItem: (JewelleryItem) -> Unit,
    onRemoveItem: (JewelleryItem) -> Unit,
    onGenerateBill: () -> Unit
) {
    val decimalFormat = DecimalFormat("#,##0.00")
    var showAddDialog by remember { mutableStateOf(false) }
    var showAddExchangeDialog by remember { mutableStateOf(false) }
    var selectedMetalType by remember { mutableStateOf(MetalType.SILVER) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Billing Screen",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Rates Display
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Gold: ₹${decimalFormat.format(bill.goldRate)}/g",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Silver: ₹${decimalFormat.format(bill.silverRate)}/g",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Add Item Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    selectedMetalType = MetalType.GOLD
                    showAddDialog = true
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Gold Item")
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Button(
                onClick = {
                    selectedMetalType = MetalType.SILVER
                    showAddDialog = true
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Silver Item")
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Button(
                onClick = { showAddExchangeDialog = true },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Exchange Item")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Items List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(bill.items) { item ->
                ItemCard(
                    item = item,
                    metalRate = if (item.metalType == MetalType.GOLD) bill.goldRate else bill.silverRate,
                    onRemove = { onRemoveItem(item) }
                )
            }
            items(bill.exchangeItems) { item ->
                ExchangeItemCard(item = item, onRemove = { /* call removeExchangeItem */ })
            }
        }
        
        // Totals
        TotalsCard(bill = bill)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Generate Bill Button
        Button(
            onClick = onGenerateBill,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = bill.items.isNotEmpty()
        ) {
            Text(
                text = "Generate Bill",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
    
    // Add Item Dialog
    if (showAddDialog) {
        AddItemDialog(
            metalType = selectedMetalType,
            onDismiss = { showAddDialog = false },
            onAddItem = { item ->
                onAddItem(item)
                showAddDialog = false
            }
        )
    }
    
    // Add Exchange Item Dialog
    if (showAddExchangeDialog) {
        AddExchangeItemDialog(
            onDismiss = { showAddExchangeDialog = false },
            onAddItem = { item ->
                // call viewModel.addExchangeItem(item)
                showAddExchangeDialog = false
            }
        )
    }
}

@Composable
fun ItemCard(
    item: JewelleryItem,
    metalRate: Double,
    onRemove: () -> Unit
) {
    val decimalFormat = DecimalFormat("#,##0.00")
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${item.name} (${item.metalType.name})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${item.weight}g × ${item.quantity} = ${decimalFormat.format(item.calculateTotalWeight())}g",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Making: ₹${decimalFormat.format(item.makingCharge)}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "₹${decimalFormat.format(item.calculateFinalPrice(metalRate))}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove")
                }
            }
        }
    }
}

@Composable
fun ExchangeItemCard(item: ExchangeItem, onRemove: () -> Unit) {
    val decimalFormat = DecimalFormat("#,##0.00")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Exchange (${item.metalType.name})",
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Net: ${decimalFormat.format(item.netGrams())}g × ₹${decimalFormat.format(item.gramRate)}"
                )
                Text(
                    text = "Value: ₹${decimalFormat.format(item.calculateValue())}"
                )
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Remove")
            }
        }
    }
}

@Composable
fun TotalsCard(bill: Bill) {
    val decimalFormat = DecimalFormat("#,##0.00")
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Totals",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Silver Total:")
                Text(
                    text = "₹${decimalFormat.format(bill.calculateSilverTotal())}",
                    fontWeight = FontWeight.Medium
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Gold Total:")
                Text(
                    text = "₹${decimalFormat.format(bill.calculateGoldTotal())}",
                    fontWeight = FontWeight.Medium
                )
            }
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Grand Total:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "₹${decimalFormat.format(bill.calculateGrandTotal())}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
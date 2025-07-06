package com.jewellerycalculator.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.jewellerycalculator.model.JewelleryItem
import com.jewellerycalculator.model.MetalType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemDialog(
    metalType: MetalType,
    onDismiss: () -> Unit,
    onAddItem: (JewelleryItem) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("1") }
    var makingCharge by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Add ${metalType.name} Item",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Item Name/ID") },
                    placeholder = { Text("e.g., Ring, s1, g1") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Weight (grams)") },
                    placeholder = { Text("e.g., 10.5") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity") },
                    placeholder = { Text("e.g., 2") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = makingCharge,
                    onValueChange = { makingCharge = it },
                    label = { Text("Making Charge") },
                    placeholder = { Text("e.g., 250") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                
                if (showError) {
                    Text(
                        text = "Please fill all fields with valid values",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = {
                            val weightValue = weight.toDoubleOrNull()
                            val quantityValue = quantity.toIntOrNull()
                            val makingChargeValue = makingCharge.toDoubleOrNull()
                            
                            if (name.isNotBlank() && 
                                weightValue != null && weightValue > 0 &&
                                quantityValue != null && quantityValue > 0 &&
                                makingChargeValue != null && makingChargeValue >= 0) {
                                
                                val item = JewelleryItem(
                                    name = name,
                                    weight = weightValue,
                                    quantity = quantityValue,
                                    makingCharge = makingChargeValue,
                                    metalType = metalType
                                )
                                onAddItem(item)
                            } else {
                                showError = true
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Add Item")
                    }
                }
            }
        }
    }
}
package com.jewellerycalculator.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.jewellerycalculator.model.ExchangeItem
import com.jewellerycalculator.model.MetalType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExchangeItemDialog(
    onDismiss: () -> Unit,
    onAddItem: (ExchangeItem) -> Unit
) {
    var metalType by remember { mutableStateOf(MetalType.SILVER) }
    var weight by remember { mutableStateOf("") }
    var wastage by remember { mutableStateOf("") }
    var purity by remember { mutableStateOf("") }
    var gramRate by remember { mutableStateOf("") }
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
                    text = "Add Exchange Item",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Row {
                    RadioButton(
                        selected = metalType == MetalType.GOLD,
                        onClick = { metalType = MetalType.GOLD }
                    )
                    Text("Gold", modifier = Modifier.padding(end = 16.dp))
                    RadioButton(
                        selected = metalType == MetalType.SILVER,
                        onClick = { metalType = MetalType.SILVER }
                    )
                    Text("Silver")
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Weight (grams)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = wastage,
                    onValueChange = { wastage = it },
                    label = { Text("Wastage (grams)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = purity,
                    onValueChange = { purity = it },
                    label = { Text("Purity (e.g., 0.75)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = gramRate,
                    onValueChange = { gramRate = it },
                    label = { Text("Gram Rate") },
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
                            val w = weight.toDoubleOrNull()
                            val wa = wastage.toDoubleOrNull()
                            val p = purity.toDoubleOrNull()
                            val gr = gramRate.toDoubleOrNull()
                            if (w != null && wa != null && p != null && gr != null && w > 0 && p > 0 && gr > 0) {
                                onAddItem(
                                    ExchangeItem(
                                        metalType = metalType,
                                        weight = w,
                                        wastage = wa,
                                        purity = p,
                                        gramRate = gr
                                    )
                                )
                            } else {
                                showError = true
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Add Exchange")
                    }
                }
            }
        }
    }
}

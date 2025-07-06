package com.jewellerycalculator.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateInputScreen(
    onRatesSet: (goldRate: Double, silverRate: Double) -> Unit
) {
    var goldRateText by remember { mutableStateOf("") }
    var silverRateText by remember { mutableStateOf("") }
    var goldRateError by remember { mutableStateOf(false) }
    var silverRateError by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Jewellery Calculator",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        Text(
            text = "Enter Today's Metal Rates",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = goldRateText,
                    onValueChange = {
                        goldRateText = it
                        goldRateError = false
                    },
                    label = { Text("Gold Rate per gram") },
                    placeholder = { Text("e.g., 9100") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = goldRateError,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = silverRateText,
                    onValueChange = {
                        silverRateText = it
                        silverRateError = false
                    },
                    label = { Text("Silver Rate per gram") },
                    placeholder = { Text("e.g., 108") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = silverRateError,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = {
                val goldRate = goldRateText.toDoubleOrNull()
                val silverRate = silverRateText.toDoubleOrNull()
                
                goldRateError = goldRate == null || goldRate <= 0
                silverRateError = silverRate == null || silverRate <= 0
                
                if (!goldRateError && !silverRateError) {
                    onRatesSet(goldRate!!, silverRate!!)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "Continue to Billing",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
        
        if (goldRateError || silverRateError) {
            Text(
                text = "Please enter valid rates for both metals",
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
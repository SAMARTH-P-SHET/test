package com.jewellerycalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jewellerycalculator.ui.screen.RateInputScreen
import com.jewellerycalculator.ui.screen.BillingScreen
import com.jewellerycalculator.ui.screen.BillSummaryScreen
import com.jewellerycalculator.ui.theme.JewelleryCalculatorTheme
import com.jewellerycalculator.viewmodel.BillViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JewelleryCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    JewelleryCalculatorApp()
                }
            }
        }
    }
}

@Composable
fun JewelleryCalculatorApp() {
    val navController = rememberNavController()
    val billViewModel: BillViewModel = viewModel()
    val bill by billViewModel.bill.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = "rate_input"
    ) {
        composable("rate_input") {
            RateInputScreen(
                onRatesSet = { goldRate, silverRate ->
                    billViewModel.setRates(goldRate, silverRate)
                    navController.navigate("billing")
                }
            )
        }
        
        composable("billing") {
            BillingScreen(
                bill = bill,
                onAddItem = { item ->
                    billViewModel.addItem(item)
                },
                onRemoveItem = { item ->
                    billViewModel.removeItem(item)
                },
                onGenerateBill = {
                    navController.navigate("bill_summary")
                }
            )
        }
        
        composable("bill_summary") {
            BillSummaryScreen(
                bill = bill,
                onBackToBilling = {
                    navController.navigateUp()
                },
                onNewBill = {
                    billViewModel.clearBill()
                    navController.navigate("rate_input") {
                        popUpTo("rate_input") { inclusive = true }
                    }
                }
            )
        }
    }
}
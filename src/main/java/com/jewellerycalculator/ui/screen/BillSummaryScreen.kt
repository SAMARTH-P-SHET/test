package com.jewellerycalculator.ui.screen

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jewellerycalculator.model.Bill
import com.jewellerycalculator.model.JewelleryItem
import com.jewellerycalculator.model.MetalType
import java.io.File
import java.io.FileOutputStream
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun BillSummaryScreen(
    bill: Bill,
    onBackToBilling: () -> Unit,
    onNewBill: () -> Unit
) {
    val decimalFormat = DecimalFormat("#,##0.00")
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Bill Summary",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
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
                    text = "Gold Rate: ₹${decimalFormat.format(bill.goldRate)}/g",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Silver Rate: ₹${decimalFormat.format(bill.silverRate)}/g",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Items List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Silver Items
            val silverItems = bill.getSilverItems()
            if (silverItems.isNotEmpty()) {
                item {
                    SectionHeader("Silver Items")
                }
                items(silverItems) { item ->
                    SummaryItemCard(item = item, metalRate = bill.silverRate)
                }
                item {
                    SectionTotal("Silver Total", bill.calculateSilverTotal())
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Gold Items
            val goldItems = bill.getGoldItems()
            if (goldItems.isNotEmpty()) {
                item {
                    SectionHeader("Gold Items")
                }
                items(goldItems) { item ->
                    SummaryItemCard(item = item, metalRate = bill.goldRate)
                }
                item {
                    SectionTotal("Gold Total", bill.calculateGoldTotal())
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // Grand Total
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Grand Total:",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "₹${decimalFormat.format(bill.calculateGrandTotal())}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                if (bill.exchangeItems.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Exchange Deduction:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "-₹${decimalFormat.format(bill.calculateExchangeDeduction())}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Red
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Adjusted Total:",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "₹${decimalFormat.format(bill.calculateAdjustedGrandTotal())}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = onBackToBilling,
                modifier = Modifier.weight(1f)
            ) {
                Text("Back to Billing")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onNewBill,
                modifier = Modifier.weight(1f)
            ) {
                Text("New Bill")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    generateBillPdf(context, bill)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Print")
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun SummaryItemCard(
    item: JewelleryItem,
    metalRate: Double
) {
    val decimalFormat = DecimalFormat("#,##0.00")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "₹${decimalFormat.format(item.calculateFinalPrice(metalRate))}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column {
                Text(
                    text = "Weight: ${item.weight}g × ${item.quantity} = ${decimalFormat.format(item.calculateTotalWeight())}g",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Wastage (10%): ${decimalFormat.format(item.calculateWastage())}g",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Metal Cost: ${decimalFormat.format(item.calculateTotalWeight() + item.calculateWastage())}g × ₹${decimalFormat.format(metalRate)} = ₹${decimalFormat.format((item.calculateTotalWeight() + item.calculateWastage()) * metalRate)}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Making Charge: ₹${decimalFormat.format(item.makingCharge)}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun SectionTotal(title: String, amount: Double) {
    val decimalFormat = DecimalFormat("#,##0.00")

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
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "₹${decimalFormat.format(amount)}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// PDF generation function
fun generateBillPdf(context: Context, bill: Bill) {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas
    val paint = Paint()
    var y = 30

    paint.textSize = 16f
    paint.isFakeBoldText = true
    canvas.drawText("Sri Vinayaka Jewellery Works", 20f, y.toFloat(), paint)

    paint.textSize = 12f
    paint.isFakeBoldText = false
    y += 25
    val dateStr = SimpleDateFormat("dd-MM-yyyy").format(Date())
    canvas.drawText("Date: $dateStr", 20f, y.toFloat(), paint)

    y += 20
    paint.isFakeBoldText = true
    canvas.drawText("Bill Summary", 20f, y.toFloat(), paint)
    paint.isFakeBoldText = false

    y += 20
    canvas.drawText("Gold Rate: ₹${bill.goldRate}/g", 20f, y.toFloat(), paint)
    y += 15
    canvas.drawText("Silver Rate: ₹${bill.silverRate}/g", 20f, y.toFloat(), paint)

    y += 20
    canvas.drawText("Items:", 20f, y.toFloat(), paint)
    y += 15

    bill.items.forEach { item ->
        if (y > 550) return@forEach // avoid overflow
        canvas.drawText("${item.name} (${item.weight}g x${item.quantity})", 25f, y.toFloat(), paint)
        y += 15
        canvas.drawText("Price: ₹${DecimalFormat("#,##0.00").format(item.calculateFinalPrice(
            if (item.metalType == MetalType.GOLD) bill.goldRate else bill.silverRate
        ))}", 40f, y.toFloat(), paint)
        y += 15
    }

    y += 10
    paint.isFakeBoldText = true
    canvas.drawText("Grand Total: ₹${DecimalFormat("#,##0.00").format(bill.calculateGrandTotal())}", 20f, y.toFloat(), paint)

    pdfDocument.finishPage(page)

    val fileName = "Bill_${dateStr}.pdf"
    val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    val file = File(dir, fileName)
    try {
        pdfDocument.writeTo(FileOutputStream(file))
        Toast.makeText(context, "PDF saved: ${file.absolutePath}", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Error saving PDF: ${e.message}", Toast.LENGTH_LONG).show()
    }
    pdfDocument.close()
}
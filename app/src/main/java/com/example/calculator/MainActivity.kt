package com.example.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    //binding
    private lateinit var binding: ActivityMainBinding

    //other
    private var firstNumber = ""
    private var currentNumber = ""
    private var currentOperator = ""
    private var result = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // NoLimitScreen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        // initViews
        binding.apply {
            // get all buttons
            binding.layoutMain.children.filterIsInstance<Button>().forEach { button ->
                // buttons click listener
                button.setOnClickListener {
                    // get clicked button text
                    val buttonText = button.text.toString()
                    when {
                        buttonText.matches(Regex("[0-9]")) -> {
                            if (currentOperator.isEmpty()) {
                                firstNumber += buttonText
                                updateTextViewText(firstNumber)
                            } else {
                                currentNumber += buttonText
                                updateTextViewText(currentNumber)
                            }
                        }
                        buttonText.matches(Regex("[+\\-*/]")) -> {
                            currentNumber = ""
                            if (tvResult.text.toString().isNotEmpty()) {
                                currentOperator = buttonText
                                updateTextViewText("0")
                            }
                        }
                        buttonText == "=" -> {
                            if (currentNumber.isNotEmpty() && currentOperator.isNotEmpty()) {
                                tvFormula.text = "$firstNumber$currentOperator$currentNumber"
                                result = evaluateExpression(firstNumber, currentNumber, currentOperator)
                                firstNumber = result
                                updateTextViewText(result)
                            }
                        }
                        buttonText == "." -> {
                            if (currentOperator.isEmpty()) {
                                if (!firstNumber.contains(".")) {
                                    firstNumber += if (firstNumber.isEmpty()) "0$buttonText" else buttonText
                                    updateTextViewText(firstNumber)
                                }
                            } else {
                                if (!currentNumber.contains(".")) {
                                    currentNumber += if (currentNumber.isEmpty()) "0$buttonText" else buttonText
                                    updateTextViewText(currentNumber)
                                }
                            }
                        }
                        buttonText == "C" -> {
                            currentNumber = ""
                            firstNumber = ""
                            currentOperator = ""
                            updateTextViewText("0")
                            tvFormula.text = ""
                        }
                    }
                }
            }
        }
    }

    private fun updateTextViewText(text: String) {
        val maxLength = 10
        val limitedText = if (text.length > maxLength) {
            text.substring(0, maxLength)
        } else {
            text
        }

        binding.tvResult.text = limitedText
    }

    // functions
    private fun evaluateExpression(firstNumber: String, secondNumber: String, operator: String): String {
        val num1 = firstNumber.toDouble()
        val num2 = secondNumber.toDouble()
        return when (operator) {
            "+" -> (num1 + num2).toString()
            "-" -> (num1 - num2).toString()
            "*" -> (num1 * num2).toString()
            "/" -> (num1 / num2).toString()
            else -> ""
        }
    }
}

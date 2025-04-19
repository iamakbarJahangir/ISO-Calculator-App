package com.example.calculator;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // UI Elements for both layouts
    private TextView displayText;

    // Variables for calculator logic
    private double firstOperand = 0;
    private double secondOperand = 0;
    private double memory = 0;
    private double result = 0;
    private String currentOperation = "";
    private boolean isNewOperation = true;
    private boolean isCalculating = false;
    private DecimalFormat formatter;

    // Constants
    private static final double PI = Math.PI;
    private static final double E = Math.E;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set appropriate layout based on orientation
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main);
            setupLandscapeLayout();
        } else {
            setContentView(R.layout.activity_main);
            setupPortraitLayout();
        }

        formatter = new DecimalFormat("#.##########");
    }

    /**
     * Setup handlers for portrait mode - basic calculator
     */
    private void setupPortraitLayout() {
        displayText = findViewById(R.id.resultDisplay);

        // Number buttons
        findViewById(R.id.num0Btn).setOnClickListener(v -> appendNumber("0"));
        findViewById(R.id.num1Btn).setOnClickListener(v -> appendNumber("1"));
        findViewById(R.id.num2Btn).setOnClickListener(v -> appendNumber("2"));
        findViewById(R.id.num3Btn).setOnClickListener(v -> appendNumber("3"));
        findViewById(R.id.num4Btn).setOnClickListener(v -> appendNumber("4"));
        findViewById(R.id.num5Btn).setOnClickListener(v -> appendNumber("5"));
        findViewById(R.id.num6Btn).setOnClickListener(v -> appendNumber("6"));
        findViewById(R.id.num7Btn).setOnClickListener(v -> appendNumber("7"));
        findViewById(R.id.num8Btn).setOnClickListener(v -> appendNumber("8"));
        findViewById(R.id.num9Btn).setOnClickListener(v -> appendNumber("9"));

        // Decimal point
        findViewById(R.id.decimalBtn).setOnClickListener(v -> appendDecimal());

        // Clear button
        findViewById(R.id.clearBtn).setOnClickListener(v -> clearDisplay());

        // Operations
        findViewById(R.id.plusBtn).setOnClickListener(v -> performOperation("+"));
        findViewById(R.id.minusBtn).setOnClickListener(v -> performOperation("-"));
        findViewById(R.id.multiplyBtn).setOnClickListener(v -> performOperation("*"));
        findViewById(R.id.divBtn).setOnClickListener(v -> performOperation("/"));

        // Equals
        findViewById(R.id.equalsBtn).setOnClickListener(v -> calculateResult());

        // Other operations
        findViewById(R.id.percentBtn).setOnClickListener(v -> calculatePercent());
        findViewById(R.id.negateBtn).setOnClickListener(v -> negateNumber());
    }

    /**
     * Setup handlers for landscape mode - scientific calculator
     */
    private void setupLandscapeLayout() {
        displayText = findViewById(R.id.calcResult);

        // Number buttons
        findViewById(R.id.digit0Btn).setOnClickListener(v -> appendNumber("0"));
        findViewById(R.id.digit1Btn).setOnClickListener(v -> appendNumber("1"));
        findViewById(R.id.digit2Btn).setOnClickListener(v -> appendNumber("2"));
        findViewById(R.id.digit3Btn).setOnClickListener(v -> appendNumber("3"));
        findViewById(R.id.digit4Btn).setOnClickListener(v -> appendNumber("4"));
        findViewById(R.id.digit5Btn).setOnClickListener(v -> appendNumber("5"));
        findViewById(R.id.digit6Btn).setOnClickListener(v -> appendNumber("6"));
        findViewById(R.id.digit7Btn).setOnClickListener(v -> appendNumber("7"));
        findViewById(R.id.digit8Btn).setOnClickListener(v -> appendNumber("8"));
        findViewById(R.id.digit9Btn).setOnClickListener(v -> appendNumber("9"));

        // Decimal point
        findViewById(R.id.decimalPointBtn).setOnClickListener(v -> appendDecimal());

        // Clear button
        findViewById(R.id.clearAllBtn).setOnClickListener(v -> clearDisplay());

        // Basic operations
        findViewById(R.id.additionBtn).setOnClickListener(v -> performOperation("+"));
        findViewById(R.id.subtractionBtn).setOnClickListener(v -> performOperation("-"));
        findViewById(R.id.multiplicationBtn).setOnClickListener(v -> performOperation("*"));
        findViewById(R.id.divideBtn).setOnClickListener(v -> performOperation("/"));

        // Equals
        findViewById(R.id.calculateBtn).setOnClickListener(v -> calculateResult());

        // Memory operations
        findViewById(R.id.mcBtn).setOnClickListener(v -> memoryClear());
        findViewById(R.id.mpBtn).setOnClickListener(v -> memoryPlus());
        findViewById(R.id.mmBtn).setOnClickListener(v -> memoryMinus());
        findViewById(R.id.mrBtn).setOnClickListener(v -> memoryRecall());

        // Other operations
        findViewById(R.id.percentageBtn).setOnClickListener(v -> calculatePercent());
        findViewById(R.id.signBtn).setOnClickListener(v -> negateNumber());

        // Scientific operations
        findViewById(R.id.squrBtn).setOnClickListener(v -> calculateSquare());
        findViewById(R.id.cubeBtn).setOnClickListener(v -> calculateCube());
        findViewById(R.id.powerBtn).setOnClickListener(v -> performOperation("^"));
        findViewById(R.id.sqrrootBtn).setOnClickListener(v -> calculateSquareRoot());
        findViewById(R.id.cuberootBtn).setOnClickListener(v -> calculateCubeRoot());
        findViewById(R.id.overBtn).setOnClickListener(v -> calculateReciprocal());
        findViewById(R.id.FactorialBtn).setOnClickListener(v -> calculateFactorial());

        // Logarithmic operations
        findViewById(R.id.logBtn).setOnClickListener(v -> calculateLog10());
        findViewById(R.id.lnBtn).setOnClickListener(v -> calculateLn());

        // Trigonometric operations
        findViewById(R.id.sineBtn).setOnClickListener(v -> calculateSin());
        findViewById(R.id.cosineBtn).setOnClickListener(v -> calculateCos());
        findViewById(R.id.tangentBtn).setOnClickListener(v -> calculateTan());
        findViewById(R.id.sinhBtn).setOnClickListener(v -> calculateSinh());
        findViewById(R.id.coshBtn).setOnClickListener(v -> calculateCosh());
        findViewById(R.id.tanhBtn).setOnClickListener(v -> calculateTanh());

        // Constants and special functions
        findViewById(R.id.piBtn).setOnClickListener(v -> insertPi());
        findViewById(R.id.randBtn).setOnClickListener(v -> generateRandom());
    }

    /**
     * Append a digit to the display
     */
    private void appendNumber(String digit) {
        if (isNewOperation) {
            displayText.setText(digit);
            isNewOperation = false;
        } else {
            if (displayText.getText().toString().equals("0")) {
                displayText.setText(digit);
            } else {
                displayText.setText(displayText.getText() + digit);
            }
        }
    }

    /**
     * Append decimal point to the display
     */
    private void appendDecimal() {
        String currentText = displayText.getText().toString();

        if (isNewOperation) {
            displayText.setText("0.");
            isNewOperation = false;
        } else if (!currentText.contains(".")) {
            displayText.setText(currentText + ".");
        }
    }

    /**
     * Clear the display and reset calculations
     */
    private void clearDisplay() {
        displayText.setText("0");
        firstOperand = 0;
        secondOperand = 0;
        currentOperation = "";
        isNewOperation = true;
        isCalculating = false;
    }

    /**
     * Perform basic arithmetic operation
     */
    private void performOperation(String operation) {
        try {
            if (!isCalculating) {
                firstOperand = Double.parseDouble(displayText.getText().toString());
                isCalculating = true;
            } else {
                secondOperand = Double.parseDouble(displayText.getText().toString());
                calculateResult();
                firstOperand = result;
            }

            currentOperation = operation;
            isNewOperation = true;
        } catch (Exception e) {
            handleError("Invalid operation");
        }
    }

    /**
     * Calculate the result of the current operation
     */
    private void calculateResult() {
        try {
            if (isCalculating) {
                secondOperand = Double.parseDouble(displayText.getText().toString());

                switch (currentOperation) {
                    case "+":
                        result = firstOperand + secondOperand;
                        break;
                    case "-":
                        result = firstOperand - secondOperand;
                        break;
                    case "*":
                        result = firstOperand * secondOperand;
                        break;
                    case "/":
                        if (secondOperand == 0) {
                            handleError("Cannot divide by zero");
                            return;
                        }
                        result = firstOperand / secondOperand;
                        break;
                    case "^":
                        result = Math.pow(firstOperand, secondOperand);
                        break;
                    default:
                        result = secondOperand;
                }

                displayText.setText(formatResult(result));
                isCalculating = false;
                isNewOperation = true;
            }
        } catch (Exception e) {
            handleError("Calculation error");
        }
    }

    /**
     * Calculate percentage
     */
    private void calculatePercent() {
        try {
            double value = Double.parseDouble(displayText.getText().toString());

            if (isCalculating) {
                // If in middle of calculation, percentage is relative to first operand
                value = firstOperand * (value / 100);
            } else {
                value = value / 100;
            }

            displayText.setText(formatResult(value));
            isNewOperation = true;
        } catch (Exception e) {
            handleError("Percentage calculation error");
        }
    }

    /**
     * Negate the current number
     */
    private void negateNumber() {
        try {
            double value = Double.parseDouble(displayText.getText().toString());
            value = -value;
            displayText.setText(formatResult(value));
        } catch (Exception e) {
            handleError("Negation error");
        }
    }

    /**
     * Calculate square of current number
     */
    private void calculateSquare() {
        try {
            double value = Double.parseDouble(displayText.getText().toString());
            double result = Math.pow(value, 2);
            displayText.setText(formatResult(result));
            isNewOperation = true;
        } catch (Exception e) {
            handleError("Square calculation error");
        }
    }

    /**
     * Calculate cube of current number
     */
    private void calculateCube() {
        try {
            double value = Double.parseDouble(displayText.getText().toString());
            double result = Math.pow(value, 3);
            displayText.setText(formatResult(result));
            isNewOperation = true;
        } catch (Exception e) {
            handleError("Cube calculation error");
        }
    }

    /**
     * Calculate square root of current number
     */
    private void calculateSquareRoot() {
        try {
            double value = Double.parseDouble(displayText.getText().toString());

            if (value < 0) {
                handleError("Cannot calculate square root of negative number");
                return;
            }

            double result = Math.sqrt(value);
            displayText.setText(formatResult(result));
            isNewOperation = true;
        } catch (Exception e) {
            handleError("Square root calculation error");
        }
    }

    /**
     * Calculate cube root of current number
     */
    private void calculateCubeRoot() {
        try {
            double value = Double.parseDouble(displayText.getText().toString());
            double result = Math.cbrt(value);
            displayText.setText(formatResult(result));
            isNewOperation = true;
        } catch (Exception e) {
            handleError("Cube root calculation error");
        }
    }

    /**
     * Calculate reciprocal (1/x) of current number
     */
    private void calculateReciprocal() {
        try {
            double value = Double.parseDouble(displayText.getText().toString());

            if (value == 0) {
                handleError("Cannot divide by zero");
                return;
            }

            double result = 1.0 / value;
            displayText.setText(formatResult(result));
            isNewOperation = true;
        } catch (Exception e) {
            handleError("Reciprocal calculation error");
        }
    }

    /**
     * Calculate factorial of current number
     */
    private void calculateFactorial() {
        try {
            double value = Double.parseDouble(displayText.getText().toString());

            if (value < 0 || value != Math.floor(value)) {
                handleError("Factorial requires non-negative integer");
                return;
            }

            if (value > 170) {
                handleError("Value too large for factorial");
                return;
            }

            double result = factorial((int) value);
            displayText.setText(formatResult(result));
            isNewOperation = true;
        } catch (Exception e) {
            handleError("Factorial calculation error");
        }
    }

    /**
     * Calculate factorial recursively
     */
    private double factorial(int n) {
        if (n <= 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    /**
     * Calculate base-10 logarithm
     */
    private void calculateLog10() {
        try {
            double value = Double.parseDouble(displayText.getText().toString());

            if (value <= 0) {
                handleError("Log requires positive number");
                return;
            }

            double result = Math.log10(value);
            displayText.setText(formatResult(result));
            isNewOperation = true;
        } catch (Exception e) {
            handleError("Log calculation error");
        }
    }

    /**
     * Calculate natural logarithm
     */
    private void calculateLn() {
        try {
            double value = Double.parseDouble(displayText.getText().toString());

            if (value <= 0) {
                handleError("Natural log requires positive number");
                return;
            }

            double result = Math.log(value);
            displayText.setText(formatResult(result));
            isNewOperation = true;
        } catch (Exception e) {
            handleError("Natural log calculation error");
        }
    }

    /**
     * Calculate sine (in radians)
     */
    private void calculateSin() {
        try {
            double value = Double.parseDouble(displayText.getText().toString());
            double result = Math.sin(value);
            displayText.setText(formatResult(result));
            isNewOperation = true;
        } catch (Exception e) {
            handleError("Sine calculation error");
        }
    }

    /**
     * Calculate cosine (in radians)
     */
    private void calculateCos() {
        try {
            double value = Double.parseDouble(displayText.getText().toString());
            double result = Math.cos(value);
            displayText.setText(formatResult(result));
            isNewOperation = true;
        } catch (Exception e) {
            handleError("Cosine calculation error");
        }
    }

    /**
     * Calculate tangent (in radians)
     */
    private void calculateTan() {
        try {
            double value = Double.parseDouble(displayText.getText().toString());

            // Check for undefined tangent values (odd multiples of π/2)
            double modPiHalf = value % Math.PI;
            if (Math.abs(modPiHalf - Math.PI/2) < 0.0000001) {
                handleError("Tangent undefined at this value");
                return;
            }

            double result = Math.tan(value);
            displayText.setText(formatResult(result));
            isNewOperation = true;
        } catch (Exception e) {
            handleError("Tangent calculation error");
        }
    }

    /**
     * Calculate hyperbolic sine
     */
    private void calculateSinh() {
        try {
            double value = Double.parseDouble(displayText.getText().toString());
            double result = Math.sinh(value);
            displayText.setText(formatResult(result));
            isNewOperation = true;
        } catch (Exception e) {
            handleError("Hyperbolic sine calculation error");
        }
    }

    /**
     * Calculate hyperbolic cosine
     */
    private void calculateCosh() {
        try {
            double value = Double.parseDouble(displayText.getText().toString());
            double result = Math.cosh(value);
            displayText.setText(formatResult(result));
            isNewOperation = true;
        } catch (Exception e) {
            handleError("Hyperbolic cosine calculation error");
        }
    }

    /**
     * Calculate hyperbolic tangent
     */
    private void calculateTanh() {
        try {
            double value = Double.parseDouble(displayText.getText().toString());
            double result = Math.tanh(value);
            displayText.setText(formatResult(result));
            isNewOperation = true;
        } catch (Exception e) {
            handleError("Hyperbolic tangent calculation error");
        }
    }

    /**
     * Insert Pi constant
     */
    private void insertPi() {
        displayText.setText(formatResult(PI));
        isNewOperation = true;
    }

    /**
     * Generate random number between 0 and 1
     */
    private void generateRandom() {
        try {
            Random random = new Random();
            double result = random.nextDouble();
            displayText.setText(formatResult(result));
            isNewOperation = true;
        } catch (Exception e) {
            handleError("Random number generation error");
        }
    }

    /**
     * Memory clear operation
     */
    private void memoryClear() {
        memory = 0;
        showToast("Memory cleared");
    }

    /**
     * Memory plus operation
     */
    private void memoryPlus() {
        try {
            double value = Double.parseDouble(displayText.getText().toString());
            memory += value;
            showToast("Added to memory");
            isNewOperation = true;
        } catch (Exception e) {
            handleError("Memory operation error");
        }
    }

    /**
     * Memory minus operation
     */
    private void memoryMinus() {
        try {
            double value = Double.parseDouble(displayText.getText().toString());
            memory -= value;
            showToast("Subtracted from memory");
            isNewOperation = true;
        } catch (Exception e) {
            handleError("Memory operation error");
        }
    }

    /**
     * Memory recall operation
     */
    private void memoryRecall() {
        displayText.setText(formatResult(memory));
        isNewOperation = true;
    }

    /**
     * Format a result for display, removing trailing zeros
     */
    private String formatResult(double value) {
        // Handle special cases first
        if (Double.isNaN(value)) {
            return "Error";
        }

        if (Double.isInfinite(value)) {
            return value > 0 ? "∞" : "-∞";
        }

        // Check if it's effectively an integer
        if (Math.abs(value - Math.round(value)) < 0.00000001) {
            return String.valueOf((long) value);
        }

        // Otherwise format as a double
        return formatter.format(value);
    }

    /**
     * Handle calculation errors
     */
    private void handleError(String message) {
        displayText.setText("Error");
        showToast(message);
        isNewOperation = true;
    }

    /**
     * Show toast message
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Handle device orientation changes
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Save the current state
        String currentDisplay = displayText.getText().toString();

        // Re-create the UI
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main);
            setupLandscapeLayout();
        } else {
            setContentView(R.layout.activity_main);
            setupPortraitLayout();
        }

        // Restore the calculator state
        displayText.setText(currentDisplay);
    }
}
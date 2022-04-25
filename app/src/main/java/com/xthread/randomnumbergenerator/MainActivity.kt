package com.xthread.randomnumbergenerator

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xthread.randomnumbergenerator.ui.theme.RandomNumberGeneratorTheme
import kotlinx.coroutines.launch
import kotlin.random.Random.Default.nextLong

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RandomNumberGeneratorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    var result by rememberSaveable{ mutableStateOf("")}
                    var min by rememberSaveable { mutableStateOf("") }
                    var max by rememberSaveable { mutableStateOf("") }

                    val clipboardManager: ClipboardManager = LocalClipboardManager.current

                    val isValid = min.isNotBlank() &&min.isValidLong() && max.isNotBlank()&& max.isValidLong() && min.toLong() <= max.toLong()
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        Column(modifier = Modifier.padding(all = 16.dp),) {
                            minTextField(
                                value = min,
                                onValueChange = {if (it.length <= 16) {
                                    min = it
                                }},
                                modifier =Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            maxTextField(
                                value = max,
                                onValueChange = {if (it.length <= 16) {
                                    max = it
                                }},
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                enabled = isValid,
                                onClick = {
                                    val range: LongRange = (min.toLong()..max.toLong())
                                    result = range.random().toString()
                                    clipboardManager.setText(AnnotatedString(result))
                                },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text("Generate")
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                result,
                                textAlign = TextAlign.Center,
                                fontSize = 28.sp,
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 1
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { result = clipboardManager.getText()?.text ?: "" }
                            ) {
                                Text(text = "Copy")
                            }
                        }

                    }


                }
            }
        }
    }
}



@Composable
fun minTextField(
    value : String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
){
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange =onValueChange,
        singleLine = true,
        modifier = modifier,
        label = { Text("min") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next,
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        )
    )
}

@Composable
fun maxTextField(
    value : String,
    onValueChange: (String) -> Unit,
    modifier: Modifier){
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = modifier,
        label = { Text("max") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        )
    )
}



fun getRandom(min:Int,max:Int) : Int {
    val range = (min..max)
    return range.random()
}

@Composable
fun ResultText(
    result: String,
    modifier: Modifier = Modifier
){
    Text(text = result,modifier = Modifier)
}

fun String.isValidLong() = toLongOrNull() != null


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RandomNumberGeneratorTheme {
        Greeting("Android")
    }
}
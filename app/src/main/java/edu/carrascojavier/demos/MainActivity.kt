package edu.carrascojavier.demos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import edu.carrascojavier.demos.b1.PantallaTema1c
import edu.carrascojavier.demos.ui.theme.DemosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DemosTheme {
                PantallaTema1c()
            }
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=411dp,height=891dp",
    name = "Pixel 5"
)
@Composable
fun PantallaCompletaPreview() {
    PantallaTema1c()
}
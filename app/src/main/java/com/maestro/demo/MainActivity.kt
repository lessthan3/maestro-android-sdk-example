package com.maestro.demo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.lessthan3.maestropanel.shared.core.sdk.CoreEventDelegate
import com.lessthan3.maestropanel.shared.core.sdk.CoreMaestroSDK
import com.lessthan3.maestropanel.shared.core.sdk.CoreSDKParameters
import com.lessthan3.maestropanel.shared.feature.panellayout.PanelAction
import com.maestro.demo.ui.theme.MyApplicationTheme
import com.maestro.uitoolkit.core.MaestroCoreOverlayView
import com.maestro.uitoolkit.core.MaestroCorePanel
import com.maestro.uitoolkit.core.MaestroCoreSDK
import com.maestro.uitoolkit.core.toAlignment
import kotlinx.coroutines.delay

private val PanelWidth = 400.dp

class MainActivity : ComponentActivity() {

    private lateinit var sdk: CoreMaestroSDK.Instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sdk = MaestroCoreSDK.configure(
            context = this,
            params = CoreSDKParameters(
                siteId = "6ac799368ce97df1773ea003",
                pageId = "6ac799368ce97df1773ea03c",
                configBaseUrl = "https://static.gcp.maestro.io",
            ),
            delegate = MaestroDelegate(),
        )

        setContent {
            MyApplicationTheme {
                // A real player reports the wall-clock moment it is showing (an HLS program date time, say);
                // designed panels that read it show the data for that moment.
                LaunchedEffect(Unit) {
                    while (true) {
                        sdk.onPlayerTimecodeUpdated(System.currentTimeMillis())
                        delay(1_000)
                    }
                }
                val overlay by sdk.overlays.collectAsState(initial = null)
                Content(
                    isOverlayShowing = overlay != null,
                    onShowTestOverlay = { sdk.debugInterface.triggerTestOverlay() },
                    modifier = Modifier.fillMaxSize(),
                )

                Box(modifier = Modifier.fillMaxSize()) {
                    MaestroCorePanel(
                        sdk = sdk,
                        modifier = Modifier
                            .width(PanelWidth)
                            .fillMaxHeight()
                            .align(Alignment.CenterEnd),
                    )
                    // Overlays get the app's area left of the panel, so they never cover it.
                    overlay?.let { current ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(end = PanelWidth),
                            contentAlignment = current.position?.toAlignment() ?: Alignment.TopEnd,
                        ) {
                            MaestroCoreOverlayView(
                                sdk = sdk,
                                onDismiss = { Log.d(Tag, "Overlay dismissed") },
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        sdk.detach()
        super.onDestroy()
    }
}

class MaestroDelegate : CoreEventDelegate {
    override fun trackImpression(analytics: Map<String, String>) {
        Log.d(Tag, "Track impression: $analytics")
    }

    override fun trackAction(analytics: Map<String, String>) {
        Log.d(Tag, "Track action: $analytics")
    }

    override fun shouldShowPanel() {
        Log.d(Tag, "Should show panel")
    }

    override fun shouldHidePanel() {
        Log.d(Tag, "Should hide panel")
    }

    override fun onPanelAction(action: PanelAction) {
        Log.d(Tag, "Panel action: $action")
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun Content(
    isOverlayShowing: Boolean,
    onShowTestOverlay: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RectangleShape,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(2.dp, Color.Blue),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(end = PanelWidth),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(text = "This is your app")
                Button(onClick = onShowTestOverlay) {
                    Text(text = "Show test overlay")
                }
                if (isOverlayShowing) {
                    Text(text = "A Maestro overlay is showing")
                }
            }
        }
    }
}

private const val Tag = "DemoApp"

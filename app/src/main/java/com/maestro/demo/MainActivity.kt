package com.maestro.demo

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.lessthan3.maestropanel.core.events.MaestroAuthData
import com.lessthan3.maestropanel.core.events.MaestroEventDelegate
import com.lessthan3.maestropanel.core.manager.MaestroSDK
import com.lessthan3.maestropanel.core.manager.MaestroSDKParameters
import com.lessthan3.maestropanel.core.networking.MaestroLoadableResult
import com.lessthan3.maestropanel.ui.MaestroPanel
import com.lessthan3.maestropanel.ui.keyplays.model.MaestroKeyPlaysResponse
import com.lessthan3.maestropanel.ui.maestroEventViewModel
import com.lessthan3.maestropanel.ui.overlay.MaestroOverlays
import com.lessthan3.maestropanel.ui.overlay.maestroOverlayViewModel
import com.lessthan3.maestropanel.ui.topheader.MaestroPanelType
import com.maestro.demo.ui.theme.MyApplicationTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val siteId = "695c05610f99e7584a1b7407"
        val pageId: String? = "695c05610f99e7584a1b743e"

        val delegate = MaestroDelegate()

        val sdk = configureSDK(applicationContext, delegate, siteId = siteId, pageId = pageId)

        setContent {
            MyApplicationTheme {
                Content(
                    modifier = Modifier.fillMaxSize(),
                )

                Box(modifier = Modifier.fillMaxSize()) {
                    MaestroPanelContent(
                        sdk,
                        modifier = Modifier
                            .width(300.dp)
                            .align(Alignment.CenterEnd)
                    )
                    MaestroOverlaysContent(
                        sdk,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                    )
                }
            }
        }
    }
}

class MaestroDelegate: MaestroEventDelegate {
    override fun keyPlaysData(): StateFlow<MaestroLoadableResult<MaestroKeyPlaysResponse>> {
        return MutableStateFlow(MaestroLoadableResult.Success(MaestroKeyPlaysResponse()))
    }

    override fun authData(): StateFlow<MaestroAuthData?> {
        return MutableStateFlow(null)
    }

    override fun playClip(index: Int, clipId: String?) {
        Log.d(Tag, "Play clip $index")
    }

    override fun onKeyPlaysRefreshNeeded() {
        Log.d(Tag, "Key plays refresh needed")
    }

    override fun shouldShowPanel() {
        Log.d(Tag, "Should show panel")
    }

    override fun shouldHidePanel() {
        Log.d(Tag, "Should hide panel")
    }

    override fun trackImpression(analytics: Map<String, String>) {
        Log.d(Tag, "Track action")
    }

    override fun trackAction(analytics: Map<String, String>) {
        Log.d(Tag, "Track action")
    }

    override fun onLoginClicked() {
        Log.d(Tag, "Login clicked")
    }

    override fun onPanelSelected(panel: MaestroPanelType) {
        Log.d(Tag, "Panel selected: $panel")
    }
}

@Composable
fun MaestroPanelContent(sdk: MaestroSDK.Instance, modifier: Modifier = Modifier) {
    val eventVm = maestroEventViewModel(sdk)
    MaestroPanel(
        eventVm,
        modifier = modifier.fillMaxSize().border(2.dp, Color.Red),
    )
}

@Composable
fun MaestroOverlaysContent(sdk: MaestroSDK.Instance, modifier: Modifier = Modifier) {
    val overlayVM = maestroOverlayViewModel(sdk)
    MaestroOverlays(
        viewModel = overlayVM,
        modifier = modifier.wrapContentSize(),
    )
}

@Composable
fun Content(modifier: Modifier = Modifier) {
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
            Text(
                text = "This is your app",
                modifier = Modifier,
            )
        }
    }
}

private fun configureSDK(
    applicationContext: Context,
    delegate: MaestroEventDelegate,
    siteId: String,
    pageId: String?,
): MaestroSDK.Instance {
    val instance = MaestroSDK.configure(
        context = applicationContext,
        maestroEventDelegate = delegate,
        params = MaestroSDKParameters(
            siteId = siteId,
            pageId = pageId,
            isProduction = true,
        ),
    )
    MaestroSDK.logHandler = { log ->
        Log.d(Tag, "Log from sdk: $log")
    }
    return instance
}

private const val Tag = "DemoApp"

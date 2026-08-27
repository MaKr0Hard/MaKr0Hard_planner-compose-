package idk.makr0hard.makr0hardplanner


import android.content.Context
import android.content.Intent
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Bundle
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.widget.Scroller

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.ContentView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.ListItem;
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.lazy.transformedHeight
import idk.makr0hard.makr0hardplanner.ui.theme.MaKr0HardPlannerTheme
import idk.makr0hard.makr0hardplanner.ui.theme.Purple40
import idk.makr0hard.makr0hardplanner.ui.theme.Purple80
import java.nio.file.WatchEvent
import kotlin.jvm.java
import java.time.DayOfWeek;
import java.time.Year;
import java.time.Month;
import java.time.MonthDay;
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaKr0HardPlannerTheme {
                MaKr0HardPlannerApp()
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
fun MaKr0HardPlannerApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    var scrollState = rememberScrollState();
    val view = LocalView.current;
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior();
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {

                item(
                    icon = { Icon(painterResource(it.icon), contentDescription = it.label) },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = {  view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP); currentDestination = it; },
                )
            }
        }
    ) /* end NavigationSuiteScaffold */ {
        Scaffold(modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize()
                .wrapContentWidth(),
            topBar = {LargeTopAppBar(
                title = {Column(){
                    Text(text = "Planner", fontSize = 67.sp, lineHeight = 69.sp);
                    Text(text = "by MaKr0Hard", fontSize = 7.sp);
                }},
                scrollBehavior = scrollBehavior)/* end topBar */}

            ) /* end Scaffold */ { innerPadding ->
            Column (modifier = Modifier.padding(innerPadding)
                .verticalScroll(scrollState)
                .fillMaxSize()

            ) /* end Column */ {
            when (currentDestination) { // Why, just why jetbrains ? why can't we just have a syntax keyword like assembly to change this woke language purely designed to attract the apple sheep into something like C/CPP, where you really want to
                                        // spend your time debugging, all that because stoopid android doesn't have native apis, long live halium ! KDE ! GNOME ! pmOS !
                    AppDestinations.HOME -> {
                        home();
                    }
                    AppDestinations.PLANNER -> {
                        planner();
                    }
                    else -> {
                        why();
                    }
                }
            }
        }
    }
}

enum class AppDestinations(val label: String, val icon: Int, ) {
    HOME("Accueil", R.drawable.outline_home_24),
    PLANNER("Planning", R.drawable.outline_calendar_month_24),
    PROFILE("Plus", R.drawable.outline_more_horiz_24),
}

@Composable
fun home() {

    Column(Modifier) {
        //Image(painter = painterResource(R.drawable.construction), modifier = Modifier.fillMaxWidth(), contentDescription = null, contentScale = ContentScale.Crop);

            Box(modifier = Modifier) {

                Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max)) {

                    Box(modifier = Modifier.weight(1f)) {
                        HomeWidget(Modifier.fillMaxSize(), colour = Purple40) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                var currentLocal = LocalDateTime.now();
                                var day = currentLocal.dayOfWeek.getDisplayName(
                                    TextStyle.FULL,
                                    Locale.getDefault()
                                );
                                var formatter_date = DateTimeFormatter.ofPattern("dd");
                                var date = currentLocal.format(formatter_date);
                                Text(text = day, fontSize = 20.sp);
                                Text(date, fontSize = 70.sp);
                                var month = currentLocal.month.getDisplayName(
                                    TextStyle.FULL,
                                    Locale.getDefault()
                                );
                                var year_formatter = DateTimeFormatter.ofPattern("YYYY");
                                var year = currentLocal.format(year_formatter);
                                Text(text = month + " " + year, fontSize = 20.sp);
                            }
                        }
                    }
                    Box(modifier = Modifier.weight(1.35f)) {
                        counterUntil(get_remaining_days(get_holiday_vect().elementAt(0)).toInt(), get_holiday_vect().elementAt(0).name);
                    }
                };

            }

            //Spacer(modifier = Modifier.weight(3f));
            //Text(text = "sorry bruh, still in construction", fontSize = 40.sp, lineHeight = 50.sp);
    }

}

@Composable
fun planner() {


}

@Composable
fun why() {
    Text(text = "You have witnessed a rare bug");
}

@Composable
fun counterUntil(numberof_days_before_dayoff: Int, dayoffname: String) {


        Box(modifier = Modifier.fillMaxSize()) {
            HomeWidget(Modifier.fillMaxSize()) {
                Column() {
                    val context = LocalContext.current;

                    Text(text = numberof_days_before_dayoff.toString(), fontSize = 60.sp);
                    Text(text = "Days until ");
                    Text(text = dayoffname);
                    Button(onClick = {
                        val intent = Intent(context, Otherdaysoff::class.java).apply {
                            putExtra("EXTRA_KEY", "Valeur à transmettre");
                        }

                        context.startActivity(intent);

                    }) {
                        Row() {
                            Text(text = "Other days off ");
                            Icon(painter = painterResource(id = R.drawable.outline_arrow_forward_24), null);
                        }
                    }
                }
            }
        }

}

@Composable
fun HomeWidget(modifier: Modifier = Modifier, colour: Color = Color(0xFFD0BCFF), content: @Composable BoxScope.() -> Unit) {
    var roundness: Dp = 16.dp;
    Box(modifier = Modifier.padding(10.dp)) {
        Box(modifier = Modifier.background(color = colour, shape = RoundedCornerShape(size = roundness)).padding(all = roundness / 2)) {
            Box(modifier = modifier) {
                content();
            }
        }
    }
}

@Composable
fun otherdaysoff() {

}
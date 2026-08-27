package idk.makr0hard.makr0hardplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import idk.makr0hard.makr0hardplanner.ui.theme.MaKr0HardPlannerTheme
import idk.makr0hard.makr0hardplanner.ui.theme.Purple40
import idk.makr0hard.makr0hardplanner.ui.theme.Purple80
import java.nio.file.WatchEvent
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Vector

data class Holiday(var name: String, var day: Int, var month: Int, var year: Int);
var holiday_vect: Vector<Holiday> = Vector();

fun get_holiday_vect(): Vector<Holiday> {
    var holiday_vectt: Vector<Holiday> = Vector();
    holiday_vectt.addElement(Holiday("Halloween", 19, 10, 2026, ));
    holiday_vectt.addElement(Holiday("Christmas", 21, 12, 2026, ));
    holiday_vectt.addElement(Holiday("February", 22, 2, 2027, ));
    holiday_vectt.addElement(Holiday("Spring", 26, 4, 2027, ));
    return holiday_vectt;
}

fun get_remaining_days(item: Holiday) :Long {
    var d: LocalDate = LocalDate.of(item.year, item.month, item.day);
    var d1: LocalDate = LocalDate.now();
    var difference: Long = ChronoUnit.DAYS.between(d1, d);
    return difference;
}

class Otherdaysoff : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        holiday_vect = get_holiday_vect();

        super.onCreate(savedInstanceState)
        enableEdgeToEdge();
        setContent {
            MaKr0HardPlannerTheme {
                Otherdaysoff_layout();
            }
        }
    }
}

@Composable
fun Otherdaysoff_layout() {
    Scaffold(modifier = Modifier.fillMaxWidth()) { paddingValues -> //TODO: Add top bar
        Column(modifier = Modifier.padding(paddingValues)) {
            for (item in holiday_vect) {
                holiday_card(item.name, item.day, item.month, item.year, item);
            }
        }
    }
}

@Composable
fun holiday_card(name: String, day: Int?, month: Int?, year: Int?, holiday: Holiday) {
    ListElement(modifier = Modifier.padding(10.dp).fillMaxWidth(), colour = Purple40) {
        Box () {
            Column() {
                Text(text = name);
                Text(text = (day.toString() + "/" + month.toString() + "/" + year.toString()));
                Text(text = get_remaining_days(holiday).toString(), fontSize = 36.sp)
            }
        }
    }

}

@Composable
fun ListElement(modifier: Modifier = Modifier, colour: Color = Color(0xFFD0BCFF), content: @Composable BoxScope.() -> Unit) {
    var roundness: Dp = 16.dp;
    Box(modifier = Modifier.padding(10.dp)) {
        Box(modifier = Modifier.background(color = colour, shape = RoundedCornerShape(size = roundness)).padding(all = roundness / 2)) {
            Box(modifier = modifier) {
                content();
            }
        }
    }
}

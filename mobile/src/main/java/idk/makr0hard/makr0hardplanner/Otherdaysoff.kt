package idk.makr0hard.makr0hardplanner

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import androidx.room3.ColumnInfo
import androidx.room3.Dao
import androidx.room3.Database
import androidx.room3.Entity
import androidx.room3.Insert
import androidx.room3.PrimaryKey
import androidx.room3.Query
import androidx.room3.RoomDatabase
import idk.makr0hard.makr0hardplanner.ui.theme.MaKr0HardPlannerTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.file.WatchEvent
import java.time.LocalDate

var selectedd_date: LocalDate? = null;
data class datemonthyear(
    var day: Int?,
    var month: Int?,
    var year: Int?,
); //Kotlin : "Hey C, can I copy your homework ?"
   //C : "Yeah, but change up it a little"
   //Kotlin : **FAAAHH**

var selected_date: datemonthyear = datemonthyear(null, null, null);

fun setSelectedDate() {

}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "days-off");

@Serializable
data class DayOff(
    val id: Int,
    val name: String,
    val date_begin: datemonthyear,
    val is_more_than_one_day: Boolean = false,
    val date_end: datemonthyear
);

val DAYS_OFF_KEY = stringPreferencesKey("days_off_key");

class DataRepoDaysOff (private val context: Context ) {

    val dayOff_one: Flow<DayOff> = context.dataStore.data

        .map { preferences ->
            val dayoff_json_str: String? = preferences[DAYS_OFF_KEY]
            dayoff_json_str.let { json ->
                if (json != null) {
                    Json.decodeFromString<DayOff>(json);
                } else {

                }

            } as DayOff
        }

    suspend fun save_day_off(dayoff: DayOff) {
        context.dataStore.edit { preferences ->
            preferences[DAYS_OFF_KEY] = Json.encodeToString(dayoff);
        }
    }
}

class DayOffViewModel(private val repo: DataRepoDaysOff) : ViewModel() {
    fun saveDayOff(id: Int, name: String, date_begin: datemonthyear, is_more_than_one_day: Boolean, date_end: datemonthyear) {
        viewModelScope.launch {
            val day_off = DayOff(id, name, date_begin, is_more_than_one_day, date_end);
            repo.save_day_off(day_off)
        }
    }
}

class Otherdaysoff : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge();
        setContent {
            MaKr0HardPlannerTheme {
                Otherdaysoff_layout({setSelectedDate()}, {}, );
            }
        }
    }


}

var showdatepickerdialog: Boolean? = null;

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Otherdaysoff_layout(onDateSelected: (Long?) -> Unit,
                        onDismiss: () -> Unit,
                        )
/* end Otherdaysoff_layout */ {

    val context = LocalContext.current;
    val repo = remember { DataRepoDaysOff(context) };

    val scope = rememberCoroutineScope();

    val saved_days_off by repo.dayOff_one.collectAsState(initial = null);

    var name_dayoff_Input by remember { mutableStateOf("") };


    var scrollBehiviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior();
    var backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher;
    Scaffold (modifier = Modifier.fillMaxSize().wrapContentWidth().nestedScroll(scrollBehiviour.nestedScrollConnection),
        topBar = {LargeTopAppBar(title = {Text(text = "Other Days off")},
        navigationIcon = { IconButton(onClick = {backPressedDispatcher?.onBackPressed()}) {Icon(painter = painterResource(R.drawable.outline_arrow_back_24), null)} })}
    ) /*end Scaffold*/ {
        innerPadding -> Box(modifier = Modifier.padding(innerPadding)) {
        Box(modifier = Modifier.fillMaxSize()) {

            var showCustomDialog: Boolean by remember { mutableStateOf(false) };
            var datePickerState = rememberDatePickerState();
            var showdatepickerdialog by remember {mutableStateOf(false)};

            if (showCustomDialog) {

                Dialog(onDismissRequest = { showCustomDialog = false }) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(1.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) /* end Card */ {
                        Column(
                            modifier = Modifier.padding(1.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) /* end Column */ {
                            Text("Add day off", style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                label = {
                                    Text("Name")
                                },
                                value = name_dayoff_Input,
                                onValueChange = {name_dayoff_Input = it},
                                //state = rememberTextFieldState()
                            ) /* end TextField */;
                            Button(onClick = {
                                val input = name_dayoff_Input;
                                val dayoff_to_save = DayOff(id = 0,name = input , date_begin = datemonthyear(0, 0, 0), is_more_than_one_day = false, date_end = datemonthyear(0, 0, 0) );
                                scope.launch {
                                    repo.save_day_off(dayoff_to_save);
                                }
                            }) /* end Button */ {
                                Text("click to save name")
                            }
                            Row(modifier = Modifier.padding(15.dp)
                                    .clickable(enabled = true,
                                        onClick = {showdatepickerdialog = true}
                                    ) /* end .clickable */
                                .fillMaxWidth()
                            ) /* end Row */ {
                                if (datePickerState.getSelectedDate() != null) {
                                    Text(datePickerState.getSelectedDate()?.dayOfMonth.toString() + " " + datePickerState.getSelectedDate()?.month.toString() + " " + datePickerState.getSelectedDate()?.year.toString());
                                } else {
                                    Text(text = "Please select date");
                                }
                            }
                            if (showdatepickerdialog) {
                                key(showdatepickerdialog, selectedd_date, onDateSelected, onDismiss, datePickerState) {
                                    DatePickerDialog(
                                        onDismissRequest = { showdatepickerdialog = false },
                                        confirmButton = {
                                            TextButton(
                                                onClick = {
                                                    showdatepickerdialog = false;
                                                    selectedd_date =
                                                        datePickerState.getSelectedDate();
                                                    onDateSelected(datePickerState.selectedDateMillis);
                                                    onDismiss();
                                                    return@TextButton;
                                                }
                                            ) /* end TextButton */ {
                                                Text("OK");
                                            }
                                        },
                                        dismissButton = {
                                            TextButton(onClick = {
                                                showdatepickerdialog = false;
                                                onDismiss;
                                            }) /* end TextButton*/ {
                                                Text("Annuler");
                                            }
                                        }
                                    ) /* end DatePickerDialog */ {
                                        key(datePickerState) {
                                            DatePicker(state = datePickerState);
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp));
                            Row() {
                                Button(onClick = { showCustomDialog = false }) {
                                    Text("OK");
                                }
                                Spacer(modifier = Modifier.width(8.dp));
                                Button(onClick = { showCustomDialog = false }) {
                                    Text("Cancel");
                                }
                            }
                        }
                    }
                }
            }
            LazyColumn() {

            }
            Button(onClick = {showCustomDialog = true}, modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)) {
                Text("Add day off")
            }
        }
        }

    }
}


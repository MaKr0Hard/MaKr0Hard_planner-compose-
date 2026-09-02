package idk.makr0hard.makr0hardplanner

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.datastore.core.FileStorage
import idk.makr0hard.makr0hardplanner.ui.theme.MaKr0HardPlannerTheme
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import org.json.JSONArray

import org.json.JSONObject
import java.io.File
import java.io.RandomAccessFile
import java.util.Vector

data class SchoolPeriod (
    var name: String,
    var teacher: String,
    var room: Int,
);

data class TimeTableData (
    var monday: List<SchoolPeriod>?,
    var tuesday: List<SchoolPeriod>?,
    var wednesday: List<SchoolPeriod>?,
    var thursday: List<SchoolPeriod>?,
    var friday: List<SchoolPeriod>?,
);

var timeTableData: TimeTableData = TimeTableData(null, null, null, null, null);

var json: JSONObject = JSONObject();

class TimeTable: ComponentActivity() {

    fun createAllJsonStuff() : JSONObject {
        var jsonobj: JSONObject = JSONObject();
        var monday: JSONArray = JSONArray();
        var tuesday: JSONArray = JSONArray();
        var wednesday: JSONArray = JSONArray();
        var thursday: JSONArray = JSONArray();
        var friday: JSONArray = JSONArray();

        jsonobj.put("monday", monday);
        jsonobj.put("tuesday", tuesday);
        jsonobj.put("wednesday", wednesday);
        jsonobj.put("thursday", thursday);
        jsonobj.put("friday", friday);
        return jsonobj;
    }

    fun openFileAndWriteNecessaryStuff(): Unit {
        val file = File(this.filesDir, "info.json");

        RandomAccessFile(file, "rw").use { raf ->
            val bytes = ByteArray(raf.length().toInt());
            raf.readFully(bytes);
            val content = String(bytes, Charsets.UTF_8);

            var json: JSONObject;
            if (content.isNotBlank()) {
                json = JSONObject(content);

            } else {
                json = createAllJsonStuff();

            } //Make it self-explanatory


        }
    }

    override fun onCreate(savedInstanceState: Bundle?): Unit {


        super.onCreate(savedInstanceState);
        enableEdgeToEdge();
        setContent {
            MaKr0HardPlannerTheme {
                Layout();
            }
        }
    }
}

@Composable
fun Layout() {
    Scaffold(modifier = Modifier.fillMaxSize()) {
        paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
                .fillMaxSize()
        ) /* end Column */ {
            Button(onClick = {}) {
                Text("Click me !");

            }
        }
        if (timeTableData.monday != null) {
            LazyColumn() {

                items(timeTableData.monday, key = { it.id }) { item ->
                    Text()
                }
            }
        }
    }
}
package idk.makr0hard.makr0hardplanner

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.datastore.core.FileStorage
import idk.makr0hard.makr0hardplanner.ui.theme.MaKr0HardPlannerTheme
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import org.json.JSONArray

import org.json.JSONObject
import java.io.File
import java.io.RandomAccessFile
import java.util.Vector
import kotlin.collections.emptyList

@Serializable
data class SchoolPeriod (
    var name: String,
    var teacher: String,
    var room: Int,
);

@Serializable
data class TimeTableData (
    var monday: List<SchoolPeriod>?,
    var tuesday: List<SchoolPeriod>?,
    var wednesday: List<SchoolPeriod>?,
    var thursday: List<SchoolPeriod>?,
    var friday: List<SchoolPeriod>?,
);

//var timeTableData: TimeTableData = TimeTableData(null, null, null, null, null);

var content: String = String();

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
            content = String(bytes, Charsets.UTF_8);

            var json: JSONObject;
            if (content.isNotBlank()) {
                json = JSONObject(content);

            } else {
                json = createAllJsonStuff();

            } //Make it self-explanatory

            val updatedBytes = json.toString(2).toByteArray(Charsets.UTF_8)
            raf.seek(0)
            raf.write(updatedBytes)
            raf.setLength(updatedBytes.size.toLong())
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
    val list = remember(content) {
        try {
            Json.decodeFromString<TimeTableData>(content);
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }
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
        Row() {
            Column () {
                Text (text= "Monday");
                //TODO : day()
            }
        }

    }
}

@Composable
fun day(day: List<SchoolPeriod>) {
    LazyColumn() {

        items(day) { item ->
            ElevatedCard() {
                Text(text = item.name);
                Text(text = item.teacher, fontStyle = FontStyle.Italic);
                Text(text = item.room.toString());
            }

        }
    }
}
package com.example.trickydices

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trickydices.ui.theme.TrickyDicesTheme
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.random.Random

data class Side (val number: String, var probability:Float)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrickyDicesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TrickyDiceApp(this)
                }
            }
        }
    }
}


var currentside by mutableIntStateOf(1)
var modifyOpen by mutableStateOf(false)
val sides : MutableList<Side> = mutableListOf(Side("1",1/6f),Side("2",1/6f),Side("3",1/6f),Side("4",1/6f),Side("5",1/6f),Side("6",1/6f))

@Composable
fun TrickyDiceApp(context: Context) {
    var enabledbutton = true
    var music = MediaPlayer.create(context, R.raw.throwdices)
    val background = painterResource(id = R.drawable.background)
    Image(painter = background, contentDescription ="Background",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .fillMaxSize() )
    
    if (modifyOpen)
    {
        ModifytModal(sides)
    }
    Column (modifier = Modifier.fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp), horizontalArrangement = Arrangement.End
        ){
            IconButton(onClick = { modifyOpen = !modifyOpen})
            {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings", modifier = Modifier.fillMaxSize())
            }
        }
            Image(
                painter = painterResource(
                    id = when(currentside) {
                        1-> R.drawable.dice_1
                        2 -> R.drawable.dice_2
                        3 -> R.drawable.dice_3
                        4 -> R.drawable.dice_4
                        5-> R.drawable.dice_5
                        6  -> R.drawable.dice_6
                        else -> R.drawable.ic_launcher_foreground
                    }
                ),
                contentDescription = "Dice"
            )
        Card (colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)){

            Text(
                text = when (currentside) {
                    1 -> "ONE!"
                    2 -> "TWO!"
                    3 -> "TREE!"
                    4 -> "FOUR!"
                    5 -> "FIVE!"
                    6 -> "SIX!"
                    else -> "THROWING!"
                },
                fontSize = 40.sp,
                modifier = Modifier.padding(5.dp)
            )
        }


            Spacer(modifier = Modifier.height(200.dp))
            Button(onClick = {
                enabledbutton = !enabledbutton
                roll()
                enabledbutton = !enabledbutton


            },
                modifier=Modifier,
                enabled = enabledbutton
            ) {
                Text("ROLL", fontSize = 80.sp,)
            }
        }
}

fun roll(){

    while (true){
        val value = Random.nextFloat()
        var side = sides.random()
        if (value <= side.probability && value>0.1f) {
            currentside = side.number.toInt()
            break
        }
    }
}
fun changeProb(side: Side, sides:MutableList<Side>): String{
    return if (side.probability in 0.0..100.0){
        sides[side.number.toInt()-1].probability=side.probability
        ""
    }
    else{
        "Error invalid probabilities"
    }
}
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifytModal(sides: MutableList<Side>)
{
    androidx.compose.material3.AlertDialog(
        onDismissRequest = {
        }
    ) {
        var side1 by mutableStateOf(sides[0].probability)
        var side2 by  mutableStateOf( sides[1].probability)
        var side3 by  mutableStateOf(sides[2].probability)
        var side4 by mutableStateOf(sides[3].probability)
        var side5 by mutableStateOf(sides[4].probability)
        var side6 by mutableStateOf(sides[5].probability)

        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        ElevatedCard() {
            Column (modifier = Modifier.padding(10.dp)){
                Row (
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                ){
                    IconButton(onClick = {
                        sides[0].probability = side1
                        sides[1].probability = side2
                        sides[2].probability = side3
                        sides[3].probability = side4
                        sides[4].probability = side5
                        sides[5].probability = side6

                        modifyOpen = !modifyOpen}) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Exit", modifier = Modifier.fillMaxSize())
                    }
                }
                Text(
                    text = "Modify probabilities",
                    fontSize = 20.sp,
                )

                Divider(modifier = Modifier.padding(10.dp))
                Text(text = "Side: 1 have a ${df.format(side1 *100)}% of chance")
                Slider(
                    value = side1,
                    onValueChange = { side1 = it
                    } ,
                    valueRange = 0f..1f,
                    steps = 10,
                )
                Text(text = "Side: 2 have a ${df.format(side2*100)}% of chance")
                Slider(
                    value = side2,
                    onValueChange = { side2 = it
                    } ,
                    valueRange = 0f..1f,
                    steps = 10,
                )
                Text(text = "Side: 3 have a ${df.format(side3*100)}% of chance")
                Slider(
                    value = side3,
                    onValueChange = { side3 = it
                    } ,
                    valueRange = 0f..1f,
                    steps = 10,
                )
                Text(text = "Side: 4 have a ${df.format(side4*100)}% of chance")
                Slider(
                    value = side4,
                    onValueChange = { side4 = it
                    } ,
                    valueRange = 0f..1f,
                    steps = 10,
                )
                Text(text = "Side: 5 have a ${df.format(side5*100)}% of chance")
                Slider(
                    value = side5,
                    onValueChange = { side5 = it
                    } ,
                    valueRange = 0f..1f,
                    steps = 10,
                )
                Text(text = "Side: 6 have a ${df.format(side6*100)}% of chance")
                Slider(
                    value = side6,
                    onValueChange = { side6= it
                    } ,
                    valueRange = 0f..1f,
                    steps = 10,
                )
            }
        }
    }
}
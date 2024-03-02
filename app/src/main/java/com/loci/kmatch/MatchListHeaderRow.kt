package com.loci.kmatch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MatchListHeaderRow(listRowColor: Color, matchStatus: String, matchDate: String) {
    val altText: String
    val altColor: Color
    when (matchStatus) {
        "IN_PLAY" -> {
            altText = "경기중"
            altColor = Color.Red
        }

        "LIVE" -> {
            altText = "경기중"
            altColor = Color.Red
        }

        "PAUSED" -> {
            altText = "일시중지"
            altColor = Color.Red
        }

        "SUSPENDED" -> {
            altText = "정지됨"
            altColor = Color.Red
        }

        "POSTPONED" -> {
            altText = "연기됨"
            altColor = Color.Gray
        }

        "CANCELLED" -> {
            altText = "취소됨"
            altColor = Color.Gray
        }

        else -> {
            altText = ""
            altColor = Color.DarkGray
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(listRowColor)
            .padding(5.dp, 5.dp, 15.dp, 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = matchDate,
            color = Color.LightGray,
            fontSize = 15.sp,
            modifier = Modifier
                .background(listRowColor)
                .padding(start = 10.dp)

        )
        if (matchStatus !in listOf("FINISHED", "SCHEDULED", "TIMED")) {

            Box(
                modifier = Modifier
                    .size(47.dp, 18.dp)
                    .background(altColor, shape = RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = altText,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                )
            }
        }
    }
}
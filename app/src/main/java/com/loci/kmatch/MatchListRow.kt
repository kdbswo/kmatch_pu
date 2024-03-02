package com.loci.kmatch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest

@Composable
fun MatchListRow(crest: String?, teamName: String, score: Int?, listRowColor: Color) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(listRowColor)
            .padding(15.dp, 5.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {

        val logoBgColor = if (teamName == "토트넘") Color.White else listRowColor
        Box(
            modifier = Modifier
                .size(27.dp)
                .clip(CircleShape)
                .background(logoBgColor),
            contentAlignment = Alignment.Center

        ) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(crest)
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(25.dp)

            )
        }

        Spacer(modifier = Modifier.width(15.dp))

        Text(text = teamName, fontSize = 18.sp, color = Color.White, modifier = Modifier.weight(8f))

        Spacer(modifier = Modifier.weight(1f))

        if (score !== null) {
            Text(text = score.toString(), color = Color.White, modifier = Modifier.weight(1f))
        }
    }
}
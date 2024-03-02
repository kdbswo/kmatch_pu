package com.loci.kmatch

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.loci.kmatch.ui.theme.KmatchTheme

@Composable
fun ShimmerListItem(
    isLoading: Boolean,
    contentAfterLoading: @Composable () -> Unit,
    modifier: Modifier
) {
    if (isLoading) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)

        ) {
            items(4) {

                Card(
                    modifier = modifier,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(0.8.dp, Color.DarkGray),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black
                    )
                ) {

                    Box(
                        modifier = Modifier
                            .padding(start = 15.dp, top = 6.dp)
                            .size(130.dp, 15.dp)
                            .shimmerEffect()
                    )

                    Row(
                        modifier = Modifier
                            .height(80.dp)
                            .padding(start = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .size(30.dp)
                                .clip(CircleShape)
                                .shimmerEffect()
                        )

                        Box(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .size(100.dp, 23.dp)
                                .shimmerEffect()
                        )
                    }

                    Row(
                        modifier = Modifier
                            .height(80.dp)
                            .padding(start = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .size(30.dp)
                                .clip(CircleShape)
                                .shimmerEffect()
                        )

                        Box(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .size(150.dp, 23.dp)
                                .shimmerEffect()
                        )
                    }
                }
            }
        }
    } else {
        contentAfterLoading()
    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition(label = "")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ), label = ""
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFF929292),
                Color(0xFF686868),
                Color(0xFF929292),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    KmatchTheme {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            ShimmerListItem(
                isLoading = true,
                contentAfterLoading = {
                    Box(modifier = Modifier.fillMaxSize())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
            )
        }
    }
}

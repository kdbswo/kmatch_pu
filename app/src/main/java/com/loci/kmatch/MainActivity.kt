package com.loci.kmatch

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loci.kmatch.data.Match
import com.loci.kmatch.data.Score
import com.loci.kmatch.data.Team
import com.loci.kmatch.data.Time
import com.loci.kmatch.ui.theme.KmatchTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepVisibleCondition {
                viewModel.isLoading.value
            }
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.5f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 500L
                zoomX.doOnEnd { screen.remove() }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.5f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 500L
                zoomY.doOnEnd { screen.remove() }

                zoomX.start()
                zoomY.start()
            }
        }
        setContent {
            KmatchTheme {
                MainScaffold(viewModel)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(viewModel: MainViewModel) {
    Scaffold(
        topBar = {
            MainTopBar(viewModel)
        },
        bottomBar = {
            MainBottomAppBar()
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = Color.Black
        ) {
            MainScreen(viewModel)
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(viewModel: MainViewModel) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.match_list), color = Color.LightGray)
        },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add",
                    tint = Color.LightGray
                )
            }
        },
        actions = {
            IconButton(onClick = { viewModel.fetchAllMatches() }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "refresh",
                    tint = Color.LightGray
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(Color(0xF2000000))
    )
}

@Composable
fun MainBottomAppBar() {
    BottomAppBar(
        containerColor = Color(0xF2000000)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "home",
                    tint = Color.LightGray
                )
            }

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "list",
                    tint = Color.LightGray
                )
            }

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "favorite",
                    tint = Color.LightGray
                )
            }

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "setting",
                    tint = Color.LightGray
                )
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val matchesState by viewModel.matchesList.collectAsStateWithLifecycle()
    val isDataLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val matches: List<Match> = matchesState

    if (isDataLoading || matches.isEmpty()) {
        ShimmerListItem(
            isLoading = isDataLoading,
            contentAfterLoading = {
                MainMatchList(viewModel, matches)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
        )
    } else {
        MainMatchList(viewModel, matches)
    }
}

@Composable
fun MainMatchList(viewModel: MainViewModel, matches: List<Match>) {

    val timedMatchIndex = viewModel.findTimedFirstMatch(matches)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        state = LazyListState(
            firstVisibleItemIndex = timedMatchIndex,
            firstVisibleItemScrollOffset = -50
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        itemsIndexed(items = matches, key = { index, match -> match.id }) { index, match ->

            val listColor: Color = if (timedMatchIndex > index) {
                Color.DarkGray
            } else {
                Color.Black
            }
            val koreanHomeTeamName = viewModel.convertToKoreanTeamName(match.homeTeam.name)
            val koreanAwayTeamName = viewModel.convertToKoreanTeamName(match.awayTeam.name)


            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .height(190.dp),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(0.8.dp, Color.DarkGray)
            ) {

                MatchListHeaderRow(
                    listColor,
                    match.status,
                    viewModel.convertUtcToKoreanTime(match.utcDate)
                )

                MatchListRow(
                    crest = match.homeTeam.crest,
                    teamName = koreanHomeTeamName,
                    score = match.score?.fullTime?.home,
                    listRowColor = listColor
                )

                MatchListRow(
                    crest = match.awayTeam.crest,
                    teamName = koreanAwayTeamName,
                    score = match.score?.fullTime?.away,
                    listRowColor = listColor
                )

            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KmatchTheme {
        val viewModel = MainViewModel()

        MainScaffold(viewModel)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview1() {
    KmatchTheme {
        val matches: List<Match> = listOf(
            Match(
                id = 1,
                utcDate = "2023-12-31T12:00:00Z",
                status = "FINISHED",
                homeTeam = Team(
                    1,
                    "Home Teamasdfasdfasdfasdfasdfsadfsadfsdaf 1",
                    "home_team_logo_url"
                ),
                awayTeam = Team(2, "Away Team 1", "away_team_logo_url"),
                score = Score("HOME", "FULL_TIME", Time(3, 1))
            ),
            Match(
                id = 2,
                utcDate = "2023-12-31T12:00:00Z",
                status = "IN_PLAY",
                homeTeam = Team(1, "Home Team 1", "home_team_logo_url"),
                awayTeam = Team(2, "Away Team 1", "away_team_logo_url"),
                score = Score("HOME", "FULL_TIME", Time(3, 1))
            ),
        )
        val viewModel = MainViewModel()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {

            MainMatchList(viewModel, matches)
        }
    }
}








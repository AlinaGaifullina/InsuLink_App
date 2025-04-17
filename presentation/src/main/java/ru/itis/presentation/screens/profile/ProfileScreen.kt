package ru.itis.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::event
    val action by viewModel.action.collectAsStateWithLifecycle(null)


    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, top = 40.dp, bottom = 60.dp)
    ) {

        Text("Profile")

//        Row(verticalAlignment = Alignment.CenterVertically) {
//            SearchTextField(
//                modifier = Modifier.weight(1f),
//                searchValue = state.searchValue,
//                onChange = { searchValue -> eventHandler.invoke(HomeEvent.OnSearchValueChanged(searchValue)) }
//            )
//            SquareButton(
//                icon = R.drawable.ic_filter,
//                iconColor = MaterialTheme.colorScheme.primary,
//                backgroundColor = MaterialTheme.colorScheme.tertiary,
//                onClick = {  }
//            )
//            SquareButton(
//                icon = R.drawable.ic_search,
//                iconColor = MaterialTheme.colorScheme.primary,
//                backgroundColor = MaterialTheme.colorScheme.tertiary,
//                onClick = {  }
//            )
//        }
//
//        LazyColumn(
//            Modifier
//                .fillMaxSize()
//                .padding(top = 8.dp)
//        ) {
//            items(state.dishes.size) {
//                DishEntityCard(
//                    modifier = Modifier,
//                    dish = state.dishes[it],
//                    favouritesId = state.favouritesId,
//                    authorPhotoUrl = "https://i.pinimg.com/originals/c0/c2/16/c0c216b3743c6cb9fd67ab7df6b2c330.jpg",
//                    onCardClick = {
//                        eventHandler.invoke(
//                            HomeEvent.OnDishClick(
//                                state.dishes[it].dishId ?: throw IllegalArgumentException()
//                            )
//                        )
//                    },
//                    onLikeClick = {
//                        eventHandler.invoke(
//                            HomeEvent.OnLikeClick(state.dishes[it].dishId)
//                        )
//                    },
//                    onBuyClick = {
//                        eventHandler.invoke(
//                            HomeEvent.OnBuyClick(state.dishes[it].dishId)
//                        )
//                    },
//                    onPlusMinusClick = { sign ->
//                        eventHandler.invoke(
//                            HomeEvent.OnPlusMinusClick(state.dishes[it].dishId, sign)
//                        )
//                    },
//                    isBuyButtonPressed = state.cart.dishes.contains(state.dishes[it].dishId),
//                    numberOfDishes = if(state.cart.dishes.contains(state.dishes[it].dishId)){
//                        state.cart.numberOfDishes[state.cart.dishes.indexOf(state.dishes[it].dishId)]
//                    } else 0
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//        }
//
//        if (state.isLoading) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
//            }
//            return
//        }
    }
}
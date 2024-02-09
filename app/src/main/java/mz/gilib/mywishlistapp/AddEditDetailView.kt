package mz.gilib.mywishlistapp

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import mz.gilib.mywishlistapp.data.Wish
import mz.gilib.mywishlistapp.ui.theme.MyWishListAppTheme

@Composable
fun AddEditDetailView(
    id: Long,
    viewModel: WishViewModel,
    navController: NavController
) {

    val context = LocalContext.current

    val snackMessage = remember {
        mutableStateOf("")
    }

//    val scope = rememberCoroutineScope()

//    val scaffoldState = rememberScaffoldState()

    if(id != 0L) {
        val wish = viewModel.getWishById(id).collectAsState(initial = Wish())
        viewModel.wishTitleState = wish.value.title
        viewModel.wishDescriptionState = wish.value.description
    } else {
        viewModel.wishTitleState = ""
        viewModel.wishDescriptionState = ""
    }

    Scaffold(
        topBar = { AppBarView(
            title =
            if (id == 0L)
                stringResource(id = R.string.add_wish)
            else
                stringResource(id = R.string.update_wish),
            onBackNavClicked = { navController.navigateUp() }
        )},
//        scaffoldState = scaffoldState,
//        backgroundColor = MaterialTheme.colorScheme.background
    ) { it ->
        Column(
            modifier = Modifier
                .padding(it)
                .wrapContentSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            WishTextField(
                label = "Title",
                value = viewModel.wishTitleState,
                onValueChange = {
                    viewModel.onWishTitleChanged(it)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            WishTextField(
                label = "Description",
                value = viewModel.wishDescriptionState,
                onValueChange = {
                    viewModel.onWishDescriptionChanged(it)
                },
                modifier = Modifier.weight(1f),
                singleLine = false
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (
                        viewModel.wishTitleState.isNotBlank() &&
                        viewModel.wishDescriptionState.isNotBlank()
                    ) {
                        if (id == 0L) {
                            viewModel.addWish(
                                Wish(
                                    title = viewModel.wishTitleState.trim(),
                                    description = viewModel.wishDescriptionState.trim()
                                )
                            )
                            snackMessage.value = "Wish has been created"
                        }
                        else {
                            viewModel.updateWish(
                                Wish(
                                    id = id,
                                    title = viewModel.wishTitleState.trim(),
                                    description = viewModel.wishDescriptionState.trim()
                                )
                            )
                            snackMessage.value = "Wish has been updated"
                        }
                        navController.navigateUp()
                    } else {
                        snackMessage.value = "Please fill the mandatory fields"
                    }
//                    scope.launch {
//                        scaffoldState.snackbarHostState.showSnackbar(snackMessage.value)
//                        navController.navigateUp()
//                    }
                    Toast.makeText(context, snackMessage.value, Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.app_bar_color),
                    contentColor = Color.White

                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text =
                        if (id == 0L)
                            stringResource(id = R.string.add_wish)
                        else
                            stringResource(id = R.string.update_wish),
                    style = TextStyle(
                        fontSize = 18.sp,
                    ),
                    fontWeight = FontWeight.ExtraBold
                )
                
            }
        }
    }
}

@Composable
fun WishTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        singleLine = singleLine,
        onValueChange = onValueChange,
        label = { Text(text = label, fontWeight = FontWeight.ExtraBold) },
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colorScheme.inverseSurface,
            unfocusedBorderColor = MaterialTheme.colorScheme.inverseSurface,
            focusedBorderColor = colorResource(id = R.color.app_bar_color),
            cursorColor = colorResource(id = R.color.app_bar_color),
            focusedLabelColor = colorResource(id = R.color.app_bar_color)
        )
    )
}

@Preview (showBackground = true)
@Composable
fun Prev() {
    val viewModel: WishViewModel = viewModel()
    MyWishListAppTheme(darkTheme = true) {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
//            WishTextField(label = "Title", value = "Dfinefinrin", onValueChange = {})
            AddEditDetailView(id = 0, viewModel = viewModel, navController = rememberNavController())
        }
    }

}
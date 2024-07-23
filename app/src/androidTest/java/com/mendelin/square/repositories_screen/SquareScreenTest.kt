package com.mendelin.square.repositories_screen

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.ViewModelProvider
import com.mendelin.square.R
import com.mendelin.square.di.RepositoryModule
import com.mendelin.square.util.Constants
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(RepositoryModule::class)
class SquareScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<SquareActivity>()

    private lateinit var viewModel: SquareViewModel

    @Before
    fun setup() {
        hiltRule.inject()

        viewModel = ViewModelProvider(composeTestRule.activity)[SquareViewModel::class.java]

        composeTestRule.activity.setContent {
            SquareScreen(viewModel)
        }
    }

    @Test
    fun repository_list_is_loaded_items_are_displayed() {
        with(composeTestRule) {
            onNodeWithTag(Constants.LIST_TAG).assertIsDisplayed()

            onNodeWithText("JetBrains0").assertIsDisplayed()
            onNodeWithText("kotlin0").assertIsDisplayed()
            onNodeWithText("JetBrains0/kotlin0").assertIsDisplayed()
        }
    }

    @Test
    fun repository_list_is_loaded_clicking_on_item_shows_detail_screen() {
        with(composeTestRule) {
            onNodeWithTag(Constants.LIST_TAG).assertExists()

            onNodeWithText("JetBrains2")
                .assertExists()
                .performClick()

            onNodeWithText(activity.getString(R.string.description)).assertExists()
            onNodeWithText(activity.getString(R.string.license_type)).assertExists()
            onNodeWithText(activity.getString(R.string.programming_language)).assertExists()
            onNodeWithText("The Kotlin Programming Language.").assertExists()
        }
    }
}

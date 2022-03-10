package com.jerry.johnlewis.ui

import android.os.SystemClock
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.google.gson.Gson
import com.jerry.johnlewis.R
import com.jerry.johnlewis.units.recyclerItemAtPosition
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import getProduct
import getProductListResponse
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.net.HttpURLConnection

@HiltAndroidTest
@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    lateinit var mockWebServer: MockWebServer

    @Rule
    @JvmField
    var mMainActivityResult = ActivityTestRule(MainActivity::class.java, true, false)


    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    fun mockNetworkResponse(path: String, responseCode: Int) = mockWebServer.enqueue(
        MockResponse()
            .setResponseCode(responseCode)
            .setBody(getJson(path))
    )

    @Test
    fun test_all_success() {
        mockNetworkResponse("list", HttpURLConnection.HTTP_OK)

        mMainActivityResult.launchActivity(null)

        //waiting loading
        SystemClock.sleep(1000)

        //Check if item at 0th position is having 0th element in json
        onView(withId(R.id.productList))
            .check(
                ViewAssertions.matches(
                    recyclerItemAtPosition(
                        0,
                        ViewMatchers.hasDescendant(ViewMatchers.withText("title1"))
                    )
                )
            )

        SystemClock.sleep(1000)
        mockNetworkResponse("detail", HttpURLConnection.HTTP_OK)

        //Check on 1 item
        clickOnViewAtRow(1)

        //goto detail page and waiting loading
        SystemClock.sleep(1000)

        Espresso.onView(withId(R.id.tvPrice)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(
                    "£1.00"
                )
            )
        )

        SystemClock.sleep(2000)
    }

    @Test
    fun test_with_list_load_fail_and_then_retry_success() {
        mockNetworkResponse("list", HttpURLConnection.HTTP_NOT_FOUND)

        mMainActivityResult.launchActivity(null)

        //waiting loading
        SystemClock.sleep(1000)

        //check  error dialog
        onView(withId(android.R.id.message)).check(
            ViewAssertions.matches(ViewMatchers.withSubstring("404"))
        )

        mockNetworkResponse("list", HttpURLConnection.HTTP_OK)

        //click retry
        onView(withId(android.R.id.button1)).perform(ViewActions.click())

        //waiting loading
        SystemClock.sleep(1000)

        //Check if item at 0th position is having 0th element in json
        Espresso.onView(withId(R.id.productList))
            .check(
                ViewAssertions.matches(
                    recyclerItemAtPosition(
                        0,
                        ViewMatchers.hasDescendant(ViewMatchers.withText("title1"))
                    )
                )
            )
    }

    @Test
    fun test_with_detail_load_fail_and_then_retry_success() {
        mockNetworkResponse("list", HttpURLConnection.HTTP_OK)

        mMainActivityResult.launchActivity(null)

        //waiting loading
        SystemClock.sleep(1000)

        //Check if item at 0th position is having 0th element in json
        onView(withId(R.id.productList))
            .check(
                ViewAssertions.matches(
                    recyclerItemAtPosition(
                        0,
                        ViewMatchers.hasDescendant(ViewMatchers.withText("title1"))
                    )
                )
            )

        SystemClock.sleep(1000)

        mockNetworkResponse("detail", HttpURLConnection.HTTP_NOT_FOUND)

        //Check on 1 item
        clickOnViewAtRow(1)

        //goto detail page and waiting loading
        SystemClock.sleep(1000)

        //check  error dialog
        onView(withId(android.R.id.message)).check(
            ViewAssertions.matches(ViewMatchers.withSubstring("404"))
        )

        mockNetworkResponse("detail", HttpURLConnection.HTTP_OK)

        //click retry
        onView(withId(android.R.id.button1)).perform(ViewActions.click())

        SystemClock.sleep(1000)

        Espresso.onView(withId(R.id.tvPrice)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(
                    "£1.00"
                )
            )
        )
    }

    fun getJson(path: String):String{
        if("detail".equals(path))
            return Gson().toJson(getProduct())
        else if("list".equals(path))
                return Gson().toJson(getProductListResponse())
        return ""
    }

    private fun clickOnViewAtRow(position: Int) {
        Espresso.onView(ViewMatchers.withId(R.id.productList)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>
            (position, ClickOnButtonView()))
    }

    inner class ClickOnButtonView : ViewAction {
        internal var click = ViewActions.click()

        override fun getConstraints(): Matcher<View> {
            return click.constraints
        }

        override fun getDescription(): String {
            return " click on custom button view"
        }

        override fun perform(uiController: UiController, view: View) {
            //btnClickMe -> Custom row item view button
            click.perform(uiController, view.findViewById(R.id.sectionItemContainer))
        }
    }
}
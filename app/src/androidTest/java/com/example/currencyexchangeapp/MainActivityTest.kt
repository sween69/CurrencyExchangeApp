package com.example.currencyexchangeapp

import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    //private lateinit var activity: MainActivity

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    /*@Before
    fun setUp() {
        activityRule.scenario.onActivity { activity = it }
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // spinner
        val currencyList = listOf("SGD", "MYR")
        val baseCurrencySpinner = activity.findViewById<Spinner>(R.id.baseCurrencySpinner)
        val targetCurrencySpinner = activity.findViewById<Spinner>(R.id.targetCurrencySpinner)
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, currencyList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        baseCurrencySpinner.adapter = adapter
        targetCurrencySpinner.adapter = adapter
    }*/

    @Test
    fun testCheckRateButton() {
        onView(withId(R.id.checkRateButton)).perform(click())
        onView(withId(R.id.totalAmountTextView)).check(matches(isDisplayed()))
        //onView(withId(R.id.baseRateTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun testClearButton() {
        onView(withId(R.id.clearButtonImageView)).perform(click())
        onView(withId(R.id.amountEditText)).check(matches(withText("")))
        onView(withId(R.id.totalAmountTextView)).check(matches(withText("")))
    }

    /*@Test
    fun testBaseCurrencySpinnerWithCheckRateButton() {

        onView(withId(R.id.baseCurrencySpinner)).perform(click())
        onData(
            allOf(
                `is`(instanceOf(String::class.java)),
                `is`("SGD")
            )
        ).perform(click())

        onView(withId(R.id.checkRateButton)).perform(click())

        onView(withId(R.id.baseRateTextView)).check(matches(withText(containsString("SGD"))))
    }*/

}

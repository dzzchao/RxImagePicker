package com.qingmei2.sample.wechat

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.provider.MediaStore
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.GrantPermissionRule
import com.qingmei2.rximagepicker_extension.ui.BasePreviewActivity
import com.qingmei2.sample.R
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class WechatActivityTest {

    private val successActivityResult: Instrumentation.ActivityResult =
            with(Intent()) {
                putExtra(BasePreviewActivity.EXTRA_RESULT_BUNDLE, EXTRA_BUNDLE)
                putExtra(BasePreviewActivity.EXTRA_RESULT_APPLY, EXTRA_RESULT_APPLY)

                Instrumentation.ActivityResult(Activity.RESULT_OK, this)
            }

    @Rule
    @JvmField
    var systemActivityTestRule = IntentsTestRule<WechatActivity>(WechatActivity::class.java)
    @Rule
    @JvmField
    var grantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @Test
    fun testPickCamera() {

        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(successActivityResult)

        onView(withId(R.id.fabPickCamera)).perform(click())

        onView(withId(R.id.imageView)).check(matches(isDisplayed()))
    }

    @Test
    fun testPickGallery() {
        intending(allOf(
                toPackage("com.qingmei2.rximagepicker_extension_wechat"),
                hasComponent(".ui.WechatImagePickerActivity")
        )).respondWith(successActivityResult)

        onView(withId(R.id.imageView)).check(matches(isDisplayed()))

        onView(withId(R.id.fabGallery)).perform(click())

        onView(withId(R.id.imageView)).check(doesNotExist())
    }

    companion object Mock {
        private const val EXTRA_BUNDLE = "123"
        private const val EXTRA_RESULT_APPLY = "456"
    }
}
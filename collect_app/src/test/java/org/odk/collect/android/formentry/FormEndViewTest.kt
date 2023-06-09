package org.odk.collect.android.formentry

import android.app.Application
import android.view.View
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.material.button.MaterialButton
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.odk.collect.android.R

@RunWith(AndroidJUnit4::class)
class FormEndViewTest {
    private val context: Application =
        ApplicationProvider.getApplicationContext<Application>().also {
            it.setTheme(R.style.Theme_Collect) // Needed for ?colorSurfaceContainerHighest
        }
    private val formEndViewModel = mock<FormEndViewModel>()
    private val listener = mock<FormEndView.Listener>()

    @Test
    fun `form title is displayed correctly`() {
        val view = FormEndView(context, "blah", formEndViewModel, listener)
        assertThat(view.findViewById<TextView>(R.id.description).text, equalTo(context.getString(R.string.save_enter_data_description, "blah")))
    }

    @Test
    fun `when saving drafts is enabled in settings should 'Save as draft' button be visible`() {
        whenever(formEndViewModel.isSaveDraftEnabled()).thenReturn(true)
        val view = FormEndView(context, "blah", formEndViewModel, listener)
        assertThat(view.findViewById<MaterialButton>(R.id.save_as_draft).visibility, equalTo(View.VISIBLE))
    }

    @Test
    fun `when saving drafts is disabled in settings should 'Save as draft' button be hidden`() {
        whenever(formEndViewModel.isSaveDraftEnabled()).thenReturn(false)
        val view = FormEndView(context, "blah", formEndViewModel, listener)
        assertThat(view.findViewById<MaterialButton>(R.id.save_as_draft).visibility, equalTo(View.GONE))
    }

    @Test
    fun `when 'Save as draft' button is clicked then onSaveClicked is called with false value`() {
        whenever(formEndViewModel.isSaveDraftEnabled()).thenReturn(true)
        val view = FormEndView(context, "blah", formEndViewModel, listener)
        view.findViewById<MaterialButton>(R.id.save_as_draft).performClick()
        verify(listener).onSaveClicked(false)
    }

    @Test
    fun `when finalizing forms is enabled in settings should 'Finalize' button be visible`() {
        whenever(formEndViewModel.isFinalizeEnabled()).thenReturn(true)
        val view = FormEndView(context, "blah", formEndViewModel, listener)
        assertThat(view.findViewById<MaterialButton>(R.id.finalize).visibility, equalTo(View.VISIBLE))
    }

    @Test
    fun `when finalizing forms is disabled in settings should 'Finalize' button be hidden`() {
        whenever(formEndViewModel.isFinalizeEnabled()).thenReturn(false)
        val view = FormEndView(context, "blah", formEndViewModel, listener)
        assertThat(view.findViewById<MaterialButton>(R.id.finalize).visibility, equalTo(View.GONE))
    }

    @Test
    fun `when 'Finalize' button is clicked then onSaveClicked is called with true value`() {
        whenever(formEndViewModel.isFinalizeEnabled()).thenReturn(true)
        val view = FormEndView(context, "blah", formEndViewModel, listener)
        view.findViewById<MaterialButton>(R.id.finalize).performClick()
        verify(listener).onSaveClicked(true)
    }

    @Test
    fun `when form should not be sent automatically then 'Finalize' button should be displayed`() {
        whenever(formEndViewModel.shouldFormBeSentAutomatically()).thenReturn(false)
        val view = FormEndView(context, "blah", formEndViewModel, listener)
        assertThat(view.findViewById<MaterialButton>(R.id.finalize).text, equalTo(context.getString(R.string.finalize)))
    }

    @Test
    fun `when form should be sent automatically then 'Send' button should be displayed`() {
        whenever(formEndViewModel.shouldFormBeSentAutomatically()).thenReturn(true)
        val view = FormEndView(context, "blah", formEndViewModel, listener)
        assertThat(view.findViewById<MaterialButton>(R.id.finalize).text, equalTo(context.getString(R.string.send)))
    }
}

package your.package.name

import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputConnection

class PcKeyboardService : InputMethodService(), KeyboardView.OnKeyboardActionListener {

    private lateinit var keyboardView: KeyboardView
    private lateinit var keyboard: Keyboard
    private var shiftOn = false
    private var ctrlOn = false
    private var altOn = false

    override fun onCreateInputView(): View {
        keyboardView = layoutInflater.inflate(R.layout.keyboard_view, null) as KeyboardView
        keyboard = Keyboard(this, R.xml.qwerty_pc)
        keyboardView.keyboard = keyboard
        keyboardView.setOnKeyboardActionListener(this)
        keyboardView.isPreviewEnabled = false
        return keyboardView
    }

    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
        val ic: InputConnection = currentInputConnection ?: return

        when (primaryCode) {
            Keyboard.KEYCODE_DELETE -> ic.deleteSurroundingText(1, 0)

            Keyboard.KEYCODE_SHIFT -> {
                shiftOn = !shiftOn
                keyboard.isShifted = shiftOn
                keyboardView.invalidateAllKeys()
            }

            Keyboard.KEYCODE_MODE_CHANGE -> {
                keyboard = when (keyboard.xmlLayoutResId) {
                    R.xml.qwerty_pc -> Keyboard(this, R.xml.symbols)
                    R.xml.symbols -> Keyboard(this, R.xml.farsi)
                    else -> Keyboard(this, R.xml.qwerty_pc)
                }
                keyboardView.keyboard = keyboard
            }

            Keyboard.KEYCODE_DONE -> ic.sendKeyEvent(
                KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER)
            )

            -4 -> switchToNextInputMethod(false) // تغییر کیبورد

            -101 -> { // Ctrl
                ctrlOn = !ctrlOn
            }

            -102 -> { // Alt
                altOn = !altOn
            }

            else -> {
                val code = primaryCode
                val text = code.toChar().toString()
                val metaState = (if (ctrlOn) KeyEvent.META_CTRL_ON else 0) or
                                (if (altOn) KeyEvent.META_ALT_ON else 0)

                ic.commitText(
                    if (shiftOn && text.matches(Regex("[a-z]"))) text.uppercase() else text,
                    1
                )

                ic.sendKeyEvent(
                    KeyEvent(0, 0, KeyEvent.ACTION_DOWN, code, 0, metaState)
                )

                ctrlOn = false
                altOn = false
            }
        }
    }

    override fun onPress(primaryCode: Int) {}
    override fun onRelease(primaryCode: Int) {}
    override fun onText(text: CharSequence?) {}
    override fun swipeLeft() {}
    override fun swipeRight() {}
    override fun swipeDown() {}
    override fun swipeUp() {}
}
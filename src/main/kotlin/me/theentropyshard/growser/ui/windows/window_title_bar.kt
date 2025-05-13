@file:Suppress("PrivatePropertyName", "FunctionName", "unused", "LocalVariableName", "SpellCheckingInspection",
    "UnusedVariable"
)

package me.theentropyshard.growser.ui.windows

import androidx.compose.ui.unit.dp
import com.sun.jna.Native
import com.sun.jna.NativeLibrary
import com.sun.jna.Structure
import com.sun.jna.platform.win32.BaseTSD.LONG_PTR
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef.*
import com.sun.jna.platform.win32.WinUser.*
import com.sun.jna.win32.W32APIOptions

// --------------------- https://github.com/JetBrains/compose-multiplatform/issues/3388#issuecomment-1672732504

@Suppress("FunctionName")
private interface User32Ex : User32 {
    fun SetWindowLong(hWnd: HWND, nIndex: Int, wndProc: WindowProc): LONG_PTR
    fun SetWindowLongPtr(hWnd: HWND, nIndex: Int, wndProc: WindowProc): LONG_PTR
    fun CallWindowProc(proc: LONG_PTR, hWnd: HWND, uMsg: Int, uParam: WPARAM, lParam: LPARAM): LRESULT
}

// See https://stackoverflow.com/q/62240901
@Structure.FieldOrder(
    "leftBorderWidth",
    "rightBorderWidth",
    "topBorderHeight",
    "bottomBorderHeight"
)
data class WindowMargins(
    @JvmField var leftBorderWidth: Int,
    @JvmField var rightBorderWidth: Int,
    @JvmField var topBorderHeight: Int,
    @JvmField var bottomBorderHeight: Int
) : Structure(), Structure.ByReference

class CustomWindowProcedure(private val windowHandle: HWND) : WindowProc {
    // See https://learn.microsoft.com/en-us/windows/win32/winmsg/about-messages-and-message-queues#system-defined-messages
    private val WM_NCCALCSIZE = 0x0083
    private val WM_NCHITTEST = 0x0084
    private val GWLP_WNDPROC = -4
    private val margins = WindowMargins(
        leftBorderWidth = 0,
        topBorderHeight = 0,
        rightBorderWidth = -1,
        bottomBorderHeight = -1
    )
    private val USER32EX =
        runCatching { Native.load("user32", User32Ex::class.java, W32APIOptions.DEFAULT_OPTIONS) }
            .onFailure { println("Could not load user32 library") }
            .getOrNull()

    // The default window procedure to call its methods when the default method behaviour is desired/sufficient
    private var defaultWndProc = if (is64Bit()) {
        USER32EX?.SetWindowLongPtr(windowHandle, GWLP_WNDPROC, this) ?: LONG_PTR(-1)
    } else {
        USER32EX?.SetWindowLong(windowHandle, GWLP_WNDPROC, this) ?: LONG_PTR(-1)
    }

    init {
        enableResizability()
        enableBorderAndShadow()
        // enableTransparency(alpha = 255.toByte())
    }

    override fun callback(hWnd: HWND, uMsg: Int, wParam: WPARAM, lParam: LPARAM): LRESULT {
        var lresult: LRESULT
        return when (uMsg) {
            // Returns 0 to make the window not draw the non-client area (title bar and border)
            // thus effectively making all the window our client area
            WM_NCCALCSIZE -> {
                LRESULT(0)
            }
            // The CallWindowProc function is used to pass messages that
            // are not handled by our custom callback function to the default windows procedure
            //WM_NCHITTEST -> { USER32EX?.CallWindowProc(defaultWndProc, hWnd, uMsg, wParam, lParam) ?: LRESULT(0) }
            WM_NCHITTEST -> {
                lresult = this.BorderLessHitTest(hWnd, uMsg, wParam, lParam)
                if (lresult == LRESULT(0)) {
                    return USER32EX?.CallWindowProc(defaultWndProc, hWnd, uMsg, wParam, lParam) ?: LRESULT(0)
                }
                return lresult
            }

            WM_DESTROY -> {
                USER32EX?.CallWindowProc(defaultWndProc, hWnd, uMsg, wParam, lParam) ?: LRESULT(0)
            }

            else -> {
                USER32EX?.CallWindowProc(defaultWndProc, hWnd, uMsg, wParam, lParam) ?: LRESULT(0)
            }
        }
    }

    object CustomDecorationParameters {
        var titleBarHeight: Int = 42.dp.value.toInt()
        var controlBoxWidth: Int = 150
        var iconWidth: Int = 40
        var extraLeftReservedWidth: Int = 0
        var extraRightReservedWidth: Int = 0
        var maximizedWindowFrameThickness: Int = 10
        var frameResizeBorderThickness: Int = 4
        var frameBorderThickness: Int = 1
    }

    // this method ------ https://github.com/kalibetre/CustomDecoratedJFrame/blob/master/BeranaUI/src/com/beranabyte/ui/customdecoration/CustomDecorationWindowProc.java
    fun BorderLessHitTest(hWnd: HWND?, message: Int, wParam: WPARAM?, lParam: LPARAM?): LRESULT {
        val borderOffset: Int = CustomDecorationParameters.maximizedWindowFrameThickness
        val borderThickness: Int = CustomDecorationParameters.frameResizeBorderThickness

        val ptMouse = POINT()
        val rcWindow = RECT()
        User32.INSTANCE.GetCursorPos(ptMouse)
        User32.INSTANCE.GetWindowRect(hWnd, rcWindow)

        var uRow = 1
        var uCol = 1
        var fOnResizeBorder = false
        var fOnFrameDrag = false

        val topOffset =
            if (CustomDecorationParameters.titleBarHeight == 0) borderThickness else CustomDecorationParameters.titleBarHeight
        if (ptMouse.y >= rcWindow.top && ptMouse.y < rcWindow.top + topOffset + borderOffset) {
            fOnResizeBorder = (ptMouse.y < (rcWindow.top + borderThickness)) // Top Resizing
            if (!fOnResizeBorder) {
                fOnFrameDrag =
                    (ptMouse.y <= rcWindow.top + CustomDecorationParameters.titleBarHeight + borderOffset)
                            && (ptMouse.x < (rcWindow.right - ((CustomDecorationParameters.controlBoxWidth
                            + borderOffset + CustomDecorationParameters.extraRightReservedWidth))))
                            && (ptMouse.x > ((rcWindow.left + CustomDecorationParameters.iconWidth
                            + borderOffset + CustomDecorationParameters.extraLeftReservedWidth)))
            }
            uRow = 0 // Top Resizing or Caption Moving
        } else if (ptMouse.y < rcWindow.bottom && ptMouse.y >= rcWindow.bottom - borderThickness) uRow =
            2 // Bottom Resizing

        if (ptMouse.x >= rcWindow.left && ptMouse.x < rcWindow.left + borderThickness) uCol = 0 // Left Resizing
        else if (ptMouse.x < rcWindow.right && ptMouse.x >= rcWindow.right - borderThickness) uCol = 2 // Right Resizing


        val HTTOPLEFT = 13
        val HTTOP = 12
        val HTCAPTION = 2
        val HTTOPRIGHT = 14
        val HTLEFT = 10
        val HTNOWHERE = 0
        val HTRIGHT = 11
        val HTBOTTOMLEFT = 16
        val HTBOTTOM = 15
        val HTBOTTOMRIGHT = 17
        val HTSYSMENU = 3

        val hitTests = arrayOf<IntArray?>(
            intArrayOf(
                HTTOPLEFT,
                if (fOnResizeBorder) HTTOP else if (fOnFrameDrag) HTCAPTION else HTNOWHERE,
                HTTOPRIGHT
            ),
            intArrayOf(HTLEFT, HTNOWHERE, HTRIGHT),
            intArrayOf(HTBOTTOMLEFT, HTBOTTOM, HTBOTTOMRIGHT),
        )

        return LRESULT(hitTests[uRow]!![uCol].toLong())
    }

    /**
     * For this to take effect, also set `resizable` argument of Compose Window to `true`.
     */
    private fun enableResizability() {
        val style = USER32EX?.GetWindowLong(windowHandle, GWL_STYLE) ?: return
        val newStyle = style or WS_CAPTION
        USER32EX.SetWindowLong(windowHandle, GWL_STYLE, newStyle)
    }

    /**
     * To disable window border and shadow, pass (0, 0, 0, 0) as window margins
     * (or, simply, don't call this function).
     */
    private fun enableBorderAndShadow() {
        val dwmApi = "dwmapi"
            .runCatching(NativeLibrary::getInstance)
            .onFailure { println("Could not load dwmapi library") }
            .getOrNull()
        dwmApi
            ?.runCatching { getFunction("DwmExtendFrameIntoClientArea") }
            ?.onFailure { println("Could not enable window native decorations (border/shadow/rounded corners)") }
            ?.getOrNull()
            ?.invoke(arrayOf(windowHandle, margins))
    }

    private fun is64Bit(): Boolean {
        val bitMode = System.getProperty("com.ibm.vm.bitmode")
        val model = System.getProperty("sun.arch.data.model", bitMode)
        return model == "64"
    }
}
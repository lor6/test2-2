// Generated by jextract

package foreign.c;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
class constants$20 {

    static final FunctionDescriptor _getw$FUNC = FunctionDescriptor.of(Constants$root.C_LONG$LAYOUT,
        Constants$root.C_POINTER$LAYOUT
    );
    static final MethodHandle _getw$MH = RuntimeHelper.downcallHandle(
        "_getw",
        constants$20._getw$FUNC
    );
    static final FunctionDescriptor _putw$FUNC = FunctionDescriptor.of(Constants$root.C_LONG$LAYOUT,
        Constants$root.C_LONG$LAYOUT,
        Constants$root.C_POINTER$LAYOUT
    );
    static final MethodHandle _putw$MH = RuntimeHelper.downcallHandle(
        "_putw",
        constants$20._putw$FUNC
    );
    static final FunctionDescriptor fgetwchar$FUNC = FunctionDescriptor.of(Constants$root.C_SHORT$LAYOUT);
    static final MethodHandle fgetwchar$MH = RuntimeHelper.downcallHandle(
        "fgetwchar",
        constants$20.fgetwchar$FUNC
    );
    static final FunctionDescriptor fputwchar$FUNC = FunctionDescriptor.of(Constants$root.C_SHORT$LAYOUT,
        Constants$root.C_SHORT$LAYOUT
    );
    static final MethodHandle fputwchar$MH = RuntimeHelper.downcallHandle(
        "fputwchar",
        constants$20.fputwchar$FUNC
    );
    static final FunctionDescriptor getw$FUNC = FunctionDescriptor.of(Constants$root.C_LONG$LAYOUT,
        Constants$root.C_POINTER$LAYOUT
    );
    static final MethodHandle getw$MH = RuntimeHelper.downcallHandle(
        "getw",
        constants$20.getw$FUNC
    );
    static final FunctionDescriptor putw$FUNC = FunctionDescriptor.of(Constants$root.C_LONG$LAYOUT,
        Constants$root.C_LONG$LAYOUT,
        Constants$root.C_POINTER$LAYOUT
    );
    static final MethodHandle putw$MH = RuntimeHelper.downcallHandle(
        "putw",
        constants$20.putw$FUNC
    );
}



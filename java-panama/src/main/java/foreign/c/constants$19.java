// Generated by jextract

package foreign.c;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
class constants$19 {

    static final FunctionDescriptor vsnwprintf$FUNC = FunctionDescriptor.of(Constants$root.C_LONG$LAYOUT,
        Constants$root.C_POINTER$LAYOUT,
        Constants$root.C_LONG_LONG$LAYOUT,
        Constants$root.C_POINTER$LAYOUT,
        Constants$root.C_POINTER$LAYOUT
    );
    static final MethodHandle vsnwprintf$MH = RuntimeHelper.downcallHandle(
        "vsnwprintf",
        constants$19.vsnwprintf$FUNC
    );
    static final FunctionDescriptor vwscanf$FUNC = FunctionDescriptor.of(Constants$root.C_LONG$LAYOUT,
        Constants$root.C_POINTER$LAYOUT,
        Constants$root.C_POINTER$LAYOUT
    );
    static final MethodHandle vwscanf$MH = RuntimeHelper.downcallHandle(
        "vwscanf",
        constants$19.vwscanf$FUNC
    );
    static final FunctionDescriptor vfwscanf$FUNC = FunctionDescriptor.of(Constants$root.C_LONG$LAYOUT,
        Constants$root.C_POINTER$LAYOUT,
        Constants$root.C_POINTER$LAYOUT,
        Constants$root.C_POINTER$LAYOUT
    );
    static final MethodHandle vfwscanf$MH = RuntimeHelper.downcallHandle(
        "vfwscanf",
        constants$19.vfwscanf$FUNC
    );
    static final FunctionDescriptor vswscanf$FUNC = FunctionDescriptor.of(Constants$root.C_LONG$LAYOUT,
        Constants$root.C_POINTER$LAYOUT,
        Constants$root.C_POINTER$LAYOUT,
        Constants$root.C_POINTER$LAYOUT
    );
    static final MethodHandle vswscanf$MH = RuntimeHelper.downcallHandle(
        "vswscanf",
        constants$19.vswscanf$FUNC
    );
    static final FunctionDescriptor _fgetwchar$FUNC = FunctionDescriptor.of(Constants$root.C_SHORT$LAYOUT);
    static final MethodHandle _fgetwchar$MH = RuntimeHelper.downcallHandle(
        "_fgetwchar",
        constants$19._fgetwchar$FUNC
    );
    static final FunctionDescriptor _fputwchar$FUNC = FunctionDescriptor.of(Constants$root.C_SHORT$LAYOUT,
        Constants$root.C_SHORT$LAYOUT
    );
    static final MethodHandle _fputwchar$MH = RuntimeHelper.downcallHandle(
        "_fputwchar",
        constants$19._fputwchar$FUNC
    );
}



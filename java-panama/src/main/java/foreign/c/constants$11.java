// Generated by jextract

package foreign.c;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
class constants$11 {

    static final FunctionDescriptor fseek$FUNC = FunctionDescriptor.of(Constants$root.C_LONG$LAYOUT,
        Constants$root.C_POINTER$LAYOUT,
        Constants$root.C_LONG$LAYOUT,
        Constants$root.C_LONG$LAYOUT
    );
    static final MethodHandle fseek$MH = RuntimeHelper.downcallHandle(
        "fseek",
        constants$11.fseek$FUNC
    );
    static final FunctionDescriptor ftell$FUNC = FunctionDescriptor.of(Constants$root.C_LONG$LAYOUT,
        Constants$root.C_POINTER$LAYOUT
    );
    static final MethodHandle ftell$MH = RuntimeHelper.downcallHandle(
        "ftell",
        constants$11.ftell$FUNC
    );
    static final FunctionDescriptor rewind$FUNC = FunctionDescriptor.ofVoid(
        Constants$root.C_POINTER$LAYOUT
    );
    static final MethodHandle rewind$MH = RuntimeHelper.downcallHandle(
        "rewind",
        constants$11.rewind$FUNC
    );
    static final FunctionDescriptor fgetpos$FUNC = FunctionDescriptor.of(Constants$root.C_LONG$LAYOUT,
        Constants$root.C_POINTER$LAYOUT,
        Constants$root.C_POINTER$LAYOUT
    );
    static final MethodHandle fgetpos$MH = RuntimeHelper.downcallHandle(
        "fgetpos",
        constants$11.fgetpos$FUNC
    );
    static final FunctionDescriptor fsetpos$FUNC = FunctionDescriptor.of(Constants$root.C_LONG$LAYOUT,
        Constants$root.C_POINTER$LAYOUT,
        Constants$root.C_POINTER$LAYOUT
    );
    static final MethodHandle fsetpos$MH = RuntimeHelper.downcallHandle(
        "fsetpos",
        constants$11.fsetpos$FUNC
    );
    static final FunctionDescriptor feof$FUNC = FunctionDescriptor.of(Constants$root.C_LONG$LAYOUT,
        Constants$root.C_POINTER$LAYOUT
    );
    static final MethodHandle feof$MH = RuntimeHelper.downcallHandle(
        "feof",
        constants$11.feof$FUNC
    );
}



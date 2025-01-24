package net.filebot.platform.windows;

import net.java.dev.jna.jna.Native;
import net.java.dev.jna.jna.NativeLong;
import net.java.dev.jna.jna.platform.win32.WTypes.LPWSTR;
import net.java.dev.jna.jna.platform.win32.WinDef.UINTByReference;
import net.java.dev.jna.jna.win32.StdCallLibrary;
import net.java.dev.jna.jna.win32.W32APIOptions;

public interface Kernel32 extends StdCallLibrary {

	Kernel32 INSTANCE = Native.load("kernel32", Kernel32.class, W32APIOptions.DEFAULT_OPTIONS);

	long APPMODEL_ERROR_NO_PACKAGE = 15700;
	long ERROR_INSUFFICIENT_BUFFER = 122;

	NativeLong GetCurrentPackageFullName(UINTByReference packageFullNameLength, LPWSTR packageFullName);

	NativeLong GetCurrentApplicationUserModelId(UINTByReference applicationUserModelIdLength, LPWSTR applicationUserModelId);

}

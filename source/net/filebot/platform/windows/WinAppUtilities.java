package net.filebot.platform.windows;

import static javax.swing.BorderFactory.*;
import static net.filebot.Logging.*;
import static net.filebot.ui.ThemeSupport.*;

import java.util.logging.Level;

import javax.swing.UIManager;

import net.java.dev.jna.jna.Memory;
import net.java.dev.jna.jna.Native;
import net.java.dev.jna.jna.NativeLong;
import net.java.dev.jna.jna.WString;
import net.java.dev.jna.jna.platform.win32.Shell32;
import net.java.dev.jna.jna.platform.win32.W32Errors;
import net.java.dev.jna.jna.platform.win32.WTypes.LPWSTR;
import net.java.dev.jna.jna.platform.win32.WinDef.UINT;
import net.java.dev.jna.jna.platform.win32.WinDef.UINTByReference;
import net.java.dev.jna.jna.platform.win32.WinError;
import net.java.dev.jna.jna.ptr.PointerByReference;

public class WinAppUtilities {

	public static void setAppUserModelID(String aumid) {
		try {
			Shell32.INSTANCE.SetCurrentProcessExplicitAppUserModelID(new WString(aumid));
		} catch (Throwable t) {
			debug.log(Level.WARNING, t.getMessage(), t);
		}
	}

	public static String getAppUserModelID() {
		try {
			PointerByReference ppszAppID = new PointerByReference();
			if (Shell32.INSTANCE.GetCurrentProcessExplicitAppUserModelID(ppszAppID).equals(WinError.S_OK)) {
				return ppszAppID.getValue().getWideString(0);
			}
		} catch (Throwable t) {
			debug.log(Level.WARNING, t.getMessage(), t);
		}
		return null;
	}

	public static String getPackageName() {
		UINTByReference packageFullNameLength = new UINTByReference(new UINT(64));
		LPWSTR packageFullName = new LPWSTR(new Memory(packageFullNameLength.getValue().intValue() * Native.WCHAR_SIZE));

		NativeLong r = Kernel32.INSTANCE.GetCurrentPackageFullName(packageFullNameLength, packageFullName);

		if (r.intValue() != W32Errors.ERROR_SUCCESS) {
			throw new IllegalStateException(String.format("Kernel32.GetCurrentPackageFullName (%s)", r));
		}

		return packageFullName.getValue();
	}

	public static String getPackageAppUserModelID() {
		UINTByReference applicationUserModelIdLength = new UINTByReference(new UINT(64));
		LPWSTR applicationUserModelId = new LPWSTR(new Memory(applicationUserModelIdLength.getValue().intValue() * Native.WCHAR_SIZE));

		NativeLong r = Kernel32.INSTANCE.GetCurrentApplicationUserModelId(applicationUserModelIdLength, applicationUserModelId);

		if (r.intValue() != W32Errors.ERROR_SUCCESS) {
			throw new IllegalStateException(String.format("Kernel32.GetCurrentApplicationUserModelId (%s)", r));
		}

		return applicationUserModelId.getValue();
	}

	public static void initializeApplication(String aumid) {
		if (aumid != null) {
			setAppUserModelID(aumid);
		}

		// improved UI defaults
		UIManager.put("TitledBorder.border", createCompoundBorder(createLineBorder(getColor(0xD7D7D7), 1, true), createCompoundBorder(createMatteBorder(6, 5, 6, 5, getColor(0xE5E5E5)), createEmptyBorder(0, 2, 0, 2))));
	}

	private WinAppUtilities() {
		throw new UnsupportedOperationException();
	}

}

Windows registry error: Could not open/create prefs root node Software\JavaSoft\Prefs at ... 	| Print |

Especially with Windows 7, the JVM has not by default the permission to write into the Windows registry where the backing store for java.util.prefs.preferences is located under MS-Windows.

When executing either the ReverseXSL transformer, or even the Regex tester program, one can get errors like: Could not open/create prefs root node Software\JavaSoft\Prefs at root 0x80000002. Windows RegCreateKeyEx

This does prevent registering a license. It does not prevent the software to perform transformations in the free software mode.

Fixing the issue is simply a matter of granting the necessary permissions to the registry root key at stake.

Run regedit.exe as administrator (regedit.exe is located in the c:\Windows operating system root directory).
Go to key HKEY_LOCAL_MACHINE\SOFTWARE\JavaSoft\Prefs. Right click to set permissions. Check a mark in the Full Control check box for the user(s) that need executing the reverseXSL software
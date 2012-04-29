// Copyright (c) 2011-2012, Leif Bruder <leifbruder@googlemail.com>
// 
// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice appear in all copies.
// 
// THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
// WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
// ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
// WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
// ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
// OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.

package org.lb.plc.ams;

public class ErrorCode {
	private final long errorCode;

	private ErrorCode(final long errorCode) {
		this.errorCode = errorCode;
	}

	public static ErrorCode valueOf(final long errorCode) {
		return new ErrorCode(errorCode);
	}

	public static ErrorCode noError() {
		return valueOf(0);
	}

	public boolean isError() {
		return errorCode != 0;
	}

	// Automatically generated from Beckhoff Information System
	@Override
	public String toString() {
		switch ((int) errorCode) {
		case 0:
			return "no error";
		case 1:
			return "Internal error";
		case 2:
			return "No Rtime";
		case 3:
			return "Allocation locked memory error";
		case 4:
			return "Insert mailbox error";
		case 5:
			return "Wrong receive HMSG";
		case 6:
			return "target port not found";
		case 7:
			return "target machine not found";
		case 8:
			return "Unknown command ID";
		case 9:
			return "Bad task ID";
		case 10:
			return "No IO";
		case 11:
			return "Unknown AMS command";
		case 12:
			return "Win 32 error";
		case 13:
			return "Port not connected";
		case 14:
			return "Invalid AMS length";
		case 15:
			return "Invalid AMS Net ID";
		case 16:
			return "Low Installation level";
		case 17:
			return "No debug available";
		case 18:
			return "Port disabled";
		case 19:
			return "Port already connected";
		case 20:
			return "AMS Sync Win32 error";
		case 21:
			return "AMS Sync Timeout";
		case 22:
			return "AMS Sync AMS error";
		case 23:
			return "AMS Sync no index map";
		case 24:
			return "Invalid AMS port";
		case 25:
			return "No memory";
		case 26:
			return "TCP send error";
		case 27:
			return "Host unreachable";
		case 1280:
			return "Router: no locked memory";
		case 1282:
			return "Router: mailbox full";
		case 1792:
			return "error class <device error>";
		case 1793:
			return "Service is not supported by server";
		case 1794:
			return "invalid index group";
		case 1795:
			return "invalid index offset";
		case 1796:
			return "reading/writing not permitted";
		case 1797:
			return "parameter size not correct";
		case 1798:
			return "invalid parameter value(s)";
		case 1799:
			return "device is not in a ready state";
		case 1800:
			return "device is busy";
		case 1801:
			return "invalid context (must be in Windows)";
		case 1802:
			return "out of memory";
		case 1803:
			return "invalid parameter value(s)";
		case 1804:
			return "not found (files, ...)";
		case 1805:
			return "syntax error in command or file";
		case 1806:
			return "objects do not match";
		case 1807:
			return "object already exists";
		case 1808:
			return "symbol not found";
		case 1809:
			return "symbol version invalid";
		case 1810:
			return "server is in invalid state";
		case 1811:
			return "AdsTransMode not supported";
		case 1812:
			return "Notification handle is invalid";
		case 1813:
			return "Notification client not registered";
		case 1814:
			return "no more notification handles";
		case 1815:
			return "size for watch too big";
		case 1816:
			return "device not initialized";
		case 1817:
			return "device has a timeout";
		case 1818:
			return "query interface failed";
		case 1819:
			return "wrong interface required";
		case 1820:
			return "class ID is invalid";
		case 1821:
			return "object ID is invalid";
		case 1822:
			return "request is pending";
		case 1823:
			return "request is aborted";
		case 1824:
			return "signal warning";
		case 1825:
			return "invalid array index";
		case 1856:
			return "Error class <client error>";
		case 1857:
			return "invalid parameter at service";
		case 1858:
			return "polling list is empty";
		case 1859:
			return "var connection already in use";
		case 1860:
			return "invoke ID in use";
		case 1861:
			return "timeout elapsed";
		case 1862:
			return "error in win32 subsystem";
		case 1864:
			return "ads-port not opened";
		case 1872:
			return "internal error in ads sync";
		case 1873:
			return "hash table overflow";
		case 1874:
			return "key not found in hash";
		case 1875:
			return "no more symbols in cache";
		default:
			return "Unknown error code: " + errorCode;
		}
	}
}

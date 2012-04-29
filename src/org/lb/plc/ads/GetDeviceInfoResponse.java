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

package org.lb.plc.ads;

import org.lb.plc.Toolbox;

public class GetDeviceInfoResponse extends Payload {
	private final long errorCode;
	private final int majorVersion;
	private final int minorVersion;
	private final int versionBuild;
	private final String deviceName;

	public int getMajorVersion() {
		return majorVersion;
	}

	public int getMinorVersion() {
		return minorVersion;
	}

	public int getVersionBuild() {
		return versionBuild;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public GetDeviceInfoResponse(final long errorCode, final int majorVersion,
			final int minorVersion, final int versionBuild,
			final String deviceName) {
		this.errorCode = errorCode;
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.versionBuild = versionBuild;
		this.deviceName = deviceName;
		ensureValid();
	}

	private void ensureValid() {
		if (deviceName.length() > 16)
			throw new IllegalArgumentException("DeviceName too long");
	}

	@Override
	public org.lb.plc.ams.ErrorCode getErrorCode() {
		return org.lb.plc.ams.ErrorCode.valueOf(errorCode);
	}

	@Override
	public int getCommandId() {
		return 1;
	}

	@Override
	public byte[] toBinary() {
		final byte[] ret = new byte[24];
		Toolbox.copyIntoByteArray(ret, 0, Toolbox.uint32ToBytes(errorCode));
		ret[4] = (byte) majorVersion;
		ret[5] = (byte) minorVersion;
		Toolbox.copyIntoByteArray(ret, 6, Toolbox.uint16ToBytes(versionBuild));
		Toolbox.copyIntoByteArray(ret, 8, deviceName.getBytes());
		return ret;
	}

	public static Payload valueOf(final byte[] data) {
		final long errorCode = Toolbox.bytesToUint32(data, 0);
		final int majorVersion = data[4];
		final int minorVersion = data[5];
		final int versionBuild = Toolbox.bytesToUint16(data, 6);
		final String deviceName = new String(data, 8, 16);
		return new GetDeviceInfoResponse(errorCode, majorVersion, minorVersion,
				versionBuild, deviceName);
	}

	public String toVersionString() {
		return String.format("%s %d.%d.%d", deviceName, majorVersion,
				minorVersion, versionBuild);
	}

	@Override
	public String toString() {
		return String.format("AdsGetDeviceInfoResponse: Error code: %d, "
				+ "Device Name: %s, Build: %d.%d.%d", errorCode, deviceName,
				majorVersion, minorVersion, versionBuild);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((deviceName == null) ? 0 : deviceName.hashCode());
		result = prime * result + (int) (errorCode ^ (errorCode >>> 32));
		result = prime * result + majorVersion;
		result = prime * result + minorVersion;
		result = prime * result + versionBuild;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof GetDeviceInfoResponse))
			return false;
		GetDeviceInfoResponse other = (GetDeviceInfoResponse) obj;
		if (deviceName == null) {
			if (other.deviceName != null)
				return false;
		} else if (!deviceName.equals(other.deviceName))
			return false;
		if (errorCode != other.errorCode)
			return false;
		if (majorVersion != other.majorVersion)
			return false;
		if (minorVersion != other.minorVersion)
			return false;
		if (versionBuild != other.versionBuild)
			return false;
		return true;
	}
}

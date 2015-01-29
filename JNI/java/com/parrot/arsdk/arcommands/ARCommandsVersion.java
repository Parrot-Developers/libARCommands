/*
  Copyright (C) 2014 Parrot SA

  Redistribution and use in source and binary forms, with or without
  modification, are permitted provided that the following conditions
  are met:
  * Redistributions of source code must retain the above copyright
  notice, this list of conditions and the following disclaimer.
  * Redistributions in binary form must reproduce the above copyright
  notice, this list of conditions and the following disclaimer in
  the documentation and/or other materials provided with the
  distribution.
  * Neither the name of Parrot nor the names
  of its contributors may be used to endorse or promote products
  derived from this software without specific prior written
  permission.

  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
  FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
  COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
  BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
  OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
  AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
  SUCH DAMAGE.
*/

package com.parrot.arsdk.arcommands;

public class ARCommandsVersion
{

    /**
     * Private constructor : fully static class
     */
    private ARCommandsVersion()
    {
    }

    /**
     * Gets the current version code.
     * @return The version code as a String (i.e. "3.1.0.2")
     */
    public static native String getVersionCode();

    /**
     * Gets the current version code.
     * @return The version code as an array of 4 integers (i.e. [3, 1, 0, 2])
     */
    public static native int [] getVersionCodeAsInt();


    public enum CompareResult
    {
        /** V1 is newer than V2 */
        V1_NEWER,
        /** V1 and V2 represent the same version */
        SAME_VERSIONS,
        /** V2 is newer than V1 */
        V2_NEWER
    }

    /**
     * Compares two version by them version Strings.
     * @param v1 The first version to compare.
     * @param v2 The second version to compare.
     * @return A CompareResult enum.
     * @note An invalid string is considered to be "0.0.0.0".
     * @note An incomplete string is padded with zeroes (i.e. "3.2" is padded to "3.2.0.0")
     */
    public static CompareResult compareVersions(String v1, String v2)
    {
        int nativeRes = nativeCompareVersionsCode(v1, v2);
        CompareResult res = CompareResult.SAME_VERSIONS;
        if (nativeRes > 0) { res = CompareResult.V1_NEWER; }
        else if (nativeRes < 0) { res = CompareResult.V2_NEWER; }
        return res;
    }

    private static native int nativeCompareVersionsCode(String v1, String v2);

    /**
     * Compares two version by them version arrays.
     * @param v1 The first version to compare.
     * @param v2 The second version to compare.
     * @return A CompareResult enum.
     * @note An incomplete array is padded with zeroes (i.e. [3, 2] is padded to [3, 2, 0, 0])
     */
    public static CompareResult compareVersions(int[] v1, int[] v2)
    {
        int[] v1_padded = new int[4];
        int[] v2_padded = new int[4];

        int i;
        for (i = 0; i < 4; i++)
        {
            v1_padded[i] = (i < v1.length) ? v1[i] : 0;
            v2_padded[i] = (i < v2.length) ? v2[i] : 0;
        }

        int nativeRes = nativeCompareVersions(v1_padded[0], v1_padded[1], v1_padded[2], v1_padded[3],
                                              v2_padded[0], v2_padded[1], v2_padded[2], v2_padded[3]);
        CompareResult res = CompareResult.SAME_VERSIONS;
        if (nativeRes > 0) { res = CompareResult.V1_NEWER; }
        else if (nativeRes < 0) { res = CompareResult.V2_NEWER; }
        return res;
    }

    private static native int nativeCompareVersions(int v1M, int v1m, int v1r, int v1c,
                                                    int v2M, int v2m, int v2r, int v2c);


}

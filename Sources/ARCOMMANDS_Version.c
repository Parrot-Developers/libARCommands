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

#include "../Includes/libARCommands/ARCOMMANDS_Version.h"
#include <stdio.h>



int ARCOMMANDS_Version_CompareVersions(int v1M, int v1m, int v1r, int v1c,
                                       int v2M, int v2m, int v2r, int v2c)
{
    int ret = 0;

    ret = v1M - v2M;
    if (ret == 0) { ret = v1m - v2m; }
    if (ret == 0) { ret = v1r - v2r; }
    if (ret == 0) { ret = v1c - v2c; }

    return ret;
}

int ARCOMMANDS_Version_CompareVersionCodes(const char *v1, const char *v2)
{
    int v1M = 0, v1m = 0, v1r = 0, v1c = 0;
    int v2M = 0, v2m = 0, v2r = 0, v2c = 0;

    sscanf (v1, "%d.%d.%d.%d", &v1M, &v1m, &v1r, &v1c);
    sscanf (v2, "%d.%d.%d.%d", &v2M, &v2m, &v2r, &v2c);


    return ARCOMMANDS_Version_CompareVersions(v1M, v1m, v1r, v1c, v2M, v2m, v2r, v2c);
}

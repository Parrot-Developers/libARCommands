#!/usr/bin/env python

import sys, os
import struct
import dpkt
import socket
from cStringIO import StringIO

_MAGIC = 0x21 # '!'

_TAG_NETAL_FRAME_PUSHED = 0x10
_TAG_NETAL_DATA_SENT = 0x11
_TAG_NETAL_DATA_RECEIVED = 0x12
_TAG_NETAL_FRAME_POPPED = 0x13
_TAG_APP_CMD_SENT = 0x30

_isTx = {_TAG_NETAL_FRAME_PUSHED: True,
         _TAG_NETAL_DATA_SENT: True,
         _TAG_NETAL_DATA_RECEIVED: False,
         _TAG_NETAL_FRAME_POPPED: False,
         _TAG_APP_CMD_SENT: True,
}
_needHeader = {_TAG_NETAL_FRAME_PUSHED: False,
               _TAG_NETAL_DATA_SENT: False,
               _TAG_NETAL_DATA_RECEIVED: False,
               _TAG_NETAL_FRAME_POPPED: False,
               _TAG_APP_CMD_SENT: True,
}
_SRC_ADDR = socket.inet_aton("192.168.1.1")
_DST_ADDR = socket.inet_aton("192.168.1.2")
_DST_PORT = 54321

###############################################################################
###############################################################################
class ArDumpError(Exception):
    pass

###############################################################################
###############################################################################
class ArDumpDataReader(object):
    def __init__(self, src):
        if isinstance(src, str):
            self._src = StringIO(src)
        else:
            self._src = src
        # Determine size
        self._src.seek(0, os.SEEK_END)
        self._size = self._src.tell()
        self._src.seek(0, os.SEEK_SET)

    def readData(self, count):
        data = self._src.read(count)
        if len(data) != count:
            raise ArDumpError("Unable to read %d bytes" % count)
        return data

    def readU8(self):
        return struct.unpack("<B", self.readData(1))[0]

    def readU16(self):
        return struct.unpack("<H", self.readData(2))[0]

    def readU32(self):
        return struct.unpack("<I", self.readData(4))[0]

    def readString(self):
        slen = self.readU8()
        if slen == 0:
            raise ArDumpError("Invalid string length: %d" % slen)
        sdata = self.readData(slen)
        if sdata[slen - 1] != "\0":
            raise ArDumpError("String is not null-terminated")
        return sdata[:-1]

    def remaining(self):
        return self._size - self._src.tell()

###############################################################################
###############################################################################
class ArDumpPacket(object):
    def __init__(self, tag, sizeReal, sizeDump, data, timestamp):
        self.tag = tag
        self.sizeReal = sizeReal
        self.sizeDump = sizeDump
        self.data = data
        self.timestamp = timestamp
        # Consider unknown tags as TX data
        self.isTx = _isTx[tag] if tag in _isTx else True
        # Consider unknown tags as needing a dummyarnetworkal header
        self.needHeader = _needHeader[tag] if tag in _needHeader else True

    def __repr__(self):
        if self.sizeDump < 50:
            return "{tag=0x%02x, timestamp=%d, sizeReal=%d, sizeDump=%d, isTx=%r, needHeader=%r, data=%s" % (
                self.tag, self.timestamp, self.sizeReal, self.sizeDump, self.isTx, self.needHeader, repr(self.data))
        else:
            return "{tag=0x%02x, timestamp=%d, sizeReal=%d, sizeDump=%d, isTx=%r, needHeader=%r, data=(too long)" % (
                self.tag, self.timestamp, self.sizeReal, self.sizeDump, self.isTx, self.needHeader)


###############################################################################
###############################################################################
class ArDumpParser(object):
    def __init__(self, reader):
        self._reader = reader
        self.packets = []
        while self._readPacket():
            pass

    def _readPacket(self):
        remaining = self._reader.remaining()
        if remaining < 1:
            return False
        magic = self._reader.readU8()
        if magic != _MAGIC:
            raise ArDumpError("Bad magic: 0x%02x(0x%02x)" % (magic, _MAGIC))
        try:
            tag = self._reader.readU8()
            sizeReal = self._reader.readU32()
            sizeDump = self._reader.readU32()
            timestampLo = self._reader.readU32()
            timestampHi = self._reader.readU16()
            timestamp = (timestampHi << 32) | timestampLo
            data = self._reader.readData(sizeDump)
            self.packets.append(ArDumpPacket(tag, sizeReal, sizeDump, data, timestamp))
        except ArDumpError:
            sys.stderr.write("Truncated log (%d bytes skipped)\n" % remaining)
            return False
        return True

###############################################################################
###############################################################################
def usage():
    sys.stderr.write("usage: %s <logfile>\n" % sys.argv[0])

###############################################################################
###############################################################################
def writePacket(writer, packet):
    udpPkt = dpkt.udp.UDP()
    if packet.needHeader:
        # Add a dummy ARNetwork header (data with ack, buffer 42, sequence 0)
        udpPkt.data = struct.pack('<BBBI', 4, 42, 0, len(packet.data) + 7)
        udpPkt.data += packet.data
        udpPkt.ulen = dpkt.udp.UDP_HDR_LEN + len(packet.data) + 7
    else:
        udpPkt.data = packet.data
        udpPkt.ulen = dpkt.udp.UDP_HDR_LEN + len(packet.data)
    udpPkt.dport = _DST_PORT
    udpPkt.sport = packet.tag

    ipPkt = dpkt.ip.IP()
    ipPkt.data = udpPkt
    ipPkt.src = _SRC_ADDR if packet.isTx else _DST_ADDR
    ipPkt.dst = _DST_ADDR if packet.isTx else _SRC_ADDR
    ipPkt.p = dpkt.ip.IP_PROTO_UDP
    ipPkt.len = dpkt.ip.IP_HDR_LEN + udpPkt.ulen

    ethPkt = dpkt.ethernet.Ethernet()
    ethPkt.data = ipPkt

    tv_sec = packet.timestamp // (1000 * 1000)
    tv_usec = packet.timestamp % (1000 * 1000)
    ts = tv_sec + tv_usec / (1000.0 * 1000.0)

    writer.writepkt(ethPkt, ts)

###############################################################################
###############################################################################
def main():
    if len(sys.argv) != 2:
        usage()
        sys.exit(1)
    elif sys.argv[1] == "-h" or sys.argv[1] == "--help":
        usage()
        sys.exit(0)

    try:
        logfile = open(sys.argv[1], "rb")
    except IOError as ex:
        sys.stderr.write("Unable to open '%s': err=%d(%s)\n" % (
                ex.filename, ex.errno, ex.strerror))
        sys.exit(1)

    writer = dpkt.pcap.Writer(sys.stdout)
    try:
        parser = ArDumpParser(ArDumpDataReader(logfile))
        for packet in parser.packets:
            #print(repr(packet))
            writePacket(writer, packet)
    except ArDumpError as ex:
        sys.stderr.write("%s\n" % ex.message)
        sys.exit(1)

    logfile.close()

###############################################################################
###############################################################################
if __name__ == "__main__":
    main()

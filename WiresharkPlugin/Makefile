# Makefile for ARSDK wireshark plugin
PLUGIN	:= arsdk.so
OBJS	:= packet-arsdk.o

TREE	:= tree.h
GEN	:= ../Xml/generateLibARCommands.py
INC	:= -I ../Includes -I ../Sources -I ../../libARNetworkAL/Includes -I ../gen/Includes

WARN	:= -Wall -W -Wextra -Wdeclaration-after-statement -Wendif-labels \
	-Wpointer-arith -Wno-pointer-sign -Warray-bounds -Wformat-security \
	-Wold-style-definition -Wstrict-prototypes -Wjump-misses-init -Wvla \
	-Waddress -Wattributes -Wdiv-by-zero -Wignored-qualifiers -Wpragmas \
	-Wno-overlength-strings -Wwrite-strings -Wno-long-long -Wlogical-op

CFLAGS	:= -g -O2 $(WARN) $(INC) -fPIC -DPIC -DSTANDALONE_BUILD \
	-fexcess-precision=fast -fvisibility=hidden -fwrapv \
	-fno-strict-overflow -fno-delete-null-pointer-checks

LDFLAGS	:= -shared -fPIC -DPIC
STRIP_ARGS :=

UNAME_S := $(shell uname -s)
# if platform is on MacOS
ifeq ($(UNAME_S),Darwin)
    LDFLAGS += -L/Applications/Wireshark.app/Contents/Resources/lib -lwireshark
    STRIP_ARGS += -x
endif
# if platform is on Linux
ifeq ($(UNAME_S),Linux)
    LDFLAGS += -Wl,-soname -Wl,$(PLUGIN)
endif

all: $(PLUGIN)

# don't bother handling dependencies here
.PHONY: .FORCE

$(TREE): .FORCE
	python $(GEN) -debug-cmds yes -projects all -gen-tree $(TREE)

%.o: %.c $(TREE) .FORCE
	@echo "CC $@"
	@$(CC) -c $< $(CFLAGS) -o $@

$(PLUGIN): $(OBJS)
	@echo "CCLD $@"
	@$(CC) $(LDFLAGS) $^ -o $@
	@echo "STRIP $@"
	@strip $(STRIP_ARGS) $@

clean:
	-rm -f $(OBJS) $(TREE) $(PLUGIN)

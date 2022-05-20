DESCRIPTION = "fru-print"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/LICENSE.txt;md5=cbe895ca02dfa53a3d54f227d1b1967c"

RDEPENDS:${PN} = "python3-core python3-pyyaml"
inherit python3-dir

REPO = "git://github.com/Xilinx/fru-tool.git;protocol=https"
BRANCH = "master"
SRCREV = "6098e16e6a8786c79840da8acc100fe9301b6a90"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

S = "${WORKDIR}/git"

do_install() {
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}/
    install -d ${D}${bindir}/
    install -m 0755 ${S}/fru.py ${D}${PYTHON_SITEPACKAGES_DIR}/
    install -m 0755 ${S}/fru-print ${D}${bindir}/
}

FILES:${PN} += "${PYTHON_SITEPACKAGES_DIR}/fru.py ${bindir}/fru-print"

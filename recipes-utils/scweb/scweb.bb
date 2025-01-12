#
# This file is the scweb recipe.
#

SUMMARY = "Landing Page"
SECTION = "PETALINUX/apps"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"


SRC_URI = "git://github.com/Xilinx/system-controller-web.git;branch=master;protocol=https \
	   file://scwebrun.service \
                  "
SRCREV = "c589f12e61f3d3c900540f67379bde469a6c0015"

inherit update-rc.d systemd

INITSCRIPT_NAME = "scwebrun.sh"
INITSCRIPT_PARAMS = "start 97 5 ."

SYSTEMD_PACKAGES="${PN}"
SYSTEMD_SERVICE_${PN}="scwebrun.service"
SYSTEMD_AUTO_ENABLE_${PN}="enable"

S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE_vck-sc = "${MACHINE}"
COMPATIBLE_MACHINE_vpk-sc = "${MACHINE}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_configure[noexec]="1"
do_compile[noexec]="1"

SCWEB_DIR = "${datadir}/${PN}"

RDEPENDS_${PN} += "bash \
        python3 \
        python3-flask \
        python3-flask-restful \
        system-controller-app \        
        lmsensors-sensors \
        "

do_install() {

	install -d ${D}/${SCWEB_DIR}
        cp -r ${S}/src/* ${D}/${SCWEB_DIR}

       install -d ${D}${bindir}
       install -m 0755 ${S}/scwebrun.sh ${D}${bindir}

       install -d ${D}${systemd_system_unitdir}
       install -m 0644 ${WORKDIR}/scwebrun.service ${D}${systemd_system_unitdir}

       if ${@bb.utils.contains('DISTRO_FEATURES', 'sysvinit', 'true', 'false', d)}; then
               install -d ${D}${sysconfdir}/init.d/
               install -m 0755 ${S}/scwebrun.sh ${D}${sysconfdir}/init.d/
       fi
}

FILES_${PN} += "${SCWEB_DIR}"
